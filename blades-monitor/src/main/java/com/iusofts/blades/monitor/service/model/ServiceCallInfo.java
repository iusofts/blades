package com.iusofts.blades.monitor.service.model;

/**
 * 服务调用信息
 */
public class ServiceCallInfo {

    /**
     * 服务名称
     */
    private String serviceName;

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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getCallCount() {
        return callCount;
    }

    public void setCallCount(Integer callCount) {
        this.callCount = callCount;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
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

}
