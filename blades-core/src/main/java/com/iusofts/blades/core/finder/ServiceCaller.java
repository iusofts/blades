package com.iusofts.blades.core.finder;


import com.iusofts.blades.common.domain.AsyncFuture;
import com.iusofts.blades.common.excption.ServiceNotAvailableException;
import com.iusofts.blades.common.excption.ServiceNotFoundException;
import org.codehaus.jackson.type.TypeReference;

import java.lang.reflect.Type;


public interface ServiceCaller {
    <T> AsyncFuture<T> getFuture(String serviceName, Object param, Type clazz, T fallBack) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> getFuture(String serviceName, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> getFuture(String serviceName, Object param, Type clazz, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> getFuture(String serviceName, Object param, Type clazz, T fallBack, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> postFuture(String serviceName, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> postFuture(String serviceName, Object param, Type clazz, T fallBack) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> postFuture(String serviceName, Object param, Type clazz, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> postFuture(String serviceName, Object param, Type clazz, T fallBack, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> postFuture(String serviceName, String url, Object param, Type clazz, T fallBack, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> postFuture(String serviceName, String url, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> postFuture(String serviceName, String url, Object param, Type clazz, T fallBack) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> postFuture(String serviceName, String url, Object param, Type clazz, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> getFuture(String serviceName, String url, Object param, Type clazz, T fallBack, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> getFuture(String serviceName, String url, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> getFuture(String serviceName, String url, Object param, Type clazz, T fallBack) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> AsyncFuture<T> getFuture(String serviceName, String url, Object param, Type clazz, int timeOut) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> T get(String serviceName, String url, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> T get(String serviceName, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> T post(String serviceName, String url, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> T post(String serviceName, Object param, Type clazz) throws ServiceNotFoundException, ServiceNotAvailableException;

    <T> T post(String serviceName, Object param, @SuppressWarnings("rawtypes") TypeReference typeReference) throws ServiceNotFoundException, ServiceNotAvailableException;
}
