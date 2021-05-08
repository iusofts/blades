package com.iusofts.blades.monitor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Ivan
 */
@Configuration
@EnableSwagger2
@Profile({"default", "dev", "test"})//在生产环境不开启
public class Swagger2Config {

    @Bean(value = "default API")
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(userApiInfo())
                .groupName("default")
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.iusofts.blades.monitor.web.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo userApiInfo() {
        return new ApiInfoBuilder()
                .title("接口文档")//大标题
                .version("1.0")//版本
                .build();
    }


}
