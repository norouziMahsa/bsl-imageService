package com.debijenkorf.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

/**
 * Defines the s3 directory strategy
 *
 * if the length of the image name (without extension) is smaller than or equal to 4,
 * the image is stored directly to the initial directory,
 *
 * if the length of the image name (without extension) is greater than 4 but smaller than or equal to 8,
 * the first 4 chars is considered as sub directory
 *
 * if the length of the image name (without extension) is greater than 8,
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
        String[] parts = originalImageNameWithoutExtension.split("(?<=\\G.{4})");
        if (originalNameLength <= 4) {
            return predefineType + "/" + originalImageNameWithoutExtension;
        }
        else if (originalNameLength <= 8) {
            return predefineType + "/" + parts[0] + "/" + originalImageNameWithoutExtension;
        }
        else {
            return predefineType + "/" + parts[0] + "/" + parts[1] + "/" + originalImageNameWithoutExtension;
        }
    }
}
