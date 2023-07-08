/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

/**
 *
 * @author karad
 */
import java.sql.*;
import java.util.*;

public class DijkstraAlgorithm {
    private static final int INFINITY = Integer.MAX_VALUE;


    public static void dijkstra(Map<Integer, Map<Integer, Integer>> graph, int startVertex) {
        Map<Integer, Integer> distances = new HashMap<>();
        Map<Integer, Integer> previousVertices = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>();

        for (int vertex : graph.keySet()) {
            distances.put(vertex, INFINITY);
            previousVertices.put(vertex, null);
        }

        distances.put(startVertex, 0);
        priorityQueue.offer(new Vertex(startVertex, 0));

        while (!priorityQueue.isEmpty()) {
            Vertex currentVertex = priorityQueue.poll();
            int currentCity = currentVertex.city;

            if (visited.contains(currentCity)) {
                continue;
            }

            visited.add(currentCity);

            for (Map.Entry<Integer, Integer> neighbor : graph.get(currentCity).entrySet()) {
                int neighborCity = neighbor.getKey();
                int distance = neighbor.getValue();

                if (distances.get(currentCity) + distance < distances.get(neighborCity)) {
                    distances.put(neighborCity, distances.get(currentCity) + distance);
                    previousVertices.put(neighborCity, currentCity);
                    priorityQueue.offer(new Vertex(neighborCity, distances.get(neighborCity)));
                }
            }
        }

        printShortestPaths(startVertex, distances, previousVertices);
    }

    private static void printShortestPaths(int startVertex, Map<Integer, Integer> distances,
                                           Map<Integer, Integer> previousVertices) {
        System.out.println("Shortest Paths from Vertex " + startVertex);
        for (Map.Entry<Integer, Integer> entry : distances.entrySet()) {
            int destination = entry.getKey();
            int distance = entry.getValue();

            List<Integer> path = buildPath(destination, previousVertices);
            System.out.println("Destination: " + destination + "\tDistance: " + distance + "\tPath: " + path);
        }
    }

    private static List<Integer> buildPath(int destination, Map<Integer, Integer> previousVertices) {
        List<Integer> path = new ArrayList<>();
        int vertex = destination;

        while (vertex != 0) {
            path.add(0, vertex);
            vertex = previousVertices.get(vertex);
        }

        return path;
    }
    
     private static void printGraph(Map<Integer, Map<Integer, Integer>> graph) {
        System.out.println("Graph:");
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : graph.entrySet()) {
            int sourceVertex = entry.getKey();
            Map<Integer, Integer> neighbors = entry.getValue();

            System.out.print(sourceVertex + ": ");
            for (Map.Entry<Integer, Integer> neighbor : neighbors.entrySet()) {
                int destinationVertex = neighbor.getKey();
                int distance = neighbor.getValue();
                System.out.print(destinationVertex + "(" + distance + ") ");
            }
            System.out.println();
        }
    }
    
    public static void formGraph(){
        Connection conn = DB.getInstance().getConnection();
        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select IdCity1, IdCity2, Distance\n" +
                                                            "from Distances");
            while (resultSet.next()) {
                int vertex1 = resultSet.getInt("IdCity1");
                int vertex2 = resultSet.getInt("IdCity2");
                int distance = resultSet.getInt("Distance");

                graph.computeIfAbsent(vertex1, k -> new HashMap<>()).put(vertex2, distance);
                graph.computeIfAbsent(vertex2, k -> new HashMap<>()).put(vertex1, distance);
            }

            printGraph(graph);
            dijkstra(graph, 1);

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        formGraph();
    }

    static class Vertex implements Comparable<Vertex> {
        int city;
        int distance;

        Vertex(int city, int distance) {
            this.city = city;
            this.distance = distance;
        }

        @Override
        public int compareTo(Vertex other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}

