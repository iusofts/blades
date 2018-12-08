package com.iusofts.blades.monitor.service.model;

/**
 * 应用服务关系
 */
public class ApplicationServiceRelation extends ApplicationRelation {

    /**
     * 服务名称
     */
    String serviceName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
