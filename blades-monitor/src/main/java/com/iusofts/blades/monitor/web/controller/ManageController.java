package com.iusofts.blades.monitor.web.controller;

import com.iusofts.blades.monitor.inft.ManageInterface;
import com.iusofts.blades.monitor.inft.dto.*;
import com.iusofts.blades.monitor.service.model.ServiceConsumerInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(description = "服务管理")
@RequestMapping("/manage")
@RestController
public class ManageController {

	private static Logger logger = LoggerFactory.getLogger(ManageController.class);

	@Resource
	private ManageInterface manageInterface;

	@ApiOperation("服务列表")
	@RequestMapping(value = "/getServiceList", method = RequestMethod.POST)
	public List<ServiceInfo> getServiceList() {
		return manageInterface.getServiceList();
	}

	@ApiOperation("应用列表")
	@RequestMapping(value = "/getApplicationList", method = RequestMethod.POST)
	public List<Application> getApplicationList() {
		return manageInterface.getApplicationList();
	}

	@ApiOperation("添加权限")
	@RequestMapping(value = "/addAuthorization", method = RequestMethod.POST)
	public boolean addAuthorization(@RequestBody Authorization authorization) {
		return manageInterface.addAuthorization(authorization);
	}

	@ApiOperation("编辑权限")
	@RequestMapping(value = "/editAuthorization", method = RequestMethod.POST)
	public boolean editAuthorization(@RequestBody Authorization authorization) {
		return manageInterface.editAuthorization(authorization);
	}

	@ApiOperation("删除权限")
	@RequestMapping(value = "/deleteAuthorization", method = RequestMethod.POST)
	public boolean deleteAuthorization(@RequestParam String appName) {
		return manageInterface.deleteAuthorization(appName);
	}

	@ApiOperation("授权列表")
	@RequestMapping(value = "/getAuthorizationList", method = RequestMethod.POST)
	public List<Authorization> getAuthorizationList() {
		return manageInterface.getAuthorizationList();
	}

	@ApiOperation("服务提供者列表")
	@RequestMapping(value = "/getProviderList", method = RequestMethod.POST)
	public List<Provider> getProviderList(@RequestParam String serviceName) {
		return manageInterface.getProviderList(serviceName);
	}

	@ApiOperation("服务消费者")
	@RequestMapping(value = "/getConsumerList", method = RequestMethod.POST)
	public List<ServiceConsumerInfo> getConsumerList(@RequestParam String serviceName) {
		return manageInterface.getConsumerList(serviceName);
	}

	@ApiOperation("禁用服务提供者")
	@RequestMapping(value = "/disableService", method = RequestMethod.POST)
	public boolean disableService(@RequestParam String serviceName, @RequestParam String serviceId) {
		return manageInterface.disableService(serviceName, serviceId);
	}

	@ApiOperation("启用服务提供者")
	@RequestMapping(value = "/enableService", method = RequestMethod.POST)
	public boolean enableService(@RequestParam String serviceName, @RequestParam String serviceId) {
		return manageInterface.enableService(serviceName, serviceId);
	}

	@ApiOperation("刷新缓存")
	@RequestMapping(value = "/reload", method = RequestMethod.GET)
	public String reload() {
		manageInterface.init();
		return "success";
	}

}
