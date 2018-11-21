package com.iusofts.blades.monitor.web.controller;

import com.iusofts.blades.monitor.service.dto.*;
import com.iusofts.blades.monitor.service.util.BladesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服务管理
 */
@RequestMapping("/manage")
@RestController
public class ManageController {
	private static Logger logger = LoggerFactory.getLogger(ManageController.class);

	/**
	 * 服务列表
	 * @return
	 */
	@RequestMapping(value = "/getServiceList", method = RequestMethod.POST)
	public List<Service> getServiceList() {
		return BladesUtil.getServiceList();
	}

	/**
	 * 应用列表
	 * @return
	 */
	@RequestMapping(value = "/getApplicationList", method = RequestMethod.POST)
	public List<Application> getApplicationList() {
		return BladesUtil.getApplicationList();
	}

	/**
	 * 添加权限
	 * @return
	 */
	@RequestMapping(value = "/addAuthorization", method = RequestMethod.POST)
	public boolean addAuthorization(@RequestBody Authorization authorization) {
		return BladesUtil.addAuthorization(authorization);
	}

	/**
	 * 编辑权限
	 * @return
	 */
	@RequestMapping(value = "/editAuthorization", method = RequestMethod.POST)
	public boolean editAuthorization(@RequestBody Authorization authorization) {
		return BladesUtil.editAuthorization(authorization);
	}

	/**
	 * 删除权限
	 * @return
	 */
	@RequestMapping(value = "/deleteAuthorization", method = RequestMethod.POST)
	public boolean deleteAuthorization(@RequestParam String appName) {
		return BladesUtil.deleteAuthorization(appName);
	}

	/**
	 * 授权列表
	 * @return
	 */
	@RequestMapping(value = "/getAuthorizationList", method = RequestMethod.POST)
	public List<Authorization> getAuthorizationList() {
		return BladesUtil.getAuthorizationList();
	}

	/**
	 * 服务提供者列表
	 * @param serviceName
	 * @return
	 */
	@RequestMapping(value = "/getProviderList", method = RequestMethod.POST)
	public List<Provider> getProviderList(@RequestParam String serviceName) {
		return BladesUtil.getProviderList(serviceName);
	}

	/**
	 * 服务消费者
	 * @param appName
	 * @return
	 */
	@RequestMapping(value = "/getConsumerList", method = RequestMethod.POST)
	public List<Consumer> getConsumerList(@RequestParam String appName) {
		return BladesUtil.getConsumerList(appName);
	}

	/**
	 * 禁用服务提供者
	 * @param serviceName
	 * @param serviceId
	 * @return
	 */
	@RequestMapping(value = "/disableService", method = RequestMethod.POST)
	public boolean disableService(@RequestParam String serviceName, @RequestParam String serviceId) {
		return BladesUtil.disableService(serviceName, serviceId);
	}

	/**
	 * 启用服务提供者
	 * @param serviceName
	 * @param serviceId
	 * @return
	 */
	@RequestMapping(value = "/enableService", method = RequestMethod.POST)
	public boolean enableService(@RequestParam String serviceName, @RequestParam String serviceId) {
		return BladesUtil.enableService(serviceName, serviceId);
	}

	/**
	 * 刷新缓存
	 * @return
	 */
	@RequestMapping(value = "/reload", method = RequestMethod.POST)
	public String reload() {
		BladesUtil.init();
		return "success";
	}

}
