package com.iusofts.blades.registry.initial;

import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;

import com.iusofts.blades.common.annotation.BladesService;
import com.iusofts.blades.common.util.IPUtil;
import com.iusofts.blades.registry.IRegister;
import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


public class BladesInitial implements ApplicationContextAware, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(BladesInitial.class);
    private static Map<String, Object> bladesServices = new HashMap<>();

    public static String ip;
    public static String port;
    public static String group;
    public static String hostName;
    public static boolean openMonitor = true;// 默认打开监控
    public static String monitorServiceName = "blades-monitor.monitor.record";// 监控服务名称
    private IRegister register;
    private List<Class> excludeClasses;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        bladesServices.putAll(applicationContext.getBeansWithAnnotation(Controller.class));
        bladesServices.putAll(applicationContext.getBeansWithAnnotation(RestController.class));
        // exclude
        if (CollectionUtils.isNotEmpty(excludeClasses)) {
            for (Iterator<Map.Entry<String, Object>> it = bladesServices.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> item = it.next();
                if (excludeClasses.contains(item.getValue().getClass())) {
                    it.remove();
                }
            }
        }
        logger.info("blades service initialing , find all blades service {}", bladesServices);
    }

    @Override
    public void afterPropertiesSet() {
        final String localIp = this.getLocalHost()[0];
        final String localHostName = this.getLocalHost()[1];
        final String localPort = port;
        ip = localIp;
        hostName = localHostName;
        new Thread(() -> {
            for (Object service : bladesServices.values()) {

                RequestMapping requestMapping = AnnotationUtils.findAnnotation(service.getClass(), RequestMapping.class);
                BladesService bladesServiceClass = AnnotationUtils.findAnnotation(service.getClass(), BladesService.class);

                String path = "";
                if (requestMapping != null) {
                    path = getPath(requestMapping);
                }
                String classPath = "";
                if (StringUtils.isNotEmpty(group)) {
                    classPath = "/" + group + path;
                }
                Method[] methods = ReflectionUtils.getAllDeclaredMethods(service.getClass());

                for (Method method : methods) {
                    RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                    PostMapping methodPostMapping = method.getAnnotation(PostMapping.class);
                    GetMapping methodGetMapping = method.getAnnotation(GetMapping.class);
                    String methodPath;
                    if (methodRequestMapping != null) {
                        methodPath = getPath(methodRequestMapping);
                    } else if (methodPostMapping != null) {
                        methodPath = getPostPath(methodPostMapping);
                    } else if (methodGetMapping != null) {
                        methodPath = getGetPath(methodGetMapping);
                    } else {
                        continue;
                    }

                    String serviceName, serviceGroup;
                    // 优先获取注解中的服务名和分组
                    BladesService bladesServiceMethod = AnnotationUtils.findAnnotation(method, BladesService.class);

                    serviceGroup = getServiceGroup(bladesServiceMethod, "");
                    if (StringUtils.isEmpty(serviceGroup)) {
                        serviceGroup = getServiceGroup(bladesServiceClass, group);
                    }

                    if (bladesServiceMethod == null) {
                        serviceName = (path + methodPath).replace("/", ".");
                        if (serviceName.indexOf(".") == 0) {
                            serviceName = serviceName.substring(1);
                        }
                    } else {
                        serviceName = getServiceName(bladesServiceMethod, "");
                    }

                    register.registerService(localHostName, localIp, localPort, classPath, methodPath, serviceName, serviceGroup);
                }
                logger.info("Register all service for controller:{} success", service);
            }
        }).start();
    }


    private String[] getLocalHost() {
        if (StringUtils.isNotEmpty(this.ip) && StringUtils.isNotEmpty(this.hostName)) {
            return new String[]{ip, hostName};
        }

        String localIP = "127.0.0.1";
        String localHostName = "local";

        try {
            InetAddress address = IPUtil.getLocalHostLANAddress();
            if (address != null) {
                localIP = address.getHostAddress();
                localHostName = address.getHostName();
            }
        } catch (Exception e) {
            logger.error("getLocalHost failed", e);
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
        return getMappingPath(requestMapping.path(), requestMapping.value());
    }

    private static final String getPostPath(PostMapping requestMapping) {
        return getMappingPath(requestMapping.path(), requestMapping.value());
    }

    private static final String getGetPath(GetMapping requestMapping) {
        return getMappingPath(requestMapping.path(), requestMapping.value());
    }

    private static String getMappingPath(String[] path2, String[] value2) {
        String[] path = path2;
        if (ArrayUtils.isNotEmpty(path)) {
            return path[0];
        } else {
            String[] value = value2;
            return ArrayUtils.isNotEmpty(value) ? value[0] : null;
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

    public void addExcludeClass(Class... excludeClass) {
        List<Class> classList = new ArrayList<>();
        if (excludeClass != null && excludeClass.length > 0) {
            for (Class aClass : excludeClass) {
                classList.add(aClass);
            }
        }
        if (this.excludeClasses != null) {
            this.excludeClasses.addAll(classList);
        } else {
            this.excludeClasses = classList;
        }
    }

    public static void setOpenMonitor(boolean openMonitor) {
        BladesInitial.openMonitor = openMonitor;
    }

    public static void setMonitorServiceName(String monitorServiceName) {
        BladesInitial.monitorServiceName = monitorServiceName;
    }
}
