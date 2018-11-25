package com.iusofts.blades.common.influx;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * influx模板类
 */
public class InfluxTemplate {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private InfluxDB influxDB;

    public InfluxTemplate(String connectUrl, String user,String password) {
        influxDB = InfluxDBFactory.connect(connectUrl,user,password);
        //>1500个或者50毫秒就开始写入到db中 异步写
        influxDB.enableBatch(50, 50, TimeUnit.MILLISECONDS);
    }

    public void write(String database, Point point) {
        try {
            influxDB.write(database,"default",point);
        } catch (Exception e) {
            // never throw exception
            logger.error("write to influxdb error",e);
        }

    }
}
