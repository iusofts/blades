/*
 * Copyright (C) 2006-2016 AiJia All rights reserved
 * Author: Ivan Shen
 * Date: 2017/1/18
 * Description:Node.java
 */
package com.iusofts.blades.monitor.service.dto;

/**
 * 节点
 *
 * @author Ivan Shen
 */
public class Node {

    /**
     * 类型
     */
    private Integer category;

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private Integer value;

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
