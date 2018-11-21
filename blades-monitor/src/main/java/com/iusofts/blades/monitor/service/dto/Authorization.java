/*
 * Copyright (C) 2006-2016 AiJia All rights reserved
 * Author: Ivan Shen
 * Date: 2017/1/18
 * Description:Authorization.java
 */
package com.iusofts.blades.monitor.service.dto;

/**
 * 授权
 *
 * @author Ivan Shen
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
