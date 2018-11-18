package com.iusofts.blades.common.alarm.handler;

import com.iusofts.blades.common.alarm.po.CommonEvent;
import com.iusofts.blades.common.alarm.report.EventReport;
import com.iusofts.blades.common.util.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractEventHandler<T extends CommonEvent> implements ApplicationListener<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //tag: 本地IP和context
    private final String localIP = fetchLocalIP();

    private String context;

    private EventReport eventReport;

    private AtomicBoolean inited = new AtomicBoolean();

    @Override
    public void onApplicationEvent(T event) {
        if (inited.compareAndSet(false,true)) {
            this.eventReport = (EventReport) ServiceLocator.init().getService(EventReport.class);
        }
        logger.debug("receive event:{}", event);

        if (null == event || StringUtils.isEmpty(event.getName()) || ignoreEvent(event)) {
            logger.debug("event is empty or ignore");
            return;
        }

        //参数
        Map<String, Object> params = new HashMap<>();
        this.addArgs(event, params);


        //tags
        Map<String, String> tags = new HashMap<>();
        tags.put("context", "activiti");
        tags.put("event", event.getName());
        tags.put("host", event.getName());
        tags.put("hostAddress", localIP);
        this.addTags(event, tags);

        eventReport.report("blades_default", getMeasurement(), tags, params);
    }

    /**
     * 子类实现了该方法可以自定义是否忽略该事件
     * @param event
     * @return
     */
    private boolean ignoreEvent(T event) {
        return false;
    }

    /**
     * 设置参数
     *
     * @param args args
     */
    protected void addArgs(final T event, final Map<String, Object> args) {
    }

    /**
     * 设置tags
     *
     * @param tags tags
     */
    protected void addTags(final T event, final Map<String, String> tags) {
    }

    /**
     * 返回存储的表名
     * @return
     */
    protected String getMeasurement(){
        return null;
    }


    public static String fetchLocalIP() {
        String localIP = "127.0.0.1";
        DatagramSocket sock = null;

        try {
            InetSocketAddress e = new InetSocketAddress(InetAddress.getByName("1.2.3.4"), 1);
            sock = new DatagramSocket();
            sock.connect(e);
            localIP = sock.getLocalAddress().getHostAddress();
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            sock.disconnect();
            sock.close();
            sock = null;
        }

        return localIP;
    }

    public void setContext(String context) {
        this.context = context;
    }

}
