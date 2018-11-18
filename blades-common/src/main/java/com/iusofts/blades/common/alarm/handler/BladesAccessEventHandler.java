package com.iusofts.blades.common.alarm.handler;


import com.iusofts.blades.common.alarm.po.BladesAccessEvent;

import java.util.Map;

public class BladesAccessEventHandler extends AbstractEventHandler<BladesAccessEvent> {

    @Override
    protected void addArgs(BladesAccessEvent event, Map<String, Object> args) {
        args.put("costTime",event.getCostTime());
    }

    @Override
    protected void addTags(BladesAccessEvent event, Map<String, String> tags) {
        tags.put("from", event.getFrom());
        tags.put("to", event.getTo());
        tags.put("callService", event.getCallService());
        tags.put("callResult", event.getCallResult());
    }

    @Override
    protected String getMeasurement() {
        return "blades_access";
    }
}
