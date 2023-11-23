package com.soho.pos.config;

import com.soho.pos.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    @Bean
    public AuthInterceptor getSecurityInterceptor() {
        return new AuthInterceptor();
    }
    
    /**
     * 定義攔截配置
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        //排除配置
        addInterceptor.excludePathPatterns("/js/**");
        addInterceptor.excludePathPatterns("/css/**");
        addInterceptor.excludePathPatterns("/images/**");
        addInterceptor.excludePathPatterns("/plugins/**");
        addInterceptor.excludePathPatterns("/doLogin");
        addInterceptor.excludePathPatterns("/doLogOut");
        addInterceptor.excludePathPatterns("/");
        
        //攔截配置
        addInterceptor.addPathPatterns("/**");
    }
    
    /**
     * 新增資源路徑
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/ckfinder/**").addResourceLocations("classpath:/static/plugins/ckfinder/");
    }
}
