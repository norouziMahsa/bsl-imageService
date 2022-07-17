package com.debijenkorf.repository.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.debijenkorf.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class S3RepositoryImpl implements S3Repository {

    private final AmazonS3 s3Client;

    @Override
    // to have a better performance, the result is cached
    // so we don't need to look up for an image in entire S3 bucket every time
    @Cacheable(value="imageServiceCache", key="{#bucketName, #imageKey}")
    public boolean doesImageExist(String bucketName, String imageKey) {
        return s3Client.doesObjectExist(bucketName, imageKey);
    }

    @Override
    public S3Object getImage(String bucketName, String imageKey) {
        return s3Client.getObject(bucketName, imageKey);
    }

    @Override
    public PutObjectResult storeImage(String bucketName, String imageKey, File image) {
        return s3Client.putObject(new PutObjectRequest(bucketName, imageKey, image));
    }

    @Override
    // after each delete, the image will be removed from the cache
    @CacheEvict(value = "imageServiceCache", key="{#bucketName, #imageKey}")
    public void deleteImage(String bucketName, String imageKey) {
        s3Client.deleteObject(bucketName, imageKey);
    }
}
