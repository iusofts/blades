package com.iusofts.blades.common.alarm.handler;

import com.iusofts.blades.common.alarm.po.BladesAccessEvent;
import com.iusofts.blades.common.alarm.po.CommonEvent;
import com.iusofts.blades.common.alarm.report.BladesEventReport;
import com.iusofts.blades.common.util.IPUtil;
import com.iusofts.blades.common.util.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractEventHandler<T extends CommonEvent> implements ApplicationListener<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private BladesEventReport eventReport;

    private AtomicBoolean inited = new AtomicBoolean();

    @Override
    public void onApplicationEvent(T event) {
        if (inited.compareAndSet(false, true)) {
            this.eventReport = (BladesEventReport) ServiceLocator.init().getService(BladesEventReport.class);
        }
        logger.info("receive event:{}", event);

        if (null == event || StringUtils.isEmpty(event.getName()) || ignoreEvent(event)) {
            logger.info("event is empty or ignore");
            return;
        }

        if (eventReport != null) {
            if (event instanceof BladesAccessEvent) {
                BladesAccessEvent accessEvent = (BladesAccessEvent) event;
                eventReport.report(accessEvent.getCallService(), accessEvent.isSuccess(), accessEvent.getCostTime(), accessEvent.getProviderDetail(), null);
            }
        }

    }

    /**
     * 子类实现了该方法可以自定义是否忽略该事件
     *
     * @param event
     * @return
     */
    protected boolean ignoreEvent(T event) {
        return false;
    }

}
