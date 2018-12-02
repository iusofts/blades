package com.iusofts.blades.monitor.inft.dto;

/**
 * 概况统计
 */
public class OverviewCount {

    private Integer serviceCount;
    private Integer appCount;
    private Integer providerCount;
    private Integer consumerCount;
    private Integer errorCount;
    private Integer warningCount;

    public Integer getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(Integer serviceCount) {
        this.serviceCount = serviceCount;
    }

    public Integer getAppCount() {
        return appCount;
    }

    public void setAppCount(Integer appCount) {
        this.appCount = appCount;
    }

    public Integer getProviderCount() {
        return providerCount;
    }

    public void setProviderCount(Integer providerCount) {
        this.providerCount = providerCount;
    }

    public Integer getConsumerCount() {
        return consumerCount;
    }

    public void setConsumerCount(Integer consumerCount) {
        this.consumerCount = consumerCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Integer getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(Integer warningCount) {
        this.warningCount = warningCount;
    }
}
