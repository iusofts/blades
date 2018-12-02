package com.iusofts.blades.monitor.inft.dto;

/**
 * 关系
 */
public class Edge {


    /**
     * fromID : 2
     * relation : 调用200次
     * toID : 1
     * weight : 1
     * color : #000
     */

    private String fromID;
    private String relation;
    private String toID;
    private int weight;
    private String color;

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
