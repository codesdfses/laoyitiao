/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laoyitiao.auth.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import java.util.ArrayList;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Resource
	private PasswordEncoder passwordEncoder;

	@Resource
	private AuthenticationManager authenticationManager;
	@Resource
	private UserDetailsService userDetailsService;
	@Resource
	@Qualifier("customJwtTokenStore")
	private TokenStore tokenStore;
	@Resource
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	@Resource
	private JWTTokenEnhancer jwtTokenEnhancer;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		// 设置JWT增强内容
		TokenEnhancerChain chain = new TokenEnhancerChain();
		ArrayList<TokenEnhancer> list = new ArrayList<>();
		list.add(jwtTokenEnhancer);
		list.add(jwtAccessTokenConverter);
		chain.setTokenEnhancers(list);

		endpoints.authenticationManager(authenticationManager)
				.userDetailsService(userDetailsService)
				// accessToken转成jwt
				.tokenStore(tokenStore)
				.accessTokenConverter(jwtAccessTokenConverter)
				.tokenEnhancer(chain)
				.exceptionTranslator(new MyWebResponseExceptionTranslator())
				;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("client")
				.secret(passwordEncoder.encode("123"))
				.redirectUris("http://localhost:8090/login")
				.scopes("all","个人信息","仓库")
				.accessTokenValiditySeconds(60*30)
				.refreshTokenValiditySeconds(60*60*24*14)
				.autoApprove(true) // 自动同意授权
				.authorizedGrantTypes("authorization_code","password","refresh_token");
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		// 获取密钥必须要身份认证 配置单点登录必须要配置
		security.tokenKeyAccess("isAuthenticated()");
	}



	private static class MyWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {
		@Override
		public ResponseEntity<OAuth2Exception> translate(Exception e) {
			CustomOauthException customOauthException = new CustomOauthException(e.getLocalizedMessage());
			customOauthException.addAdditionalInformation("status","false");
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(customOauthException);
		}
	}
}
