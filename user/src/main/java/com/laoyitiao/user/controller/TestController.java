package com.laoyitiao.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestController {
    @RequestMapping("/test")
    public Object test(HttpServletRequest request){
        System.out.println("request.getAttribute(\"Authorization\") = " + request.getAttribute("Authorization"));
        return "pong";
    }
}
