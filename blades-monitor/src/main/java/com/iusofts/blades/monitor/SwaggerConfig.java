package com.iusofts.blades.monitor;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableSwagger2 // 启用 Swagger
@Profile({"default", "dev", "test"})//在生产环境不开启
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        Predicate<RequestHandler> predicate = input -> {
            Class<?> declaringClass = input.declaringClass();
            if (declaringClass == BasicErrorController.class)// 排除
                return false;
            if(declaringClass.isAnnotationPresent(RestController.class)) // 被注解的类
                return true;
            if(input.isAnnotatedWith(ResponseBody.class)) // 被注解的方法
                return true;
            return false;
        };
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(predicate)
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("blades monitor 接口文档")//大标题
                .version("1.0")//版本
                .build();
    }

}
