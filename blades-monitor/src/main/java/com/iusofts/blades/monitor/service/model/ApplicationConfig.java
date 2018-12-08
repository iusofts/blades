package com.iusofts.blades.monitor.service.model;

/**
 * 应用配置
 */
public class ApplicationConfig {

    String property;
    String value;

    public ApplicationConfig() {
    }

    public ApplicationConfig(String property, String vlaue) {
        this.property = property;
        this.value = vlaue;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
