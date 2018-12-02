package com.iusofts.blades.monitor.service;

import com.iusofts.blades.common.domain.ServiceInstanceDetail;
import com.iusofts.blades.monitor.inft.MonitorInterface;
import com.iusofts.blades.monitor.inft.dto.OverviewCount;
import com.iusofts.blades.monitor.service.dao.MonitorRecordDao;
import com.iusofts.blades.monitor.service.model.ApplicationCount;
import com.iusofts.blades.monitor.service.model.MonitorRecord;
import com.iusofts.blades.monitor.service.util.BladesUtil;
import com.iusofts.blades.monitor.web.vo.MonitorRecordVo;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public OverviewCount overviewCount() {
        OverviewCount count = new OverviewCount();
        count.setServiceCount(BladesUtil.getServiceList().size());
        count.setAppCount(BladesUtil.getApplicationList().size());

        Set<String> providerSet = new HashSet<>();
        for (ServiceInstance<ServiceInstanceDetail> instance : BladesUtil.getServiceDataList()) {
            providerSet.add(instance.getAddress() + ":" + instance.getPort());
        }
        count.setProviderCount(providerSet.size());

        //count.setConsumerCount(4);
        //count.setErrorCount(5);
        //count.setWarningCount(6);
        return count;
    }
}
