package com.scu.hufu;


import com.scu.hufu.config.JwtFilter;
import com.scu.hufu.config.RequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class PhototabserverApplication {


	public static void main(String[] args) {

		SpringApplication.run(PhototabserverApplication.class, args);


	}


	/*
	* 修改默认的dispatcher
	* 只接受以.action结尾的请求
	*
	*/
	@Bean
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
		registration.getUrlMappings().clear();
		registration.addUrlMappings("*.action");
		return registration;
	}

	//注册url请求过滤器
	@Bean
	public FilterRegistrationBean servletRequestRegistrationBean() {
		FilterRegistrationBean bean= new FilterRegistrationBean(new RequestFilter());
		bean.addUrlPatterns("/*");
		bean.setOrder(1);
		return bean;
	}

	//注册token JWT过滤器
	@Bean
	public FilterRegistrationBean jwtFilterRegistrationBean(){
		FilterRegistrationBean bean=new FilterRegistrationBean(new JwtFilter());
		bean.addUrlPatterns("/session/token/*");
		bean.setOrder(2);
		return bean;
	}

}

