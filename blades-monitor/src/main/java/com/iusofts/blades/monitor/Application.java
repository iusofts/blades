package com.iusofts.blades.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

public class Application  extends SpringBootServletInitializer {

    public static void main(String... args) {
        SpringApplication.run(SiteConfig.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SiteConfig.class);
    }
}
