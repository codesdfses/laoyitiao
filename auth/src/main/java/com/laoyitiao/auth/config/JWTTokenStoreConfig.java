package com.laoyitiao.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
public class JWTTokenStoreConfig {

    @Bean
    public TokenStore customJwtTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    // 将默认的token转换成JWT
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        // 设置jwt密钥
        tokenConverter.setSigningKey("asmr$se/cvcx88d*");
        return tokenConverter;
    }


    @Bean
    public JWTTokenEnhancer jwtTokenEnhancer(){
        return new JWTTokenEnhancer();
    }
}
