package com.iusofts.blades.monitor.service.model;

/**
 * 应用关系
 */
public class ApplicationRelation {

    /**
     * 提供者
     */
    private String providerName;

    /**
     * 消费者
     */
    private String consumerName;

    /**
     * 调用次数
     */
    private Integer count;

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
