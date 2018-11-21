/*
 * Copyright (C) 2006-2016 AiJia All rights reserved
 * Author: Ivan Shen
 * Date: 2017/1/17
 * Description:Provider.java
 */
package com.iusofts.blades.monitor.service.dto;

/**
 * 提供者
 *
 * @author Ivan Shen
 */
public class Provider {

    /**
     * 编号
     */
    private String id;

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

    /**
     * 禁用
     */
    private boolean disable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
