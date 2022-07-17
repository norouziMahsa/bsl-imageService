package com.debijenkorf.repository;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;

public interface S3Repository {
    /**
     * check if image exists in S3 bucket
     * @param bucketName
     * @param imageKey
     * @return true if image exists otherwise false
     */
    boolean doesImageExist(String bucketName, String imageKey);

    /**
     * retrieves image from S3 bucket
     * @param bucketName
     * @param imageKey
     * @return
     */
    S3Object getImage(String bucketName, String imageKey);

    /**
     * stores image in S3 bucket
     * @param bucketName
     * @param imageKey
     * @param image
     * @return
     */
    PutObjectResult storeImage(String bucketName, String imageKey, File image);

    /**
     * deletes image from S3 bucket
     * @param bucketName
     * @param imageKey
     */
    void deleteImage(String bucketName, String imageKey);
}
