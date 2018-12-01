package com.iusofts.blades.monitor.web.controller;

import com.iusofts.blades.common.util.JsonUtils;
import com.iusofts.blades.monitor.inft.MonitorInterface;
import com.iusofts.blades.monitor.web.vo.MonitorRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 服务监控
 */
@Api(description = "服务监控")
@RequestMapping("/monitor")
@RestController
public class MonitorController {

    private static Logger logger = LoggerFactory.getLogger(MonitorController.class);

    @Autowired
    private MonitorInterface monitorInterface;

    @ApiOperation("调用记录")
    @RequestMapping(value = "/record", method = RequestMethod.POST)
    public void record(@RequestBody MonitorRecordVo recordVo) {
        logger.info("monitor record:{}", JsonUtils.obj2json(recordVo));// 临时设置info级别
        monitorInterface.monitor(recordVo);
    }

}
