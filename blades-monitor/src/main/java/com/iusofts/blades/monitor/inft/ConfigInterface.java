package com.iusofts.blades.monitor.inft;

import com.iusofts.blades.monitor.service.model.ApplicationConfig;

import java.util.List;

/**
 * 配置接口
 */
public interface ConfigInterface {

    List<ApplicationConfig> getApplicationConfig(String appName);

    void saveApplicationConfig(String appName, List<ApplicationConfig> configs);

}
