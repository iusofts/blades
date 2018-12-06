package com.iusofts.blades.monitor.service.dao;

import com.iusofts.blades.monitor.service.model.*;

import java.util.List;
import java.util.Map;

/**
 * 监控记录Dao
 */
public interface MonitorRecordDao {

    /**
     * 添加监控记录
     *
     * @param record
     */
    void add(MonitorRecord record);

    /**
     * 获取全部提供者和被调用量
     *
     * @param leadTime 提前时间 如: 30m
     * @return
     */
    List<ApplicationCount> getAllProviderAndCallCount(String leadTime);

    /**
     * 获取提供者每分钟调用量
     *
     * @param providerName
     * @return
     */
    List<UnitCount> getProviderCallCountByMinute(String providerName, String leadTime, String groupBy);

    /**
     * 获取应用依赖关系
     *
     * @return
     */
    List<ApplicationRelation> getApplicationRelations();

    /**
     * 获取服务消费者信息
     */
    List<ServiceConsumerInfo> getServiceConsumerInfos();

    /**
     * 获取服务调用情况
     * @return
     */
    List<ServiceCallInfo> getServiceCallInfos();

    /**
     * 获取服务失败调用量
     */
    Map<String,Integer> getServiceFailedCallCount();

}
