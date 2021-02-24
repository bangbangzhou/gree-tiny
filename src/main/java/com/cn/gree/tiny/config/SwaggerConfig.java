package com.cn.gree.tiny.config;


import com.cn.gree.tiny.common.baseconfig.BaseSwaggerConfig;
import com.cn.gree.tiny.common.demain.SwaggerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

///**
// * Swagger API文档相关配置
// *
// */
///**
// * Swagger2API文档的配置
// */
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//    @Bean
//    public Docket createRestApi(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                //为当前包下controller生成API文档
//                .apis(RequestHandlerSelectors.basePackage("com.cn.gree.tiny.modules.admin.controller"))
//                //为有@Api注解的Controller生成API文档
////                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
//                //为有@ApiOperation注解的方法生成API文档
////                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("SwaggerUI演示")
//                .description("gree-tiny")
//                .contact("zbb")
//                .version("1.0")
//                .build();
//    }
//}

/**
 * Swagger API文档相关配置
 * Created by macro on 2018/4/26.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.cn.gree.tiny.modules")
                .title("gree-tiny项目骨架")
                .description("gree-tiny项目骨架相关接口文档")
                .contactName("zbb")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
