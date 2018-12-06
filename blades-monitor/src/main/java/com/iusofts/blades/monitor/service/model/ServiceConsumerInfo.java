package com.iusofts.blades.monitor.service.model;

/**
 * 服务消费者调用量
 */
public class ServiceConsumerInfo {

    /**
     * 服务名称
     */
    String serviceName;

    /**
     * 消费者名称
     */
    String consumerName;

    /**
     * 消费者IP
     */
    String consumerIP;

    /**
     * 消费者端口
     */
    String consumerPort;

    /**
     * 调用量
     */
    private Integer count;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
