package com.iusofts.blades.monitor;

import com.iusofts.blades.core.BladesDefaultConfig;
import com.iusofts.blades.monitor.web.controller.ManageController;
import com.iusofts.blades.monitor.web.controller.ViewController;
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

    @Value("${blades.application.name}")
    private String applicationName;
    @Value("${blades.application.port}")
    private String applicationPort;

    @Bean("serviceInitial")
    public BladesInitial createBladesInitial(BladesRegister bladesRegister) throws Exception {
        BladesInitial bladesInitial = new BladesInitial();
        bladesInitial.setPort(applicationPort);
        bladesInitial.setGroup(applicationName);
        bladesInitial.setRegister(bladesRegister);
        // 过滤掉不想暴露出去的控制器
        bladesInitial.addExcludeClass(ApiResourceController.class, Swagger2Controller.class);
        bladesInitial.addExcludeClass(BasicErrorController.class, ViewController.class);
        bladesInitial.addExcludeClass(ManageController.class);
        return bladesInitial;
    }

}
