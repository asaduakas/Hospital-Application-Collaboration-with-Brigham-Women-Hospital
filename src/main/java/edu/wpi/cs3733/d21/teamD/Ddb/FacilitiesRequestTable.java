package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.FacilitiesNodeInfo;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import javafx.collections.ObservableList;

public class FacilitiesRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,location,assignedEmployee,urgencyLevel(high med
  // low),descriptionOfIssue(a very long string)

  public FacilitiesRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE FacilitiesServiceRequest("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(200) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "urgencyLevel VARCHAR(450) NOT NULL,"
              + "descriptionOfIssue VARCHAR(500) DEFAULT '',"
              + "PRIMARY KEY(id),"
              //              + "CONSTRAINT FAC_employee_FK FOREIGN KEY (assignedEmployee)
              // REFERENCES Users (id),"
              // + "CONSTRAINT FAC_location_FK FOREIGN KEY (location) REFERENCES Nodes (nodeID),"
              + "CONSTRAINT FAC_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')),"
              + "CONSTRAINT FAC_urgency_check CHECK (urgencyLevel IN ('Low Priority', 'Medium Priority', 'High Priority')))";

      stmt.executeUpdate(query);
      System.out.println("Facilities Service Request table created");
    } catch (Exception e) {
      System.out.println("Facilities Service Request table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void addEntity(
      Connection conn,
      String firstName,
      String lastName,
      String contactInfo,
      String location,
      String assignedEmployee,
      String urgencyLevel,
      String descriptionOfIssue) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO FacilitiesServiceRequest (firstName, lastName, contactInfo, location, assignedEmployee, urgencyLevel, descriptionOfIssue) VALUES(?,?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, assignedEmployee);
      stmt.setString(6, urgencyLevel);
      stmt.setString(7, descriptionOfIssue);
      stmt.executeUpdate();

      FDatabaseTables.getAllServiceTable()
          .addEntity(
              GlobalDb.getConnection(),
              this.getID(GlobalDb.getConnection()),
              location,
              "Incomplete",
              assignedEmployee,
              "FACIL");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addIntoFacilitiesDataList(
      ObservableList<FacilitiesNodeInfo> facilitiesData, boolean employeeAccess)
      throws IOException {
    PreparedStatement stmt = null;
    Connection conn = GlobalDb.getConnection();
    try {
      if (employeeAccess) {
        stmt =
            conn.prepareStatement(
                "SELECT * FROM FacilitiesServiceRequest WHERE assignedEmployee = ? OR assignedEmployee  IS NULL");
        stmt.setString(1, HomeController.username);
        //        System.out.println(
        //            "this is trying to add data into the employee table " +
        // HomeController.username);
        //        System.out.println("this is getting the userType " + HomeController.userTypeEnum);
      } else {
        stmt = conn.prepareStatement("SELECT * FROM FacilitiesServiceRequest");
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        facilitiesData.add(
            new FacilitiesNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("urgencyLevel"),
                rs.getString("descriptionOfIssue")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public ObservableList<FacilitiesNodeInfo> changeFacilitiesData(
      ObservableList<FacilitiesNodeInfo> facilitiesData) {
    for (FacilitiesNodeInfo facilitiesInfo : facilitiesData) {
      if (!(facilitiesInfo.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE FacilitiesServiceRequest SET status = ?, assignedEmployee = ? WHERE id=?");
          stmt.setString(1, facilitiesInfo.getStatus());
          stmt.setString(2, facilitiesInfo.getAssignedEmployee());
          stmt.setString(3, facilitiesInfo.getId());
          stmt.executeUpdate();

          AllServiceTable.updateEntity(
              GlobalDb.getConnection(),
              facilitiesInfo.getId(),
              facilitiesInfo.getStatus(),
              facilitiesInfo.getAssignedEmployee(),
              "EXT");

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return facilitiesData;
  }

  public int getID(Connection conn) {
    int id = 420;
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT id FROM FacilitiesServiceRequest");
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
    HashMap<Integer, String> facilitiesRequestList = new HashMap<>();
    String id = HomeController.username;
    int i = 0;
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "SELECT location, firstName, lastName, contactInfo FROM FacilitiesServiceRequest WHERE status = 'Incomplete' AND assignedEmployee = ?");
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        facilitiesRequestList.put(
            i,
            "Facilities Service"
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
    return facilitiesRequestList;
  }
}
