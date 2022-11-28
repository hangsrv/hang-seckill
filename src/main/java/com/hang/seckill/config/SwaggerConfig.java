package com.hang.seckill.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootConfiguration
public class SwaggerConfig {
    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("秒杀案例")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hang.seckill.controller"))
                .build();
    }

}
