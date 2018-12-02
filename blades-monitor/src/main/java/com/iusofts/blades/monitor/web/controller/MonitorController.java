package com.iusofts.blades.monitor.web.controller;

import com.iusofts.blades.common.util.JsonUtils;
import com.iusofts.blades.monitor.inft.MonitorInterface;
import com.iusofts.blades.monitor.inft.dto.Dependency;
import com.iusofts.blades.monitor.inft.dto.OverviewCount;
import com.iusofts.blades.monitor.service.model.ApplicationCount;
import com.iusofts.blades.monitor.service.model.UnitCount;
import com.iusofts.blades.monitor.web.vo.ApplicationCountVo;
import com.iusofts.blades.monitor.web.vo.MonitorRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务监控
 */
@Api(description = "服务监控")
@RequestMapping("/monitor")
@RestController
public class MonitorController {

    private static Logger logger = LoggerFactory.getLogger(MonitorController.class);

    @Autowired
    private MonitorInterface monitorInterface;

    @ApiOperation("调用记录")
    @RequestMapping(value = "/record", method = RequestMethod.POST)
    public void record(@RequestBody MonitorRecordVo recordVo) {
        logger.info("monitor record:{}", JsonUtils.obj2json(recordVo));// 临时设置info级别
        monitorInterface.monitor(recordVo);
    }

    @ApiOperation("概况统计")
    @RequestMapping(value = "/overviewCount", method = RequestMethod.GET)
    public OverviewCount overviewCount() {
        return monitorInterface.overviewCount();
    }

    @ApiOperation("应用调用量统计")
    @RequestMapping(value = "/getAllApplicationCount", method = RequestMethod.GET)
    public ApplicationCountVo getAllApplicationCount() {
        ApplicationCountVo countVo = new ApplicationCountVo();
        List<String> appNames = new ArrayList<>();
        List<String> times = new ArrayList<>();
        List<List<Integer>> unitCountList = new ArrayList<>();

        List<ApplicationCount> countList = monitorInterface.getAllApplicationCount();
        for (ApplicationCount count : countList) {
            appNames.add(count.getAppName());
            if (CollectionUtils.isNotEmpty(count.getUnitCountList())) {
                if (CollectionUtils.isEmpty(times)) {
                    for (UnitCount unitCount : count.getUnitCountList()) {
                        times.add(unitCount.getUnit());
                    }
                }
                unitCountList.add(count.getUnitCountList().stream().map(e -> e.getCount()).collect(Collectors.toList()));
            }
        }

        countVo.setAppNames(appNames);
        countVo.setTimes(times);
        countVo.setUnitCountList(unitCountList);
        return countVo;
    }

    @ApiOperation("调用依赖关系")
    @RequestMapping(value = "/getApplicationDependency", method = RequestMethod.GET)
    public Dependency getApplicationDependency() {
        return monitorInterface.getApplicationDependency();
    }

}
