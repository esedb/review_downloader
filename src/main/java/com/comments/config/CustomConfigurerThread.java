package com.comments.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.DispatcherServlet;



@Configuration
public class CustomConfigurerThread implements WebApplicationInitializer  {
	
	@Override
	public void onStartup(ServletContext ctx) throws ServletException {
		DispatcherServlet servlet = new DispatcherServlet();
		ServletRegistration.Dynamic registration = ctx.addServlet("dispatcher", servlet);
		registration.setAsyncSupported(true);
		
	}

}
