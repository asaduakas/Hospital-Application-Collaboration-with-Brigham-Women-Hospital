package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.SecurityRequestNodeInfo;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import javafx.collections.ObservableList;

public class SecurityRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,location,assignedEmployee,urgencyLevel(high med
  // low),other
  // **description - field should be optional for user

  public SecurityRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE SecurityRequest ("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(100) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "urgencyLevel VARCHAR(25) NOT NULL,"
              + "description VARCHAR(400) DEFAULT '',"
              + "PRIMARY KEY(id),"
              //              + "CONSTRAINT SEC_employee_FK FOREIGN KEY(assignedEmployee) REFERENCES
              // Users(id),"
              // + "CONSTRAINT SEC_location_FK FOREIGN KEY(location) REFERENCES Nodes(nodeID),"
              + "CONSTRAINT SEC_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')),"
              + "CONSTRAINT SEC_urgency_check CHECK (urgencyLevel IN ('Low Priority', 'Medium Priority', 'High Priority')))";
      stmt.executeUpdate(query);
      System.out.println("Security Service Request table created");
    } catch (Exception e) {
      System.out.println("Security Service Request table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void populateTable(Connection conn, String filePath) {}

  public void addEntity(
      Connection conn,
      String firstName,
      String lastName,
      String contactInfo,
      String location,
      String assignedEmployee,
      String urgencyLev,
      String des) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO SecurityRequest (firstName, lastName, contactInfo, "
                  + "location,assignedEmployee, urgencyLevel, description) VALUES(?,?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, assignedEmployee);
      stmt.setString(6, urgencyLev);
      stmt.setString(7, des);
      stmt.executeUpdate();

      FDatabaseTables.getAllServiceTable()
          .addEntity(
              GlobalDb.getConnection(),
              this.getID(GlobalDb.getConnection()),
              location,
              "Incomplete",
              assignedEmployee,
              "SECUR");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addIntoSanitationList(
      ObservableList<SecurityRequestNodeInfo> securityData, boolean employeeAccess)
      throws IOException {
    PreparedStatement stmt = null;
    Connection conn = GlobalDb.getConnection();
    try {
      if (employeeAccess) {
        stmt =
            conn.prepareStatement(
                "SELECT * FROM SecurityRequest WHERE assignedEmployee = ? OR assignedEmployee  IS NULL");
        stmt.setString(1, HomeController.username);
        //        System.out.println(
        //            "this is trying to add data into the employee table " +
        // HomeController.username);
        //        System.out.println("this is getting the userType " + HomeController.userTypeEnum);
      } else {
        stmt = conn.prepareStatement("SELECT * FROM SecurityRequest");
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        securityData.add(
            new SecurityRequestNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("urgencyLevel"),
                rs.getString("description")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public ObservableList<SecurityRequestNodeInfo> changeSecurityData(
      ObservableList<SecurityRequestNodeInfo> securityData) {
    for (SecurityRequestNodeInfo securityInfo : securityData) {
      if (!(securityInfo.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE SecurityRequest SET status = ?, assignedEmployee = ? WHERE id=?");
          stmt.setString(1, securityInfo.getStatus());
          stmt.setString(2, securityInfo.getAssignedEmployee());
          stmt.setString(3, securityInfo.getId());
          stmt.executeUpdate();

          AllServiceTable.updateEntity(
              GlobalDb.getConnection(),
              Integer.valueOf(securityInfo.getId()),
              securityInfo.getStatus(),
              securityInfo.getAssignedEmployee(),
              "SECUR");

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return securityData;
  }

  public int getID(Connection conn) {
    int id = 420;
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT id FROM SecurityRequest");
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
    HashMap<Integer, String> securityRequestList = new HashMap<>();
    String id = HomeController.username;
    int i = 0;
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "SELECT location, firstName, lastName, contactInfo FROM SecurityRequest WHERE status = 'Incomplete' AND assignedEmployee = ?");
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        securityRequestList.put(
            i,
            "Security Request"
                + " -- "
                + rs.getString("location")
                + " -- Name: "
                + rs.getString("firstName")
                + " "
                + rs.getString("lastName")
                + " -- Contact: "
                + rs.getString("contactInfo"));
        i++;
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return securityRequestList;
  }
}
