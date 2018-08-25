package com.map.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Model class for a node in Graph data structures
 */
public class Node {
    private String name;
    private Set<Node> adjacents = new HashSet<>();

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<Node> getAdjacents() {
        return adjacents;
    }
}
