package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.LaundryNodeInfo;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import javafx.collections.ObservableList;

public class LaundryRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,location,assignedEmployee

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE LaundryRequest("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(20) NOT NULL,"
              + "lastName VARCHAR(20) NOT NULL,"
              + "contactInfo VARCHAR(45) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "PRIMARY KEY(id),"
              // + "CONSTRAINT LAU_employee_FK FOREIGN KEY(assignedEmployee) REFERENCES Users(id),"
              + "CONSTRAINT LAU_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')))";
      // + "CONSTRAINT LAU_location_FK FOREIGN KEY(location) REFERENCES Nodes(nodeID))";
      stmt.executeUpdate(query);
      System.out.println("Laundry Service Request table created");
    } catch (Exception e) {
      System.out.println("Laundry Service Request table was not created");
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
      String assignedEmployee) {
    PreparedStatement stmt = null;
    String query =
        "INSERT INTO LaundryRequest (firstName, lastName, contactInfo, location, assignedEmployee) VALUES(?,?,?,?,?)";
    try {
      stmt = conn.prepareStatement(query);
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, assignedEmployee);
      stmt.executeUpdate();
      System.out.println("this is in laundry table chceking assigned employee " + assignedEmployee);

      FDatabaseTables.getAllServiceTable()
          .addEntity(
              GlobalDb.getConnection(),
              this.getID(GlobalDb.getConnection()),
              location,
              "Incomplete",
              assignedEmployee,
              "LAUN");

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private static void dispLaundRequestsTable(Connection conn) {
    Statement stmt = null;

    try {
      stmt = conn.createStatement();
      String query = "SELECT * FROM LaundryRequest";
      ResultSet rs = stmt.executeQuery(query);
      System.out.println(
          "id \tstatus \tfirstName \tlastName \tcontactInfo \tlocation \tassignedEmployee");
      while (rs.next()) {
        // id,status,firstName,lastName,contactInfo,location,transType
        System.out.println(
            rs.getInt("id")
                + " \t"
                + rs.getString("status")
                + " \t"
                + rs.getString("firstName")
                + " \t"
                + rs.getString("lastName")
                + " \t"
                + rs.getString("contactInfo")
                + " \t"
                + rs.getString("location")
                + " \t"
                + rs.getInt("assignedEmployee"));
        System.out.println(" ");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void addIntoLaundServiceList(
      ObservableList<LaundryNodeInfo> laundryData, boolean employeeAccess) throws IOException {
    PreparedStatement stmt = null;
    Connection conn = GlobalDb.getConnection();
    try {
      if (employeeAccess) {
        stmt =
            conn.prepareStatement(
                "SELECT * FROM LaundryRequest WHERE assignedEmployee = ? OR assignedEmployee  IS NULL");
        stmt.setString(1, HomeController.username);
        //        System.out.println(
        //            "this is trying to add data into the employee table " +
        // HomeController.username);
        //        System.out.println("this is getting the userType " + HomeController.userTypeEnum);
      } else {
        stmt = conn.prepareStatement("SELECT * FROM LaundryRequest");
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        laundryData.add(
            new LaundryNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public ObservableList<LaundryNodeInfo> changeLaundServiceData(
      ObservableList<LaundryNodeInfo> laundryData) {
    for (LaundryNodeInfo laundryInfo : laundryData) {
      if (!(laundryInfo.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE LaundryRequest SET status = ?, assignedEmployee = ? WHERE id=?");
          stmt.setString(1, laundryInfo.getStatus());
          stmt.setString(2, laundryInfo.getAssignedEmployee());
          stmt.setString(3, laundryInfo.getId());
          stmt.executeUpdate();

          AllServiceTable.updateEntity(
              GlobalDb.getConnection(),
              laundryInfo.getId(),
              laundryInfo.getStatus(),
              laundryInfo.getAssignedEmployee(),
              "LAUN");

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return laundryData;
  }

  public int getID(Connection conn) {
    int id = 420;
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT id FROM LaundryRequest");
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
    HashMap<Integer, String> laundryRequestList = new HashMap<>();
    String id = HomeController.username;
    int i = 0;
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT location, status FROM LaundryRequest");

      PreparedStatement stmt =
          conn.prepareStatement(
              "SELECT location, firstName, lastName, contactInfo FROM LaundryRequest WHERE status = 'Incomplete' AND assignedEmployee = ?");
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        laundryRequestList.put(
            i,
            "Laundry Service"
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
    return laundryRequestList;
  }
}
