package edu.wpi.teamname.Astar;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class RoomGraph {

  private HashMap<Node, List<Edge>> graphInfo;
  private HashMap<String, Node> nodes;
  private final String nodesFile, edgesFile;

  private LinkedList<Edge> listOfEdges = new LinkedList<Edge>();

  public RoomGraph(String nodesFile, String edgesFile) throws IOException {
    CSVRead_nodes read_nodes = new CSVRead_nodes(nodesFile);
    this.nodes = read_nodes.getNodes();
    this.nodesFile = nodesFile;
    this.edgesFile = edgesFile;
    this.graphInfo =
        convert_to_roomGraph(); // make sure to set value of nodes before using the function
  }

  public RoomGraph() { // for testing
    this.edgesFile = "Test";
    this.nodesFile = "Test";
    this.graphInfo = new HashMap<Node, List<Edge>>();
    this.nodes = new HashMap<String, Node>();
  }

  public RoomGraph(Connection conn) { // for database
    this.edgesFile = "Database";
    this.nodesFile = "Database";
    this.nodes = new HashMap<String, Node>();
    this.graphInfo = this.convertDatabaseToRoomGraph(conn);
  }

  // {Parent Node : [{start1, end1, cost1}, {start2, end2, cost2}, {start3, end3, cost3}, ...]}
  private HashMap<Node, List<Edge>> convert_to_roomGraph() throws IOException {
    HashMap<Node, List<Edge>> data = new HashMap<Node, List<Edge>>();

    CSVRead_Edges read_edges = new CSVRead_Edges(this.edgesFile);
    List<Edge> edges = read_edges.getEdges(this.nodes); // CSV conversion to list of edges
    List<Edge> children_value;

    /*
    selecting elements/nodes (start nodes and the end nodes) from the edges list of string arrays
    mapping them with the list of nodes
    storing them in the dictionary as parent:child node
     */
    for (int i = 0; i < edges.size(); i++) {
      // ignore the  th element of the string array
      // 1st elem of the string array is start node (parent??)
      // 2nd element of the string array is end node (children??)
      if (this.nodes.containsKey(edges.get(i).getStartNode().getNodeID())
          && this.nodes.containsKey(edges.get(i).getEndNode().getNodeID())) {
        if (data.containsKey(edges.get(i).getStartNode())) {
          children_value = data.get(edges.get(i).getStartNode());
          children_value.add(edges.get(i));
        } else {
          children_value = new LinkedList<Edge>();
          children_value.add(edges.get(i));
          data.put(edges.get(i).getStartNode(), children_value);
        }
        listOfEdges.add(edges.get(i));
      }
    }
    return data;
  }

  private HashMap<Node, List<Edge>> convertDatabaseToRoomGraph(Connection conn) {
    HashMap<Node, List<Edge>> data = new HashMap<Node, List<Edge>>();
    Statement stmt = null;

    try {
      stmt = conn.createStatement();
      // load nodes
      String query = "SELECT * FROM Nodes";
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        String nodeID = rs.getString("nodeID");
        Node node =
            new Node(
                nodeID,
                rs.getInt("xcoord"),
                rs.getInt("ycoord"),
                rs.getString("floor"),
                rs.getString("building"),
                rs.getString("nodeType"),
                rs.getString("longName"),
                rs.getString("shortName"));
        this.nodes.put(nodeID, node);
      }

      // load edges
      LinkedList<Edge> edges = new LinkedList<Edge>();
      for (Map.Entry<String, Node> entry : nodes.entrySet()) {
        data.put(entry.getValue(), new LinkedList<Edge>());
        PreparedStatement prepStmt = null;
        String nodeID = entry.getKey();
        prepStmt = conn.prepareStatement("SELECT * FROM Edges WHERE startNode = ?");
        prepStmt.setString(1, nodeID);
        ResultSet rsP = prepStmt.executeQuery();
        while (rsP.next()) {
          if (nodes.containsKey(nodeID) && nodes.containsKey(rsP.getString("endNode"))) {
            double cost =
                nodes
                    .get(rsP.getString("startNode"))
                    .getMeasuredDistance(nodes.get(rsP.getString("endNode")));
            Edge edge =
                new Edge(
                    nodes.get(rsP.getString("startNode")),
                    nodes.get(rsP.getString("endNode")),
                    cost);
            Edge edge1 =
                new Edge(
                    nodes.get(rsP.getString("endNode")),
                    nodes.get(rsP.getString("startNode")),
                    cost);
            edges.add(edge);
            edges.add(edge1);
            listOfEdges.add(edge);
          }
        }
      } // end of for loop

      List<Edge> children_value; // temp

      /*
      selecting elements/nodes (start nodes and the end nodes) from the edges list of string arrays
      mapping them with the list of nodes
      storing them in the dictionary as parent:child node
       */
      for (int i = 0; i < edges.size(); i++) {
        // ignore the  th element of the string array
        // 1st elem of the string array is start node (parent??)
        // 2nd element of the string array is end node (children??)
        if (this.nodes.containsKey(edges.get(i).getStartNode().getNodeID())
            && this.nodes.containsKey(edges.get(i).getEndNode().getNodeID())) {
          if (data.containsKey(edges.get(i).getStartNode())) {
            children_value = data.get(edges.get(i).getStartNode());
            children_value.add(edges.get(i));
          } else {
            children_value = new LinkedList<Edge>();
            children_value.add(edges.get(i));
            data.put(edges.get(i).getStartNode(), children_value);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return data;
  }

  public HashMap<Node, List<Edge>> getGraphInfo() {
    return this.graphInfo;
  }

  public Node getNode(String nodeID) {
    if (nodes.containsKey(nodeID)) return nodes.get(nodeID);
    else {
      System.out.printf("Node %s not found in data\n", nodeID);
      return null;
    }
  }

  // Getters and setters primarily for testing algos
  public void setGraphInfo(HashMap<Node, List<Edge>> graphInfo) {
    this.graphInfo = graphInfo;
  }

  public HashMap<String, Node> getNodes() {
    return nodes;
  }

  public void setNodes(HashMap<String, Node> nodes) {
    this.nodes = nodes;
  }

  public LinkedList<Edge> getListOfEdges() {
    return listOfEdges;
  }

  public void printGraph() {
    System.out.println();
    for (Map.Entry<Node, List<Edge>> entry : graphInfo.entrySet()) {
      System.out.print(entry.getKey().getNodeID());
      System.out.print(" - ");
      for (int i = 0; i < entry.getValue().size(); i++) {
        entry.getValue().get(i).printEdge();
        System.out.print(" ");
      }
      System.out.println();
    }
    if (graphInfo.isEmpty()) System.out.println("graphInfo is empty");
    System.out.println();
  }
}
