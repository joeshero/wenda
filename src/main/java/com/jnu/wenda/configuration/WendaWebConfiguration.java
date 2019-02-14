package com.jnu.wenda.configuration;

import com.jnu.wenda.intercepter.LoginRequiredIntercepter;
import com.jnu.wenda.intercepter.PassportIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by Joe
 */
@Configuration
public class WendaWebConfiguration implements WebMvcConfigurer {

    @Autowired
    private PassportIntercepter passportIntercepter;

    @Autowired
    private LoginRequiredIntercepter loginRequiredIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //拦截器一：会话维持
        registry.addInterceptor(passportIntercepter);
        //拦截器二：未登录跳转
        registry.addInterceptor(loginRequiredIntercepter).addPathPatterns("/user/*");
    }
}
