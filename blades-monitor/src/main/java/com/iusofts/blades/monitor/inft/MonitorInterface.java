package com.iusofts.blades.monitor.inft;

import com.iusofts.blades.monitor.inft.dto.Dependency;
import com.iusofts.blades.monitor.inft.dto.OverviewCount;
import com.iusofts.blades.monitor.inft.enums.ApplicationCallCountType;
import com.iusofts.blades.monitor.service.model.ApplicationCount;
import com.iusofts.blades.monitor.web.vo.MonitorRecordVo;

import java.util.List;

/**
 * 监控服务接口
 */
public interface MonitorInterface {

    void monitor(MonitorRecordVo recordVo);

    /**
     * 获取一段时间内所有应用的调用量
     * @return
     */
    List<ApplicationCount> getAllApplicationCount(ApplicationCallCountType type);

    /**
     * 概况统计
     */
    OverviewCount overviewCount();

    /**
     * 获取应用依赖关系
     * @return
     */
    Dependency getApplicationDependency();

    /**
     * 获取应用服务依赖关系
     * @return
     */
    Dependency getApplicationServiceDependency();

}
