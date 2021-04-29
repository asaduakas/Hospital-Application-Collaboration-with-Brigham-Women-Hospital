package edu.wpi.teamname.Astar;

import javafx.beans.property.SimpleStringProperty;

public class Edge {
  private String startNode;
  private String endNode;
  private double cost;

  // for ui
  private String edgeID;
  private String startNodeShortName;
  private String endNodeShortName;

  // for editing columns
  private SimpleStringProperty edgeIDsimple;
  private SimpleStringProperty startShortsimple;
  private SimpleStringProperty endShortSimple;

  public Edge(Node startNode, Node endNode, double cost) {
    this.startNode = startNode.getNodeID();
    this.endNode = endNode.getNodeID();
    this.cost = cost;
    this.edgeID = startNode.getNodeID() + "_" + endNode.getNodeID();
    this.startNodeShortName = startNode.getShortName();
    this.endNodeShortName = endNode.getShortName();
    this.edgeIDsimple = new SimpleStringProperty(edgeID);
    this.startShortsimple = new SimpleStringProperty(startNodeShortName);
    this.endShortSimple = new SimpleStringProperty(endNodeShortName);
  }

  public Edge(String edgeID, String startNode, String endNode) {
    this.edgeID = edgeID;
    this.startNodeShortName = startNode;
    this.endNodeShortName = endNode;
  }

  public void printEdge() {
    System.out.print("{");
    System.out.print(edgeID);
    System.out.print(", ");
    System.out.print(startNode);
    System.out.print(", ");
    System.out.print(endNode);
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

  public String getStartNode() {
    return startNode;
  }

  public String getEndNode() {
    return endNode;
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

  public String getEdgeIDsimple() {
    return edgeIDsimple.get();
  }

  public SimpleStringProperty edgeIDsimpleProperty() {
    return edgeIDsimple;
  }

  public void setEdgeIDsimple(String edgeIDsimple) {
    this.edgeIDsimple.set(edgeIDsimple);
  }

  public String getStartShortsimple() {
    return startShortsimple.get();
  }

  public SimpleStringProperty startShortsimpleProperty() {
    return startShortsimple;
  }

  public void setStartShortsimple(String startShortsimple) {
    this.startShortsimple.set(startShortsimple);
  }

  public String getEndShortSimple() {
    return endShortSimple.get();
  }

  public SimpleStringProperty endShortSimpleProperty() {
    return endShortSimple;
  }

  public void setEndShortSimple(String endShortSimple) {
    this.endShortSimple.set(endShortSimple);
  }
}
