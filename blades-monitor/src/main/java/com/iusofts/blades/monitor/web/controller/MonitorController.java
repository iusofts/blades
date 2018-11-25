package com.iusofts.blades.monitor.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务监控
 */
@Api(description = "服务监控")
@RequestMapping("/monitor")
@RestController
public class MonitorController {
    private static Logger logger = LoggerFactory.getLogger(MonitorController.class);

    @ApiOperation("调用记录")
    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public void monitor(@RequestParam String serviceName, @RequestParam boolean success,
                        @RequestParam String consumerName, @RequestParam String consumerIP, @RequestParam String consumerPort) {
        logger.info("serviceName:{},success{},consumerName:{},consumerIP:{},consumerPort:{}",
                serviceName, success, consumerName, consumerIP, consumerPort);
    }

}
