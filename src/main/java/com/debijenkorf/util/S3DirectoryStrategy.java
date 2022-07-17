package com.debijenkorf.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

/**
 * Defines the s3 directory strategy
 * if the length of the image name (without extension) is smaller or equal to 4 characters,
 * or greater than 4 but smaller than or equal to 8, the image is stored directly,
 * otherwise,
 * the first 4 chars is considered as initial sub directory, and the second 4 chars as second sub directory
 */
public final class S3DirectoryStrategy {
    public static String getKey(String predefineType, String reference) {
        if (!StringUtils.hasText(predefineType)) {
            predefineType = "original";
        }
        var originalImageNameWithoutExtension = FilenameUtils.removeExtension(reference);
        originalImageNameWithoutExtension = originalImageNameWithoutExtension.replaceAll("/", "_");
        var originalNameLength = originalImageNameWithoutExtension.length();
        if (originalNameLength <= 4) {
            return predefineType + "/" + originalImageNameWithoutExtension;
        }
        else if (originalNameLength <= 8) {
            return predefineType + "/" + originalImageNameWithoutExtension;
        }
        else {
            String[] parts = originalImageNameWithoutExtension.split("(?<=\\G.{4})");
            return predefineType + "/" + parts[0] + "/" + parts[1] + "/" + originalImageNameWithoutExtension;
        }
    }
}
