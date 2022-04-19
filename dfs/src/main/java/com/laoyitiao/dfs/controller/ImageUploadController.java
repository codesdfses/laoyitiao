package com.laoyitiao.dfs.controller;

import com.laoyitiao.dfs.utils.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class ImageUploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/doUpload")
    public Object uploadImage(MultipartFile file){
        Map<String,Object> map = new HashMap<>();
        String uri = uploadService.uploadImage(file);
        map.put("accessPath", uri);
        return map;
    }
}
