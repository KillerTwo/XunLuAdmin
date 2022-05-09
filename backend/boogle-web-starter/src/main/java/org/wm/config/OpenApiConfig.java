package org.wm.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @创建人 sk
 * @创建时间 2022/3/26
 * @描述
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    /*@Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder()
                .group("system")
                .pathsToMatch("/system/**")
                .build();
    }*/

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion) {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("basicScheme",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
                .info(new Info().title("SpringShop API").version(appVersion)
                        .license(new License().name("Apache 2.0")
                                .url("http://springdoc.org")))/*.addTagsItem(new Tag().name("mytag"))*/
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
