package com.iusofts.blades.monitor.inft.dto;

import java.util.List;

/**
 * 调用依赖关系
 */
public class Dependency {

    /**
     * 节点
     */
    private List<Node> nodes;

    /**
     * 关系
     */
    private List<Edge> edges;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }
}
