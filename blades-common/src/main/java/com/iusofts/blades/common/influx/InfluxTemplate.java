package com.iusofts.blades.common.influx;

import com.alibaba.fastjson.serializer.Labels;
import com.iusofts.blades.common.util.JsonUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * influx模板类
 */
public class InfluxTemplate {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private InfluxDB influxDB;

    public InfluxTemplate(String connectUrl) {
        influxDB = InfluxDBFactory.connect(connectUrl);
        //>1500个或者50毫秒就开始写入到db中 异步写
        influxDB.enableBatch(50, 50, TimeUnit.MILLISECONDS);
    }

    public InfluxTemplate(String connectUrl, String user, String password) {
        influxDB = InfluxDBFactory.connect(connectUrl, user, password);
        //>1500个或者50毫秒就开始写入到db中 异步写
        influxDB.enableBatch(50, 50, TimeUnit.MILLISECONDS);
    }

    /**
     * 插入一条数据
     *
     * @param database 库名
     * @param point    数据
     */
    public void write(String database, Point point) {
        try {
            influxDB.write(database, "default", point);
        } catch (Exception e) {
            logger.error("write to influxdb error", e);
        }

    }

    /**
     * 查询数据
     *
     * @param database
     * @param command
     * @return
     */
    public QueryResult query(String database, String command) {
        return influxDB.query(new Query(command, database));
    }

    /**
     * 构造一条数据
     *
     * @param measurement 测量指标名,等同于表名
     * @param data        数据
     * @return
     */
    public static Point buildPoint(String measurement, Object data) {
        Point.Builder pointBuilder = Point.measurement(measurement);
        if (data != null) {
            Map<String, Object> tagsMap = JsonUtils.obj2map(data, Labels.includes("tag"));
            if (!CollectionUtils.isEmpty(tagsMap)) {
                for (Map.Entry<String, Object> tagEntry : tagsMap.entrySet()) {
                    pointBuilder.tag(tagEntry.getKey(), tagEntry.getValue().toString());
                }
            }

            Map<String, Object> fieldsMap = JsonUtils.obj2map(data, Labels.includes("field"));
            if (!CollectionUtils.isEmpty(fieldsMap)) {
                for (Map.Entry<String, Object> fieldEntry : fieldsMap.entrySet()) {
                    //ignore tag
                    if (tagsMap != null && tagsMap.keySet().contains(fieldEntry.getKey())) {
                        continue;
                    }

                    //ignore null
                    if (fieldEntry.getKey() == null || fieldEntry.getValue() == null) {
                        continue;
                    }

                    pointBuilder.field(fieldEntry.getKey(), fieldEntry.getValue());
                }
            }
        }


        return pointBuilder.build();
    }
}
