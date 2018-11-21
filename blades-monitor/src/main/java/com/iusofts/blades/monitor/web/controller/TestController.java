package com.iusofts.blades.monitor.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "测试模块",description = "测试模块描述")
@RestController
public class TestController {

    @ApiOperation("测试接口")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public TestJsonVO input(@RequestBody TestJsonVO jsonVO){
        return jsonVO;
    }


}
