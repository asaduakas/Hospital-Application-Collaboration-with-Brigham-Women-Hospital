package edu.wpi.teamname.Astar;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Dijkstras implements IPathFinding {

  private Path nodeTo; // visited nodes (in order) that are part of the final path
  private boolean hasPath; // to stop search if path is found

  public Dijkstras() {
    this.nodeTo = new Path();
    this.hasPath = false;
  }

  public void search(RoomGraph data, Node start, Node target) {
    if (start != null && target != null) {
      // reinitialize
      this.nodeTo = new Path();
      hasPath = false;

      LinkedList<Node> closedList =
          new LinkedList<Node>(); // nodes we don't want to consider anymore
      LinkedList<Node> openList = new LinkedList<Node>(); // nodes queued for search

      HashMap<String, Edge> nodesTo =
          new HashMap<String, Edge>(); // key = node, value = edge leading to node, holds parent

      openList.add(start);
      while (!openList.isEmpty()) { // until queue is empty
        Node current = openList.peek();
        if (current == target) { // if current node is target
          hasPath = true;
          break;
        }
        LinkedList<Edge> edges =
            (LinkedList<Edge>)
                data.getNode(current).getEdges(); // get edges with this node as start

        insertionSortEdgesCost(edges);

        if (edges != null) {
          for (Edge edge : edges) { // iterate through each edge
            Node child = data.getNodeByID(edge.getEndNodeID());
            if (!openList.contains(child) && !closedList.contains(child)) {
              nodesTo.put(child.getNodeID(), edge);
              openList.add(child);
            }
          }
        }

        // remove from queue and put into already visited list
        openList.remove(current);
        closedList.add(current);
      }

      if (hasPath) {
        Node node = target;
        nodeTo.getPath().addFirst(node);
        while (node != start) {
          Node parent = data.getNodeByID(nodesTo.get(node.getNodeID()).getStartNodeID());
          nodeTo.getPath().addFirst(parent);
          nodeTo.getPathEdges().addFirst(nodesTo.get(node.getNodeID()));
          nodeTo.setCost(nodeTo.getCost() + nodesTo.get(node.getNodeID()).getCost());
          node = parent;
        }
      }
    }
  }

  private void insertionSortEdgesCost(List<Edge> edges) { // sort edges by cost
    int N = edges.size();
    for (int i = 1; i < N; i++) { // move down the list starting from left to right
      for (int j = i;
          j > 0 && (edges.get(j).getCost() < edges.get(j - 1).getCost());
          j--) { // exchange edges if previous is smaller
        Edge temp = edges.get(j - 1);
        Edge temp2 = edges.get(j);
        edges.remove(edges.get(j));
        edges.remove(edges.get(j - 1));
        edges.add(j - 1, temp2);
        edges.add(j, temp);
      }
    }
  }

  public Path shortestPath() {
    return this.nodeTo;
  }

  private double calculateHeuristic(Node current, Node target) {
    return Math.max(
        Math.abs(target.getXCoord() - current.getXCoord()),
        Math.abs(target.getYCoord() - current.getYCoord()));
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
