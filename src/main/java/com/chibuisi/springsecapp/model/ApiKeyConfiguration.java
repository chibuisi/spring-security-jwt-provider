package com.chibuisi.springsecapp.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiKeyConfiguration {
    @Value("${api.key}")
    private String apiKey;

    @Value("${api.client.id.ui}")
    private String apiClientIdUI;

    @Bean
    public String getApiKey() {
        return apiKey;
    }
    @Bean
    public String getApiClientIdUI() {
        return apiClientIdUI;
    }
}

