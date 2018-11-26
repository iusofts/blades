package com.iusofts.blades.core;

import com.iusofts.blades.common.alarm.publish.EventPublisher;
import com.iusofts.blades.common.config.ConfigReader;
import com.iusofts.blades.common.config.ConfigUtil;
import com.iusofts.blades.common.config.ConfigWriter;
import com.iusofts.blades.common.util.ServiceLocator;
import com.iusofts.blades.core.alarm.BladesAccessEventHandler;
import com.iusofts.blades.core.alarm.MonitorReport;
import com.iusofts.blades.core.finder.Invoker;
import com.iusofts.blades.core.finder.ServiceFinder;
import com.iusofts.blades.core.finder.ZkServiceFinder;
import com.iusofts.blades.core.finder.strategy.RandomStrategy;
import com.iusofts.blades.core.finder.strategy.RoundRobinStrategy;
import com.iusofts.blades.registry.BladesRegister;
import com.iusofts.blades.registry.ZkCuratorServiceClient;
import com.iusofts.blades.registry.initial.CuratorFrameworkFactoryBean;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 锦衣卫配置
 */
@Import(HystrixConfig.class)
@Configuration
public class BladesDefaultConfig {

    @Value("${blades.zookeeper.address}")
    private String zkAddress;
    @Value("${blades.zookeeper.path}")
    private String zkPath;
    @Value("${blades.executionTimeOut}")
    private Integer executionTimeOut;
    @Value("${blades.waitingTimeOut}")
    private Integer waitingTimeOut;
    private String servicePath = "/services";
    private String configPath = "/config";
    private String configFile = "hystrix.properties";


    @Bean
    public CuratorFrameworkFactoryBean createCuratorFrameworkFactoryBean() {
        CuratorFrameworkFactoryBean bean = new CuratorFrameworkFactoryBean();
        bean.setConnectString(zkAddress);
        bean.setSessionTimeoutMs(3000);
        //bean.setNamespace("");
        return bean;
    }

    @Bean("core-config-writer")
    public ConfigWriter createConfigWriter(CuratorFramework curatorFramework) {
        ConfigWriter configWriter = new ConfigWriter();
        configWriter.setConfigFile(configFile);
        configWriter.setConfigPath(zkPath + configPath);
        configWriter.setClient(curatorFramework);
        return configWriter;
    }

    @Bean("core-config-reader")
    @DependsOn("core-config-writer")
    public ConfigReader createConfigReader() {
        return new ConfigReader();
    }


    @Bean
    @DependsOn("core-config-reader")
    public ConfigUtil createConfigUtil() {
        return new ConfigUtil();
    }

    @Bean
    public RestTemplate createRestTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        return restTemplate;
    }

    @Bean("simpleClientHttpRequestFactory")
    public SimpleClientHttpRequestFactory createSimpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(30000);
        factory.setReadTimeout(30000);
        return factory;
    }

    @Bean("serviceRegister")
    public BladesRegister createBladesRegister(CuratorFramework curatorFramework) throws Exception {
        BladesRegister bladesRegister = new BladesRegister(curatorFramework, zkPath + servicePath);
        return bladesRegister;
    }

    @Bean
    public ZkCuratorServiceClient createZkCuratorServiceClient(CuratorFramework curatorFramework) throws Exception {
        ZkCuratorServiceClient client = new ZkCuratorServiceClient(curatorFramework, zkPath + servicePath);
        return client;
    }

    @Bean
    public ZkServiceFinder createZkServiceFinder(ZkCuratorServiceClient zkCuratorServiceClient, RoundRobinStrategy roundRobinStrategy) {
        ZkServiceFinder zkServiceFinder = new ZkServiceFinder();
        zkServiceFinder.setClient(zkCuratorServiceClient);
        zkServiceFinder.setStrategy(roundRobinStrategy);
        return zkServiceFinder;
    }

    @Bean("serviceCaller")
    public Invoker createInvoker(ServiceFinder serviceFinder, EventPublisher eventPublisher, RestTemplate restTemplate) {
        Invoker invoker = new Invoker();
        invoker.setServiceFinder(serviceFinder);
        invoker.setDefaultTimeOut(executionTimeOut);
        invoker.setWaitingTimeOut(waitingTimeOut);
        invoker.setRestTemplate(restTemplate);
        invoker.setEventPublisher(eventPublisher);
        return invoker;
    }

    @Bean
    public RandomStrategy createRandomStrategy() {
        return new RandomStrategy();
    }

    @Bean
    public RoundRobinStrategy createRoundRobinStrategy() {
        return new RoundRobinStrategy();
    }

    @Bean
    public EventPublisher createEventPublisher() {
        return new EventPublisher();
    }

    @Bean
    public BladesAccessEventHandler createBladesAccessEventHandler() {
        return new BladesAccessEventHandler();
    }

    @Bean("bladesEventReport")
    public MonitorReport createMonitorReport() {
        return new MonitorReport();
    }

    @Bean
    public ServiceLocator createServiceLocator() {
        return ServiceLocator.init();
    }


}
