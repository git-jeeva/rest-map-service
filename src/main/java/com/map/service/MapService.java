package com.map.service;

import com.map.exception.MapException;
import com.map.model.Node;
import com.map.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Logger;

/**
 * The MapService determines if two cities are connected using a breadth first search traversal algorithm.
 * It has a worst case time complexity of O(|E| + |V|) and worst case space complexity of O(|V|),
 * where cities (nodes) are denoted by Vertices (V) and connections between cities (roads) are denoted by edges (E)
 */
@Service
public class MapService {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    Messages messages;

    @Value("classpath:city.txt")
    private Resource cityConfig;

    private Map<String, Node> nodeMap = new HashMap<>();

    private void visit(Node node, StringBuilder sb) {
        sb.append(node.getName().trim()).append(" - ");
    }

    /**
     * Breadth-First-Search traversal to print connected cities
     *
     * @param root origin city
     * @return String names of connected cities delimited by hyphen
     */
    public String bfs(String root) {
        return bfs(nodeMap.get(getNodeKey(root)));
    }

    private String bfs(Node root) {
        final String bfsGraph;

        if (root == null) {
            bfsGraph = "";
        } else {
            final StringBuilder sb = new StringBuilder();
            final Set<Node> visitedSet = new HashSet<>();

            final Queue<Node> queue = new LinkedList<>();
            queue.add(root);

            while (!queue.isEmpty()) {
                final Node node = queue.poll();

                if (visitedSet.contains(node)) {
                    continue;
                }

                visit(node, sb);
                visitedSet.add(node);

                for (Node adjacent : node.getAdjacents()) {
                    queue.add(adjacent);
                }
            }

            bfsGraph = sb.toString().substring(0, sb.length() - 3);
        }
        return bfsGraph;
    }

    private void addEdge(String src, String dest) {
        final String srcKey = getNodeKey(src);
        final String destKey = getNodeKey(dest);

        final Node srcNode = nodeMap.getOrDefault(srcKey, new Node(src));
        final Node destNode = nodeMap.getOrDefault(destKey, new Node(dest));
        nodeMap.putIfAbsent(srcKey, srcNode);
        nodeMap.putIfAbsent(destKey, destNode);

        Set<Node> srcAdjacents = srcNode.getAdjacents();
        Set<Node> destAdjacents = destNode.getAdjacents();

        srcAdjacents.add(destNode);
        destAdjacents.add(srcNode);
    }

    /**
     * Checks if the given cities are connected by road/s
     *
     * @param src  origin city
     * @param dest destination city
     * @return boolean true if connected, false otherwise
     */
    public boolean hasBFSPath(String src, String dest) {
        if (src == null || src.trim().isEmpty() || dest == null || dest.trim().isEmpty() || getNodeKey(src).equals(getNodeKey(dest))) {
            return false;
        }

        final Node srcNode = nodeMap.get(getNodeKey(src));

        final Set<Node> visitedNodeSet = new HashSet<>();
        final Queue<Node> bfsQueue = new LinkedList<>();
        bfsQueue.add(srcNode);

        while (!bfsQueue.isEmpty()) {
            final Node node = bfsQueue.poll();

            if (getNodeKey(node.getName()).equals(getNodeKey(dest))) {
                return true;
            }

            if (visitedNodeSet.contains(node)) {
                continue;
            }

            visitedNodeSet.add(node);
            for (Node adjacent : node.getAdjacents()) {
                bfsQueue.add(adjacent);
            }
        }

        return false;
    }

    private String getNodeKey(String name) {
        return name.trim().toLowerCase();
    }

    /**
     * Application startup routine to initialize the graph of cities and their connections based on the
     * configuration file - city.txt
     */
    @PostConstruct
    public void init() throws MapException {
        final String filePath = cityConfig.getFilename();
        log.info("Loading file: " + filePath);

        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(cityConfig.getInputStream()));

            while ((line = br.readLine()) != null) {
                String[] cities = line.split(",");
                addEdge(cities[0], cities[1]);
            }
        } catch (IOException ioe) {
            throw new MapException("Error while Loading file: " + filePath, ioe);
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                // Nothing to do here.
            }
        }
    }
}
