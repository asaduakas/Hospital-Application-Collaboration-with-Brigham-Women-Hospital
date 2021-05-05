package edu.wpi.teamname.Ddb;

import edu.wpi.teamname.Astar.Node;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class NodesTable extends AbsTables {

  public NodesTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE Nodes("
              + "nodeID VARCHAR(100) NOT NULL,"
              + "xcoord INT NOT NULL,"
              + "ycoord INT NOT NULL,"
              + "floor VARCHAR(200) NOT NULL,"
              + "building VARCHAR(100) NOT NULL,"
              + "nodeType VARCHAR(400) NOT NULL,"
              + "longName VARCHAR(450) NOT NULL,"
              + "shortName VARCHAR(250) NOT NULL,"
              + "favN INT NOT NULL,"
              + "PRIMARY KEY(nodeID))";
      stmt.executeUpdate(query);
      System.out.println("Nodes table created");
    } catch (Exception e) {
      System.out.println("Nodes table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void populateTable(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "MapDAllNodes.csv";
    Scanner sc = null;
    Statement stmt = null;
    try {
      sc = new Scanner(NodesTable.class.getResourceAsStream("/csv/" + filePath));
    } catch (Exception e) {
      System.out.println("nodes path file not found");
      e.printStackTrace();
      return;
    }
    sc.nextLine(); // get rid of the first line which is the column names
    while (sc.hasNextLine()) {
      // nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName (column names)
      String[] row = sc.nextLine().split(",");
      stmt = null;

      try {
        stmt = conn.createStatement();
        String query =
            "INSERT INTO Nodes VALUES("
                + "'"
                + row[0]
                + "', "
                + Integer.parseInt(row[1])
                + ", "
                + Integer.parseInt(row[2])
                + ", "
                + "'"
                + row[3]
                + "', "
                + "'"
                + row[4]
                + "', "
                + "'"
                + row[5]
                + "', "
                + "'"
                + row[6]
                + "', "
                + "'"
                + row[7]
                + "' ,0)";

        int num = stmt.executeUpdate(query);
      } catch (SQLException e) {
        e.printStackTrace();
        return;
      }
    }
    System.out.println("Nodes Table populated");
  }

  public void populateNodesTableExternal(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "MapDnodes.csv";
    Scanner sc = null;
    Statement stmt = null;
    try {
      sc = new Scanner(new File(filePath));
      // sc.useDelimiter("\r\n|\n");
    } catch (Exception e) {
      System.out.println("nodes path file not found");
      e.printStackTrace();
      return;
    }
    System.out.println(sc.nextLine()); // get rid of the first line which is the column names
    while (sc.hasNextLine()) {
      System.out.println("Reading line of nodes CSV");
      // nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName (column names)
      String[] row = sc.nextLine().split(",");
      stmt = null;

      try {
        stmt = conn.createStatement();
        String query =
            "INSERT into Nodes VALUES("
                + "'"
                + row[0]
                + "', "
                + Integer.parseInt(row[1])
                + ", "
                + Integer.parseInt(row[2])
                + ", "
                + "'"
                + row[3]
                + "', "
                + "'"
                + row[4]
                + "', "
                + "'"
                + row[5]
                + "', "
                + "'"
                + row[6]
                + "', "
                + "'"
                + row[7]
                + "' ,0)";

        int num = stmt.executeUpdate(query);
      } catch (SQLException e) {
        e.printStackTrace();
        return;
      }
    }
    System.out.println("Nodes Table populated");
  }

  @Override
  public void deleteEntity(Connection conn, String tableName, String id) {
    try {
      System.out.println(id);
      PreparedStatement stmt = conn.prepareStatement("DELETE FROM Nodes WHERE nodeID=?");
      stmt.setString(1, id);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addEntity(
      Connection conn,
      String nID,
      int xc,
      int yc,
      String f,
      String b,
      String nType,
      String ln,
      String sn,
      int favN) {
    try {
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO Nodes VALUES(?,?,?,?,?,?,?,?,?)");
      stmt.setString(1, nID);
      stmt.setInt(2, xc);
      stmt.setInt(3, yc);
      stmt.setString(4, f);
      stmt.setString(5, b);
      stmt.setString(6, nType);
      stmt.setString(7, ln);
      stmt.setString(8, sn);
      stmt.setInt(9, favN);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void createFavoriteNodeTable(Connection conn) {
    try {
      Statement stmt = conn.createStatement();
      String query =
          "CREATE TABLE FavoriteNodes("
              + "userID VARCHAR(100) NOT NULL,"
              + "nodeID VARCHAR(100) NOT NULL,"
              + "longName VARCHAR(450) NOT NULL,"
              + "CONSTRAINT userID_FK FOREIGN KEY (userID) REFERENCES Users(id),"
              + "CONSTRAINT nodeID_FK FOREIGN KEY (nodeID) REFERENCES Nodes(nodeID))";
      // + "CONSTRAINT longName_FK FOREIGN KEY (longName) REFERENCES Nodes(longName));
      stmt.executeUpdate(query);
      System.out.println("Favorite node table created");
    } catch (Exception e) {
      System.out.println("Favorite node table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void addToFavoriteNodes(Connection conn, String userID, String nodeID, String longName) {
    try {
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO FavoriteNodes VALUES(?,?,?)");
      stmt.setString(1, userID);
      stmt.setString(2, nodeID);
      stmt.setString(3, longName);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ArrayList<String> fetchLongNameFavorites(Connection conn, String userID) {
    ArrayList<String> longNames = new ArrayList<String>();
    try {
      PreparedStatement longNameStmt =
          conn.prepareStatement("SELECT longName FROM FavoriteNodes WHERE userID = ?");
      longNameStmt.setString(1, userID);
      ResultSet rs = longNameStmt.executeQuery();
      while (rs.next()) {
        longNames.add(rs.getString(1));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return longNames;
  }

  public void populateFavoriteNodeTable(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "FavoriteNodes.csv";
    try {
      Scanner sc = new Scanner(NodesTable.class.getResourceAsStream("/csv/" + filePath));
      Statement stmt = conn.createStatement();

      sc.nextLine();
      while (sc.hasNextLine()) {
        try {
          String[] row = sc.nextLine().split(",");
          String query =
              "INSERT INTO FavoriteNodes VALUES(" + "'" + row[0] + "'," + "'" + row[1] + "')";
          stmt.execute(query);
        } catch (Exception e) {
          System.out.println("Favorite node table did not populate");
          e.printStackTrace();
        }
      }

      System.out.println("Favorite node table populated");
    } catch (Exception e) {
      System.out.println("Favorite node table did not populate");
      e.printStackTrace();
      return;
    }
  }

  public ArrayList<String> getCategoryTry(Connection conn, String cate) {
    ArrayList<String> category = new ArrayList<>();
    try {
      PreparedStatement stmt =
          conn.prepareStatement("SELECT longName FROM Nodes WHERE nodeType = ?");
      stmt.setString(1, cate);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        category.add(rs.getString("longName"));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return category;
  }

  public void updateNodeXCoord(Connection conn, String nID, int xc) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET xCoord=? WHERE nodeID=?");
      stmt.setInt(1, xc);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void updateNodeYCoord(Connection conn, String nID, int yc) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET yCoord=? WHERE nodeID=?");
      stmt.setInt(1, yc);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void updateNodeFloor(Connection conn, String nID, String f) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET floor=? WHERE nodeID=?");
      stmt.setString(1, f);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void updateNodeBuilding(Connection conn, String nID, String b) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET building=? WHERE nodeID=?");
      stmt.setString(1, b);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void updateNodeType(Connection conn, String nID, String nType) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET nodeType=? WHERE nodeID=?");
      stmt.setString(1, nType);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void updateNodeLongName(Connection conn, String nID, String longName) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET longName=? WHERE nodeID=?");
      stmt.setString(1, longName);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void updateNodeShortName(Connection conn, String nID, String shortName) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET shortName=? WHERE nodeID=?");
      stmt.setString(1, shortName);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public ArrayList<String> fetchLongName(Connection conn) {
    ArrayList<String> longNames = new ArrayList<String>();
    try {
      PreparedStatement longNameStmt = conn.prepareStatement("SELECT longName FROM Nodes");
      ResultSet rs = longNameStmt.executeQuery();
      while (rs.next()) {
        longNames.add(rs.getString(1));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return longNames;
  }

  public ArrayList<String> fetchLongNameNoHall(Connection conn) {
    ArrayList<String> longNames = new ArrayList<String>();
    try {
      PreparedStatement longNameStmt =
          conn.prepareStatement(
              "SELECT longName FROM Nodes WHERE nodeType = 'ELEV' OR nodeType = 'REST' OR nodeType = 'STAI' OR nodeType = 'DEPT' OR nodeType = 'LABS' OR nodeType = 'INFO' OR nodeType = 'CONF' OR nodeType = 'EXIT' OR nodeType = 'RETL' OR nodeType = 'SERV' OR nodeType = 'PARK'");
      ResultSet rs = longNameStmt.executeQuery();
      while (rs.next()) {
        longNames.add(rs.getString(1));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    System.out.println();
    return longNames;
  }

  private void updateCoord(Connection conn) {
    PreparedStatement stmt1 = null;
    PreparedStatement stmt2 = null;
    Scanner sc = new Scanner(System.in);

    try {
      stmt1 = conn.prepareStatement("UPDATE Nodes SET xcoord = ? WHERE Nodes.nodeID = ?");
      System.out.print("Enter nodeID: ");
      String userNodeID = sc.nextLine();

      System.out.print("Enter new xcoord: ");
      String userXCoord = sc.nextLine();

      stmt1.setString(1, userXCoord);
      stmt1.setString(2, userNodeID);

      int num1 = stmt1.executeUpdate();

      stmt2 = conn.prepareStatement("UPDATE Nodes SET ycoord = ? WHERE Nodes.nodeID = ?");
      System.out.print("Enter new ycoord: ");
      String userYCoord = sc.nextLine();

      stmt2.setString(1, userYCoord);
      stmt2.setString(2, userNodeID);

      int num2 = stmt2.executeUpdate();

      // print new coord
      System.out.println(
          "New xcoord ("
              + userXCoord
              + ") and ycoord ("
              + userYCoord
              + ") of nodeID "
              + userNodeID
              + " has been updated.");

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void updateLongName(Connection conn) {
    PreparedStatement stmt1 = null;
    Scanner sc = new Scanner(System.in);

    try {

      stmt1 = conn.prepareStatement("UPDATE Nodes SET longName = ? WHERE Nodes.nodeID = ?");
      System.out.print("Enter nodeID: ");
      String userNodeID = sc.nextLine();

      System.out.print("Enter new longName: ");
      String userLongName = sc.nextLine();

      stmt1.setString(1, userLongName);
      stmt1.setString(2, userNodeID);

      int num1 = stmt1.executeUpdate();

      // print new coord
      System.out.println(
          "New longName (" + userLongName + ") of nodeID " + userNodeID + " has been updated.");

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void dispAll(Connection conn) {
    Statement stmt = null;

    try {
      stmt = conn.createStatement();
      String query = "SELECT * FROM Nodes";
      ResultSet rs = stmt.executeQuery(query);
      System.out.println(
          "NodeID \txcoord \tycoord \tfloor \tbuilding \tnodeType \tlongName \tshortName");
      while (rs.next()) {
        // nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName
        System.out.println(
            rs.getString("nodeID")
                + " \t"
                + rs.getInt("xcoord")
                + " \t"
                + rs.getInt("ycoord")
                + " \t"
                + rs.getString("floor")
                + " \t"
                + rs.getString("building")
                + " \t"
                + rs.getString("nodeType")
                + " \t"
                + rs.getString("longName")
                + " \t"
                + rs.getString("shortName"));
        System.out.println(" ");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public LinkedList<Node> convertNodesToLL(Connection conn) {
    LinkedList<Node> graphInfo = new LinkedList<>();
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
        graphInfo.add(node);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return graphInfo;
  }

  public void writeNodesTable(Connection connection, String nodesPath) {
    try {
      writeTableToCSV(
          connection,
          "Nodes",
          nodesPath,
          8,
          "nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
