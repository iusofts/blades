package com.iusofts.blades.common.alarm.publish;

import com.iusofts.blades.common.alarm.po.CommonEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Spring event publisher
 */
public class EventPublisher implements ApplicationEventPublisherAware {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    public void publish(CommonEvent event) {
        logger.info("publish event {}", event);
        if (null == event) {
            logger.info("the publishing event is empty");
            return;
        }

        eventPublisher.publishEvent(event);
    }
}
