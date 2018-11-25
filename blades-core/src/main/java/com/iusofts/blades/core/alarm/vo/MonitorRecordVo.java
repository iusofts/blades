package com.iusofts.blades.core.alarm.vo;

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
    @ApiModelProperty("消费者名称")
    String consumerName;
    @ApiModelProperty("消费者IP")
    String consumerIP;
    @ApiModelProperty("消费者端口")
    String consumerPort;
    @ApiModelProperty("消费者主机名")
    String hostName;

    public MonitorRecordVo() {
    }

    public MonitorRecordVo(String serviceName, boolean success, long costTime, String consumerName, String consumerIP, String consumerPort, String hostName) {
        this.serviceName = serviceName;
        this.success = success;
        this.costTime = costTime;
        this.consumerName = consumerName;
        this.consumerIP = consumerIP;
        this.consumerPort = consumerPort;
        this.hostName = hostName;
    }

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

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
