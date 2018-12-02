package com.iusofts.blades.monitor.service;

import com.iusofts.blades.monitor.inft.MonitorInterface;
import com.iusofts.blades.monitor.service.dao.MonitorRecordDao;
import com.iusofts.blades.monitor.service.model.ApplicationCount;
import com.iusofts.blades.monitor.service.model.MonitorRecord;
import com.iusofts.blades.monitor.web.vo.MonitorRecordVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 监控服务
 */
@Service
public class MonitorService implements MonitorInterface {

    @Autowired
    private MonitorRecordDao monitorRecordDao;

    @Override
    public void monitor(MonitorRecordVo recordVo) {
        if (recordVo != null) {
            MonitorRecord record = new MonitorRecord();
            BeanUtils.copyProperties(recordVo, record);
            monitorRecordDao.add(record);
        }
    }

    @Override
    public List<ApplicationCount> getAllApplicationCount() {
        List<ApplicationCount> countList = this.monitorRecordDao.getAllProviderAndCallCount();
        for (ApplicationCount count : countList) {
            count.setUnitCountList(this.monitorRecordDao.getProviderCallCountByMinute(count.getAppName()));
        }
        return countList;
    }
}
