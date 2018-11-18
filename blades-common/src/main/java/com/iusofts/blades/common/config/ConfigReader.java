package com.iusofts.blades.common.config;

import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

public class ConfigReader {

    /**
     * 读取Spring类型的配置
     * @param key  Key
     * @param defaultValue 默认值
     * @return value
     */
    public String getString(String key, String defaultValue) {
        final DynamicStringProperty property = DynamicPropertyFactory
                .getInstance().getStringProperty(key, defaultValue);
        return property.get();
    }

    /**
     * 读取int类型的配置
     * @param key  Key
     * @param defaultValue 默认值
     * @return value
     */
    public int getInt(String key, int defaultValue) {
        final DynamicIntProperty property = DynamicPropertyFactory
                .getInstance().getIntProperty(key, defaultValue);
        return property.get();
    }

    /**
     * 读取boolean类型的配置
     * @param key  Key
     * @param defaultValue 默认值
     * @return value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        final DynamicBooleanProperty property = DynamicPropertyFactory
                .getInstance().getBooleanProperty(key, defaultValue);
        return property.get();
    }
}
