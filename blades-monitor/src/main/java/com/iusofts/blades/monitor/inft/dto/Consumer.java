package com.iusofts.blades.monitor.inft.dto;

/**
 * 消费者
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
