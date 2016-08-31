package com.zale.shortlink.web.cfg;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.cardsmart.inf.controller.dubbo.DubboController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Created by Zale on 16/6/29.
 */
@Configuration
public class WebConfiguration {
    @Bean
    public FilterRegistrationBean webFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        registration.setFilter(characterEncodingFilter);
        return registration;
    }

    @Bean
    public HttpMessageConverters messageConverters() {
        System.out.println(address);
        FastJsonHttpMessageConverter fastjson = new FastJsonHttpMessageConverter();
        FormHttpMessageConverter httpMessageConverter = new FormHttpMessageConverter();

        return new HttpMessageConverters(fastjson, httpMessageConverter);
    }

    @Value("${zookeeper.address}")
    private String address;

    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("shortlink-dmz");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("zookeeper");
        registry.setAddress(address);
        return registry;
    }

    @Bean
    public ReferenceConfig referenceConfig() {
        ReferenceConfig config = new ReferenceConfig();
        config.setApplication(applicationConfig);
        config.setRegistry(registryConfig);
        config.setInterface("com.cardsmart.inf.controller.dubbo.DubboController");
        config.setVersion("1.0");
        config.setTimeout(3000);
        config.setId("dubboContorller");
        config.setCheck(false);
        return config;
    }

    @Autowired
    private ApplicationConfig applicationConfig;
    @Autowired
    private RegistryConfig registryConfig;
    @Autowired
    private ReferenceConfig referenceConfig;

    @Bean
    public DubboController dubboController() {
        return (DubboController) referenceConfig.get();
    }
}
