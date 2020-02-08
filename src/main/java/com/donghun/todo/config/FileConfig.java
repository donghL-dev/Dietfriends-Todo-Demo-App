package com.donghun.todo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class FileConfig {

    @Value("${file.upload-dir}")
    private String uploadDir;
}
