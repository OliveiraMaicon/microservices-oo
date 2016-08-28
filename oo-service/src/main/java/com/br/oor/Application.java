package com.br.oor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@EnableDiscoveryClient
@SpringBootApplication
public class Application {


    private static final String RESOURCE_BUNDLE_MESSAGE_NAME = "messages";
    private static final String ENCODING = "UTF-8";

    

    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasename(RESOURCE_BUNDLE_MESSAGE_NAME);
        messageSource.setDefaultEncoding(ENCODING);

        return messageSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
