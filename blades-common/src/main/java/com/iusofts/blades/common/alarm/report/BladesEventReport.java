package com.iusofts.blades.common.alarm.report;

import java.util.Map;

public interface BladesEventReport {

    /**
     * 上报一个事件
     *
     * @param serviceName 服务名称
     * @param success 是否成功
     * @param costTime 花费时长
     * @param extend 拓展数据
     */
    void report(String serviceName, boolean success, long costTime, Map<String, Object> extend);
}
