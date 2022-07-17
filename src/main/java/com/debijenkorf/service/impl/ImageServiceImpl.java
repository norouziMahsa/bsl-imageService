package com.debijenkorf.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.debijenkorf.exception.ApplicationException;
import com.debijenkorf.model.Original;
import com.debijenkorf.model.PredefineType;
import com.debijenkorf.model.PredefineTypeEnum;
import com.debijenkorf.model.ThumbNail;
import com.debijenkorf.repository.S3Repository;
import com.debijenkorf.service.ImageService;
import com.debijenkorf.service.SourceService;
import com.debijenkorf.util.S3DirectoryStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final S3Repository s3Repository;
    private final SourceService sourceService;
    private final Original original;
    private final ThumbNail thumbNail;

    @Value("${application.bucket.original.name}")
    private String originalBucketName;

    @Value("${application.bucket.optimized.name}")
    private String optimizedBucketName;

    @Override
    public byte[] showImage(String predefineType, String reference) {
        try {
            var imageKey = S3DirectoryStrategy.getKey(predefineType, reference);
            if (doesOptimizedImageExist(imageKey)) {
                return downloadFileFromS3(imageKey);
            }
            else if (doesOriginalExist(imageKey)) {
                var originalImageObject = s3Repository.getImage(originalBucketName, imageKey);
                var optimizedImage = optimizeOriginalImage(getPredefineType(predefineType), originalImageObject);
                storeOptimizedImageInS3(imageKey, optimizedImage);
                return showImage(predefineType, reference);
            }
            else {
                var originalImageFromSite = downloadOriginalImageFromSourceUrl(reference);
                s3Repository.storeImage(originalBucketName, imageKey, originalImageFromSite);
                return showImage(predefineType, reference);
            }
        } catch (SdkClientException exception) {
            log.error(exception.getMessage(), exception);
            throw new ApplicationException("an exception occurred while showing image " + reference);
        }
    }

    @Override
    public void flushImage(String predefineType, String reference) {
        try {
            switch (PredefineTypeEnum.valueOf(predefineType)) {
                case THUMBNAIL:
                    var imageKeyForSpecificPredefineType = S3DirectoryStrategy.getKey(predefineType, reference);
                    s3Repository.deleteImage(optimizedBucketName, imageKeyForSpecificPredefineType);
                    s3Repository.deleteImage(originalBucketName, imageKeyForSpecificPredefineType);
                    break;
                case ORIGINAL:
                    // in case of original, all available images for all variants should be deleted from S3 bucket
                    for (PredefineTypeEnum enumValue : PredefineTypeEnum.values()) {
                        String imageKeyBasedOnPredefineType = S3DirectoryStrategy.getKey(enumValue.name(), reference);
                        s3Repository.deleteImage(optimizedBucketName, imageKeyBasedOnPredefineType);
                        s3Repository.deleteImage(originalBucketName, imageKeyBasedOnPredefineType);
                    }
                    break;
                default:
                    log.info("can not flush image, invalid predefine type {}", predefineType);
                    throw new ApplicationException("invalid predefine type " + predefineType);
            }
        } catch (SdkClientException exception) {
            log.error(exception.getMessage(), exception);
            throw new ApplicationException("exception occurred while flushing file from S3 bucket");
        }
    }

    public byte[] downloadFileFromS3(String imageKey) {
        var s3Object = s3Repository.getImage(optimizedBucketName, imageKey);
        var inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
            throw new ApplicationException("exception occurred while downloading file from S3 bucket");
        }
    }

    /**
     * stores optimized image in s3
     * if the first attempt is not successful, retry writing after 200ms
     * @param imageKey including correct directories and original file name
     * @param optimizedImage
     */
    private void storeOptimizedImageInS3(String imageKey, File optimizedImage) {
        var count = 0;
        var maxRetries = 2;
        while (true) {
            try {
                s3Repository.storeImage(optimizedBucketName, imageKey, optimizedImage);
            } catch (SdkClientException exception) {
                log.warn("can not store image in s3 bucket in {} attempt", count);
                if (++count == maxRetries) {
                    log.error(exception.getMessage(), exception);
                    throw new ApplicationException("unable to store optimized image in s3");
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException interruptedException) {
                    log.error(interruptedException.getMessage(), interruptedException);
                    throw new RuntimeException("an unexpected exception occurred while retrying to store optimized image in s3");
                }
            }
        }
    }

    /**
     * @param predefineType
     * @returns the appropriate configuration based on predefine type
     */
    private PredefineType getPredefineType(String predefineType) {
        switch (PredefineTypeEnum.valueOf(predefineType)) {
            case ORIGINAL:
                return original;
            case THUMBNAIL:
                return thumbNail;
            default:
                log.info("invalid predefine type {}", predefineType);
                throw new ApplicationException("invalid predefine type");
        }
    }

    /**
     * optimizes the original image
     * @param predefineType which contains all the properties for the correct resizing of the image
     * @param originalImageObject which contains file information
     * @return optimized image
     */
    private File optimizeOriginalImage(PredefineType predefineType, S3Object originalImageObject) {
        // do optimization, we could do it by adding some extra libraries
        // for simplicity we have mocked this method and just returned a simple file
        return new File("original file name");
    }

    private boolean doesOptimizedImageExist(String imageKey) {
        try {
            return s3Repository.doesImageExist(optimizedBucketName, imageKey);
        } catch (SdkClientException exception) {
            log.error(exception.getMessage(), exception);
            throw new ApplicationException("an exception occurred while looking up for optimized image");
        }
    }

    private boolean doesOriginalExist(String imageKey) {
        try {
            return s3Repository.doesImageExist(originalBucketName, imageKey);
        } catch (SdkClientException exception) {
            log.error(exception.getMessage(), exception);
            throw new ApplicationException("an exception occurred while looking up for original image");
        }
    }

    private File downloadOriginalImageFromSourceUrl(String reference) {
        return sourceService.downloadImage(reference);
    }
}
