package com.iusofts.blades.monitor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("应用调用量统计")
public class ApplicationCountVo {

    @ApiModelProperty("应用名称集合")
    private List<String> appNames;

    @ApiModelProperty("时间集合")
    private List<String> times;

    @ApiModelProperty("单元调用量")
    private List<List<Integer>> unitCountList;

    public List<String> getAppNames() {
        return appNames;
    }

    public void setAppNames(List<String> appNames) {
        this.appNames = appNames;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public List<List<Integer>> getUnitCountList() {
        return unitCountList;
    }

    public void setUnitCountList(List<List<Integer>> unitCountList) {
        this.unitCountList = unitCountList;
    }
}
