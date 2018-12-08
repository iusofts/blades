package com.iusofts.blades.monitor.web.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {

    @ApiOperation("首页")
    @RequestMapping("/index")
    public String input() {
        return "/index";
    }

    @ApiOperation("服务列表")
    @RequestMapping("/services")
    public String services() {
        return "/services";
    }

    @ApiOperation("应用列表")
    @RequestMapping("/applications")
    public String applications() {
        return "/applications";
    }

    @ApiOperation("调用关系")
    @RequestMapping("/relationship")
    public String relationship() {
        return "/relationship";
    }

    @ApiOperation("调用关系")
    @RequestMapping("/servicesRelationship")
    public String servicesRelationship() {
        return "/services-relationship";
    }

    @ApiOperation("授权列表")
    @RequestMapping("/authorizations")
    public String authorizations() {
        return "/authorizations";
    }

    @ApiOperation("服务提供者列表")
    @RequestMapping("/getProviderList")
    public ModelAndView getProviderList(@RequestParam String serviceName) {
        ModelAndView mv = new ModelAndView("/provider-list");
        mv.addObject("serviceName", serviceName);
        return mv;
    }

    @ApiOperation("服务消费者")
    @RequestMapping("/getConsumerList")
    public ModelAndView getConsumerList(@RequestParam String serviceName) {
        ModelAndView mv = new ModelAndView("/consumer-list");
        mv.addObject("serviceName", serviceName);
        return mv;
    }

    @ApiOperation("添加权限")
    @RequestMapping("/addAuthorization")
    public String addAuthorization() {
        return "/add-authorization";
    }

    @ApiOperation("编辑权限")
    @RequestMapping("/editAuthorization")
    public String editAuthorization() {
        return "/edit-authorization";
    }

}
