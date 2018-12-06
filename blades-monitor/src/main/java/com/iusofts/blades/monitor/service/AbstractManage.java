package com.iusofts.blades.monitor.service;

import com.iusofts.blades.common.domain.ServiceInstanceDetail;
import com.iusofts.blades.monitor.service.model.ServiceCallInfo;
import com.iusofts.blades.monitor.service.model.ServiceConsumerInfo;
import org.apache.curator.x.discovery.ServiceInstance;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务管理抽象类
 */
public class AbstractManage {

    protected final int SESSION_TIMEOUT = 10000;
    protected final String PATH_SUFFIX = "/";
    protected final String AUTHORIZATION_SUFFIX = ".authorization";
    protected final String SERVICE_PATH = "/services";
    protected final String CONFIG_PATH = "/config";

    @Value("${blades.zookeeper.address}")
    protected String zkAddress;
    @Value("${blades.zookeeper.path}")
    protected String basePath;

    /**
     * 以下为缓存数据
     */
    // 所有服务数据
    protected List<ServiceInstance<ServiceInstanceDetail>> serviceDataList = new ArrayList<>();
    protected Map<String, Map<String, ServiceInstance<ServiceInstanceDetail>>> serviceDataMap = new HashMap<>();
    // 服务消费者信息
    protected Map<String,List<ServiceConsumerInfo>> serviceConsumerInfoMap = new HashMap<>();
    // 获取服务调用情况
    protected Map<String,ServiceCallInfo> serviceCallInfoMap = new HashMap<>();
    // 获取服务失败调用量
    protected Map<String,Integer> serviceFailedCallCountMap = new HashMap<>();
    // 应用权限
    protected Map<String, String[]> applicationAuthorizationMap = new HashMap<>();

    protected ObjectMapper objectMapper;

    {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

}
