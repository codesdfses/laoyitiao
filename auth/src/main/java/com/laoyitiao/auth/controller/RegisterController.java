package com.laoyitiao.auth.controller;

import com.laoyitiao.auth.service.RegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RegisterController {

    @Resource
    private RegisterService registerService;

    @PostMapping("/doRegister")
    public Object register(String username, String password, String nickname, String emailAddress, String phoneNumber){
        return registerService.register(username,password,nickname,emailAddress,phoneNumber);
    }
}
