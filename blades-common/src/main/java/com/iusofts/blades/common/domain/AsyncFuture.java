package com.iusofts.blades.common.domain;


import com.iusofts.blades.common.api.ApiUtil;
import com.iusofts.blades.common.excption.ServiceNotAuthException;
import com.iusofts.blades.common.excption.ServiceNotAvailableException;
import com.iusofts.blades.common.excption.ServiceNotFoundException;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AsyncFuture<T> {

    private static final Logger logger = LoggerFactory.getLogger(AsyncFuture.class);

    private Future<T> future;
    private int waitingTime;
    private String serviceName;

    public AsyncFuture(Future<T> future, String serviceName, int waitingTime) {
        this.future = future;
        this.waitingTime = waitingTime;
        this.serviceName = serviceName;
    }

    public T get() throws ServiceNotAvailableException {
        return this.get(waitingTime);
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	public <T> T get(TypeReference typeReference) throws ServiceNotAvailableException {
    	return (T) ApiUtil.mapper(this.get(waitingTime), typeReference);
    }

    public T get(int timeOut) throws ServiceNotAvailableException,ServiceNotFoundException {
        T t = null;
        try {
            t = future.get(timeOut, TimeUnit.SECONDS);
        } catch (Exception e) {
            if (e instanceof ExecutionException) {
                if (e.getCause() instanceof ServiceNotAvailableException) {
                    logger.info("call service {} exception {}",this.serviceName,e.getCause());
                    throw (ServiceNotAvailableException) e.getCause();
                } else if (e.getCause() instanceof ServiceNotFoundException) {
                    logger.info("call service {} exception {}",this.serviceName,e.getCause());
                    throw (ServiceNotFoundException) e.getCause();
                } else if (e.getCause() instanceof ServiceNotAuthException) {
                    logger.info("call service {} exception {}",this.serviceName,e.getCause());
                    throw (ServiceNotAuthException) e.getCause();
                } else {
                    logger.error("call service {} failed with exception {} ",this.serviceName,e);
                    throw new ServiceNotAvailableException(serviceName);
                }
            } else {
                logger.error("call service {} failed with exception {} ",this.serviceName,e);
                throw new ServiceNotAvailableException(serviceName);
            }
        }
        logger.info("call service [{}] success return [{}]",serviceName,t);
        return t;
    }
}
