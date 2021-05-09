package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.LaundryNodeInfo;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
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
              + "firstName VARCHAR(10) NOT NULL,"
              + "lastName VARCHAR(4) NOT NULL,"
              + "contactInfo VARCHAR(45) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "PRIMARY KEY(id),"
              + "CONSTRAINT LAU_employee_FK FOREIGN KEY(assignedEmployee) REFERENCES Users(id),"
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

  public static void addEntity(
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

      int count = stmt.executeUpdate();

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

  public void addIntoLaundServiceList(ObservableList<LaundryNodeInfo> laundryData)
      throws IOException {
    try {
      String query = "SELECT * FROM LaundryRequest";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
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

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return laundryData;
  }

  public LinkedList<LocalStatus> getLocalStatus(Connection conn) {
    LinkedList<LocalStatus> LocalStatus = new LinkedList<>();
    try {
      PreparedStatement stmt =
          conn.prepareStatement("SELECT location, status FROM AudVisServiceRequest");

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        LocalStatus localStatus = new LocalStatus(rs.getString("location"), rs.getString("status"));
        LocalStatus.add(localStatus);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return LocalStatus;
  }
}
