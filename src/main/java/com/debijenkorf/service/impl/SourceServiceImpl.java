package com.debijenkorf.service.impl;

import com.debijenkorf.exception.ApplicationException;
import com.debijenkorf.service.SourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Service
@Slf4j
public class SourceServiceImpl implements SourceService {

    @Value("${source.root-url}")
    private String sourceRootUrl;

    public File downloadImage(String reference) {
        try {
            var file = new File("the original image name");
            var url = new URL(sourceRootUrl + reference);
            FileUtils.copyURLToFile(url, file);
            return file;
        } catch (IOException exception) {
            log.info(exception.getMessage(), exception);
            throw new ApplicationException("an exception occurred while downloading file with name " + reference);
        }
    }
}
