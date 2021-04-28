package edu.wpi.teamname.Astar;

import java.util.*;

public class aStar implements IPathFinding {

  private Path nodeTo; // visited nodes (in order) that are part of the final path
  private boolean hasPath; // to stop search if path is found
  private HashMap<Node, Double> scores =
      new HashMap<Node, Double>(); // key = node, value = score of node = cost + heuristic

  public aStar() {
    this.nodeTo = new Path();
    this.hasPath = false;
  }

  public void search(RoomGraph data, Node start, Node target) {
    if (start != null && target != null) {
      // reinitialize
      this.nodeTo = new Path();
      this.scores = new HashMap<Node, Double>();
      hasPath = false;

      LinkedList<Node> closedList =
          new LinkedList<Node>(); // nodes we don't want to consider anymore
      LinkedList<Node> openList = new LinkedList<Node>(); // nodes queued for search

      HashMap<Node, Edge> nodesTo =
          new HashMap<Node, Edge>(); // key = node, value = edge leading to node, holds parent
      HashMap<Node, Double> costs =
          new HashMap<Node, Double>(); // key = node, value = accumulated cost to get to node

      // initialize at start
      costs.put(start, 0.0);
      scores.put(start, calculateHeuristic(start, target));
      openList.add(start);

      while (!openList.isEmpty()) { // until queue is empty
        insertionSortScore(openList); // sort queue by smallest score first
        Node current = openList.peek();
        if (current == target) { // if current node is target
          hasPath = true;
          break;
        }

        LinkedList<Edge> edges =
            (LinkedList<Edge>)
                data.getGraphInfo().get(current); // get edges with this node as start

        if (edges != null) {
          for (Edge edge : edges) { // iterate through each edge
            Node child = edge.getEndNode();
            double totalWeight =
                costs.get(edge.getStartNode())
                    + edge.getCost(); // calculate cost to get to this node

            if (!openList.contains(child)
                && !closedList.contains(child)) { // if neither list contain, put into queue
              nodesTo.put(child, edge);
              costs.put(child, totalWeight);
              scores.put(child, costs.get(child) + calculateHeuristic(child, target));
              openList.add(child);
            } else {
              if (totalWeight
                  < costs.get(child)) { // if this cost is smaller than previous recorded cost
                nodesTo.put(child, edge);
                costs.put(child, totalWeight);
                scores.put(child, costs.get(child) + calculateHeuristic(child, target));

                if (closedList.contains(child)) { // move back from closed list to open list
                  closedList.remove(child);
                  openList.add(child);
                }
              }
            }
          }
        }

        // remove from queue and put into already visited list
        openList.remove(current);
        closedList.add(current);
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

  private void insertionSortScore(List<Node> nodes) { // sort edges by cost
    int N = nodes.size();
    for (int i = 1; i < N; i++) { // move down the list starting from left to right
      for (int j = i;
          j > 0 && (scores.get(nodes.get(j)) < scores.get(nodes.get(j - 1)));
          j--) { // exchange edges if previous is smaller
        Node temp = nodes.get(j - 1);
        Node temp2 = nodes.get(j);
        nodes.remove(nodes.get(j));
        nodes.remove(nodes.get(j - 1));
        nodes.add(j - 1, temp2);
        nodes.add(j, temp);
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
