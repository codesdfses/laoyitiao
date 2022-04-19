package com.laoyitiao.auth.config;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class CustomOauthException extends OAuth2Exception {
    public CustomOauthException(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "登录失败";
    }

    @Override
    public int getHttpErrorCode() {
        return 401;
    }

}
