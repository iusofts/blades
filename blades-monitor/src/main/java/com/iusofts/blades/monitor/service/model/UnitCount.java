package com.iusofts.blades.monitor.service.model;

/**
 * 单元调用量(如:每分钟调用量)
 */
public class UnitCount {

    private String unit;

    private Integer count;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
