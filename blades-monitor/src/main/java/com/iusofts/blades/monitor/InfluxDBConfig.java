package com.iusofts.blades.monitor;

import com.iusofts.blades.common.influx.InfluxTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    @Value("${influx.connectUrl}")
    private String connectUrl;

    @Bean
    public InfluxTemplate createInfluxTemplate() {
        return new InfluxTemplate(connectUrl);
    }

}
