/*
 * Copyright (C) 2006-2016 AiJia All rights reserved
 * Author: Ivan Shen
 * Date: 2017/1/17
 * Description:Consumer.java
 */
package com.iusofts.blades.monitor.service.dto;

/**
 * 消费者
 *
 * @author Ivan Shen
 */
public class Consumer {

    /**
     * 名称
     */
    private String appName;

    /**
     * IP地址
     */
    private String address;

    /**
     * 端口
     */
    private Integer port;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

}
