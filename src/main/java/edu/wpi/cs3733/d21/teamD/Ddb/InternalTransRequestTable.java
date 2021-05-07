package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.InternalTransNodeInfo;
import java.io.IOException;
import java.sql.*;
import javafx.collections.ObservableList;

public class InternalTransRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,destination,assignedEmployee,typeOfTransport

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
              + "destination VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "typeOfTransport VARCHAR(100) NOT NULL,"
              + "PRIMARY KEY(id),"
              + "CONSTRAINT INT_employee_FK FOREIGN KEY(assignedEmployee) REFERENCES Users(id),"
              + "CONSTRAINT INT_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')))";
      // + "CONSTRAINT INT_location_FK FOREIGN KEY(destination) REFERENCES Nodes(nodeID))";
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
      String destination,
      String assignedEmp,
      String typeOfTransport) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO InternalTransReq (firstName, lastName, contactInfo,"
                  + "destination, assignedEmployee, typeOfTransport) VALUES(?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, destination);
      stmt.setString(5, assignedEmp);
      stmt.setString(6, typeOfTransport);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addIntoInternalTransList(ObservableList<InternalTransNodeInfo> internalTransData)
      throws IOException {
    try {
      String query = "SELECT * FROM InternalTransReq";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        internalTransData.add(
            new InternalTransNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("destination"),
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

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return internalTransData;
  }
}