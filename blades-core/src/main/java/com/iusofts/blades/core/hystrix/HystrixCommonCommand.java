package com.iusofts.blades.core.hystrix;

import com.iusofts.blades.common.alarm.po.BladesAccessEvent;
import com.iusofts.blades.common.alarm.publish.EventPublisher;
import com.iusofts.blades.common.domain.ServiceInstanceDetail;
import com.iusofts.blades.common.excption.ServiceNotAuthException;
import com.iusofts.blades.common.excption.ServiceNotAvailableException;
import com.iusofts.blades.common.excption.ServiceNotFoundException;
import com.iusofts.blades.core.finder.ServiceFinder;
import com.iusofts.blades.registry.initial.BladesInitial;
import com.iusofts.blades.core.util.RestTemplateUtils;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

public class HystrixCommonCommand<T> extends HystrixCommand<T> {

    private static final Logger logger = LoggerFactory.getLogger(HystrixCommonCommand.class);

    private String serviceName;
    private String uri;
    private ServiceFinder serviceFinder;
    private RequestMethod methodType;
    private Object param;
    private Class<T> responseType;
    private T fallBack;
    private RestTemplate restTemplate;
    private EventPublisher eventPublisher;

    public HystrixCommonCommand(String serviceName, String uri, ServiceFinder serviceFinder, EventPublisher eventPublisher, RequestMethod methodType, Object param, Class<T> responseType, int timeOut, RestTemplate restTemplate) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(serviceName.split("\\.")[0]))
                .andCommandKey(HystrixCommandKey.Factory.asKey(serviceName))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(timeOut)));
        this.serviceName = serviceName;
        this.uri = uri;
        this.serviceFinder = serviceFinder;
        this.methodType = methodType;
        this.param = param;
        this.responseType = responseType;
        this.restTemplate = restTemplate;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 重写run方法，实现熔断器保护下的接口调用
     *
     * @return
     * @throws Exception
     */
    @Override
    protected T run() throws Exception {
        ServiceInstanceDetail detail = null;
        String url = "";
        try {
            detail = serviceFinder.getService(serviceName);
            if (StringUtils.isNotBlank(uri) && uri.indexOf("http") == 0) {
                url = uri;
            } else {
                // http call
                url = this.buildUrl(detail);
            }
            long startTime = System.currentTimeMillis();
            T response = this.httpCall(url);
            long endTime = System.currentTimeMillis();
            // 超过500毫秒 打印日志
            if (endTime - startTime > 500) {
                logger.warn("call service : {} ,url is : {} spend {}ms, please check it");
            }
            this.publishAccessSuccessEvent(endTime - startTime, detail);
            return response;
        } catch (Exception e) {
            this.publishAccessFailedEvent(e.getMessage(), detail);
            if (e instanceof ServiceNotAvailableException
                    || e instanceof ServiceNotFoundException
                    || e instanceof ServiceNotAuthException) {
                throw new HystrixBadRequestException(e.getMessage(), e);
            } else {
                logger.warn("call service:{} at url:{}  failed with exception.", serviceName, url, e);
                throw e;
            }
        }

    }

    private void publishAccessFailedEvent(String errorMsg, ServiceInstanceDetail detail) {
        if (null == eventPublisher) {
            // 未配置
            return;
        }
        try {
            // 调用失败的不统计调用耗时
            BladesAccessEvent bladesAccessEvent = new BladesAccessEvent("bladesAccess", BladesInitial.group, 0, serviceName, false, errorMsg, detail);
            eventPublisher.publish(bladesAccessEvent);
        } catch (Exception e) {
            // never throw exception
            logger.error("publish BladesAccessEvent error", e);
        }

    }

    private void publishAccessSuccessEvent(long costTime, ServiceInstanceDetail detail) {
        if (null == eventPublisher) {
            // 未配置
            return;
        }
        try {
            BladesAccessEvent bladesAccessEvent = new BladesAccessEvent("bladesAccess", BladesInitial.group, costTime, serviceName, true, "success", detail);
            eventPublisher.publish(bladesAccessEvent);
        } catch (Exception e) {
            // never throw exception
            logger.error("publish BladesAccessEvent error", e);
        }
    }

    private String buildUrl(ServiceInstanceDetail detail) {
        String url = "http://" + detail.getLocalIp() + ":" + detail.getLocalPort();
        if (StringUtils.isNotBlank(uri)) {
            return url + uri;
        }
        return url + detail.getClassPath() + detail.getMethodPath();
    }

    // 使用RestTemplate进行http调用
    private T httpCall(String url) throws Exception {
        if (methodType.equals(RequestMethod.GET)) {
            return RestTemplateUtils.get(this.restTemplate, url, param, responseType);
        } else {
            return RestTemplateUtils.post(this.restTemplate, url, param, responseType);
        }
    }

    public void setFallBack(T fallBack) {
        this.fallBack = fallBack;
    }

    /**
     * 降级，接口调用失败会执行fallback
     *
     * @return
     */
    protected T getFallback() {
        logger.info("execute service {} failed ,do fallback", serviceName);
        if (null != fallBack) {
            // 执行fallback
            fallBack = doFallBack();
            return fallBack;
        } else {
            throw new UnsupportedOperationException("No fallback available." + serviceName);
        }
    }

    private T doFallBack() {
        // do something
        return fallBack;
    }
}
