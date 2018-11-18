package com.iusofts.blades.registry.initial;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class CuratorFrameworkFactoryBean implements FactoryBean<CuratorFramework>,InitializingBean,DisposableBean {
    // curator client
    private CuratorFramework curator;

    // 连接ZooKeeper
    private String connectString;

    // 重试策略
    private RetryPolicy retryPolicy;

    // session超时时间（ms）
    private Integer sessionTimeoutMs;

    // 命名空间，所有service的前缀
    private String namespace;

    @Override
    public CuratorFramework getObject() throws Exception {
        return this.curator;
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void destroy() throws Exception {
        this.curator.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (null != connectString) {
            // curator builder
            Builder builder = CuratorFrameworkFactory.builder();
            builder.connectString(connectString);
            if (null == retryPolicy) {
                this.retryPolicy = new ExponentialBackoffRetry(1000, 3);
            }
            builder.retryPolicy(retryPolicy);
            if (null != sessionTimeoutMs) {
                builder.sessionTimeoutMs(sessionTimeoutMs);
            }
            if (null != namespace) {
                builder.namespace(namespace);
            }
            this.curator = builder.build();
            this.curator.start();
        }


    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public void setSessionTimeoutMs(Integer sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
