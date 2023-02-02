package com.ecommerce.config;

import io.swagger.v3.oas.annotations.info.Contact;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration{


    private static final String BASE_PACKAGE = "com.ecommerce.controller";
    private static final String API_TITLE = "Product Category API";
    private static final String API_DESCRIPTION = "REST API to category management";
    private static final String CONTACT_NAME = "Gabriel Mota Melo";
    private static final String CONTACT_GITHUB = "https://gtihub.com/gabmotamelo";
    private static final String CONTACT_EMAIL = "";

    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group(API_TITLE)
                .packagesToScan(BASE_PACKAGE)
                .build();
    }

}
