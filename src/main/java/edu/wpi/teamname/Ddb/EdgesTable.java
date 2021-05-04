package edu.wpi.teamname.Ddb;

import edu.wpi.teamname.Astar.Edge;
import edu.wpi.teamname.Astar.Node;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;

public class EdgesTable extends AbsTables {

  public EdgesTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE Edges("
              + "edgeID VARCHAR(100) NOT NULL,"
              + "startNode VARCHAR(100) NOT NULL,"
              + "endNode VARCHAR(100) NOT NULL, "
              + "PRIMARY KEY(edgeID),"
              + "CONSTRAINT EDGE_startNode_FK FOREIGN KEY (startNode) REFERENCES Nodes(nodeID) ON DELETE CASCADE,"
              + "CONSTRAINT EDGE_endNode_FK FOREIGN KEY (endNode) REFERENCES Nodes(nodeID) ON DELETE CASCADE)";
      stmt.executeUpdate(query);
      conn.commit();
      System.out.println("Edges table created");
    } catch (Exception e) {
      System.out.println("Edges table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void populateTable(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "MapDAllEdges.csv";
    Statement stmt = null;
    try {
      // substitute your database name for myDB
      // conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      Scanner sc = new Scanner(EdgesTable.class.getResourceAsStream("/csv/" + filePath));
      stmt = conn.createStatement();
      sc.nextLine();
      while (sc.hasNextLine()) {
        try {
          String[] row = sc.nextLine().split(",");
          String query =
              "INSERT INTO Edges VALUES("
                  + "'"
                  + row[0]
                  + "',"
                  + "'"
                  + row[1]
                  + "',"
                  + "'"
                  + row[2]
                  + "')";
          stmt.execute(query);
          conn.commit();
        } catch (Exception e) {
          System.out.println("Edges table did not populate");
          e.printStackTrace();
        }
      }

      System.out.println("Edges Table populated");
    } catch (Exception e) {
      System.out.println("Edges table did not populate");
      e.printStackTrace();
      return;
    }
  }

  public void addEntity(Connection conn, String edgeID, String startNode, String endNode) {
    try {
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO Edges VALUES(?,?,?)");
      stmt.setString(1, edgeID);
      stmt.setString(2, startNode);
      stmt.setString(3, endNode);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteEntity(Connection conn, String tableName, String eID) {
    try {
      PreparedStatement stmt = conn.prepareStatement("DELETE FROM Edges WHERE edgeID=?");
      stmt.setString(1, eID);
      stmt.executeUpdate();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateEdgeStartNode(Connection conn, String eID, String startNode) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Edges SET startNode=? WHERE edgeID=?");
      stmt.setString(1, startNode);
      stmt.setString(2, eID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void updateEdgeEndNode(Connection conn, String eID, String endNode) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Edges SET endNode=? WHERE edgeID=?");
      stmt.setString(1, endNode);
      stmt.setString(2, eID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void updateEdgeID(Connection conn, String eID, String NewID) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Edges SET edgeID=? WHERE edgeID=?");
      stmt.setString(1, NewID);
      stmt.setString(2, eID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void getEdgeInfo(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query = "SELECT * FROM Edges";
      ResultSet rs = stmt.executeQuery(query);
      System.out.println("edgeID \tstartNode \tendNode");
      while (rs.next()) {
        // edgeID, startNode, endNode
        System.out.println(
            rs.getString("edgeID")
                + " \t"
                + rs.getString("startNode")
                + " \t"
                + rs.getString("endNode"));
        System.out.println(" ");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void populateEdgesTableExternal(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "MapDedges.csv";
    Statement stmt = null;
    try {
      // substitute your database name for myDB
      // conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      Scanner sc = new Scanner(new File(filePath));
      // sc.useDelimiter("\r\n|\n");
      stmt = conn.createStatement();
      System.out.println(sc.nextLine());
      while (sc.hasNextLine()) {
        System.out.println("Reading line of edges CSV");
        try {
          String[] row = sc.nextLine().split(",");
          String query =
              "INSERT INTO Edges VALUES("
                  + "'"
                  + row[0]
                  + "',"
                  + "'"
                  + row[1]
                  + "',"
                  + "'"
                  + row[2]
                  + "')";
          stmt.execute(query);
        } catch (Exception e) {
          System.out.println("Edges table did not populate");
          e.printStackTrace();
        }
      }

      System.out.println("Edges Table populated");
    } catch (Exception e) {
      System.out.println("Edges table did not populate");
      e.printStackTrace();
      return;
    }
  }

  public LinkedList<Edge> convertEdgesToLL(Connection conn, LinkedList<Node> graphInfo) {
    // load edges
    LinkedList<Edge> listOfEdges = new LinkedList<Edge>();
    try {
      for (Node entry : graphInfo) {
        PreparedStatement prepStmt = null;
        String nodeID = entry.getNodeID();
        prepStmt = conn.prepareStatement("SELECT * FROM Edges WHERE startNode = ?");
        prepStmt.setString(1, nodeID);
        ResultSet rsP = prepStmt.executeQuery();
        while (rsP.next()) {
          if (graphInfo.contains(entry)
              && graphInfo.contains(this.getNodeByID(rsP.getString("endNode"), graphInfo))) {
            double cost =
                this.getNodeByID(rsP.getString("startNode"), graphInfo)
                    .getMeasuredDistance(this.getNodeByID(rsP.getString("endNode"), graphInfo));
            Edge edge =
                new Edge(
                    this.getNodeByID(rsP.getString("startNode"), graphInfo),
                    this.getNodeByID(rsP.getString("endNode"), graphInfo),
                    cost);
            Edge edge1 =
                new Edge(
                    this.getNodeByID(rsP.getString("endNode"), graphInfo),
                    this.getNodeByID(rsP.getString("startNode"), graphInfo),
                    cost);
            entry.getEdges().add(edge);
            getNodeByID(rsP.getString("endNode"), graphInfo).getEdges().add(edge1);
            listOfEdges.add(edge);
          }
        }
      } // end of for loop
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listOfEdges;
    /*
    selecting elements/nodes (start nodes and the end nodes) from the edges list of string arrays
    mapping them with the list of nodes
    storing them in the dictionary as parent:child node
     */
  }

  public Node getNodeByID(String nodeID, LinkedList<Node> graphInfo) {
    for (Node node : graphInfo) {
      if (nodeID.equals(node.getNodeID())) {
        return node;
      }
    }
    System.out.printf("Node %s not found in data\n", nodeID);
    return null;
  }

  public void writeEdgesTable(Connection connection, String edgesPath) {
    try {
      writeTableToCSV(connection, "Edges", edgesPath, 3, "edgeID,startNode,endNode");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
