package com.iusofts.blades.common.alarm.po;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.context.ApplicationEvent;

public class CommonEvent extends ApplicationEvent {

    private String name;

    /**
     * Create a new ApplicationEvent.
     *
     * @param name the object on which the event initially occurred (never {@code null})
     */
    public CommonEvent(String name) {
        super(name);
        this.name = name;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String getName() {
        return this.name;
    }
}
