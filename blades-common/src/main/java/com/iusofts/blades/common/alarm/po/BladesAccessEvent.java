package com.iusofts.blades.common.alarm.po;

import com.iusofts.blades.common.domain.ServiceInstanceDetail;

/**
 * 访问事件
 */
public class BladesAccessEvent extends CommonEvent {

    /**
     * 调用方
     */
    private String from;

    /**
     * 被调用方
     */
    private String to;

    /**
     * 被调用服务
     */
    private String callService;

    /**
     * 调用是否成功
     */
    private boolean success;

    /**
     * 调用结果
     */
    private String callResult;

    /**
     * 调用耗时
     */
    private long costTime;

    /**
     * 提供者信息
     */
    private ServiceInstanceDetail providerDetail;

    /**
     * Create a new ApplicationEvent.
     *
     * @param name the object on which the event initially occurred (never {@code null})
     */
    public BladesAccessEvent(String name, String from, long costTime, String callService, boolean success, String callResult, ServiceInstanceDetail providerDetail) {
        super(name);
        this.from = from;
        this.to = callService.split("\\.")[0];
        this.costTime = costTime;
        this.callService = callService;
        this.success = success;
        this.callResult = callResult;
        this.providerDetail = providerDetail;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCallService() {
        return callService;
    }

    public void setCallService(String callService) {
        this.callService = callService;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCallResult() {
        return callResult;
    }

    public void setCallResult(String callResult) {
        this.callResult = callResult;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public ServiceInstanceDetail getProviderDetail() {
        return providerDetail;
    }

    public void setProviderDetail(ServiceInstanceDetail providerDetail) {
        this.providerDetail = providerDetail;
    }
}
