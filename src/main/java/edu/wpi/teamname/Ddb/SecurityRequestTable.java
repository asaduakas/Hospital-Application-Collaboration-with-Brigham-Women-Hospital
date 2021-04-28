package edu.wpi.teamname.Ddb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SecurityRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,location,assignedEmployee,urgencyLevel(high med
  // low),other
  // **description - field should be optional for user

  public SecurityRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE SecurityRequest ("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(100) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "urgencyLevel VARCHAR(25) NOT NULL,"
              + "description VARCHAR(400) DEFAULT '',"
              + "PRIMARY KEY(id),"
              + "CONSTRAINT SEC_employee_FK FOREIGN KEY(assignedEmployee) REFERENCES Users(id),"
              // + "CONSTRAINT SEC_location_FK FOREIGN KEY(location) REFERENCES Nodes(nodeID),"
              + "CONSTRAINT SEC_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')),"
              + "CONSTRAINT SEC_urgency_check CHECK (urgencyLevel IN ('Low Priority', 'Medium Priority', 'High Priority')))";
      stmt.executeUpdate(query);
      System.out.println("Security Service Request table created");
    } catch (Exception e) {
      System.out.println("Security Service Request table was not created");
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
      String urgencyLev,
      String des) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO SecurityRequest (firstName, lastName, contactInfo, "
                  + "location, assignedEmployee, urgencyLevel, description) VALUES(?,?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, assignedEmp);
      stmt.setString(6, urgencyLev);
      stmt.setString(7, des);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
