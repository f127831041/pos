package com.soho.pos.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @Author viper
 * @Date 2023/5/31 下午 01:13
 * @Version 1.0
 */
@Configuration
public class SessionConfig implements HttpSessionListener {
    
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setMaxInactiveInterval(0);
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // 在会话销毁时执行的逻辑
    }
}
