package com.iusofts.blades.common.domain;


import com.alibaba.fastjson.JSON;
import com.iusofts.blades.common.excption.ServiceNotAuthException;
import com.iusofts.blades.common.excption.ServiceNotAvailableException;
import com.iusofts.blades.common.excption.ServiceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AsyncFuture<T> {

    private static final Logger logger = LoggerFactory.getLogger(AsyncFuture.class);

    private Future<T> future;
    private int waitingTime;
    private String serviceName;
    private Type clazz;

    public AsyncFuture(Future<T> future, Type clazz, String serviceName, int waitingTime) {
        this.future = future;
        this.waitingTime = waitingTime;
        this.serviceName = serviceName;
        this.clazz = clazz;
    }

    public <T> T get() throws ServiceNotAvailableException {
        return this.get(waitingTime);
    }

    public <T> T get(int timeOut) throws ServiceNotAvailableException, ServiceNotFoundException {
        T t = null;
        try {
            Object object = future.get(timeOut, TimeUnit.SECONDS);
            t = JSON.parseObject(JSON.toJSONString(object), clazz);
        } catch (Exception e) {
            if (e instanceof ExecutionException) {
                if (e.getCause() instanceof ServiceNotAvailableException) {
                    logger.info("call service {} exception {}", this.serviceName, e.getCause());
                    throw (ServiceNotAvailableException) e.getCause();
                } else if (e.getCause() instanceof ServiceNotFoundException) {
                    logger.info("call service {} exception {}", this.serviceName, e.getCause());
                    throw (ServiceNotFoundException) e.getCause();
                } else if (e.getCause() instanceof ServiceNotAuthException) {
                    logger.info("call service {} exception {}", this.serviceName, e.getCause());
                    throw (ServiceNotAuthException) e.getCause();
                } else {
                    logger.error("call service {} failed with exception {} ", this.serviceName, e);
                    throw new ServiceNotAvailableException(serviceName);
                }
            } else {
                logger.error("call service {} failed with exception {} ", this.serviceName, e);
                throw new ServiceNotAvailableException(serviceName);
            }
        }
        logger.info("call service [{}] success return [{}]", serviceName, t);
        return t;
    }
}
