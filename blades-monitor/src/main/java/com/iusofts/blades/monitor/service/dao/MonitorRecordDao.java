package com.iusofts.blades.monitor.service.dao;

import com.iusofts.blades.common.influx.InfluxTemplate;
import com.iusofts.blades.monitor.service.model.ApplicationCount;
import com.iusofts.blades.monitor.service.model.MonitorRecord;
import com.iusofts.blades.monitor.service.model.UnitCount;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.iusofts.blades.common.influx.InfluxTemplate.buildPoint;

/**
 * 监控记录Dao
 */
@Component
public class MonitorRecordDao {

    private final String database = "blades_monitor";
    private final String measurement = "monitor_record";
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");//注意格式化的表达式
    private final SimpleDateFormat minuteormat = new SimpleDateFormat("HH:mm");//注意格式化的表达式


    @Autowired
    private InfluxTemplate influxTemplate;

    public void add(MonitorRecord record) {
        if (influxTemplate != null) {
            influxTemplate.write(database, buildPoint(measurement, record));
        }
    }

    /**
     * 获取全部提供者和被调用量
     *
     * @return
     */
    public List<ApplicationCount> getAllProviderAndCallCount() {
        List<ApplicationCount> applicationCountList = new ArrayList<>();
        String sql = "select count(consumerIP) from monitor_record where time > now() - 30m  group by providerName";
        QueryResult queryResult = this.influxTemplate.query(database, sql);
        if (StringUtils.isEmpty(queryResult.getError()) && CollectionUtils.isNotEmpty(queryResult.getResults())) {
            List<QueryResult.Series> seriesList = queryResult.getResults().get(0).getSeries();
            if (CollectionUtils.isNotEmpty(seriesList)) {
                for (QueryResult.Series series : seriesList) {
                    ApplicationCount count = new ApplicationCount();
                    count.setAppName(series.getTags().get("providerName"));
                    count.setCount(((Double) series.getValues().get(0).get(1)).intValue());
                    applicationCountList.add(count);
                }
            }
        }
        return applicationCountList;
    }

    /**
     * 获取提供者每分钟调用量
     *
     * @param providerName
     * @return
     */
    public List<UnitCount> getProviderCallCountByMinute(String providerName) {
        List<UnitCount> unitCountList = new ArrayList<>();
        String sql = "select count(consumerIP) from monitor_record where time > now() - 30m and providerName = '" + providerName + "'  group by time(1m)  limit 30;";
        QueryResult queryResult = this.influxTemplate.query(database, sql);
        if (StringUtils.isEmpty(queryResult.getError()) && CollectionUtils.isNotEmpty(queryResult.getResults())) {
            List<QueryResult.Series> seriesList = queryResult.getResults().get(0).getSeries();
            if (CollectionUtils.isNotEmpty(seriesList)) {
                QueryResult.Series series = seriesList.get(0);
                if (CollectionUtils.isNotEmpty(series.getValues())) {
                    for (List<Object> value : series.getValues()) {
                        try {
                            String dateStr = value.get(0).toString().replace("Z", " UTC");//注意是空格+UTC
                            Date date = format.parse(dateStr);
                            UnitCount unitCount = new UnitCount();
                            unitCount.setUnit(minuteormat.format(date));
                            unitCount.setCount(((Double) value.get(1)).intValue());
                            unitCountList.add(unitCount);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return unitCountList;
    }

}
