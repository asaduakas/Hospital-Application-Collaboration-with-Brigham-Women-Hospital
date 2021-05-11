package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.ComputerNodeInfo;
import java.io.IOException;
import java.sql.*;
import javafx.collections.ObservableList;

public class ComputerRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,location,assignedEmployee,descriptionOfIssue(a very
  // long string)

  public ComputerRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE ComputerServiceRequest("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(200) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "descriptionOfIssue VARCHAR(500) NOT NULL,"
              + "PRIMARY KEY(id),"
              //              + "CONSTRAINT CSR_employee_FK FOREIGN KEY (assignedEmployee)
              // REFERENCES Users (id),"
              // + "CONSTRAINT CSR_location_FK FOREIGN KEY (location) REFERENCES Nodes (nodeID),"
              + "CONSTRAINT CSR_status_chk CHECK (status IN ('Incomplete', 'Complete', 'In Progress')))";
      stmt.executeUpdate(query);
      System.out.println("Computer Service Request table created");
    } catch (Exception e) {
      System.out.println("Computer Service Request table was not created");
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
      String descriptionOfIssue) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO ComputerServiceRequest (firstName, lastName, contactInfo, location, descriptionOfIssue) VALUES(?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, descriptionOfIssue);
      stmt.executeUpdate();

      FDatabaseTables.getAllServiceTable()
          .addEntity(
              GlobalDb.getConnection(),
              this.getID(GlobalDb.getConnection()),
              location,
              "Incomplete",
              assignedEmployee,
              "COMP");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addIntoComputerDataList(
      ObservableList<ComputerNodeInfo> computerData, boolean employeeAccess) throws IOException {
    PreparedStatement stmt = null;
    Connection conn = GlobalDb.getConnection();
    try {
      if (employeeAccess) {
        stmt =
            conn.prepareStatement(
                "SELECT * FROM ComputerServiceRequest WHERE assignedEmployee = ? OR assignedEmployee = ''");
        stmt.setString(1, HomeController.username);
        //        System.out.println(
        //            "this is trying to add data into the employee table " +
        // HomeController.username);
        //        System.out.println("this is getting the userType " + HomeController.userTypeEnum);
      } else {
        stmt = conn.prepareStatement("SELECT * FROM ComputerServiceRequest");
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        computerData.add(
            new ComputerNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("descriptionOfIssue")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public ObservableList<ComputerNodeInfo> changeComputerData(
      ObservableList<ComputerNodeInfo> computerData) {
    for (ComputerNodeInfo computerInfo : computerData) {
      if (!(computerInfo.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE ComputerServiceRequest SET status = ?, assignedEmployee = ? WHERE id=?");
          stmt.setString(1, computerInfo.getStatus());
          stmt.setString(2, computerInfo.getAssignedEmployee());
          stmt.setString(3, computerInfo.getId());
          stmt.executeUpdate();

          AllServiceTable.updateEntity(
              GlobalDb.getConnection(),
              computerInfo.getId(),
              computerInfo.getStatus(),
              computerInfo.getAssignedEmployee(),
              "COMP");

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return computerData;
  }

  public int getID(Connection conn) {
    int id = 420;
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT id FROM ComputerServiceRequest");
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
