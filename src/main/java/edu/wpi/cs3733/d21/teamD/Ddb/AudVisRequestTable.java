package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.AudVisNodeInfo;
import java.io.IOException;
import java.sql.*;
import javafx.collections.ObservableList;

public class AudVisRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,location,assignedEmployee,descriptionOfProblem(a very
  // long string)

  public AudVisRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE AudVisServiceRequest("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(200) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "descriptionOfProblem VARCHAR(500) NOT NULL,"
              + "PRIMARY KEY(id),"
              + "CONSTRAINT AVSR_employee_FK FOREIGN KEY (assignedEmployee) REFERENCES Users (id),"
              // + "CONSTRAINT AVSR_location_FK FOREIGN KEY (location) REFERENCES Nodes(nodeID),"
              + "CONSTRAINT AVSR_status_chk CHECK (status IN ('Incomplete', 'Complete', 'In Progress')))";
      stmt.executeUpdate(query);
      System.out.println("Audio/Visual Service Request table created");
    } catch (Exception e) {
      System.out.println("Audio/Visual Service Request table was not created");
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
      String descriptionOfProblem) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO AudVisServiceRequest (firstName, lastName, contactInfo, location, assignedEmployee, descriptionOfProblem) VALUES(?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, assignedEmployee);
      stmt.setString(6, descriptionOfProblem);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addIntoAudVisDataList(ObservableList<AudVisNodeInfo> audVisData) throws IOException {
    try {
      String query = "SELECT * FROM AudVisServiceRequest";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        audVisData.add(
            new AudVisNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("descriptionOfProblem")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public ObservableList<AudVisNodeInfo> changeAudVisData(
      ObservableList<AudVisNodeInfo> audVisData) {
    for (AudVisNodeInfo audVisInfo : audVisData) {
      if (!(audVisInfo.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE AudVisServiceRequest SET status = ?, assignedEmployee = ? WHERE id=?");
          stmt.setString(1, audVisInfo.getStatus());
          stmt.setString(2, audVisInfo.getAssignedEmployee());
          stmt.setString(3, audVisInfo.getId());
          stmt.executeUpdate();

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return audVisData;
  }
}