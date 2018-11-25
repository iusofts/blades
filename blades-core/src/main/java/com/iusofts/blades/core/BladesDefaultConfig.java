package com.iusofts.blades.core;

import com.iusofts.blades.common.util.ServiceLocator;
import com.iusofts.blades.core.alarm.BladesAccessEventHandler;
import com.iusofts.blades.common.alarm.publish.EventPublisher;
import com.iusofts.blades.common.config.ConfigReader;
import com.iusofts.blades.common.config.ConfigUtil;
import com.iusofts.blades.common.config.ConfigWriter;
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
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 锦衣卫配置
 */
@Configuration
public class BladesDefaultConfig {

    @Value("${blades.zkAddress}")
    private String zkAddress;
    @Value("${blades.executionTimeOut}")
    private Integer executionTimeOut;
    @Value("${blades.waitingTimeOut}")
    private Integer waitingTimeOut;
    @Value("${blades.servicePath}")
    private String servicePath;
    @Value("${blades.configPath}")
    private String configPath;
    @Value("${blades.configFile}")
    private String configFile;


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
        configWriter.setConfigPath(configPath);
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
    public SimpleClientHttpRequestFactory createSimpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        return factory;
    }

    @Bean("serviceRegister")
    public BladesRegister createBladesRegister(CuratorFramework curatorFramework) throws Exception {
        BladesRegister bladesRegister = new BladesRegister(curatorFramework, servicePath);
        return bladesRegister;
    }

    @Bean
    public ZkCuratorServiceClient createZkCuratorServiceClient(CuratorFramework curatorFramework) throws Exception {
        ZkCuratorServiceClient client = new ZkCuratorServiceClient(curatorFramework, servicePath);
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
    public Invoker createInvoker(ServiceFinder serviceFinder, EventPublisher eventPublisher) {
        Invoker invoker = new Invoker();
        invoker.setServiceFinder(serviceFinder);
        invoker.setDefaultTimeOut(executionTimeOut);
        invoker.setWaitingTimeOut(waitingTimeOut);
        invoker.setRestTemplate(new RestTemplate());
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
