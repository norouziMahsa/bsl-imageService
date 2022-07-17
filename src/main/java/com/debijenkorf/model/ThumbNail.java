package com.debijenkorf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThumbNail implements PredefineType {
    @Value("${thumbNail.height}")
    private int height;
    @Value("${thumbNail.width}")
    private int width;
    @Value("${thumbNail.quality}")
    private int quality;
    @Value("${thumbNail.scaleType}")
    private ScaleType scaleType;
    @Value("${thumbNail.fillColor}")
    private int fillColor;
    @Value("${thumbNail.type}")
    private Type type;
}
