package com.iusofts.blades.monitor.service;

import com.iusofts.blades.monitor.BaseTest;
import com.iusofts.blades.monitor.service.model.ApplicationConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ConfigServiceTest extends BaseTest {

    @Autowired
    private ConfigService configService;

    @Test
    public void getApplicationConfig() throws Exception {
        configService.getApplicationConfig("basic");
    }

    @Test
    public void saveApplicationConfig() throws Exception {
        configService.saveApplicationConfig("basic", Arrays.asList(new ApplicationConfig("hystrix.threadpool.basic.coreSize", "12")));
    }

}