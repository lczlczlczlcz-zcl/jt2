package com.jt.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jt.interceptor.UserInterceptor;

//该配置类,实质就是web.xml配置文件
@Configuration
public class MvcConfigurer implements WebMvcConfigurer{
	
	//开启匹配后缀型配置
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		//5.2.4以后不建议使用,但是加上依然有效果.
		configurer.setUseSuffixPatternMatch(true);
	}
	
	@Autowired
	private UserInterceptor userInterceptor;
	
	//新增拦截器  不登录 不允许访问购物车/订单业务
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		//将自己的拦截器添加到注册中心. 拦截器需要设定拦截路径
		//  /cart/** 购物的多级目录     /cart/* 购物车一级目录
		//例子: /cart/aaa/bbb/ccc
		registry.addInterceptor(userInterceptor)
				.addPathPatterns("/cart/**","/order/**");
	}
	
	
	
	
	
	
}
