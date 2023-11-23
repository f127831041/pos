package com.soho.pos.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class StringJsonTrimConfig {
    /**
     * 过滤JSON数据
     *
     * @return
     */
    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        SimpleModule module = new SimpleModule();
        //自定义序列化过滤配置(XssStringJsonDeserializer), 对入参进行转译
        module.addDeserializer(String.class, new StringJsonDeserializer());
        // 注册解析器
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        objectMapper.registerModule(module);
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}
