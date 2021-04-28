package edu.wpi.teamname.Astar;
// initially made in Algos repo

import java.util.HashMap;
import java.util.LinkedList;

public class singleBFS implements IPathFinding {

  private Path nodeTo; // visited nodes (in order) that are part of the final path
  private boolean hasPath; // to stop search if path is found

  public singleBFS() {
    this.nodeTo = new Path();
    this.hasPath = false;
  }

  public void search(RoomGraph data, Node start, Node target) {
    if (start != null && target != null) {
      // reinitialize
      this.nodeTo = new Path();
      this.hasPath = false;

      LinkedList<Node> searchQueue =
          new LinkedList<Node>(); // holds the priority of nodes we want to check the children of
      LinkedList<Node> visited = new LinkedList<Node>(); // holds the nodes we visited already
      HashMap<Node, Edge> nodesTo =
          new HashMap<Node, Edge>(); // key = node, value = parent node in path

      visited.add(start);
      searchQueue.add(start);
      while (!searchQueue.isEmpty()) {
        Node node = searchQueue.removeFirst();
        LinkedList<Edge> edges = (LinkedList<Edge>) data.getGraphInfo().get(node);
        for (Edge edge : edges) { // for each edge connected to node
          Node child = edge.getEndNode();
          if (child == target) hasPath = true;
          if (!visited.contains(child)) { // if we haven't visited child, enqueue
            searchQueue.add(child);
            visited.add(child);
            nodesTo.put(child, edge);
          }
        }
      }

      // makes Path from nodesTo hashmap info
      if (hasPath) {
        Node node = target;
        nodeTo.getPath().addFirst(node);
        while (node != start) {
          Node parent = nodesTo.get(node).getStartNode();
          nodeTo.getPath().addFirst(parent);
          nodeTo.getPathEdges().addFirst(nodesTo.get(node));
          nodeTo.setCost(nodeTo.getCost() + nodesTo.get(node).getCost());
          node = parent;
        }
      }
    }
  }

  public Path shortestPath() {
    return this.nodeTo;
  }

  public boolean pathExists() {
    return this.hasPath;
  }

  public void printPathTo() {
    if (this.hasPath) {
      for (Node node : nodeTo.getPath()) {
        System.out.println("NodeID: " + node.getNodeID());
      }
      System.out.println("Cost: " + nodeTo.getCost());
    } else {
      System.out.println("Path doesn't exist");
    }
  }

  public void printEdgesTo() {
    if (this.hasPath) {
      for (Edge edge : nodeTo.getPathEdges()) {
        edge.printEdge();
        System.out.println();
      }
      System.out.println("Cost: " + nodeTo.getCost());
    } else {
      System.out.println("Path doesn't exist");
    }
  }
}
