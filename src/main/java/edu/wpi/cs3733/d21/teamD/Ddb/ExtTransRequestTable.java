package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.ExtTransNodeInfo;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;
import javafx.collections.ObservableList;

public class ExtTransRequestTable extends AbsTables {
  // id,serviceType,pFirstName,pLastName,contactInfo,location,transType,assignedEmployee,status

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();

      String query =
          "CREATE TABLE ExternalTransRequests("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "serviceType VARCHAR(40) NOT NULL,"
              + "pFirstName VARCHAR(30) NOT NULL,"
              + "pLastName VARCHAR(30) NOT NULL,"
              + "contactInfo VARCHAR(45) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "transType VARCHAR(25) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "status VARCHAR(20) DEFAULT 'Incomplete',"
              + "CONSTRAINT EXT_REQUESTID PRIMARY KEY(id),"
              + "CONSTRAINT EXT_employee_FK FOREIGN KEY(assignedEmployee) REFERENCES Users(id),"
              // + "CONSTRAINT EXT_location_FK FOREIGN KEY(location) REFERENCES Nodes(nodeID),"
              + "CONSTRAINT EXT_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')))";
      stmt.executeUpdate(query);
      System.out.println("External Trans requests table created");
    } catch (Exception e) {
      System.out.println("External Trans requests table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void populateTable(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "Users.csv";
    try {
      Scanner sc = new Scanner(ExtTransRequestTable.class.getResourceAsStream("/csv/" + filePath));
      Statement stmt = conn.createStatement();

      sc.nextLine();
      while (sc.hasNextLine()) {
        try {
          String[] row = sc.nextLine().split(",");
          String query = "INSERT INTO Users VALUES(" + "'" + row[0] + "'," + "'" + row[1] + "')";
          stmt.execute(query);
        } catch (Exception e) {
          System.out.println("External Transportation Request table did not populate");
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

  public void addEntity(
      Connection conn,
      String serTy,
      String pFN,
      String pLN,
      String contact,
      String location,
      String transType,
      String assignedEmployee) {
    PreparedStatement stmt = null;
    String query =
        "INSERT INTO ExternalTransRequests(serviceType, pFirstName, pLastName, contactInfo, location, transType, assignedEmployee) VALUES(?,?,?,?,?,?,?)";
    try {
      stmt = conn.prepareStatement(query);
      stmt.setString(1, serTy);
      stmt.setString(2, pFN);
      stmt.setString(3, pLN);
      stmt.setString(4, contact);
      stmt.setString(5, location);
      stmt.setString(6, transType);
      stmt.setString(7, assignedEmployee);
      int count = stmt.executeUpdate();

      FDatabaseTables.getAllServiceTable()
          .addEntity(
              GlobalDb.getConnection(),
              this.getID(GlobalDb.getConnection()),
              location,
              "Incomplete",
              assignedEmployee,
              "EXT");

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
          "id \ttype \tpFirstName \tpLastName \tcontactInfo \tlocation \ttransType \tassignedEmployee \tstatus");

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
                + rs.getString("assignedEmployee")
                + " \t"
                + rs.getString("status"));
        System.out.println(" ");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void addIntoExTransDataList(
      ObservableList<ExtTransNodeInfo> ExTransData, boolean employeeAccess) throws IOException {
    Connection conn = GlobalDb.getConnection();
    PreparedStatement stmt = null;
    try {
      if (employeeAccess) {
        stmt =
            conn.prepareStatement(
                "SELECT * FROM ExternalTransRequests WHERE assignedEmployee = ? OR assignedEmployee = ''");
        stmt.setString(1, HomeController.username);
        //        System.out.println(
        //            "this is trying to add data into the employee table " +
        // HomeController.username);
      } else {
        stmt = conn.prepareStatement("SELECT * FROM ExternalTransRequests");
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        ExTransData.add(
            new ExtTransNodeInfo(
                rs.getString("id"),
                rs.getString("serviceType"),
                rs.getString("pFirstName"),
                rs.getString("pLastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("transType"),
                rs.getString("assignedEmployee"),
                rs.getString("status")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    //     return ExTransData;
  }

  public ObservableList<ExtTransNodeInfo> changeExTransData(
      ObservableList<ExtTransNodeInfo> ExTransData) {
    for (ExtTransNodeInfo info : ExTransData) {
      if (!(info.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE ExternalTransRequests SET status = ?, assignedEmployee = ? WHERE id=?");
          stmt.setString(1, info.getStatus());
          stmt.setString(2, info.getAssignedTo());
          stmt.setString(3, info.getId());
          stmt.executeUpdate();

          AllServiceTable.updateEntity(
              GlobalDb.getConnection(),
              info.getId(),
              info.getStatus(),
              info.getAssignedTo(),
              "EXT");

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return ExTransData;
  }

  public int getID(Connection conn) {
    int id = 420;
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT id FROM ExternalTransRequests");
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        System.out.println("LOOK HERE:" + id);
        id = rs.getInt(1);
        System.out.println("LOOK HERE:" + id);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    System.out.println();
    return id;
  }

  public HashMap<Integer, String> getIncompleteRequest() {
    Connection conn = GlobalDb.getConnection();
    HashMap<Integer, String> exTransList = new HashMap<>();
    String id = HomeController.username;
    int i = 0;
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "SELECT serviceType, location, pFirstName, pLastName, contactInfo FROM ExternalTransRequests WHERE status = 'Incomplete' AND assignedEmployee = ?");
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        exTransList.put(
            i,
            rs.getString("serviceType")
                + " -- "
                + rs.getString("location")
                + " -- Name: "
                + rs.getString("pFirstName")
                + " "
                + rs.getString("pLastName")
                + " -- Contact: "
                + rs.getString("contactInfo"));
        i++;
        System.out.println(exTransList);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return exTransList;
  }
}
