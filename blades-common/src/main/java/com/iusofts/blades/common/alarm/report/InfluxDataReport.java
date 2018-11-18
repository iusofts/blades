package com.iusofts.blades.common.alarm.report;

import org.influxdb.dto.Point;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 上报事件到influx实现类
 */
public class InfluxDataReport implements EventReport {

    private InfluxTemplate influxTemplate;

    @Override
    public void report(String database, String measurement, Map<String, String> tags, Map<String, Object> fields) {
        Point point = buildPoint(measurement,tags,fields);
        influxTemplate.write(database, point);
    }

    private Point buildPoint(String measurement, Map<String, String> tags, Map<String, Object> fields) {
        Point.Builder pointBuilder = Point.measurement(measurement);
        if (!CollectionUtils.isEmpty(tags)) {
            for (Map.Entry<String, String> tagEntry : tags.entrySet()) {
                pointBuilder.tag(tagEntry.getKey(),tagEntry.getValue());
            }
        }

        if (!CollectionUtils.isEmpty(fields)) {
            for (Map.Entry<String, Object> fieldEntry : fields.entrySet()) {
                //ignore tag
                if (tags != null  && tags.keySet().contains(fieldEntry.getKey())) {
                    continue;
                }

                //ignore null
                if (fieldEntry.getKey() == null || fieldEntry.getValue() == null) {
                    continue;
                }

                pointBuilder.field(fieldEntry.getKey(), fieldEntry.getValue());
            }
        }

        return pointBuilder.build();
    }

    public void setInfluxTemplate(InfluxTemplate influxTemplate) {
        this.influxTemplate = influxTemplate;
    }
}
