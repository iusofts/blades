package com.iusofts.blades.monitor.service.model;

import java.util.List;

/**
 * 应用调用量
 */
public class ApplicationCount {

    private String appName;

    private Integer count;

    List<UnitCount> unitCountList;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<UnitCount> getUnitCountList() {
        return unitCountList;
    }

    public void setUnitCountList(List<UnitCount> unitCountList) {
        this.unitCountList = unitCountList;
    }
}
