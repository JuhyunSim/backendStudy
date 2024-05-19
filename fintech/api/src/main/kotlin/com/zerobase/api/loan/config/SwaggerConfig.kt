package com.zerobase.api.loan.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {
    // http://localhost:8080/swagger-ui/index.html#/
    @Bean
    fun springShopOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info().title("대출 심사 프로젝트")
                    .description("대출 요건을 심사 절차를 구현한 프로젝트")
                    .version("v0.0.1 ")
            )
    }
}