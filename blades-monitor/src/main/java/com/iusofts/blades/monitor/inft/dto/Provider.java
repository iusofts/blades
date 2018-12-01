package com.iusofts.blades.monitor.inft.dto;

/**
 * 提供者
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
