package edu.wpi.cs3733.d21.teamD.Astar;

import java.util.LinkedList;

public class Node {
  private String nodeID;
  private int xcoord;
  private int ycoord;
  private String floor;
  private String building;
  private String nodeType;
  private String longName;
  private String shortName;
  private LinkedList<Edge> edges;

  public Node(
      String nodeID,
      int xcoord,
      int ycoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    this.nodeID = nodeID;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
    this.edges = new LinkedList<Edge>();
  }

  public Node() {
    this.nodeID = "empty";
    this.xcoord = 0;
    this.ycoord = 0;
    this.floor = "floor";
    this.building = "building";
    this.nodeType = "nodeType";
    this.longName = "longName";
    this.shortName = "shortName";
    this.edges = new LinkedList<Edge>();
  }

  public double getMeasuredDistance(Node child) {
    return Math.sqrt(
        Math.pow(this.xcoord - child.getXCoord(), 2)
            + Math.pow(this.ycoord - child.getYCoord(), 2));
  }

  // FOR TESTING
  public void printInfo() {
    System.out.println(this.nodeID);
    System.out.println(this.xcoord);
    System.out.println(this.ycoord);
    System.out.println(this.floor);
    System.out.println(this.building);
    System.out.println(this.nodeType);
    System.out.println(this.longName);
    System.out.println(this.shortName);
    System.out.println();
  }

  public void setCoords(int x, int y) {
    this.xcoord = x;
    this.ycoord = y;
  }

  // GETTERS
  public String getNodeID() {
    return nodeID;
  }

  public int getXCoord() {
    return xcoord;
  }

  public int getYCoord() {
    return ycoord;
  }

  public String getFloor() {
    return floor;
  }

  public String getBuilding() {
    return building;
  }

  public String getNodeType() {
    return nodeType;
  }

  public String getLongName() {
    return this.longName;
  }

  public String getShortName() {
    return shortName;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public void setXcoord(int xcoord) {
    this.xcoord = xcoord;
  }

  public void setYcoord(int ycoord) {
    this.ycoord = ycoord;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public LinkedList<Edge> getEdges() {
    return edges;
  }

  public void setEdges(LinkedList<Edge> edges) {
    this.edges = edges;
  }

  public void addEdge(Edge edge) {
    this.edges.add(edge);
  }

  public int compareFloor(Node other) {
    switch (this.floor) {
      case "L2":
        if (other.getFloor().equals("L2")) return 0;
        else return -1;
      case "L1":
        if (other.getFloor().equals("L2")) return 1;
        if (other.getFloor().equals("L1")) return 0;
        else return -1;
      case "1":
        if (other.getFloor().equals("L2") || other.getFloor().equals("L1")) return 1;
        if (other.getFloor().equals("1")) return 0;
        else return -1;
      case "2":
        if (other.getFloor().equals("3")) return -1;
        if (other.getFloor().equals("2")) return 0;
        else return 1;
      case "3":
        if (other.getFloor().equals("3")) return 0;
        else return 1;
      default:
        return -100;
    }
  }
}
