package com.iusofts.blades.monitor.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务监控
 */
@RestController
public class MonitorController {
    private static Logger logger = LoggerFactory.getLogger(MonitorController.class);

    /**
     * 调用记录
     *
     * @param group
     * @param serviceName
     * @param success
     * @return
     */
    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public void monitor(@RequestParam String group, @RequestParam String serviceName, @RequestParam boolean success) {
        logger.info("group:{},serviceName:{},success{}", group, serviceName, success);
    }

}
