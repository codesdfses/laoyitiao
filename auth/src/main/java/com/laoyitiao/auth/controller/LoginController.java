package com.laoyitiao.auth.controller;

import com.laoyitiao.auth.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/doLogin")
    public Object login(String username,String password)
    {
        return loginService.login(username,password);
    }

}
