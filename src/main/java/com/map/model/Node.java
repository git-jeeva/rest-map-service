package com.map.model;

import java.util.LinkedList;
import java.util.List;

public class Node {
    private String name;
    private List<Node> adjacents = new LinkedList<>();
    private boolean visited;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getAdjacents() {
        return adjacents;
    }

    public void setAdjacents(List<Node> adjacents) {
        this.adjacents = adjacents;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
