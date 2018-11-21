/*
 * Copyright (C) 2006-2016 AiJia All rights reserved
 * Author: Ivan Shen
 * Date: 2017/1/17
 * Description:Service.java
 */
package com.iusofts.blades.monitor.service.dto;

/**
 * 服务
 *
 * @author Ivan Shen
 */
public class Service {

    /**
     * 服务名称
     */
    private String name;

    /**
     * 分组
     */
    private String group;

    /**
     * 提供者数
     */
    private Integer providerAmount;

    /**
     * 消费者数
     */
    private Integer consumerAmount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getProviderAmount() {
        return providerAmount;
    }

    public void setProviderAmount(Integer providerAmount) {
        this.providerAmount = providerAmount;
    }

    public Integer getConsumerAmount() {
        return consumerAmount;
    }

    public void setConsumerAmount(Integer consumerAmount) {
        this.consumerAmount = consumerAmount;
    }
}
