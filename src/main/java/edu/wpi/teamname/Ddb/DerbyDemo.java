package edu.wpi.teamname.Ddb;

/** Created by Wilson Wong Modified 1/22/2020 by Chris Myers and Wilson Wong */
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DerbyDemo {

  public static void main(String[] args) {
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
    Connection connection = null;

    try {
      // substitute your database name for myDB
      connection =
          DriverManager.getConnection("jdbc:derby:myDB;create=true;username=Admin;password=Admin");
      System.out.println("Apache Derby connection established!");

      if (args.length == 0) {
        // display the options
        System.out.println("Argument needed. Options are:");
        System.out.println("1 - Display Node Information");
        System.out.println("2 - Update Node Coordinates");
        System.out.println("3 - Update Node Location Long Name");
        System.out.println("4 - Display Edge Information");
        System.out.println("5 - Exit Program");
        System.out.println("Please re-run program with an argument.");
        return;
      } else {
        switch (args[0]) {
          case "1":
            dispExTransRequestsTable(connection);
            break;
          case "2":
            updateCoord(connection);
            break;
          case "3":
            updateLongName(connection);
            break;
          case "4":
            getEdgeInfo(connection);
            break;
          case "5": // exit program
            System.out.println("Exiting Program...");
            return;
        }
      }

    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return;
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

  //  private static void updateNode(Connection conn, String nID) {
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

  public static void dispUsersTable(Connection conn) {
    Statement stmt = null;

    try {
      stmt = conn.createStatement();
      String query = "SELECT * FROM Users";
      ResultSet rs = stmt.executeQuery(query);
      System.out.println("name \tcategory");
      while (rs.next()) {
        // id,type,pFirstName,pLastName,contactInfo,location,transType,status
        System.out.println(rs.getString("name") + " \t" + rs.getString("category"));
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
}
