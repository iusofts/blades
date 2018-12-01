package com.iusofts.blades.monitor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("监控记录")
public class MonitorRecordVo {

    @ApiModelProperty("服务名称")
    String serviceName;
    @ApiModelProperty("是否成功")
    boolean success;
    @ApiModelProperty("花费时间")
    long costTime;

    @ApiModelProperty("提供者名称")
    String providerName;
    @ApiModelProperty("提供者IP")
    String providerIP;
    @ApiModelProperty("提供者端口")
    String providerPort;
    @ApiModelProperty("提供者主机名")
    String providerHostName;

    @ApiModelProperty("消费者名称")
    String consumerName;
    @ApiModelProperty("消费者IP")
    String consumerIP;
    @ApiModelProperty("消费者端口")
    String consumerPort;
    @ApiModelProperty("消费者主机名")
    String consumerHostName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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
}
