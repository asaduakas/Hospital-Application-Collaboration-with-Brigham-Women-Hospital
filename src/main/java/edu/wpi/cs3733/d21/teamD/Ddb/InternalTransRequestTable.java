package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.InternalTransNodeInfo;
import java.io.IOException;
import java.sql.*;
import javafx.collections.ObservableList;

public class InternalTransRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo, location,assignedEmployee,typeOfTransport

  public InternalTransRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE InternalTransReq ("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(100) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "typeOfTransport VARCHAR(100) NOT NULL,"
              + "PRIMARY KEY(id),"
              + "CONSTRAINT INT_employee_FK FOREIGN KEY(assignedEmployee) REFERENCES Users(id),"
              + "CONSTRAINT INT_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')))";
      // + "CONSTRAINT INT_location_FK FOREIGN KEY(location) REFERENCES Nodes(nodeID))";
      stmt.executeUpdate(query);
      System.out.println("Internal Transportation Request table created");
    } catch (Exception e) {
      System.out.println("Internal Transportation Request table was not created");
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
      String assignedEmp,
      String typeOfTransport) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO InternalTransReq (firstName, lastName, contactInfo,"
                  + "location, assignedEmployee, typeOfTransport) VALUES(?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, assignedEmp);
      stmt.setString(6, typeOfTransport);
      stmt.executeUpdate();

      FDatabaseTables.getAllServiceTable()
          .addEntity(
              GlobalDb.getConnection(),
              this.getID(GlobalDb.getConnection()),
              location,
              "Incomplete",
              assignedEmp,
              "ITRAN");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addIntoInternalTransList(
      ObservableList<InternalTransNodeInfo> internalTransData, boolean employeeAccess)
      throws IOException {
    PreparedStatement stmt = null;
    Connection conn = GlobalDb.getConnection();
    try {
      if (employeeAccess) {
        stmt =
            conn.prepareStatement(
                "SELECT * FROM InternalTransReq WHERE assignedEmployee = ? OR assignedEmployee = ''");
        stmt.setString(1, HomeController.username);
        //        System.out.println(
        //            "this is trying to add data into the employee table " +
        // HomeController.username);
        //        System.out.println("this is getting the userType " + HomeController.userTypeEnum);
      } else {
        stmt = conn.prepareStatement("SELECT * FROM InternalTransReq");
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        internalTransData.add(
            new InternalTransNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("typeOfTransport")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public ObservableList<InternalTransNodeInfo> changeInternalTransData(
      ObservableList<InternalTransNodeInfo> internalTransData) {
    for (InternalTransNodeInfo intTransInfo : internalTransData) {
      if (!(intTransInfo.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE InternalTransReq SET status = ?, assignedEmployee = ? WHERE id=?");
          stmt.setString(1, intTransInfo.getStatus());
          stmt.setString(2, intTransInfo.getAssignedEmployee());
          stmt.setString(3, intTransInfo.getId());
          stmt.executeUpdate();

          AllServiceTable.updateEntity(
              GlobalDb.getConnection(),
              intTransInfo.getId(),
              intTransInfo.getStatus(),
              intTransInfo.getAssignedEmployee(),
              "ITRAN");

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return internalTransData;
  }

  public int getID(Connection conn) {
    int id = 420;
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT id FROM InternalTransReq");
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
