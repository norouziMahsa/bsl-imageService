package com.debijenkorf.service;

import java.io.File;

public interface SourceService {
    /**
     * downloads original image from source url
     * @param reference the unique file name
     * @return original file from source url
     */
    File downloadImage(String reference);
}

