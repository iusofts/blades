package com.iusofts.blades.registry.initial;

import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.iusofts.blades.common.annotation.BladesService;
import com.iusofts.blades.registry.IRegister;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


public class BladesInitial implements ApplicationContextAware, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(BladesInitial.class);
    private static Map<String, Object> bladesServices = new HashMap<>();

    private String ip;
    private String port;
    private IRegister register;
    public static String group;
    private String hostName;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        bladesServices.putAll(applicationContext.getBeansWithAnnotation(Controller.class));
        bladesServices.putAll(applicationContext.getBeansWithAnnotation(RestController.class));
        logger.info("blades service initialing , find all blades service {}", bladesServices);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final String localIp = this.getLocalHost()[0];
        final String localHostName = this.getLocalHost()[1];
        final String localPort = port;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Object service : bladesServices.values()) {

                    RequestMapping requestMapping = AnnotationUtils.findAnnotation(service.getClass(), RequestMapping.class);
                    BladesService bladesServiceClass = AnnotationUtils.findAnnotation(service.getClass(), BladesService.class);

                    String serviceGroup = getServiceGroup(bladesServiceClass, "");

                    String path = getPath(requestMapping);
                    String classPath = "";
                    if (StringUtils.isNotEmpty(group)) {
                        classPath = "/" + group + path;
                    }
                    Method[] methods = ReflectionUtils.getAllDeclaredMethods(service.getClass());

                    for (Method method : methods) {
                        RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                        if (null != methodRequestMapping) {
                            BladesService bladesServiceMethod = AnnotationUtils.findAnnotation(method, BladesService.class);

                            if (StringUtils.isEmpty(serviceGroup)) {
                                serviceGroup = getServiceGroup(bladesServiceMethod, group);
                            }
                            String methodPath = getPath(methodRequestMapping);
                            String serviceName = (path + methodPath).replace("/", ".");
                            if (serviceName.indexOf(".") == 0) {
                                serviceName = serviceName.substring(1);
                            }
                            register.registerService(localHostName, localIp, localPort, classPath, methodPath, serviceName, serviceGroup);
                        }
                    }
                    logger.info("Register all service for controller:{} success", service);
                }
            }
        }).start();
    }


    private String[] getLocalHost() {
        if (StringUtils.isNotEmpty(this.ip) && StringUtils.isNotEmpty(this.hostName)) {
            return new String[]{ip, hostName};
        }
        String localIP = "127.0.0.1";
        String localHostName = "local";
        DatagramSocket sock = null;

        try {
            InetSocketAddress e = new InetSocketAddress(InetAddress.getByName("1.2.3.4"), 1);
            sock = new DatagramSocket();
            sock.connect(e);
            localIP = sock.getLocalAddress().getHostAddress();
            localHostName = sock.getLocalAddress().getHostName();
            this.ip = localIP;
            this.hostName = localHostName;
        } catch (Exception e) {
            logger.error("get local ip error", e);
        } finally {
            sock.disconnect();
            sock.close();
            sock = null;
        }

        return new String[]{localIP, localHostName};
    }

    private String getServiceName(BladesService bladesService, String defaultName) {
        if (null == bladesService) {
            return defaultName;
        }
        if (StringUtils.isBlank(bladesService.value())) {
            return defaultName;
        }
        return bladesService.value();
    }

    private String getServiceGroup(BladesService bladesService, String defaultGroup) {
        if (null == bladesService) {
            return defaultGroup;
        }
        if (StringUtils.isBlank(bladesService.group())) {
            return defaultGroup;
        }
        return bladesService.group();
    }

    private static final String getPath(RequestMapping requestMapping) {
        if (requestMapping == null) {
            return "";
        } else {
            String[] path = requestMapping.path();
            if (ArrayUtils.isNotEmpty(path)) {
                return path[0];
            } else {
                String[] value = requestMapping.value();
                return ArrayUtils.isNotEmpty(value) ? value[0] : null;
            }
        }
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setRegister(IRegister register) {
        this.register = register;
    }

    public void setGroup(String group) {
        BladesInitial.group = group;
    }
}
