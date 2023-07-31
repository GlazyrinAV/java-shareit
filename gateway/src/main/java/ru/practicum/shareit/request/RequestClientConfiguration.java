package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RequestClientConfiguration {

    @Value("${shareit-server.url}")
    private String serverUrl;

    @Value("/requests")
    private String API_PREFIX;

    @Bean
    public RequestClient requestClient(RestTemplateBuilder builder) {
        var restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .build();
        return new RequestClient(restTemplate);
    }

}