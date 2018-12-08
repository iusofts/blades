package com.iusofts.blades.monitor.service;

import com.iusofts.blades.monitor.inft.ConfigInterface;
import com.iusofts.blades.monitor.service.model.ApplicationConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ConfigService implements ConfigInterface {

    private Logger LOGGER = LoggerFactory.getLogger(ConfigService.class);

    @Autowired
    private CuratorFramework client;
    @Value("${blades.zookeeper.path}")
    private String zkPath;
    private String configPath = "/config";

    @Override
    public List<ApplicationConfig> getApplicationConfig(String appName) {
        List<ApplicationConfig> configList = new ArrayList<>();
        final String path = zkPath + configPath;
        try {
            if (client.checkExists().forPath(path) != null) {

                List<String> childPaths = this.client.getChildren().forPath(path);
                if (CollectionUtils.isNotEmpty(childPaths)) {
                    for (String childPath : childPaths) {
                        if (childPath.indexOf(appName) != -1) {
                            String value = new String(client.getData().forPath(path + "/" + childPath));
                            configList.add(new ApplicationConfig(childPath, value));
                            LOGGER.info("read configuration: {} --> {} success", childPath, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("getApplicationConfig failed", e);
        }
        return configList;
    }

    @Override
    public void saveApplicationConfig(String appName, List<ApplicationConfig> configs) {

        try {
            for (ApplicationConfig config : configs) {
                if(StringUtils.isNotBlank(config.getValue())) {
                    final String path = zkPath + configPath + "/" + config.getProperty();
                    if (client.checkExists().forPath(path) == null) {
                        this.client.create().forPath(path, config.getValue().getBytes("UTF-8"));
                        LOGGER.info("add configuration: {} --> {} success", config.getProperty(), config.getValue());
                    } else {
                        this.client.setData().forPath(path, config.getValue().getBytes("UTF-8"));
                        LOGGER.info("set configuration: {} --> {} success", config.getProperty(), config.getValue());
                    }
                }
            }
            LOGGER.info("save all configurations success");
        } catch (Exception e) {
            LOGGER.error("save all configurations to zookeeper failed with exception.", e);
        }

    }
}
