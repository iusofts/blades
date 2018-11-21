package com.iusofts.blades.monitor.web.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @ApiOperation("首页")
    @RequestMapping("/index")
    public String input(){
        return "/index";
    }

    @ApiOperation("服务列表")
    @RequestMapping("/services")
    public String services(){
        return "/services";
    }

    @ApiOperation("应用列表")
    @RequestMapping("/applications")
    public String applications(){
        return "/applications";
    }

    @ApiOperation("授权列表")
    @RequestMapping("/authorizations")
    public String authorizations(){
        return "/authorizations";
    }


}
