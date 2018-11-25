package com.iusofts.blades.monitor.web.controller;

import com.iusofts.blades.monitor.web.vo.MonitorRecordVo;
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
    public void monitor(MonitorRecordVo recordVo) {
        logger.info("monitor record serviceName:{},success{},costTime:{},consumerName:{},consumerIP:{},consumerPort:{},hostName{}",
                recordVo.getServiceName(), recordVo.isSuccess(), recordVo.getCostTime(), recordVo.getConsumerName(), recordVo.getConsumerIP(), recordVo.getConsumerPort(), recordVo.getHostName());
//        logger.info("monitor record serviceName:{},success{},costTime:{},consumerName:{},consumerIP:{},consumerPort:{},hostName{}",
//                serviceName, success, costTime, consumerName, consumerIP, consumerPort, hostName);
    }

}
