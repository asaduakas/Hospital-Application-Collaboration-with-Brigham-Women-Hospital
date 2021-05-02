package edu.wpi.teamname.Astar;

import edu.wpi.teamname.Ddb.FDatabaseTables;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class RoomGraph {

  private LinkedList<Node> graphInfo;
  private final String nodesFile, edgesFile;

  private LinkedList<Edge> listOfEdges = new LinkedList<Edge>();

  public RoomGraph(String nodesFile, String edgesFile) throws IOException {
    this.nodesFile = nodesFile;
    this.edgesFile = edgesFile;
    this.graphInfo = new LinkedList<Node>();
    convert_to_roomGraph(); // make sure to set value of nodes before using the function
  }

  public RoomGraph() { // for testing
    this.edgesFile = "Test";
    this.nodesFile = "Test";
    this.graphInfo = new LinkedList<Node>();
  }

  public RoomGraph(Connection conn) { // for database
    this.edgesFile = "Database";
    this.nodesFile = "Database";
    this.graphInfo = new LinkedList<Node>();
    //new reference to convert database to RoomGraph:
    FDatabaseTables.getNodeTable().convertNodesToLL(conn);
    //old reference: convertDatabaseToRoomGraph(conn);
  }

  // {Parent Node : [{start1, end1, cost1}, {start2, end2, cost2}, {start3, end3, cost3}, ...]}
  private LinkedList<Node> convert_to_roomGraph() throws IOException {

    CSVRead_Edges read_edges = new CSVRead_Edges(this.edgesFile);
    CSVRead_nodes read_nodes = new CSVRead_nodes(nodesFile);
    HashMap<String, Node> nodes = read_nodes.getNodes();
    List<Edge> edges = read_edges.getEdges(nodes); // CSV conversion to list of edges

    for (Map.Entry<String, Node> E : nodes.entrySet()) {
      graphInfo.add(E.getValue());
    }

    for (int i = 0; i < edges.size(); i++) {
      // ignore the  th element of the string array
      // 1st elem of the string array is start node (parent??)
      // 2nd element of the string array is end node (children??)
      if (nodes.containsKey(edges.get(i).getStartNodeID())
          && nodes.containsKey(edges.get(i).getEndNodeID())) {
        getNodeByID(edges.get(i).getStartNodeID()).addEdge(edges.get(i)); // add edge to node
        listOfEdges.add(edges.get(i));
      }
    }

    // cleanup?
    read_edges = null;
    read_nodes = null;
    nodes = null;
    edges = null;

    return graphInfo;
  }

  public LinkedList<Node> getGraphInfo() {
    return this.graphInfo;
  }

  public Node getNodeByID(String nodeID) {
    for (Node node : graphInfo) {
      if (nodeID.equals(node.getNodeID())) {
        return node;
      }
    }
    System.out.printf("Node %s not found in data\n", nodeID);
    return null;
  }

  public Node getNode(Node entry) {
    for (Node node : graphInfo) {
      if (entry == node) {
        return node;
      }
    }
    System.out.printf("Node not found in data\n");
    return null;
  }

  // Getters and setters primarily for testing algos
  public void setGraphInfo(LinkedList<Node> graphInfo) {
    this.graphInfo = graphInfo;
  }

  public LinkedList<Edge> getListOfEdges() {
    return listOfEdges;
  }

  public void printGraph() {
    System.out.println();
    for (Node node : graphInfo) {
      System.out.print(node.getNodeID());
      System.out.print(" - ");
      for (int i = 0; i < node.getEdges().size(); i++) {
        node.getEdges().get(i).printEdge();
        System.out.print(" ");
      }
      System.out.println();
    }
    if (graphInfo.isEmpty()) System.out.println("graphInfo is empty");
    System.out.println();
  }
}
