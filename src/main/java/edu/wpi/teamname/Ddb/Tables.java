package edu.wpi.teamname.Ddb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Tables {

  public Tables() {
    System.out.println("-------Embedded Apache Derby Connection Testing --------");
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
    } catch (ClassNotFoundException e) {
      System.out.println("Apache Derby Driver not found. Add the classpath to your module.");
      System.out.println("For IntelliJ do the following:");
      System.out.println("File | Project Structure, Modules, Dependency tab");
      System.out.println("Add by clicking on the green plus icon on the right of the window");
      System.out.println(
          "Select JARs or directories. Go to the folder where the database JAR is located");
      System.out.println("Click OK, now you can compile your program and run it.");
      e.printStackTrace();
      return;
    }
    System.out.println("Apache Derby driver registered!");
  }

  public void clearTables(Connection conn) {
    try {
      for (String tableName :
          new String[] {
            "Nodes",
            "Edges",
            //            "Users",
            "FavoriteNodes",
            "ExternalTransRequests",
            "FloralRequests",
            "FoodDeliveryRequests",
            "LaundryRequests"
          }) {
        conn.createStatement().execute("DROP TABLE " + tableName);
        System.out.println(tableName + " table dropped!");
      }
    } catch (Exception e) {
      System.out.println("Tables can't be dropped");
      // e.printStackTrace();
      return;
    }
  }

  public void createNodesTable(Connection conn) {
    Statement stmt = null;
    try {
      // substitute your database name for myDB
      // conn =
      //
      // DriverManager.getConnection("jdbc:derby:myDB;create=true;username=Admin;password=Admin");
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

  public void populateNodesTable(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "MapDAllNodes.csv";
    Scanner sc = null;
    Statement stmt = null;
    try {
      sc = new Scanner(Tables.class.getResourceAsStream("/csv/" + filePath));
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

  public void createEdgesTable(Connection conn) {
    Statement stmt = null;
    try {
      // substitute your database name for myDB
      // conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE Edges("
              + "edgeID VARCHAR(21) NOT NULL,"
              + "startNode VARCHAR(10) NOT NULL,"
              + "endNode VARCHAR(20) NOT NULL, "
              + "PRIMARY KEY(edgeID))";
      stmt.executeUpdate(query);
      System.out.println("Edges table created");
    } catch (Exception e) {
      System.out.println("Edges table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void populateEdgesTable(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "MapDAllEdges.csv";
    Statement stmt = null;
    try {
      // substitute your database name for myDB
      // conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      Scanner sc = new Scanner(Tables.class.getResourceAsStream("/csv/" + filePath));
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

  public void createExternalTransRequestsTable(Connection conn) {
    Statement stmt = null;
    try {
      // substitute your database name for myDB
      // conn =
      //
      // DriverManager.getConnection("jdbc:derby:myDB;create=true;username=Admin;password=Admin");
      stmt = conn.createStatement();

      String query =
          "CREATE TABLE ExternalTransRequests("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "serviceType VARCHAR(40) NOT NULL,"
              + "pFirstName VARCHAR(30) NOT NULL,"
              + "pLastName VARCHAR(30) NOT NULL,"
              + "contactInfo VARCHAR(45) NOT NULL,"
              + "location VARCHAR(50) NOT NULL,"
              + "transType VARCHAR(25) NOT NULL,"
              + "assignedTo VARCHAR(25) NOT NULL,"
              + "status VARCHAR(20) DEFAULT 'Incomplete',"
              + "CONSTRAINT EX_REQUESTID PRIMARY KEY(id))";
      stmt.executeUpdate(query);
      System.out.println("External Trans requests table created");
    } catch (Exception e) {
      System.out.println("External Trans requests table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void createFloralRequestsTable(Connection conn) {
    Statement stmt = null;
    try {
      // substitute your database name for myDB
      // conn =
      //   DriverManager.getConnection("jdbc:derby:myDB;create=true;username=Admin;password=Admin");
      stmt = conn.createStatement();

      String query =
          "CREATE TABLE FloralRequests("
              + "id VARCHAR (20) NOT NULL,"
              + "type VARCHAR(20) NOT NULL,"
              + "status INT DEFAULT 0 NOT NULL,"
              + "pFirstName VARCHAR(10) NOT NULL,"
              + "pLastName VARCHAR(4) NOT NULL,"
              + "contactInfo VARCHAR(45) NOT NULL,"
              + "location VARCHAR(50) NOT NULL,"
              + "typeOfFlower VARCHAR(25) NOT NULL,"
              + "numOfFlower INT NOT NULL,"
              + "fromFlower VARCHAR(25) NOT NULL,"
              + "PRIMARY KEY(id))";
      stmt.executeUpdate(query);
      System.out.println("Floral requests table created");
    } catch (Exception e) {
      System.out.println("Floral requests table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void createFoodDeliveryRequestsTable(Connection conn) {
    Statement stmt = null;
    try {
      // substitute your database name for myDB
      // conn =
      //
      // DriverManager.getConnection("jdbc:derby:myDB;create=true;username=Admin;password=Admin");
      stmt = conn.createStatement();

      String query =
          "CREATE TABLE FoodDeliveryRequests("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "serviceType VARCHAR(20) NOT NULL,"
              + "pFirstName VARCHAR(10) NOT NULL,"
              + "pLastName VARCHAR(4) NOT NULL,"
              + "contactInfo VARCHAR(45) NOT NULL,"
              + "location VARCHAR(50) NOT NULL,"
              + "specialNeeds VARCHAR(200) NOT NULL,"
              + "createTime VARCHAR(8) NOT NULL,"
              + "createDate VARCHAR(25) NOT NULL,"
              + "assignedTo VARCHAR(25) NOT NULL,"
              + "status VARCHAR(20) DEFAULT 'Incomplete',"
              + "PRIMARY KEY(id))";
      stmt.executeUpdate(query);
      System.out.println("Food requests table created");
    } catch (Exception e) {
      System.out.println("Food requests table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void createLaundryRequestsTable(Connection conn) {
    Statement stmt = null;
    try {
      // substitute your database name for myDB
      // conn =
      //
      // DriverManager.getConnection("jdbc:derby:myDB;create=true;username=Admin;password=Admin");
      stmt = conn.createStatement();

      String query =
          "CREATE TABLE LaundryRequests("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "type VARCHAR(20) NOT NULL,"
              + "status INT DEFAULT 0 NOT NULL,"
              + "pFirstName VARCHAR(10) NOT NULL,"
              + "pLastName VARCHAR(4) NOT NULL,"
              + "contactInfo VARCHAR(45) NOT NULL,"
              + "location VARCHAR(50) NOT NULL,"
              + "assignedPerson VARCHAR(50) NOT NULL,"
              + "PRIMARY KEY(id))";
      stmt.executeUpdate(query);
      System.out.println("Laundry requests table created");
    } catch (Exception e) {
      System.out.println("Laundry requests table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void createUserTable(Connection conn) {
    try {
      // substitute your database name for myDB
      // conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      Statement stmt = conn.createStatement();
      String query =
          "CREATE TABLE Users("
              + "name VARCHAR(50) NOT NULL,"
              + "category VARCHAR(8) NOT NULL,"
              + "PRIMARY KEY(name))";
      stmt.executeUpdate(query);
      System.out.println("User table created");
    } catch (Exception e) {
      System.out.println("User table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void populateUserTable(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "Users.csv";
    try {
      // substitute your database name for myDB
      // conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      Scanner sc = new Scanner(Tables.class.getResourceAsStream("/csv/" + filePath));
      Statement stmt = conn.createStatement();

      sc.nextLine();
      while (sc.hasNextLine()) {
        try {
          String[] row = sc.nextLine().split(",");
          String query = "INSERT INTO Users VALUES(" + "'" + row[0] + "'," + "'" + row[1] + "')";
          stmt.execute(query);
        } catch (Exception e) {
          System.out.println("User table did not populate");
          e.printStackTrace();
        }
      }

      System.out.println("User Table populated");
    } catch (Exception e) {
      System.out.println("User table did not populate");
      e.printStackTrace();
      return;
    }
  }

  public void createFavoriteNodeTable(Connection conn) {
    try {
      // substitute your database name for myDB
      // conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      Statement stmt = conn.createStatement();
      String query =
          "CREATE TABLE FavoriteNodes("
              + "name VARCHAR(50) NOT NULL,"
              + "nodeID VARCHAR(100) NOT NULL)";
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
      // substitute your database name for myDB
      // conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      Scanner sc = new Scanner(Tables.class.getResourceAsStream("/csv/" + filePath));
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

  public void createSearchHistoryTable(Connection conn) {
    try {
      // substitute your database name for myDB
      // conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      Statement stmt = conn.createStatement();
      String query =
          "CREATE TABLE SearchHistory("
              + "startName VARCHAR(50) NOT NULL,"
              + "endName VARCHAR(50) NOT NULL)";
      stmt.executeUpdate(query);
      System.out.println("Search History table created");
    } catch (Exception e) {
      System.out.println("Search History table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void populateSearchHistoryTable(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "SearchHistory.csv";
    try {
      // substitute your database name for myDB
      // conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      Scanner sc = new Scanner(Tables.class.getResourceAsStream("/csv/" + filePath));
      Statement stmt = conn.createStatement();

      sc.nextLine();
      while (sc.hasNextLine()) {
        while (sc.hasNextLine()) {
          try {
            String[] row = sc.nextLine().split(",");
            String query =
                "INSERT INTO SearchHistory VALUES(" + "'" + row[0] + "'," + "'" + row[1] + "')";
            stmt.execute(query);
          } catch (Exception e) {
            System.out.println("Search History table did not populate");
            e.printStackTrace();
          }
        }
      }

      System.out.println("Search History table populated");
    } catch (Exception e) {
      System.out.println("Search History table did not populate");
      e.printStackTrace();
      return;
    }
  }

  private void writeTableToCSV(
      Connection conn, String tableName, String filePath, int numColumns, String firstLine)
      throws IOException {
    FileWriter fw = new FileWriter(filePath);
    fw.write(firstLine + "\n");
    try {
      Statement stmt = conn.createStatement();
      stmt.executeQuery("SELECT * FROM " + tableName);
      ResultSet rs = stmt.getResultSet();
      while (rs.next()) {
        for (int i = 1; i <= numColumns; i++) {
          fw.write(rs.getString(i));
          if (i != numColumns) {
            fw.write(",");
          }
        }
        fw.write("\n");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    fw.flush();
    fw.close();
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

  public void writeEdgesTable(Connection connection, String edgesPath) {
    try {
      writeTableToCSV(connection, "Edges", edgesPath, 3, "edgeID,startNode,endNode");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void dispAll(Connection conn) {
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

  public static void addNode(
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

  public static void deleteNode(Connection conn, String nID) {

    try {
      System.out.println(nID);
      PreparedStatement stmt = conn.prepareStatement("DELETE FROM Nodes WHERE nodeID=?");
      stmt.setString(1, nID);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void addEdge(Connection conn, String edgeID, String startNode, String endNode) {
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

  public static void deleteEdge(Connection conn, String eID) {
    try {
      PreparedStatement stmt = conn.prepareStatement("DELETE FROM Edges WHERE edgeID=?");
      stmt.setString(1, eID);
      stmt.executeUpdate();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //  private static void updateEdge(Connection conn, String eID) {
  //    PreparedStatement stmt = null;
  //    try {
  //      stmt = conn.prepareStatement(" ");
  //
  //      // (nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName, favoriteNode)
  //      // (nID, xc, yc, f, b, nType, ln, sn, favN)
  //    } catch (Exception e) {
  //      e.printStackTrace();
  //    }
  //  }

  public static void updateNodeXCoord(Connection conn, String nID, int xc) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET xCoord=? WHERE nodeID=?");
      stmt.setInt(1, xc);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void updateNodeYCoord(Connection conn, String nID, int yc) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET yCoord=? WHERE nodeID=?");
      stmt.setInt(1, yc);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void updateNodeFloor(Connection conn, String nID, String f) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET floor=? WHERE nodeID=?");
      stmt.setString(1, f);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void updateNodeBuilding(Connection conn, String nID, String b) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET building=? WHERE nodeID=?");
      stmt.setString(1, b);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void updateNodeType(Connection conn, String nID, String nType) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET nodeType=? WHERE nodeID=?");
      stmt.setString(1, nType);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void updateNodeLongName(Connection conn, String nID, String longName) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET longName=? WHERE nodeID=?");
      stmt.setString(1, longName);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void updateNodeShortName(Connection conn, String nID, String shortName) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Nodes SET shortName=? WHERE nodeID=?");
      stmt.setString(1, shortName);
      stmt.setString(2, nID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void updateEdgeStartNode(Connection conn, String eID, String startNode) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Edges SET startNode=? WHERE edgeID=?");
      stmt.setString(1, startNode);
      stmt.setString(2, eID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void updateEdgeEndNode(Connection conn, String eID, String endNode) {
    try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE Edges SET endNode=? WHERE edgeID=?");
      stmt.setString(1, endNode);
      stmt.setString(2, eID);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static ArrayList<String> fetchLongName(Connection conn) {
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

  private static void updateCoord(Connection conn) {
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

  public static void updateLongName(Connection conn) {
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

  public static void getEdgeInfo(Connection conn) {
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

  public static void addExTransInfo(
      Connection conn,
      String serTy,
      String pFN,
      String pLN,
      String contact,
      String loca,
      String transType,
      String assigned) {
    PreparedStatement stmt = null;
    String query =
        "INSERT INTO ExternalTransRequests(serviceType, pFirstName, pLastName, contactInfo, location, transType, assignedTo) VALUES(?,?,?,?,?,?,?)";
    try {
      stmt = conn.prepareStatement(query);
      stmt.setString(1, serTy);
      stmt.setString(2, pFN);
      stmt.setString(3, pLN);
      stmt.setString(4, contact);
      stmt.setString(5, loca);
      stmt.setString(6, transType);
      stmt.setString(7, assigned);

      int count = stmt.executeUpdate();

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void addFoodInfo(
      Connection conn,
      String serTy,
      String pFN,
      String pLN,
      String contact,
      String loca,
      String specialNeed,
      String createTime,
      String createDate,
      String assigned) {
    PreparedStatement stmt = null;
    String query =
        "INSERT INTO FoodDeliveryRequests(serviceType, pFirstName, pLastName, contactInfo, location, specialNeeds, createTime, createDate, assignedTo) VALUES(?,?,?,?,?,?,?,?,?)";
    try {
      stmt = conn.prepareStatement(query);
      stmt.setString(1, serTy);
      stmt.setString(2, pFN);
      stmt.setString(3, pLN);
      stmt.setString(4, contact);
      stmt.setString(5, loca);
      stmt.setString(6, specialNeed);
      stmt.setString(7, createTime);
      stmt.setString(8, createDate);
      stmt.setString(9, assigned);

      int count = stmt.executeUpdate();

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void dispFoodTable(Connection conn) {
    Statement stmt = null;

    try {
      stmt = conn.createStatement();
      String query = "SELECT * FROM FoodDeliveryRequests";
      ResultSet rs = stmt.executeQuery(query);
      System.out.println(
          "id \ttype \tfirstName \tlastName \tcontactInfo \tlocation \tneeds \ttime \tdate \tassigned \tstatus");
      while (rs.next()) {
        System.out.println(
            rs.getString("id")
                + " \t"
                + rs.getString("serviceType")
                + " \t"
                + rs.getString("pFirstName")
                + " \t"
                + rs.getString("pLastName")
                + " \t"
                + rs.getString("contactInfo")
                + " \t"
                + rs.getString("location")
                + " \t"
                + rs.getString("specialNeeds")
                + " \t"
                + rs.getString("createTime")
                + " \t"
                + rs.getString("createDate")
                + " \t"
                + rs.getString("assignedTo")
                + " \t"
                + rs.getString("status"));
        System.out.println(" ");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void dispExTransRequestsTable(Connection conn) {
    Statement stmt = null;

    try {
      stmt = conn.createStatement();
      String query = "SELECT * FROM ExternalTransRequests";
      ResultSet rs = stmt.executeQuery(query);
      System.out.println(
          "id \ttype \tpFirstName \tpLastName \tcontactInfo \tlocation \ttransType \tassignedTo \tstatus");

      while (rs.next()) {
        // id,type,pFirstName,pLastName,contactInfo,location,transType,status
        System.out.println(
            rs.getString("id")
                + " \t"
                + rs.getString("serviceType")
                + " \t"
                + rs.getString("pFirstName")
                + " \t"
                + rs.getString("pLastName")
                + " \t"
                + rs.getString("contactInfo")
                + " \t"
                + rs.getString("location")
                + " \t"
                + rs.getString("transType")
                + " \t"
                + rs.getString("assignedTo")
                + " \t"
                + rs.getString("status"));
        System.out.println(" ");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  // public static void updateExTransStatus(Connection conn, String, )
  // id,type,status,pFirstName,pLastName,contactInfo,location,assignedPerson
  public static void addLaundryRequestInfo(
      Connection conn,
      String serviceType,
      String status,
      String pFirstName,
      String pLastName,
      String contactInfo,
      String location,
      String assignedPerson) {

    PreparedStatement stmt = null;
    String query =
        "INSERT INTO LaundryRequests(serviceType, status, pFirstName, pLastName,contactInfo,location,assignedPerson) VALUES(?,?,?,?,?,?,?)";
    try {
      stmt = conn.prepareStatement(query);
      stmt.setString(1, serviceType);
      stmt.setString(2, status);
      stmt.setString(3, pFirstName);
      stmt.setString(4, pLastName);
      stmt.setString(5, contactInfo);
      stmt.setString(6, location);
      stmt.setString(7, assignedPerson);

      int count = stmt.executeUpdate();

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private static void dispLaundRequestsTable(Connection conn) {
    Statement stmt = null;

    try {
      stmt = conn.createStatement();
      String query = "SELECT * FROM LaundryRequests";
      ResultSet rs = stmt.executeQuery(query);
      System.out.println(
          "id \ttype \tstatus \tpFirstName \tpLastName \tcontactInfo \tlocation \tassignedPerson");

      while (rs.next()) {
        // id,type,pFirstName,pLastName,contactInfo,location,transType,status
        System.out.println(
            rs.getInt("id")
                + " \t"
                + rs.getString("type")
                + " \t"
                + rs.getString("status")
                + " \t"
                + rs.getString("pFirstName")
                + " \t"
                + rs.getString("pLastName")
                + " \t"
                + rs.getString("contactInfo")
                + " \t"
                + rs.getString("location")
                + " \t"
                + rs.getInt("assignedPerson"));
        System.out.println(" ");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static ObservableList<String> fetchEmployee(Connection conn) {
    ObservableList<String> employees = FXCollections.observableArrayList();
    try {
      PreparedStatement longNameStmt =
          conn.prepareStatement("SELECT id FROM Users WHERE category = 'Employee'");
      ResultSet rs = longNameStmt.executeQuery();
      while (rs.next()) {
        employees.add(rs.getString(1));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return employees;
  }
}
