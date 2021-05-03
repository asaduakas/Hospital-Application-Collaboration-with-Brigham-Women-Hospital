package edu.wpi.teamname.Ddb;

import edu.wpi.teamname.views.Mapping.CategoryNodeInfo;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.collections.ObservableList;

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
              + "name VARCHAR(50) NOT NULL,"
              + "nodeID VARCHAR(100) NOT NULL,"
              + "CONSTRAINT nodeID_FK FOREIGN KEY (nodeID) REFERENCES Nodes(nodeID))";
      stmt.executeUpdate(query);
      System.out.println("Favorite node table created");
    } catch (Exception e) {
      System.out.println("Favorite node table was not created");
      e.printStackTrace();
      return;
    }
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

  public void getCategory(Connection conn, ObservableList<CategoryNodeInfo> category, String cate) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement("SELECT longName FROM Nodes WHERE nodeType = ?");
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        stmt.setString(1, cate);
        category.add(new CategoryNodeInfo(rs.getString("longName")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
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
}
