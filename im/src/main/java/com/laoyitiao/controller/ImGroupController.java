package com.laoyitiao.controller;

import com.laoyitiao.service.ImGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImGroupController {

    @Autowired
    private ImGroupService imGroupService;

    @PostMapping("/imGroup")
    public Object createImGroup(String username, String groupName){
        System.err.println("获取到请求");
        return imGroupService.createImGroup(username,groupName);
    }
}
