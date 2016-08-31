package com.zale.shortlink.web.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by Zale on 16/6/29.
 */
@SpringBootApplication(excludeName = { "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration","org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration"})
@EnableScheduling
@ComponentScan({ "com.zale.shortlink", "com.cardsmart.esbsdk" })
//@ImportResource("classpath:dubboContext.xml")
public class WebApplication {
    public static void main(String[] args) {

        SpringApplication.run(WebApplication.class, args);
    }

}
