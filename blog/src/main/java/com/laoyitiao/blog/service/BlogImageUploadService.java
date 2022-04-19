package com.laoyitiao.blog.service;

import com.laoyitiao.blog.utils.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Service
public class BlogImageUploadService {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private RedisTemplate redisTemplate;

    public String uploadBlogImage(MultipartFile file, Authentication authentication){
        Object principal = authentication.getPrincipal();
        // 检查上传次数
        Object o = redisTemplate.opsForValue().get("uploadCount:" + principal.toString());
        if (o!=null){
            if ((Integer)o>=5){
                return "对非会员用户在24小时内只能上传5张图片哦！";
            }
        }

        // 上传
        String realPath = uploadService.uploadFile(file);

        // 更新上传次数
        Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent("uploadCount:" + principal, 1,60*60*24, TimeUnit.SECONDS);
        if (!setIfAbsent){
            redisTemplate.opsForValue().increment("uploadCount:" + principal);
        }

        // 返回图片真实访问路径
        return realPath;
    }
}
