package edu.wpi.cs3733.d21.teamD.Astar;

import java.util.LinkedList;

public class Path {

  private LinkedList<Node> path;
  private LinkedList<Edge> pathEdges;
  private double cost;

  public Path() {
    this.path = new LinkedList<Node>();
    this.pathEdges = new LinkedList<Edge>();
    this.cost = 0;
  }

  public Path(LinkedList<Node> path, LinkedList<Edge> pathEdges, double cost) {
    this.path = path;
    this.pathEdges = pathEdges;
    this.cost = cost;
  }

  public LinkedList<Node> getPath() {
    return path;
  }

  public void setPath(LinkedList<Node> path) {
    this.path = path;
  }

  public LinkedList<Edge> getPathEdges() {
    return pathEdges;
  }

  public void setPathEdges(LinkedList<Edge> pathEdges) {
    this.pathEdges = pathEdges;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  public Path clone() {
    return new Path(
        (LinkedList<Node>) this.path.clone(), (LinkedList<Edge>) this.pathEdges.clone(), this.cost);
  }

  public Path appendTo(Path aPath) {
    for (Node node : this.path) {
      aPath.getPath().add(node);
    }
    for (Edge edge : this.pathEdges) {
      aPath.getPathEdges().add(edge);
    }
    aPath.setCost(aPath.getCost() + cost);
    return aPath;
  }

  public void printPath() {
    if (this.path.isEmpty()) {
      System.out.println("Path doesn't exist");
    } else {
      System.out.println("NodeIDs: ");
      for (Node node : path) {
        System.out.print(node.getNodeID() + "\t");
      }
      System.out.println("Cost:" + this.cost);
    }
  }

  public void printPathEdges() {
    if (this.pathEdges.isEmpty()) {
      System.out.println("Path doesn't exist");
    } else {
      System.out.println("EdgeIDs: ");
      for (Edge edge : pathEdges) {
        System.out.print(edge.getEdgeID() + "\t");
      }
      System.out.println("Cost:" + this.cost);
    }
  }

  @Override
  public boolean equals(Object o) {
    boolean isEqual = true;
    Path other = (Path) o;
    if (this.getCost() != other.getCost()) isEqual = false; // check if costs are different

    LinkedList<Node> otherNodes = other.getPath();
    int otherSize = otherNodes.size();
    if (this.getPath().size() != otherSize) isEqual = false; // check if sizes are different
    else {
      for (int i = 0;
          i < this.getPath().size();
          i++) { // for each node in the list of nodes, check if the node in the same index or
        // reverse index is equal
        if (!this.getPath().get(i).equals(otherNodes.get(i))
            && !this.getPath().get(i).equals(otherNodes.get(otherSize - i - 1))) isEqual = false;
      }
    }
    return isEqual;
  }
}
