package com.iusofts.blades.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class SiteConfig implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SiteConfig.class);

    @Value("${spring.profiles.active}")
    private String profies;


    public void run(String... strings) throws Exception {
        LOG.info("enable profiles: {}", profies);
    }
}
