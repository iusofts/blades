package com.iusofts.blades.core.alarm;


import com.iusofts.blades.common.alarm.handler.AbstractEventHandler;
import com.iusofts.blades.common.alarm.po.BladesAccessEvent;
import com.iusofts.blades.registry.initial.BladesInitial;

import java.util.Map;

public class BladesAccessEventHandler extends AbstractEventHandler<BladesAccessEvent> {

    @Override
    protected boolean ignoreEvent(BladesAccessEvent event) {
        if (!BladesInitial.openMonitor || event.getCallService().equals(BladesInitial.monitorServiceName)) {
            // 当监控未开启或者当前被调用的是监控服务时选择忽略
            return true;
        }
        return false;
    }
}
