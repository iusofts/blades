package com.iusofts.blades.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.iusofts.blades.common.domain.ServiceInstanceDetail;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ZkCuratorServiceClient {

    private final Logger logger = LoggerFactory.getLogger(ZkCuratorServiceClient.class);
    ThreadFactory namedThreadFactory = (new ThreadFactoryBuilder()).setNameFormat("curator-serviceCache-thread-%d").build();
    ExecutorService serviceCacheExecutors;
    private Map<String, ServiceCache<ServiceInstanceDetail>> cacheMap = Maps.newHashMap();
    private ServiceDiscovery<ServiceInstanceDetail> serviceDiscovery;
    private List<Closeable> closeableList = Lists.newArrayList();
    private final Object lock = new Object();
    private String basePath;
    private String zkAddress;
    private CuratorFramework client;

    public ZkCuratorServiceClient(CuratorFramework client, String basePath) throws Exception {
        this.serviceCacheExecutors = Executors.newFixedThreadPool(30, this.namedThreadFactory);
        this.client =client;
        this.basePath = basePath;
        this.zkAddress = client.getZookeeperClient().getCurrentConnectionString();
        CuratorServiceInstanceSerializer serializer = new CuratorServiceInstanceSerializer(ServiceInstanceDetail.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInstanceDetail.class).client(client).basePath(basePath).serializer(serializer).build();
        serviceDiscovery.start();
    }

    public List<ServiceInstance<ServiceInstanceDetail>> getServiceByName(String serviceName) throws Exception {
        ServiceCache cache = this.cacheMap.get(serviceName);
        if (null == cache) {
            synchronized (this.lock) {
                cache = this.cacheMap.get(serviceName);
                if (null == cache) {
                    cache = this.serviceDiscovery.serviceCacheBuilder().name(serviceName).executorService(this.serviceCacheExecutors).build();
                    cache.start();
                    this.closeableList.add(cache);
                    this.cacheMap.put(serviceName,cache);
                }
            }
        }
        return cache.getInstances();
    }

}
