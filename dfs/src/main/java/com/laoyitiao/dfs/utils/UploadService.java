package com.laoyitiao.dfs.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.laoyitiao.dfs.config.UploadProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {
    Logger logger = LoggerFactory.getLogger(UploadService.class);

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadProperties properties;

    public String uploadImage(MultipartFile file) {
        // 1验证文件类型
        String contentType = file.getContentType();
        if (!properties.getAllowedTypes().contains(contentType)){
            throw new RuntimeException("文件类型不支持");
        }
        // 2校验文件属性
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null || image.getWidth() == 0 || image.getHeight() == 0){
                throw new RuntimeException("文件无效");
            }

        }catch (Exception e){
            logger.error("文件上传时，校验内容失败->{}", e);
            throw new RuntimeException("校验文件失败"+e.getMessage());
        }

        try {
            // 3上传fastdfs
            // 3.1获取扩展名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            // 返回完整访问路径
            return properties.getBaseUrl() + "/" + storePath.getFullPath();
        }catch (Exception e){
            logger.error("【文件长传】文件上传失败！->{}", e);
            throw new RuntimeException("【文件长传】文件上传失败！->"+e.getMessage());
        }
    }
}
