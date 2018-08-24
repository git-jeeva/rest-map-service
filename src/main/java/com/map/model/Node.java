package com.map.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Model class for a node in Graph data structures
 */
public class Node {
    private String name;
    private List<Node> adjacents = new LinkedList<>();

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Node> getAdjacents() {
        return adjacents;
    }
}
