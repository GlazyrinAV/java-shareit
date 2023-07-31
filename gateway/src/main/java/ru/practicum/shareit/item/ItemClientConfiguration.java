package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class ItemClientConfiguration {

    @Value("${shareit-server.url}")
    private String serverUrl;

    @Value("/items")
    private String api;

    @Bean
    public ItemClient itemClient(RestTemplateBuilder builder) {
        var restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + api))
                .build();
        return new ItemClient(restTemplate);
    }

}