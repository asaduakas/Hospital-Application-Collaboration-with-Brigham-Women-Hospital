package edu.wpi.teamname.Astar;

public class Edge {
  private String edgeID;
  private String reverseEdgeID;
  private String startNodeID;
  private String endNodeID;
  private double cost;

  // for ui
  private String startNodeShortName;
  private String endNodeShortName;

  public Edge(Node startNode, Node endNode, double cost) {
    this.startNodeID = startNode.getNodeID();
    this.endNodeID = endNode.getNodeID();
    this.cost = cost;
    this.edgeID = startNode.getNodeID() + "_" + endNode.getNodeID();
    this.reverseEdgeID = endNode.getNodeID() + "_" + startNode.getNodeID();
    this.startNodeShortName = startNode.getShortName();
    this.endNodeShortName = endNode.getShortName();
  }

  // FOR TESTING DON'T USE ACUTALLY
  public Edge(String edgeID, String startNode, String endNode) {
    this.edgeID = edgeID;
    this.startNodeShortName = startNode;
    this.endNodeShortName = endNode;
  }

  public void printEdge() {
    System.out.print("{");
    System.out.print(edgeID);
    System.out.print(", ");
    System.out.print(startNodeID);
    System.out.print(", ");
    System.out.print(endNodeID);
    System.out.print(", ");
    System.out.print(cost);
    System.out.print("}");
  }

  // Getters & Setters
  public String getEdgeID() {
    return edgeID;
  }

  public void setEdgeID(String edgeID) {
    this.edgeID = edgeID;
  }

  public String getStartNodeID() {
    return startNodeID;
  }

  public String getEndNodeID() {
    return endNodeID;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  public String getStartNodeShortName() {
    return startNodeShortName;
  }

  public void setStartNodeShortName(String startNodeShortName) {
    this.startNodeShortName = startNodeShortName;
  }

  public String getEndNodeShortName() {
    return endNodeShortName;
  }

  public void setEndNodeShortName(String endNodeShortName) {
    this.endNodeShortName = endNodeShortName;
  }

  public String getReverseEdgeID() {
    return reverseEdgeID;
  }

  public void setReverseEdgeID(String reverseEdgeID) {
    this.reverseEdgeID = reverseEdgeID;
  }

  public boolean equals(Edge e) {
    if (startNodeID.equals(e.getStartNodeID()) && endNodeID.equals(e.getEndNodeID())) return true;
    if (startNodeID.equals(e.getEndNodeID()) && endNodeID.equals(e.getStartNodeID())) return true;
    return false;
  }
}
