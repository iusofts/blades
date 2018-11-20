package com.iusofts.blades.core.finder;


import com.google.common.collect.Sets;
import com.iusofts.blades.common.alarm.publish.EventPublisher;
import com.iusofts.blades.common.api.ApiUtil;
import com.iusofts.blades.common.config.ConfigUtil;
import com.iusofts.blades.common.domain.AsyncFuture;
import com.iusofts.blades.common.excption.ServiceNotAuthException;
import com.iusofts.blades.common.excption.ServiceNotAvailableException;
import com.iusofts.blades.common.excption.ServiceNotFoundException;
import com.iusofts.blades.core.hystrix.HystrixCommonCommand;
import com.iusofts.blades.registry.initial.BladesInitial;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.Future;

@SuppressWarnings("unchecked")
public class Invoker implements ServiceCaller {

    private final static Logger logger = LoggerFactory.getLogger(Invoker.class);

    private ServiceFinder serviceFinder;

    // Timeout value in milliseconds for a command
    private int defaultTimeOut;

    // waiting timeout
    private int waitingTimeOut;

    private RestTemplate restTemplate;

    private EventPublisher eventPublisher;

    private <T> AsyncFuture<T> execute(String serviceName, String url, Object param, Type clazz, T fallBack, RequestMethod method, Integer timeOut) throws ServiceNotFoundException, ServiceNotAvailableException {
        String serviceGroup = serviceName.split("\\.")[0];
        String canAccessGroups = ConfigUtil.getString(serviceGroup + ".authorization", "");
        // 如果没有canAccessGroups，则不做限制
        if (!StringUtils.isEmpty(canAccessGroups)) {
            Set<String> canAccessSet = Sets.newHashSet(canAccessGroups.split(","));
            // 不允许访问
            if (!canAccessSet.contains(BladesInitial.group)) {
                throw new ServiceNotAuthException(serviceName, BladesInitial.group);
            }
        }
        HystrixCommonCommand commonCommand = new HystrixCommonCommand(serviceName, url, serviceFinder, eventPublisher, method, param, Object.class, timeOut, restTemplate);
        commonCommand.setFallBack(fallBack);
        Future future = commonCommand.queue();

        return new AsyncFuture(future, clazz, serviceName, waitingTimeOut);
    }

    @Override
    public <T> AsyncFuture<T> getFuture(String serviceName, Object param, Type clazz, T fallBack, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException {
        return this.execute(serviceName, null, param, clazz, fallBack, RequestMethod.GET, timeOut);
    }

    @Override
    public <T> AsyncFuture<T> getFuture(String serviceName, Object param, Type clazz, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException {
        return this.getFuture(serviceName, param, clazz, null, timeOut);
    }

    @Override
    public <T> AsyncFuture<T> getFuture(String serviceName, Object param, Type clazz, T fallBack) throws ServiceNotFoundException, ServiceNotAvailableException {
        return this.getFuture(serviceName, param, clazz, fallBack, defaultTimeOut);
    }

    @Override
    public <T> AsyncFuture<T> getFuture(String serviceName, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException {
        return this.getFuture(serviceName, param, clazz, null);
    }

    @Override
    public <T> AsyncFuture<T> postFuture(String serviceName, Object param, Type clazz, T fallBack, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException {
        return this.execute(serviceName, null, param, clazz, fallBack, RequestMethod.POST, timeOut);
    }

    @Override
    public <T> AsyncFuture<T> postFuture(String serviceName, Object param, Type clazz, T fallBack) throws ServiceNotFoundException, ServiceNotAvailableException {
        return this.postFuture(serviceName, param, clazz, fallBack, defaultTimeOut);
    }

    @Override
    public <T> AsyncFuture<T> postFuture(String serviceName, Object param, Type clazz, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException {
        return this.postFuture(serviceName, param, clazz, null, timeOut);
    }

    @Override
    public <T> AsyncFuture<T> postFuture(String serviceName, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException {
        return this.postFuture(serviceName, param, clazz, null);
    }

    @Override
    public <T> AsyncFuture<T> postFuture(String serviceName, String url, Object param, Type clazz, T fallBack, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException {
        return this.execute(serviceName, url, param, clazz, fallBack, RequestMethod.POST, timeOut);
    }

    @Override
    public <T> AsyncFuture<T> getFuture(String serviceName, String url, Object param, Type clazz, T fallBack, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException {
        return this.execute(serviceName, url, param, clazz, fallBack, RequestMethod.GET, timeOut);
    }

    @Override
    public <T> AsyncFuture<T> postFuture(String serviceName, String url, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException {
        return postFuture(serviceName, url, param, clazz, null, defaultTimeOut);
    }

    @Override
    public <T> AsyncFuture<T> postFuture(String serviceName, String url, Object param, Type clazz, T fallBack) throws ServiceNotFoundException, ServiceNotAvailableException {
        return postFuture(serviceName, url, param, clazz, fallBack, defaultTimeOut);
    }

    @Override
    public <T> AsyncFuture<T> postFuture(String serviceName, String url, Object param, Type clazz, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException {
        return postFuture(serviceName, url, param, clazz, null, timeOut);
    }

    @Override
    public <T> AsyncFuture<T> getFuture(String serviceName, String url, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException {
        return getFuture(serviceName, url, param, clazz, null, defaultTimeOut);
    }

    @Override
    public <T> AsyncFuture<T> getFuture(String serviceName, String url, Object param, Type clazz, T fallBack) throws ServiceNotFoundException, ServiceNotAvailableException {
        return getFuture(serviceName, url, param, clazz, fallBack, defaultTimeOut);
    }

    @Override
    public <T> AsyncFuture<T> getFuture(String serviceName, String url, Object param, Type clazz, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException {
        return getFuture(serviceName, url, param, clazz, null, timeOut);
    }

    @Override
    public <T> T get(String serviceName, String url, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException {
        return getFuture(serviceName, url, param, clazz).get();
    }

    @Override
    public <T> T post(String serviceName, String url, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException {
        return postFuture(serviceName, url, param, clazz).get();
    }

    @Override
    public <T> T get(String serviceName, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException {
        return getFuture(serviceName, param, clazz).get();
    }

    @Override
    public <T> T post(String serviceName, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException {
        return postFuture(serviceName, param, clazz).get();
    }

    public void setServiceFinder(ServiceFinder serviceFinder) {
        this.serviceFinder = serviceFinder;
    }

    public void setDefaultTimeOut(int defaultTimeOut) {
        this.defaultTimeOut = defaultTimeOut;
    }

    public void setWaitingTimeOut(int waitingTimeOut) {
        this.waitingTimeOut = waitingTimeOut;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public <T> T post(String serviceName, Object param, TypeReference typeReference) throws ServiceNotFoundException,
            ServiceNotAvailableException {
        Object obj = postFuture(serviceName, param, Object.class).get();
        return (T) ApiUtil.mapper(obj, typeReference);
    }

}
