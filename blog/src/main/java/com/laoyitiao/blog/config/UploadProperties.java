package com.laoyitiao.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "upload")
@Data
public class UploadProperties {
    /**
     * fastdfs的基础访问路径（storage的web访问路径）
     */
    private String baseUrl;

    /**
     * 允许上传的文件类型.
     */
    private List<String> allowedTypes;
}
