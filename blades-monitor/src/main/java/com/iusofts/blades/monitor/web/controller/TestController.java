package com.iusofts.blades.monitor.web.controller;

import com.iusofts.blades.core.finder.ServiceCaller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "测试模块", description = "测试模块描述")
@RestController
public class TestController {

    @Resource
    private ServiceCaller serviceCaller;

    @ApiOperation("测试接口")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public TestVO input(@RequestBody TestVO vo) {
        vo.setData("test");
        return vo;
    }

    @ApiOperation("测试服务接口")
    @RequestMapping(value = "/testService", method = RequestMethod.POST)
    public TestVO testService(@RequestBody TestVO vo) {
        return serviceCaller.post("blades-monitor.test", vo, TestVO.class);
    }

}
