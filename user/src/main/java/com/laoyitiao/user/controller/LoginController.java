package com.laoyitiao.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
public class LoginController {

    @PostMapping("/doLogin")
    public void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String clientCredentials = Base64.getEncoder().encodeToString("client:123".getBytes(StandardCharsets.UTF_8));
        System.out.println("clientCredentials = " + clientCredentials);
        request.setAttribute("Authorization","Basic "+ clientCredentials);
        request.setAttribute("grant_type","password");
        request.setAttribute("scope","all");
        request.setAttribute("username",request.getParameter("username"));
        request.setAttribute("password",request.getParameter("password"));
        request.getRequestDispatcher("/test").forward(request,response);
    }
}
