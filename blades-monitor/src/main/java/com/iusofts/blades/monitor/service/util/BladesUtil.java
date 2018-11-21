package com.iusofts.blades.monitor.service.util;

import com.iusofts.blades.common.domain.ServiceInstanceDetail;
import com.iusofts.blades.common.util.JsonUtils;
import com.iusofts.blades.monitor.common.zookeeper.JavaApiSample;
import com.iusofts.blades.monitor.service.dto.*;
import com.iusofts.blades.monitor.service.enums.ApplicationType;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.x.discovery.ServiceInstance;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

/**
 * 测试
 *
 * @author Ivan Shen
 */
@Component
public class BladesUtil {

	private static int SESSION_TIMEOUT = 10000;
	private static String CONNECTION_STRING = "127.0.0.1:2181";
	private static String ZK_SERVICE_PATH = "/blades_dev/services";
	private static String ZK_CONFIG_PATH = "/blades_dev/config";
	private static String PATH_SUFFIX = "/";
	private static String AUTHORIZATION_SUFFIX = ".authorization";

	private static Logger log = LoggerFactory.getLogger(BladesUtil.class);

	// 所有服务数据
	private static final Map<String, Map<String, ServiceInstance<ServiceInstanceDetail>>> serviceDataMap = new HashMap<>();
	// 消费者集合
	private static final Map<String, String[]> consumerMap = new HashMap<>();

	private static ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * 初始化数据
	 */
	public static void init() {
		JavaApiSample sample = new JavaApiSample();
		sample.createConnection(CONNECTION_STRING, SESSION_TIMEOUT);

		// 加载源数据
		List<ServiceInstance<ServiceInstanceDetail>> serviceDataList = new ArrayList<>();

		try {

			// 加载服务数据
			List<String> serviceFileNames = sample.redChildList(ZK_SERVICE_PATH);
			// System.err.println("serviceFileNames:" + serviceFileNames);
			if (!CollectionUtils.isEmpty(serviceFileNames)) {
				for (String serviceFileName : serviceFileNames) {
					String serviceFilePath = ZK_SERVICE_PATH + PATH_SUFFIX + serviceFileName;// 服务文件路径
					List<String> serviceProviderFileNames = sample.redChildList(serviceFilePath);
					if (!CollectionUtils.isEmpty(serviceProviderFileNames)) {
						Map<String, ServiceInstance<ServiceInstanceDetail>> serviceProviderMap = new HashMap<>();
						for (String serviceProviderFileName : serviceProviderFileNames) {
							String serviceProviderData = sample
									.readData(serviceFilePath + PATH_SUFFIX + serviceProviderFileName);
							if(StringUtils.isNotBlank(serviceProviderData)){
								ServiceInstance<ServiceInstanceDetail> serviceInstance = objectMapper.readValue(
										serviceProviderData, new TypeReference<ServiceInstance<ServiceInstanceDetail>>() {
										});
								serviceDataList.add(serviceInstance);
								serviceProviderMap.put(serviceProviderFileName, serviceInstance);
							}
						}
						serviceDataMap.put(serviceFileName, serviceProviderMap);
					}
				}
			}

			// 加载授权数据
			List<String> configFileNames = sample.redChildList(ZK_CONFIG_PATH);
			// System.err.println("configFileNames:" + configFileNames);

			List<Link> links = new ArrayList<>();
			for (String configFileName : configFileNames) {
				if (configFileName.indexOf(AUTHORIZATION_SUFFIX) != -1) {
					String strs[] = configFileName.split(AUTHORIZATION_SUFFIX);
					if (strs.length > 0) {
						String configData = sample.readData(ZK_CONFIG_PATH + PATH_SUFFIX + configFileName);
						if (StringUtils.isNotBlank(configData)) {
							String[] consumers = configData.split(",");
							consumerMap.put(strs[0], consumers);
						}
					}

				}
			}

			log.info("Loaded zk hystrix data");
		} catch (IOException e) {
			e.printStackTrace();
		}

		sample.releaseConnection();
	}

	/**
	 * 获取服务列表
	 *
	 * @return
	 */
	public static List<Service> getServiceList() {
		if (serviceDataMap.size() == 0) {
			init();
		}
		// 服务列表
		List<Service> serviceList = new ArrayList<>();
		for (String key : serviceDataMap.keySet()) {
			Service service = new Service();
			service.setName(key);
			if (serviceDataMap.get(key) != null) {
				service.setProviderAmount(serviceDataMap.get(key).size());
				for (String key2 : serviceDataMap.get(key).keySet()) {
					ServiceInstance<ServiceInstanceDetail> serviceInstance = serviceDataMap.get(key).get(key2);
					if (serviceInstance != null) {
						service.setGroup(serviceInstance.getPayload().getServiceGroup());
						break;
					}
				}
			}

			// 订阅应用数
			if (consumerMap.get(service.getGroup()) != null && consumerMap.get(service.getGroup()) != null) {
				service.setConsumerAmount(consumerMap.get(service.getGroup()).length);
			} else {
				service.setConsumerAmount(0);
			}

			serviceList.add(service);
		}
		return serviceList;
	}

	/**
	 * 获取应用列表
	 *
	 * @return
	 */
	public static List<Application> getApplicationList() {
		if (serviceDataMap.size() == 0) {
			init();
		}
		// 应用列表
		List<Application> applicationList = new ArrayList<>();
		Map<String, Application> appMap = new HashMap<>();

		// 提供者
		for (Service service : getServiceList()) {
				if(service!=null && StringUtils.isNotBlank(service.getGroup())){
				if (!appMap.containsKey(service.getGroup())) {
					Application application = new Application(service.getGroup());
					application.setType(ApplicationType.PROVIDER);
					applicationList.add(application);
					appMap.put(service.getGroup(), application);
				} else {
					appMap.get(service.getGroup()).incrProvideServiceAmount();
				}
			}
		}

		//没有提供者的应用
		for (String key : consumerMap.keySet()){
			if(!appMap.containsKey(key)){
				Application application = new Application(key);
				application.setInactive(true);
				application.setType(ApplicationType.PROVIDER);
				application.setProvideServiceAmount(0);
				application.setConsumeServiceAmount(0);
				applicationList.add(application);
			}
		}

		//FIXME 目前的消费者信息是来自于授权列表里面 待服务调用监控完善后更换消费者数据来源
		//FIXME 不同与dubbo使用前建立长连接即可监控到消费者 hystrix只能先调用后才能监控到消费者
		// 既是消费者也是提供者
		Set<String> consumerSet = new HashSet<>();
		for (String key : consumerMap.keySet()) {
			for (String consumerName : consumerMap.get(key)) {
				consumerSet.add(consumerName);
			}
		}
		for (Application application : applicationList) {
			if (consumerSet.contains(application.getAppName())) {
				application.setType(ApplicationType.PROVIDER_AND_CONSUMER);
				consumerSet.remove(application.getAppName());
			}
		}

		// 消费者
		for (String consumerName : consumerSet) {
			Application application = new Application(consumerName);
			application.setType(ApplicationType.CONSUMER);
			applicationList.add(application);
		}

		// 计算应用可消费服务数
		for (Application application : applicationList) {
			if (application.getType() == ApplicationType.CONSUMER
					|| application.getType() == ApplicationType.PROVIDER_AND_CONSUMER) {
				int count = 0;
				for (String key : consumerMap.keySet()) {
					for (String consumerName : consumerMap.get(key)) {
						if (application.getAppName().equals(consumerName)) {
							if (appMap.get(key) != null) {
								count += appMap.get(key).getProvideServiceAmount();
							}
							break;
						}
					}
				}
				application.setConsumeServiceAmount(count);
			}
		}

		return applicationList;
	}

	/**
	 * 获取授权列表
	 *
	 * @return
	 */
	public static List<Authorization> getAuthorizationList() {
		List<Authorization> authorizationList = new ArrayList<>();
		for (String key : consumerMap.keySet()) {
			Authorization authorization = new Authorization();
			authorization.setAppName(key);
			authorization.setAllowAppNames(consumerMap.get(key));
			authorizationList.add(authorization);
		}
		return authorizationList;
	}

	/**
	 * 添加权限
	 *
	 * @return
	 */
	public static boolean addAuthorization(Authorization authorization) {
		boolean bool = false;
		if (authorization != null && StringUtils.isNotBlank(authorization.getAppName())) {
			JavaApiSample sample = new JavaApiSample();
			sample.createConnection(CONNECTION_STRING, SESSION_TIMEOUT);
			String path = ZK_CONFIG_PATH + PATH_SUFFIX + authorization.getAppName() + AUTHORIZATION_SUFFIX;
			if (!sample.exist(path)) {//不存在新增
				bool = sample.createPath(path, StringUtils.join(authorization.getAllowAppNames(), ","));
				consumerMap.put(authorization.getAppName(),authorization.getAllowAppNames());
			}
			sample.releaseConnection();
		}
		return bool;
	}

	/**
	 * 编辑权限
	 *
	 * @return
	 */
	public static boolean editAuthorization(Authorization authorization) {
		boolean bool = false;
		if (authorization != null && StringUtils.isNotBlank(authorization.getAppName())) {
			JavaApiSample sample = new JavaApiSample();
			sample.createConnection(CONNECTION_STRING, SESSION_TIMEOUT);
			String path = ZK_CONFIG_PATH + PATH_SUFFIX + authorization.getAppName() + AUTHORIZATION_SUFFIX;
			if (sample.exist(path)) {// 存在修改
				bool = sample.writeData(path, StringUtils.join(authorization.getAllowAppNames(), ","));
				consumerMap.put(authorization.getAppName(),authorization.getAllowAppNames());
			}
			sample.releaseConnection();
		}
		return bool;
	}

	/**
	 * 删除权限
	 *
	 * @return
	 */
	public static boolean deleteAuthorization(String appName) {
		boolean bool = false;
		if (StringUtils.isNotBlank(appName)) {
			JavaApiSample sample = new JavaApiSample();
			sample.createConnection(CONNECTION_STRING, SESSION_TIMEOUT);
			String path = ZK_CONFIG_PATH + PATH_SUFFIX + appName + AUTHORIZATION_SUFFIX;
			if (sample.exist(path)) {// 存在修改
				sample.deleteNode(path);
				consumerMap.remove(appName);
				bool = true;
			}
			sample.releaseConnection();
		}
		return bool;
	}

	/**
	 * 获取服务提供者列表
	 *
	 * @param serviceName
	 * @return
	 */
	public static List<Provider> getProviderList(String serviceName) {
		Map<String, ServiceInstance<ServiceInstanceDetail>> serviceInstanceMap = serviceDataMap.get(serviceName);
		List<Provider> providerList = new ArrayList<>();
		if (serviceInstanceMap != null) {
			for (String key : serviceInstanceMap.keySet()) {
				ServiceInstance<ServiceInstanceDetail> serviceInstance = serviceInstanceMap.get(key);
				if (serviceInstance != null) {
					Provider provider = new Provider();
					provider.setId(key);
					provider.setAppName(serviceInstance.getPayload().getServiceGroup());
					provider.setAddress(serviceInstance.getAddress());
					provider.setPort(serviceInstance.getPort());
					provider.setDisable(serviceInstance.getPayload().isIsolated());
					providerList.add(provider);
				}
			}
		}
		return providerList;
	}

	/**
	 * 获取服务消费者
	 *
	 * @param appName
	 * @return
	 */
	public static List<Consumer> getConsumerList(String appName) {
		List<Consumer> consumerList = new ArrayList<>();
		if (StringUtils.isNotBlank(appName)) {
			String consumerStrs[] = consumerMap.get(appName);
			if (consumerStrs != null) {
				for (String consumerStr : consumerStrs) {
					Consumer consumer = new Consumer();
					consumer.setAppName(consumerStr);
					consumerList.add(consumer);
				}
			}
		}
		return consumerList;
	}

	/**
	 * 禁用服务
	 *
	 * @param serviceId
	 *            服务ID
	 * @return
	 */
	public static boolean disableService(String serviceName, String serviceId) {
		return updateServiceIsolated(serviceName, serviceId, true);
	}

	/**
	 * 启动服务
	 *
	 * @param serviceId
	 * @return
	 */
	public static boolean enableService(String serviceName, String serviceId) {
		return updateServiceIsolated(serviceName, serviceId, false);
	}

	/**
	 * 更新服务状态
	 *
	 * @param serviceId
	 * @param isolated
	 * @return
	 */
	public static boolean updateServiceIsolated(String serviceName, String serviceId, boolean isolated) {
		if (StringUtils.isNotBlank(serviceId)) {
			JavaApiSample sample = new JavaApiSample();
			try {
				sample.createConnection(CONNECTION_STRING, SESSION_TIMEOUT);
				String servidePath = ZK_SERVICE_PATH + PATH_SUFFIX + serviceName + PATH_SUFFIX + serviceId;
				String serviceProviderData = sample.readData(servidePath);
				ServiceInstance<ServiceInstanceDetail> serviceInstance = objectMapper.readValue(serviceProviderData,
						new TypeReference<ServiceInstance<ServiceInstanceDetail>>() {
						});
				if (serviceInstance != null && serviceInstance.getPayload() != null) {
					serviceInstance.getPayload().setIsolated(isolated);
					log.info("updateServiceIsolated serviceId:{},isolated:{}" + isolated);
					sample.writeData(servidePath, JsonUtils.obj2json(serviceInstance));

					// 刷新缓存
					Map<String, ServiceInstance<ServiceInstanceDetail>> serviceInstanceMap = serviceDataMap
							.get(serviceName);
					if (serviceInstanceMap != null) {
						ServiceInstance<ServiceInstanceDetail> serviceInstanceDetail = serviceInstanceMap
								.get(serviceId);
						if (serviceInstanceDetail != null) {
							serviceInstanceDetail.getPayload().setIsolated(isolated);
						}
					}
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				sample.releaseConnection();
			}
		}
		return false;
	}

	public static void getPraph() {

		List<Link> links = new ArrayList<>();
		for (String key : consumerMap.keySet()) {
			for (String consumer : consumerMap.get(key)) {
				Link link = new Link();
				link.setTarget(key);
				link.setSource(consumer);
				link.setName(consumer + "调用" + key);
				link.setWeight(1);
				links.add(link);
			}
		}

		System.err.println(JsonUtils.obj2json(links));
	}

	// 以下注入静态属性
	@Value("${blades.zkAddress}")
	public void setZkAddress(String zkAddress) {
		CONNECTION_STRING = zkAddress;
	}

	@Value("${blades.defaultTimeOut}")
	public void setDefaultTimeOut(String defaultTimeOut) {
		if (StringUtils.isBlank(defaultTimeOut)) {
			SESSION_TIMEOUT = Integer.parseInt(defaultTimeOut);
		}
	}

	@Value("${blades.servicePath}")
	public void setBbasePath(String basePath) {
		ZK_SERVICE_PATH = basePath;
	}

	@Value("${blades.configPath}")
	public void setConfigPath(String configPath) {
		ZK_CONFIG_PATH = configPath;
	}

	public static void main(String[] args) throws IOException {
		// enableService("account-web.api-docs.{swaggerGroup}.{apiDeclaration}",
		// "6e3f3ce7-7751-45b1-90e9-8b037bab7bfd");

		/*Authorization authorization = new Authorization();
		authorization.setAppName("user-web");
		authorization.setAllowAppNames(new String[] { "oms-web,account-web" });
		editAuthorization(authorization);*/

		//deleteAuthorization("user-web");

		getApplicationList();
	}

}
