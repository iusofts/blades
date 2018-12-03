package com.iusofts.blades.monitor.service.dao;

import com.iusofts.blades.monitor.BaseTest;
import com.iusofts.blades.monitor.inft.enums.ApplicationCallCountType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MonitorRecordDaoTest extends BaseTest {

    @Autowired
    private MonitorRecordDao monitorRecordDao;

    @Test
    public void add() throws Exception {
    }

    @Test
    public void getProviderallCount() throws Exception {
        monitorRecordDao.getAllProviderAndCallCount(ApplicationCallCountType.get(2).getName());
    }

    @Test
    public void getProviderCallCountByMinute() throws Exception {
        monitorRecordDao.getProviderCallCountByMinute("blades-monitor", ApplicationCallCountType.get(2).getName(), ApplicationCallCountType.get(2).getGroupBy());
    }

    @Test
    public void getApplicationRelations() throws Exception {
        monitorRecordDao.getApplicationRelations();
    }

}