package com.iusofts.blades.monitor.inft.dto;

import com.iusofts.blades.monitor.service.model.ServiceConsumerInfo;

import java.util.List;

/**
 * 服务
 */
public class ServiceInfo {

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

    /**
     * 调用量
     */
    private Integer callCount;

    /**
     * 失败量
     */
    private Integer failedCount;

    /**
     * 平均调用时长
     */
    private Integer meanCostTime;

    /**
     * 最大调用时长
     */
    private Integer maxCostTime;

    /**
     * 消费者列表
     */
    private List<ServiceConsumerInfo> consumerList;

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

    public Integer getCallCount() {
        return callCount;
    }

    public void setCallCount(Integer callCount) {
        this.callCount = callCount;
    }

    public Integer getMeanCostTime() {
        return meanCostTime;
    }

    public void setMeanCostTime(Integer meanCostTime) {
        this.meanCostTime = meanCostTime;
    }

    public Integer getMaxCostTime() {
        return maxCostTime;
    }

    public void setMaxCostTime(Integer maxCostTime) {
        this.maxCostTime = maxCostTime;
    }

    public List<ServiceConsumerInfo> getConsumerList() {
        return consumerList;
    }

    public void setConsumerList(List<ServiceConsumerInfo> consumerList) {
        this.consumerList = consumerList;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }
}
