package com.iusofts.blades.core.alarm;

import com.iusofts.blades.common.alarm.report.BladesEventReport;
import com.iusofts.blades.common.domain.ServiceInstanceDetail;
import com.iusofts.blades.common.util.IPUtil;
import com.iusofts.blades.common.util.ServiceLocator;
import com.iusofts.blades.core.alarm.vo.MonitorRecordVo;
import com.iusofts.blades.core.finder.ServiceCaller;
import com.iusofts.blades.registry.initial.BladesInitial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 监控上报
 */
public class MonitorReport implements BladesEventReport {

    private static Logger logger = LoggerFactory.getLogger(MonitorReport.class);
    private AtomicBoolean inited = new AtomicBoolean();
    private ServiceCaller serviceCaller;

    @Override
    public void report(String serviceName, boolean success, long costTime, ServiceInstanceDetail provider, Map<String, Object> extend) {
        logger.info("monitor report serviceName:{},success:{},costTime:{}", serviceName, success, costTime);
        if (inited.compareAndSet(false, true)) {
            this.serviceCaller = (ServiceCaller) ServiceLocator.init().getService(ServiceCaller.class);
        }
        if (serviceCaller != null) {
            MonitorRecordVo paramVo = new MonitorRecordVo();
            paramVo.setServiceName(serviceName);
            paramVo.setSuccess(success);
            paramVo.setCostTime(costTime);
            // 提供者信息
            paramVo.setProviderName(provider.getServiceGroup());
            paramVo.setProviderIP(provider.getLocalIp());
            paramVo.setProviderPort(provider.getLocalPort());
            paramVo.setProviderHostName(provider.getLocalHostName());
            // 消费者信息
            paramVo.setConsumerName(BladesInitial.group);
            paramVo.setConsumerIP(BladesInitial.ip);
            paramVo.setConsumerPort(BladesInitial.port);
            paramVo.setConsumerHostName(BladesInitial.hostName);
            serviceCaller.postFuture(BladesInitial.monitorServiceName, paramVo, Object.class, 1000L);
        }
    }
}
