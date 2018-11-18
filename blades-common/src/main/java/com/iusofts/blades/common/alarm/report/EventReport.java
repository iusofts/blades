package com.iusofts.blades.common.alarm.report;

import java.util.Map;

public interface EventReport {

    /**
     * 上报一个事件
     * @param database 数据库名称
     * @param measurement 表名称
     * @param tags 标签，用于索引
     * @param fields 字段，存储
     */
    void report(String database, String measurement, Map<String, String> tags, Map<String, Object> fields);
}
