package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.FoodDelivNodeInfo;
import java.io.IOException;
import java.sql.*;
import javafx.collections.ObservableList;

public class FoodDeliveryRequestTable extends AbsTables {
  // id,type,status,pFirstName,pLastName,contactInfo,location,specialNeeds
  // *specialNeeds - field should be optional for user

  public FoodDeliveryRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE FoodDeliveryServiceRequest("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(200) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "specialNeeds VARCHAR(500) DEFAULT '',"
              + "PRIMARY KEY(id),"
              //              + "CONSTRAINT FD_employee_FK FOREIGN KEY (assignedEmployee) REFERENCES
              // Users (id),"
              // + "CONSTRAINT FD_location_FK FOREIGN KEY (location) REFERENCES Nodes (nodeID),"
              + "CONSTRAINT FD_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')))";
      stmt.executeUpdate(query);
      System.out.println("Food Delivery Service Request table created");
    } catch (Exception e) {
      System.out.println("Food Delivery Service Request table was not created");
      e.printStackTrace();
      return;
    }
  }

  public static void dispFoodDelivery(Connection conn) {
    Statement stmt = null;

    try {
      stmt = conn.createStatement();
      String query = "SELECT * FROM FoodDeliveryServiceRequest";
      ResultSet rs = stmt.executeQuery(query);
      System.out.println(
          "id \tstatus \tfirstName \tlastName \tcontactInfo \tlocation \tassignedEm \tspecialNeeds");

      while (rs.next()) {
        // id,type,pFirstName,pLastName,contactInfo,location,transType,status
        System.out.println(
            rs.getString(1)
                + " \t"
                + rs.getString(2)
                + " \t"
                + rs.getString(3)
                + " \t"
                + rs.getString(4)
                + " \t"
                + rs.getString(5)
                + " \t"
                + rs.getString(6)
                + " \t"
                + rs.getString(7)
                + " \t"
                + rs.getString(8));
        System.out.println(" ");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void populateTable(Connection conn, String filePath) {}

  public void addEntity(
      Connection conn,
      String firstName,
      String lastName,
      String contactInfo,
      String location,
      String specialNeeds) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO FoodDeliveryServiceRequest (firstName, lastName, contactInfo, location, specialNeeds) VALUES(?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, specialNeeds);
      stmt.executeUpdate();

      FDatabaseTables.getAllServiceTable()
          .addEntity(
              GlobalDb.getConnection(),
              this.getID(GlobalDb.getConnection()),
              location,
              "Incomplete",
              assignedEmployee,
              "FOOD");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*public void addEntityNoNeeds(
      Connection conn,
      String firstName,
      String lastName,
      String contactInfo,
      String location,
      String assignedEmployee) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO FoodDeliveryServiceRequest (firstName, lastName, contactInfo, location, assignedEmployee) VALUES(?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, assignedEmployee);
      stmt.executeUpdate();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }*/

  public void addIntoFoodDelivDataList(
      ObservableList<FoodDelivNodeInfo> foodData, boolean employeeAccess) throws IOException {
    PreparedStatement stmt = null;
    Connection conn = GlobalDb.getConnection();
    try {
      if (employeeAccess) {
        stmt =
            conn.prepareStatement(
                "SELECT * FROM FoodDeliveryServiceRequest WHERE assignedEmployee = ? OR assignedEmployee = ''");
        stmt.setString(1, HomeController.username);
        //        System.out.println(
        //            "this is trying to add data into the employee table " +
        // HomeController.username);
        //        System.out.println("this is getting the userType " + HomeController.userTypeEnum);
      } else {
        stmt = conn.prepareStatement("SELECT * FROM FoodDeliveryServiceRequest");
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        foodData.add(
            new FoodDelivNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("specialNeeds")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public ObservableList<FoodDelivNodeInfo> changeFoodDelivData(
      ObservableList<FoodDelivNodeInfo> foodData) {
    for (FoodDelivNodeInfo foodInfo : foodData) {
      if (!(foodInfo.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE FoodDeliveryServiceRequest SET status = ?, assignedEmployee = ?, specialNeeds = ? WHERE id=?");
          stmt.setString(1, foodInfo.getStatus());
          stmt.setString(2, foodInfo.getAssignedEmployee());
          stmt.setString(3, foodInfo.getSpecialNeeds());
          stmt.setString(4, foodInfo.getId());
          stmt.executeUpdate();

          AllServiceTable.updateEntity(
              GlobalDb.getConnection(),
              foodInfo.getId(),
              foodInfo.getStatus(),
              foodInfo.getAssignedEmployee(),
              "FOOD");

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return foodData;
  }

  public int getID(Connection conn) {
    int id = 420;
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT id FROM FoodDeliveryServiceRequest");
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
