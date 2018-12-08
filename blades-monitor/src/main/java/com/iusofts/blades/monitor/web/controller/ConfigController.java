package com.iusofts.blades.monitor.web.controller;

import com.iusofts.blades.monitor.inft.ConfigInterface;
import com.iusofts.blades.monitor.service.model.ApplicationConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服务配置
 */
@Api(description = "服务配置")
@RequestMapping("/config")
@RestController
public class ConfigController {

    private static Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private ConfigInterface configInterface;

    @ApiOperation("获取应用配置")
    @RequestMapping(value = "/getApplicationConfig", method = RequestMethod.GET)
    public List<ApplicationConfig> getApplicationConfig(@RequestParam String appName) {
        return configInterface.getApplicationConfig(appName);
    }

    @ApiOperation("获取应用配置")
    @RequestMapping(value = "/saveApplicationConfig", method = RequestMethod.POST)
    public void saveApplicationConfig(@RequestParam String appName, @RequestBody List<ApplicationConfig> configs) {
        configInterface.saveApplicationConfig(appName, configs);
    }

}
