package com.soho.pos;

import com.soho.pos.dao.BaseDAOFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextListener;

import java.io.IOException;

@EnableTransactionManagement
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.soho.pos.dao", repositoryFactoryBeanClass = BaseDAOFactoryBean.class)
@EntityScan(basePackages = "com.soho.pos.entity")
@SpringBootApplication
public class PosApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PosApplication.class, args);
    }
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
