package com.iusofts.blades.common.alarm.po;

/**
 * 访问事件
 */
public class BladesAccessEvent extends CommonEvent {

    // 调用方
    private String from;

    // 被调用方
    private String to;

    // 被调用方
    private String callService;

    // 调用结果
    private String callResult;

    // 调用耗时
    private long costTime;

    /**
     * Create a new ApplicationEvent.
     *
     * @param name the object on which the event initially occurred (never {@code null})
     */
    public BladesAccessEvent(String name, String from, long costTime, String callService, String callResult ) {
        super(name);
        this.from = from;
        this.to = callService.split(".")[0];
        this.costTime = costTime;
        this.callService = callService;
        this.callResult = callResult;
    }

    public String getFrom() {
        return from;
    }

    public String getCallService() {
        return callService;
    }

    public String getCallResult() {
        return callResult;
    }

    public String getTo() {
        return to;
    }

    public long getCostTime() {
        return costTime;
    }
}
