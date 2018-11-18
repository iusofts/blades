package com.iusofts.blades.common.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigUtil implements ApplicationContextAware,InitializingBean {

    private static ConfigReader configReader;

    private AtomicBoolean inited = new AtomicBoolean(false);

    public static String getString(String key, String defaultValue) {
        return configReader.getString(key,defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return configReader.getInt(key,defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return configReader.getBoolean(key,defaultValue);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (inited.compareAndSet(false,true)) {
            configReader = applicationContext.getBean("core-config-reader",ConfigReader.class);
        }
    }
}
