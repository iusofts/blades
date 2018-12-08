package com.iusofts.blades.monitor.service;

import com.iusofts.blades.common.domain.ServiceInstanceDetail;
import com.iusofts.blades.common.zookeeper.JavaApiSample;
import com.iusofts.blades.monitor.inft.ManageInterface;
import com.iusofts.blades.monitor.inft.dto.*;
import com.iusofts.blades.monitor.inft.enums.ApplicationType;
import com.iusofts.blades.monitor.service.dao.MonitorRecordDao;
import com.iusofts.blades.monitor.service.model.ApplicationServiceRelation;
import com.iusofts.blades.monitor.service.model.ServiceCallInfo;
import com.iusofts.blades.monitor.service.model.ServiceConsumerInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.x.discovery.ServiceInstance;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理服务
 */
@Service
public class ManageService extends AbstractManage implements ManageInterface {

    private Logger LOGGER = LoggerFactory.getLogger(ManageService.class);

    @Autowired
    private MonitorRecordDao monitorRecordDao;

    /**
     * 初始化数据
     */
    @Override
    public void init() {
        loadServiceAndAuthData();
        loadServiceCallerInfo();
        getServiceList();
    }

    /**
     * 获取服务列表
     *
     * @return
     */
    @Override
    public List<ServiceInfo> getServiceList() {
        if (serviceDataMap.size() == 0) {
            init();
        }
        // 服务列表
        List<ServiceInfo> serviceList = new ArrayList<>();
        serviceInfoMap = new HashMap<>();
        for (String key : serviceDataMap.keySet()) {
            ServiceInfo service = new ServiceInfo();
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

            // 消费者数
            if (serviceConsumerInfoMap.containsKey(service.getName())) {
                service.setConsumerAmount(serviceConsumerInfoMap.get(service.getName()).size());
                service.setConsumerList(serviceConsumerInfoMap.get(service.getName()));
            } else {
                service.setConsumerAmount(0);
            }

            // 调用情况
            if (serviceCallInfoMap.containsKey(service.getName())) {
                ServiceCallInfo info = serviceCallInfoMap.get(service.getName());
                service.setCallCount(info.getCallCount());
                service.setMeanCostTime(info.getMeanCostTime());
                service.setMaxCostTime(info.getMaxCostTime());
            }

            // 调用失败数量
            if (serviceFailedCallCountMap.containsKey(service.getName())) {
                service.setFailedCount(serviceFailedCallCountMap.get(service.getName()));
            } else {
                service.setFailedCount(0);
            }

            serviceList.add(service);
            serviceInfoMap.put(service.getName(), service);
        }
        return serviceList;
    }

    /**
     * 获取应用列表
     *
     * @return
     */
    @Override
    public List<Application> getApplicationList() {
        if (serviceDataMap.size() == 0) {
            init();
        }
        // 应用列表
        List<Application> applicationList = new ArrayList<>();
        Map<String, Application> appMap = new HashMap<>();

        // 提供者
        for (ServiceInfo service : getServiceList()) {
            if (service != null && StringUtils.isNotBlank(service.getGroup())) {
                if (!appMap.containsKey(service.getGroup())) {
                    Application application = new Application(service.getGroup());
                    application.setType(ApplicationType.PROVIDER);
                    applicationList.add(application);
                    appMap.put(service.getGroup(), application);
                }
                appMap.get(service.getGroup()).incrProvideServiceAmount();
            }
        }

        //FIXME 目前的消费者信息是来自于授权列表里面 待服务调用监控完善后更换消费者数据来源
        //FIXME 不同与dubbo使用前建立长连接即可监控到消费者 blades只能先调用后才能监控到消费者
        // 既是消费者也是提供者
        Set<String> consumerSet = new HashSet<>();
        for (ServiceInfo serviceInfo : serviceInfoMap.values()) {
            if(CollectionUtils.isNotEmpty(serviceInfo.getConsumerList())) {
                for (ServiceConsumerInfo consumerInfo : serviceInfo.getConsumerList()) {
                    consumerSet.add(consumerInfo.getConsumerName());
                }
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

        // 计算应用调用服务数
        List<ApplicationServiceRelation> serviceRelationList = this.monitorRecordDao.getApplicationServiceRelations();
        for (Application application : applicationList) {
            if (application.getType() == ApplicationType.CONSUMER
                    || application.getType() == ApplicationType.PROVIDER_AND_CONSUMER) {
                int count = 0;
                for (ApplicationServiceRelation relation : serviceRelationList) {
                    if (application.getAppName().equals(relation.getConsumerName())) {
                        count++;
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
    @Override
    public List<Authorization> getAuthorizationList() {
        List<Authorization> authorizationList = new ArrayList<>();
        for (String key : applicationAuthorizationMap.keySet()) {
            Authorization authorization = new Authorization();
            authorization.setAppName(key);
            authorization.setAllowAppNames(applicationAuthorizationMap.get(key));
            authorizationList.add(authorization);
        }
        return authorizationList;
    }

    /**
     * 添加权限
     *
     * @return
     */
    @Override
    public boolean addAuthorization(Authorization authorization) {
        boolean bool = false;
        if (authorization != null && StringUtils.isNotBlank(authorization.getAppName())) {
            JavaApiSample sample = new JavaApiSample();
            sample.createConnection(zkAddress, SESSION_TIMEOUT);
            String path = basePath + CONFIG_PATH + PATH_SUFFIX + authorization.getAppName() + AUTHORIZATION_SUFFIX;
            if (!sample.exist(path)) {//不存在新增
                bool = sample.createPath(path, StringUtils.join(authorization.getAllowAppNames(), ","));
                applicationAuthorizationMap.put(authorization.getAppName(), authorization.getAllowAppNames());
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
    @Override
    public boolean editAuthorization(Authorization authorization) {
        boolean bool = false;
        if (authorization != null && StringUtils.isNotBlank(authorization.getAppName())) {
            JavaApiSample sample = new JavaApiSample();
            sample.createConnection(zkAddress, SESSION_TIMEOUT);
            String path = basePath + CONFIG_PATH + PATH_SUFFIX + authorization.getAppName() + AUTHORIZATION_SUFFIX;
            if (sample.exist(path)) {// 存在修改
                bool = sample.writeData(path, StringUtils.join(authorization.getAllowAppNames(), ","));
                applicationAuthorizationMap.put(authorization.getAppName(), authorization.getAllowAppNames());
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
    @Override
    public boolean deleteAuthorization(String appName) {
        boolean bool = false;
        if (StringUtils.isNotBlank(appName)) {
            JavaApiSample sample = new JavaApiSample();
            sample.createConnection(zkAddress, SESSION_TIMEOUT);
            String path = basePath + CONFIG_PATH + PATH_SUFFIX + appName + AUTHORIZATION_SUFFIX;
            if (sample.exist(path)) {// 存在修改
                sample.deleteNode(path);
                applicationAuthorizationMap.remove(appName);
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
    @Override
    public List<Provider> getProviderList(String serviceName) {
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
     * @param serviceName
     * @return
     */
    @Override
    public List<ServiceConsumerInfo> getConsumerList(String serviceName) {
        List<ServiceConsumerInfo> consumerList = serviceConsumerInfoMap.get(serviceName);
        return consumerList;
    }

    /**
     * 禁用服务
     *
     * @param serviceId 服务ID
     * @return
     */
    @Override
    public boolean disableService(String serviceName, String serviceId) {
        return updateServiceIsolated(serviceName, serviceId, true);
    }

    /**
     * 启动服务
     *
     * @param serviceId
     * @return
     */
    @Override
    public boolean enableService(String serviceName, String serviceId) {
        return updateServiceIsolated(serviceName, serviceId, false);
    }

    /**
     * 更新服务状态
     *
     * @param serviceId
     * @param isolated
     * @return
     */
    @Override
    public boolean updateServiceIsolated(String serviceName, String serviceId, boolean isolated) {
        if (StringUtils.isNotBlank(serviceId)) {
            JavaApiSample sample = new JavaApiSample();
            try {
                sample.createConnection(zkAddress, SESSION_TIMEOUT);
                String servidePath = basePath + SERVICE_PATH + PATH_SUFFIX + serviceName + PATH_SUFFIX + serviceId;
                String serviceProviderData = sample.readData(servidePath);
                ServiceInstance<ServiceInstanceDetail> serviceInstance = objectMapper.readValue(serviceProviderData,
                        new TypeReference<ServiceInstance<ServiceInstanceDetail>>() {
                        });
                if (serviceInstance != null && serviceInstance.getPayload() != null) {
                    serviceInstance.getPayload().setIsolated(isolated);
                    LOGGER.info("updateServiceIsolated serviceId:{},isolated:{}" + isolated);
                    sample.writeData(servidePath, objectMapper.writeValueAsString(serviceInstance));

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

    /**
     * 获取全部服务源数据
     *
     * @return
     */
    @Override
    public List<ServiceInstance<ServiceInstanceDetail>> getServiceDataList() {
        if (CollectionUtils.isEmpty(serviceDataList)) {
            init();
        }
        return serviceDataList;
    }

    /**
     * 加载服务和授权数据
     */
    private void loadServiceAndAuthData() {

        List<ServiceInstance<ServiceInstanceDetail>> serviceDataList = new ArrayList<>();
        Map<String, Map<String, ServiceInstance<ServiceInstanceDetail>>> serviceDataMap = new HashMap<>();
        Map<String, String[]> applicationAuthorizationMap = new HashMap<>();

        JavaApiSample sample = new JavaApiSample();
        sample.createConnection(zkAddress, SESSION_TIMEOUT);

        try {
            // 加载服务数据
            List<String> serviceFileNames = sample.redChildList(basePath + SERVICE_PATH);
            // System.err.println("serviceFileNames:" + serviceFileNames);
            if (CollectionUtils.isNotEmpty(serviceFileNames)) {
                for (String serviceFileName : serviceFileNames) {
                    String serviceFilePath = basePath + SERVICE_PATH + PATH_SUFFIX + serviceFileName;// 服务文件路径
                    List<String> serviceProviderFileNames = sample.redChildList(serviceFilePath);
                    if (CollectionUtils.isNotEmpty(serviceProviderFileNames)) {
                        Map<String, ServiceInstance<ServiceInstanceDetail>> serviceProviderMap = new HashMap<>();
                        for (String serviceProviderFileName : serviceProviderFileNames) {
                            String serviceProviderData = sample
                                    .readData(serviceFilePath + PATH_SUFFIX + serviceProviderFileName);
                            if (StringUtils.isNotBlank(serviceProviderData)) {
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
            List<String> configFileNames = sample.redChildList(basePath + CONFIG_PATH);
            // System.err.println("configFileNames:" + configFileNames);

            for (String configFileName : configFileNames) {
                if (configFileName.indexOf(AUTHORIZATION_SUFFIX) != -1) {
                    String strs[] = configFileName.split(AUTHORIZATION_SUFFIX);
                    if (strs.length > 0) {
                        String configData = sample.readData(basePath + CONFIG_PATH + PATH_SUFFIX + configFileName);
                        if (StringUtils.isNotBlank(configData)) {
                            String[] consumers = configData.split(",");
                            applicationAuthorizationMap.put(strs[0], consumers);
                        }
                    }

                }
            }

            LOGGER.info("Loaded zk blades data");
        } catch (IOException e) {
            LOGGER.info("Loaded zk blades data error", e);
        } finally {
            sample.releaseConnection();
        }

        this.serviceDataList = serviceDataList;
        this.serviceDataMap = serviceDataMap;
        this.applicationAuthorizationMap = applicationAuthorizationMap;

    }

    /**
     * 加载服务调用信息
     */
    private void loadServiceCallerInfo() {
        Map<String, List<ServiceConsumerInfo>> serviceConsumerInfoMap = new HashMap<>();
        Map<String, ServiceCallInfo> serviceCallInfoMap = new HashMap<>();
        Map<String, Integer> serviceFailedCallCountMap = new HashMap<>();

        List<ServiceConsumerInfo> serviceConsumerInfos = this.monitorRecordDao.getServiceConsumerInfos();
        if (CollectionUtils.isNotEmpty(serviceConsumerInfos)) {
            for (ServiceConsumerInfo info : serviceConsumerInfos) {
                if (serviceConsumerInfoMap.containsKey(info.getServiceName())) {
                    serviceConsumerInfoMap.get(info.getServiceName()).add(info);
                } else {
                    List<ServiceConsumerInfo> infoList = new ArrayList<>();
                    infoList.add(info);
                    serviceConsumerInfoMap.put(info.getServiceName(), infoList);
                }
            }
        }

        List<ServiceCallInfo> serviceCallInfos = this.monitorRecordDao.getServiceCallInfos();
        if (CollectionUtils.isNotEmpty(serviceCallInfos)) {
            serviceCallInfoMap = serviceCallInfos.stream().collect(Collectors.toMap(ServiceCallInfo::getServiceName, e -> e, (k1, k2) -> k1));
        }
        serviceFailedCallCountMap = this.monitorRecordDao.getServiceFailedCallCount();

        this.serviceConsumerInfoMap = serviceConsumerInfoMap;
        this.serviceCallInfoMap = serviceCallInfoMap;
        this.serviceFailedCallCountMap = serviceFailedCallCountMap;
    }


}
