package com.laoyitiao.blog.controller;

import com.laoyitiao.blog.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/page")
@RestController
public class PageController {

    @Autowired
    private PageService discoveryService;

    @GetMapping("/discovery")
    public Object getDiscoveryPage(){
        return discoveryService.getDiscoveryPage();
    }
}
