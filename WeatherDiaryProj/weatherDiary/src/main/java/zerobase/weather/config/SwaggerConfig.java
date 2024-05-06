package zerobase.weather.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("날씨 일기 프로젝트")
                        .description("날씨 일기를 CRUD 할 수 있는 백엔드 API 입니다.")
                        .version("v1.0.0"));
//                        .license(
//                            new License()
//                            .name("Apache 2.0")
//                            .url("http://localhost:8080/swagger-ui/index.html#/")
//                        ));
//                .externalDocs(new ExternalDocumentation()
//                        .description("SpringShop Wiki Documentation")
//                        .url("https://springshop.wiki.github.org/docs"));
    }
}
