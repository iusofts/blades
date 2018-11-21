package com.iusofts.blades.monitor;

import com.iusofts.blades.core.BladesDefaultConfig;
import com.iusofts.blades.monitor.web.controller.TestViewController;
import com.iusofts.blades.registry.BladesRegister;
import com.iusofts.blades.registry.initial.BladesInitial;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger.web.ApiResourceController;
import springfox.documentation.swagger2.web.Swagger2Controller;

/**
 * 锦衣卫配置
 */
@Import(BladesDefaultConfig.class)
@Configuration
public class BladesConfig {

    @Value("${blades.serviceName}")
    private String serviceName;
    @Value("${blades.servicePort}")
    private String servicePort;

    @Bean("serviceInitial")
    public BladesInitial createBladesInitial(BladesRegister bladesRegister) throws Exception {
        BladesInitial bladesInitial = new BladesInitial();
        bladesInitial.setPort(servicePort);
        bladesInitial.setGroup(serviceName);
        bladesInitial.setRegister(bladesRegister);
        bladesInitial.addExcludeClass(ApiResourceController.class, Swagger2Controller.class);
        bladesInitial.addExcludeClass(BasicErrorController.class, TestViewController.class);
        return bladesInitial;
    }

}
