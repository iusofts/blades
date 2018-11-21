package com.iusofts.blades.monitor.web.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestViewController {

    @ApiOperation("测试页面")
    @RequestMapping("/index")
    public String input(){
        return "/index";
    }


}
