package com.map.service;

import com.map.model.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MapService {
    @Value("classpath:city.txt")
    private Resource file;

    private Map<String, Node> nodeMap = new HashMap<>();

    private void visit(Node node) {
        System.out.print(node.getName() + " ");
    }

    public Map<String, Node> getNodeMap() {
        return nodeMap;
    }

    public void bfs(Node root) {
        if (root == null) return;

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node node = queue.poll();

            if (node.isVisited()) continue;

            visit(node);
            node.setVisited(true);

            for (Node adjacent : node.getAdjacents()) {
                queue.add(adjacent);
            }
        }
    }

    public void addEdge(String src, String dest) {
        String srcName = getNodeName(src);
        String destName = getNodeName(dest);

        Node srcNode = nodeMap.getOrDefault(srcName, new Node(srcName));
        Node destNode = nodeMap.getOrDefault(destName, new Node(destName));
        nodeMap.putIfAbsent(srcName, srcNode);
        nodeMap.putIfAbsent(destName, destNode);

        List<Node> srcAdjacents = srcNode.getAdjacents();
        List<Node> destAdjacents = destNode.getAdjacents();

        if (!srcAdjacents.contains(destNode)) {
            srcAdjacents.add(destNode);
        }
        if(!destAdjacents.contains(srcNode)) {
            destAdjacents.add(srcNode);
        }
    }

    public boolean hasBFSPath(String src, String dest) {
        String srcName = getNodeName(src);
        String destName = getNodeName(dest);

        Set<Node> visitedSet = new HashSet<>();

        Node srcNode = nodeMap.get(srcName);
        Queue<Node> queue = new LinkedList<>();
        queue.add(srcNode);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if(current == null) continue;

            if (getNodeName(current.getName()).equalsIgnoreCase(destName)) {
                return true;
            }

            if (visitedSet.contains(current)) {
                continue;
            }

            visitedSet.add(current);

            for (Node adjacent : current.getAdjacents()) {
                queue.add(adjacent);
            }
        }

        return false;
    }

    private String getNodeName(String name) {
        return name.trim().toLowerCase();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(file.getURI()), StandardCharsets.UTF_8);

        for (String line : lines) {
            String[] cities = line.split(",");
            addEdge(cities[0], cities[1]);
        }

        System.out.print("BFS Traversal: ");
        bfs(nodeMap.get(getNodeName("Boston")));
    }
}
