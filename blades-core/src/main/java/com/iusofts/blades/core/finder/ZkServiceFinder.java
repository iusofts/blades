package com.iusofts.blades.core.finder;


import com.iusofts.blades.common.domain.ServiceInstanceDetail;
import com.iusofts.blades.common.excption.ServiceNotFoundException;
import com.iusofts.blades.registry.ZkCuratorServiceClient;
import com.iusofts.blades.core.finder.strategy.Strategy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ZkServiceFinder implements ServiceFinder {

    private final Logger logger = LoggerFactory.getLogger(ZkServiceFinder.class);

    private ZkCuratorServiceClient client;

    private Strategy strategy;

    public ZkServiceFinder() {
    }

    @Override
    public ServiceInstanceDetail getService(String serviceName) throws ServiceNotFoundException {
        try {
            List<ServiceInstance<ServiceInstanceDetail>> instances =this.client.getServiceByName(serviceName);
            if(CollectionUtils.isEmpty(instances)) throw new ServiceNotFoundException(serviceName);
            // 过滤掉已被隔离的
            List<ServiceInstance<ServiceInstanceDetail>> availableInstances = new ArrayList<>();
            for (ServiceInstance<ServiceInstanceDetail> instance : instances) {
                ServiceInstanceDetail payLoad = instance.getPayload();
                // 当前服务已被隔离
                if (payLoad.isIsolated()) {
                    continue;
                }
                availableInstances.add(instance);
            }
            ServiceInstance<ServiceInstanceDetail> serviceInstance = this.strategy.getServiceInstance(availableInstances);
            return serviceInstance.getPayload();
        } catch (Exception e) {
            logger.error("get service {} failed {}",serviceName,e);
            throw new ServiceNotFoundException(serviceName);
        }
    }

    public void setClient(ZkCuratorServiceClient client) {
        this.client = client;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
