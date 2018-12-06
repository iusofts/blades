package com.iusofts.blades.monitor.inft;

import com.iusofts.blades.common.domain.ServiceInstanceDetail;
import com.iusofts.blades.monitor.inft.dto.*;
import com.iusofts.blades.monitor.service.model.ServiceConsumerInfo;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;

/**
 * 管理接口
 */
public interface ManageInterface {

    /**
     * 初始化数据
     */
    void init();

    /**
     * 获取服务列表
     *
     * @return
     */
    List<ServiceInfo> getServiceList();

    /**
     * 获取应用列表
     *
     * @return
     */
    List<Application> getApplicationList();

    /**
     * 获取授权列表
     *
     * @return
     */
    List<Authorization> getAuthorizationList();

    /**
     * 添加权限
     *
     * @return
     */
    boolean addAuthorization(Authorization authorization);

    /**
     * 编辑权限
     *
     * @return
     */
    boolean editAuthorization(Authorization authorization);

    /**
     * 删除权限
     *
     * @return
     */
    boolean deleteAuthorization(String appName);

    /**
     * 获取服务提供者列表
     *
     * @param serviceName
     * @return
     */
    List<Provider> getProviderList(String serviceName);

    /**
     * 获取服务消费者
     *
     * @param serviceName
     * @return
     */
    List<ServiceConsumerInfo> getConsumerList(String serviceName);

    /**
     * 禁用服务
     *
     * @param serviceId 服务ID
     * @return
     */
    boolean disableService(String serviceName, String serviceId);

    /**
     * 启动服务
     *
     * @param serviceId
     * @return
     */
    boolean enableService(String serviceName, String serviceId);

    /**
     * 更新服务状态
     *
     * @param serviceId
     * @param isolated
     * @return
     */
    boolean updateServiceIsolated(String serviceName, String serviceId, boolean isolated);

    /**
     * 获取全部服务源数据
     *
     * @return
     */
    List<ServiceInstance<ServiceInstanceDetail>> getServiceDataList();

}
