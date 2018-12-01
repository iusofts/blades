package com.iusofts.blades.monitor.inft.dto;

/**
 * 授权
 */
public class Authorization {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 允许调用应用名称
     */
    private String[] allowAppNames;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String[] getAllowAppNames() {
        return allowAppNames;
    }

    public void setAllowAppNames(String[] allowAppNames) {
        this.allowAppNames = allowAppNames;
    }
}
