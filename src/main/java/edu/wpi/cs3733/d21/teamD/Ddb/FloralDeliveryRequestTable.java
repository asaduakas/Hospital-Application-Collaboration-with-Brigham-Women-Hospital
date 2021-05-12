package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.FloralDelivNodeInfo;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import javafx.collections.ObservableList;

public class FloralDeliveryRequestTable extends AbsTables {
  // id,type,status,pFirstName,pLastName,contactInfo,location,typeOfFlower,numOfFlower,fromFlower,assignedPerson

  public FloralDeliveryRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();

      String query =
          "CREATE TABLE FloralRequests("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "pFirstName VARCHAR(50) NOT NULL,"
              + "pLastName VARCHAR(50) NOT NULL,"
              + "contactInfo VARCHAR(45) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "typeOfFlower VARCHAR(50) NOT NULL,"
              + "numOfFlower INT NOT NULL,"
              + "fromFlower VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "PRIMARY KEY(id),"
              //              + "CONSTRAINT FLO_employee_FK FOREIGN KEY (assignedEmployee)
              // REFERENCES Users (id),"
              // + "CONSTRAINT FLO_location_FK FOREIGN KEY (location) REFERENCES Nodes(nodeID),"
              + "CONSTRAINT FLO_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')))";
      stmt.executeUpdate(query);
      System.out.println("Floral requests table created");
    } catch (Exception e) {
      System.out.println("Floral requests table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void addEntity(
      Connection conn,
      String pFirstName,
      String pLastName,
      String contactInfo,
      String location,
      String assignedEmployee,
      String typeOfFlower,
      String numOfFlower,
      String fromFlower) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO FloralRequests (pFirstName, pLastName, contactInfo, location, assignedEmployee, typeOfFlower, numOfFlower, fromFlower) VALUES(?,?,?,?,?,?,?,?)");
      stmt.setString(1, pFirstName);
      stmt.setString(2, pLastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, assignedEmployee);
      stmt.setString(6, typeOfFlower);
      stmt.setString(7, numOfFlower);
      stmt.setString(8, fromFlower);
      stmt.executeUpdate();

      FDatabaseTables.getAllServiceTable()
          .addEntity(
              GlobalDb.getConnection(),
              this.getID(GlobalDb.getConnection()),
              location,
              "Incomplete",
              assignedEmployee,
              "FLOW");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addIntoFloralDeliveryList(
      ObservableList<FloralDelivNodeInfo> floralData, boolean employeeAccess) throws IOException {
    PreparedStatement stmt = null;
    Connection conn = GlobalDb.getConnection();
    try {
      if (employeeAccess) {
        stmt =
            conn.prepareStatement(
                "SELECT * FROM FloralRequests WHERE assignedEmployee = ? OR assignedEmployee  IS NULL");
        stmt.setString(1, HomeController.username);
        //        System.out.println(
        //            "this is trying to add data into the employee table " +
        // HomeController.username);
        //        System.out.println("this is getting the userType " + HomeController.userTypeEnum);
      } else {
        stmt = conn.prepareStatement("SELECT * FROM FloralRequests");
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        floralData.add(
            new FloralDelivNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("pFirstName"),
                rs.getString("pLastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("typeOfFlower"),
                rs.getString("numOfFlower"),
                rs.getString("fromFlower"),
                rs.getString("assignedEmployee")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public ObservableList<FloralDelivNodeInfo> changeFloralDelivData(
      ObservableList<FloralDelivNodeInfo> floralData) {
    for (FloralDelivNodeInfo floralInfo : floralData) {
      if (!(floralInfo.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE FloralRequests SET status = ?, assignedEmployee = ? WHERE id=?");
          stmt.setString(1, floralInfo.getStatus());
          stmt.setString(2, floralInfo.getAssignedEmployee());
          stmt.setString(3, floralInfo.getId());
          stmt.executeUpdate();

          AllServiceTable.updateEntity(
              GlobalDb.getConnection(),
              Integer.valueOf(floralInfo.getId()),
              floralInfo.getStatus(),
              floralInfo.getAssignedEmployee(),
              "FLOW");

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return floralData;
  }

  public void updateEntity(Connection conn, int id, String status, String employee) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              " UPDATE FloralRequests SET status = ?, assignedEmployee = ? WHERE id = ?");
      stmt.setString(1, status);
      stmt.setString(2, employee);
      stmt.setInt(3, id);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int getID(Connection conn) {
    int id = 420;
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT id FROM FloralRequests");
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
    HashMap<Integer, String> floralDeliveryList = new HashMap<>();
    String id = HomeController.username;
    int i = 0;
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "SELECT location, pFirstName, pLastName, contactInfo FROM FloralRequests WHERE status = 'Incomplete' AND assignedEmployee = ?");
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        floralDeliveryList.put(
            i,
            "Floral Delivery"
                + " -- "
                + rs.getString("location")
                + " -- Name: "
                + rs.getString("pFirstName")
                + " "
                + rs.getString("pLastName")
                + " -- Contact: "
                + rs.getString("contactInfo"));
        i++;
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return floralDeliveryList;
  }
}
