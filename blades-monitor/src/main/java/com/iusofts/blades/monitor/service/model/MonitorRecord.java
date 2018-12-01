package com.iusofts.blades.monitor.service.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 监控记录
 */
public class MonitorRecord {

    /**
     * 服务名称
     */
    @JSONField(label = "tag")
    String serviceName;

    /**
     * 是否成功
     */
    @JSONField(label = "tag")
    boolean success;

    /**
     * 花费时间
     */
    @JSONField(label = "field")
    long costTime;

    /**
     * 提供者名称
     */
    @JSONField(label = "tag")
    String providerName;

    /**
     * 提供者IP
     */
    @JSONField(label = "field")
    String providerIP;

    /**
     * 提供者端口
     */
    @JSONField(label = "field")
    String providerPort;

    /**
     * 提供者主机名
     */
    @JSONField(label = "field")
    String providerHostName;

    /**
     * 消费者名称
     */
    @JSONField(label = "tag")
    String consumerName;

    /**
     * 消费者IP
     */
    @JSONField(label = "field")
    String consumerIP;

    /**
     * 消费者端口
     */
    @JSONField(label = "field")
    String consumerPort;

    /**
     * 消费者主机名
     */
    @JSONField(label = "field")
    String consumerHostName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderIP() {
        return providerIP;
    }

    public void setProviderIP(String providerIP) {
        this.providerIP = providerIP;
    }

    public String getProviderPort() {
        return providerPort;
    }

    public void setProviderPort(String providerPort) {
        this.providerPort = providerPort;
    }

    public String getProviderHostName() {
        return providerHostName;
    }

    public void setProviderHostName(String providerHostName) {
        this.providerHostName = providerHostName;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerIP() {
        return consumerIP;
    }

    public void setConsumerIP(String consumerIP) {
        this.consumerIP = consumerIP;
    }

    public String getConsumerPort() {
        return consumerPort;
    }

    public void setConsumerPort(String consumerPort) {
        this.consumerPort = consumerPort;
    }

    public String getConsumerHostName() {
        return consumerHostName;
    }

    public void setConsumerHostName(String consumerHostName) {
        this.consumerHostName = consumerHostName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }
}
