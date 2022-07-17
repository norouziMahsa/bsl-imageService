package com.debijenkorf.service;

public interface ImageService {
    /**
     * shows optimized image based on predefine type from S3 bucket
     * if optimized image does not exist, the original one should be downloaded, optimized and returned
     * @param predefineType
     * @param reference the unique file name
     * @return optimized image byte array
     */
    byte[] showImage(String predefineType, String reference);

    /**
     * flushes original and optimized images from S3 bucket
     * @param predefineType
     * @param reference the unique file name
     */
    void flushImage(String predefineType, String reference);
}
