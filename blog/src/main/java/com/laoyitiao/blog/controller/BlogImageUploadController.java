package com.laoyitiao.blog.controller;

import com.laoyitiao.blog.service.BlogImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BlogImageUploadController {

    @Autowired
    private BlogImageUploadService blogImageUploadService;


    @PreAuthorize("hasAuthority('blog:write')")
    @PostMapping("/blogCover")
    public String uploadCoverImage(MultipartFile file, Authentication authentication){
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            System.out.println("authority.getAuthority() = " + authority.getAuthority());
        }
        return blogImageUploadService.uploadBlogImage(file,authentication);
    }
}
