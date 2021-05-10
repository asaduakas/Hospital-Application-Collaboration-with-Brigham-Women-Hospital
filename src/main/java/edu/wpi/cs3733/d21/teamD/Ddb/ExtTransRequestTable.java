package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.ExtTransNodeInfo;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;
import javafx.collections.ObservableList;

public class ExtTransRequestTable extends AbsTables {
  // id,serviceType,pFirstName,pLastName,contactInfo,location,transType,assignedTo,status

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
              + "assignedTo VARCHAR(100) DEFAULT '',"
              + "status VARCHAR(20) DEFAULT 'Incomplete',"
              + "CONSTRAINT EXT_REQUESTID PRIMARY KEY(id),"
              + "CONSTRAINT EXT_employee_FK FOREIGN KEY(assignedTo) REFERENCES Users(id),"
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

  public static void addEntity(
      Connection conn,
      String serTy,
      String pFN,
      String pLN,
      String contact,
      String loca,
      String transType,
      String assigned) {
    PreparedStatement stmt = null;
    String query =
        "INSERT INTO ExternalTransRequests(serviceType, pFirstName, pLastName, contactInfo, location, transType, assignedTo) VALUES(?,?,?,?,?,?,?)";
    try {
      stmt = conn.prepareStatement(query);
      stmt.setString(1, serTy);
      stmt.setString(2, pFN);
      stmt.setString(3, pLN);
      stmt.setString(4, contact);
      stmt.setString(5, loca);
      stmt.setString(6, transType);
      stmt.setString(7, assigned);

      int count = stmt.executeUpdate();

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
          "id \ttype \tpFirstName \tpLastName \tcontactInfo \tlocation \ttransType \tassignedTo \tstatus");

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
                + rs.getString("assignedTo")
                + " \t"
                + rs.getString("status"));
        System.out.println(" ");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void addIntoExTransDataList(ObservableList<ExtTransNodeInfo> ExTransData)
      throws IOException {
    // ExTransData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM ExternalTransRequests";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
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
                rs.getString("assignedTo"),
                rs.getString("status")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    // return ExTransData;
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
                      "UPDATE ExternalTransRequests SET status = ?, assignedTo = ? WHERE id=?");
          stmt.setString(1, info.getStatus());
          stmt.setString(2, info.getAssignedTo());
          stmt.setString(3, info.getId());
          stmt.executeUpdate();
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return ExTransData;
  }

  public LinkedList<LocalStatus> getLocalStatus(Connection conn) {
    LinkedList<LocalStatus> LocalStatus = new LinkedList<>();
    try {
      PreparedStatement stmt =
          conn.prepareStatement("SELECT location, status FROM ExternalTransRequests");

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
