package com.icefruit.courseteachingsystem.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return  new OpenAPI()
                .info(new Info().title("课程教学系统 Restful API")
                        .description("课程教学系统 Restful API")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringDoc Wiki Documentation")
                        .url("https://springdoc.org/v2"));
    }



//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.icefruit.courseteachingsystem.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiEndPointsInfo())
//                .useDefaultResponseMessages(false);
//    }
//
//    private ApiInfo apiEndPointsInfo() {
//        return new ApiInfoBuilder().title("课程教学系统 REST API")
//                .description("课程教学系统 REST API")
//                .license("The MIT License")
//                .licenseUrl("https://opensource.org/licenses/MIT")
//                .version("V1")
//                .build();
//    }
}
