package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.SanitationNodeInfo;
import java.io.IOException;
import java.sql.*;
import javafx.collections.ObservableList;

public class SanitationServRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,location,assignedEmployee,typeOfSanitation,urgencyLevel(high med low)

  public SanitationServRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE SanitationRequest("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(100) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "descriptionOfIssue VARCHAR(100) NOT NULL,"
              + "urgencyLevel VARCHAR(25) NOT NULL,"
              + "PRIMARY KEY(id),"
              //              + "CONSTRAINT SAN_employee_FK FOREIGN KEY(assignedEmployee) REFERENCES
              // Users(id),"
              // + "CONSTRAINT SAN_location_FK FOREIGN KEY(location) REFERENCES Nodes(nodeID),"
              + "CONSTRAINT SAN_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')),"
              + "CONSTRAINT SAN_urgency_check CHECK (urgencyLevel IN ('Low Priority', 'Medium Priority', 'High Priority')))";
      stmt.executeUpdate(query);
      System.out.println("Sanitation Service Request table created");
    } catch (Exception e) {
      System.out.println("Sanitation Service Request table was not created");
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
      String sanitationType,
      String urgencyLev) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO SanitationRequest (firstName, lastName, contactInfo,"
                  + "location, descriptionOfIssue, urgencyLevel) VALUES(?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, sanitationType);
      stmt.setString(6, urgencyLev);
      stmt.executeUpdate();

      FDatabaseTables.getAllServiceTable()
          .addEntity(
              GlobalDb.getConnection(),
              this.getID(GlobalDb.getConnection()),
              location,
              "Incomplete",
              assignedEmp,
              "SANI");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addIntoSanitationList(
      ObservableList<SanitationNodeInfo> sanitationData, boolean employeeAccess)
      throws IOException {
    PreparedStatement stmt = null;
    Connection conn = GlobalDb.getConnection();
    try {
      if (employeeAccess) {
        stmt =
            conn.prepareStatement(
                "SELECT * FROM SanitationRequest WHERE assignedEmployee = ? OR assignedEmployee = ''");
        stmt.setString(1, HomeController.username);
        //        System.out.println(
        //            "this is trying to add data into the employee table " +
        // HomeController.username);
        //        System.out.println("this is getting the userType " + HomeController.userTypeEnum);
      } else {
        stmt = conn.prepareStatement("SELECT * FROM SanitationRequest");
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        sanitationData.add(
            new SanitationNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("descriptionOfIssue"),
                rs.getString("urgencyLevel")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public ObservableList<SanitationNodeInfo> changeSanitationData(
      ObservableList<SanitationNodeInfo> sanitationData) {
    for (SanitationNodeInfo sanitationInfo : sanitationData) {
      if (!(sanitationInfo.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE SanitationRequest SET status = ?, assignedEmployee = ? WHERE id=?");
          stmt.setString(1, sanitationInfo.getStatus());
          stmt.setString(2, sanitationInfo.getAssignedEmployee());
          stmt.setString(3, sanitationInfo.getId());
          stmt.executeUpdate();

          System.out.println("ID: " + sanitationInfo.getId());

          AllServiceTable.updateEntity(
              GlobalDb.getConnection(),
              sanitationInfo.getId(),
              sanitationInfo.getStatus(),
              sanitationInfo.getAssignedEmployee(),
              "SANI");

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return sanitationData;
  }

  public int getID(Connection conn) {
    int id = 420;
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT id FROM SanitationRequest");
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
}
