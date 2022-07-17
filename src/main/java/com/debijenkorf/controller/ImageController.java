package com.debijenkorf.controller;

import com.debijenkorf.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * provides REST apis for showing and flushing images
 */
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/show/{predefineType}")
    public ResponseEntity<ByteArrayResource> showImage(
            @PathVariable("predefineType") String predefineType,
            @RequestParam(value = "reference") String reference) {
        byte[] bytes = imageService.showImage(predefineType, reference);
        ByteArrayResource resource = new ByteArrayResource(bytes);
        return ResponseEntity
                .ok()
                .contentLength(bytes.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + reference + "\"")
                .body(resource);
    }

    @DeleteMapping("/flush/{predefineType}")
    public ResponseEntity<Void> flushImage(
            @PathVariable("predefineType") String predefineType,
            @RequestParam(value = "reference") String reference) {
        imageService.flushImage(predefineType, reference);
        return ResponseEntity.noContent().build();
    }
}
