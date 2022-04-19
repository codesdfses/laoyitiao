package com.laoyitiao.auth.service;

import com.alibaba.fastjson.JSON;
import com.laoyitiao.auth.entities.UserAccount;
import com.laoyitiao.auth.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserAccountRepository accountRepository;

    public Object login(String username,String password)
    {
        // 构造 post的body内容（要post的内容，按需定义）
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("grant_type", "password");
        paramsMap.set("username", username);
        paramsMap.set("password", password);
        // 构造头部信息(若有需要)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic Y2xpZW50OjEyMw==");
        // 设置类型 "application/json;charset=UTF-8"
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 构造请求的实体。包含body和headers的内容
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(paramsMap, headers);
        // 声明 restTemplateAuth（用作请求）
        RestTemplate restTemplateAuth = new RestTemplate();

        Map<String, Object> map = new HashMap<>();
        try {
            // 进行请求，并返回数据
            Map<String, Object> tokenInfo = (Map<String, Object>) JSON.parse(restTemplateAuth.postForObject("http://localhost:9000/auth/oauth/token", request, String.class));
            if ("false".equals(tokenInfo.get("status"))){
                map.put("msg",tokenInfo.get("error_description"));
                map.put("status",false);
            }else {
                Optional<UserAccount> optional = accountRepository.findById(username);
                if (optional.isPresent()){
                    map.put("status",true);
                    map.put("tokenInfo", tokenInfo);
                    UserAccount account = optional.get();
                    // 手机号邮箱脱敏
                    if (account.getPhoneNumber()!=null){
                        String privateNumber = account.getPhoneNumber().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
                        account.setPhoneNumber(privateNumber);
                    }
                    if (account.getEmailAddress()!=null){
                        String emailAddress = account.getEmailAddress().replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2");
                        account.setEmailAddress(emailAddress);
                    }
                    map.put("account",account);
                }else {
                    map.put("status",false);
                    map.put("msg","账号异常，请联系管理员！【error:10045】");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            map.put("status",false);
            map.put("error_description","服务器异常，请稍后重试！");
        }
        return map;
    }

}
