package com.iusofts.blades.monitor.service;

import com.iusofts.blades.common.influx.InfluxTemplate;
import com.iusofts.blades.monitor.inft.MonitorInterface;
import com.iusofts.blades.monitor.service.model.MonitorRecord;
import com.iusofts.blades.monitor.web.vo.MonitorRecordVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.iusofts.blades.common.influx.InfluxTemplate.buildPoint;

/**
 * 监控服务
 */
@Service
public class MonitorService implements MonitorInterface {

    private final String database = "blades_monitor";
    private final String measurement = "monitor_record";

    @Autowired
    private InfluxTemplate influxTemplate;

    @Override
    public void monitor(MonitorRecordVo recordVo) {
        if (recordVo != null) {
            MonitorRecord record = new MonitorRecord();
            BeanUtils.copyProperties(recordVo, record);
            if (influxTemplate != null) {
                influxTemplate.write(database, buildPoint(measurement, record));
            }
        }
    }
}
