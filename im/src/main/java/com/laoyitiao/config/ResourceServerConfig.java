package com.laoyitiao.config;

import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import java.util.HashMap;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		super.configure(resources);
	}

	public ResourceServerConfig() {
		super();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.exceptionHandling()
				.authenticationEntryPoint((request, response, e) -> {
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					response.setCharacterEncoding("UTF-8");
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					HashMap<String, Object> map = new HashMap<>();
					map.put("msg","未认证的请求，请进行登录操作！");
					map.put("status",false);
					response.getWriter().write(JSON.toJSONString(map));
				})
				.accessDeniedHandler((request, response, e) -> {
					response.setStatus(HttpStatus.FORBIDDEN.value());
					response.setCharacterEncoding("UTF-8");
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					HashMap<String, Object> map = new HashMap<>();
					map.put("msg","权限不足，拒绝访问！");
					map.put("status",false);
					response.getWriter().write(JSON.toJSONString(map));
				})
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.csrf().disable();
	}
}
