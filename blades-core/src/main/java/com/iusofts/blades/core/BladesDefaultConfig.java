package com.iusofts.blades.core;

import com.iusofts.blades.common.config.ConfigReader;
import com.iusofts.blades.common.config.ConfigUtil;
import com.iusofts.blades.common.config.ConfigWriter;
import com.iusofts.blades.core.finder.Invoker;
import com.iusofts.blades.core.finder.ServiceFinder;
import com.iusofts.blades.core.finder.ZkServiceFinder;
import com.iusofts.blades.core.finder.strategy.RandomStrategy;
import com.iusofts.blades.core.finder.strategy.RoundRobinStrategy;
import com.iusofts.blades.registry.BladesRegister;
import com.iusofts.blades.registry.ZkCuratorServiceClient;
import com.iusofts.blades.registry.initial.CuratorFrameworkFactoryBean;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 锦衣卫配置
 */
@Configuration
@ConfigurationProperties(prefix = "blades")
public class BladesDefaultConfig {

    private String serviceName;
    private String servicePort;
    private String zkAddress;
    private Integer defaultTimeOut;
    private Integer waitingTimeOut;
    private String zkAdwaitingTimeOutdress;
    private String servicePath;
    private String configPath;
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
    public Invoker createInvoker(ServiceFinder serviceFinder) {
        Invoker invoker = new Invoker();
        invoker.setServiceFinder(serviceFinder);
        invoker.setDefaultTimeOut(defaultTimeOut);
        invoker.setWaitingTimeOut(waitingTimeOut);
        invoker.setRestTemplate(new RestTemplate());
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePort() {
        return servicePort;
    }

    public void setServicePort(String servicePort) {
        this.servicePort = servicePort;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public Integer getDefaultTimeOut() {
        return defaultTimeOut;
    }

    public void setDefaultTimeOut(Integer defaultTimeOut) {
        this.defaultTimeOut = defaultTimeOut;
    }

    public Integer getWaitingTimeOut() {
        return waitingTimeOut;
    }

    public void setWaitingTimeOut(Integer waitingTimeOut) {
        this.waitingTimeOut = waitingTimeOut;
    }

    public String getZkAdwaitingTimeOutdress() {
        return zkAdwaitingTimeOutdress;
    }

    public void setZkAdwaitingTimeOutdress(String zkAdwaitingTimeOutdress) {
        this.zkAdwaitingTimeOutdress = zkAdwaitingTimeOutdress;
    }

    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }
}
