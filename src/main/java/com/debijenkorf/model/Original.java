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
public class Original implements PredefineType {
    @Value("${original.height}")
    private int height;
    @Value("${original.width}")
    private int width;
    @Value("${original.quality}")
    private int quality;
    @Value("${original.scaleType}")
    private ScaleType scaleType;
    @Value("${original.fillColor}")
    private int fillColor;
    @Value("${original.type}")
    private Type type;
}
