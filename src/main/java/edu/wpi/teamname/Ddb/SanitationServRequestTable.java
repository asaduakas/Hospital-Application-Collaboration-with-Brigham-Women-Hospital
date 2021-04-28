package edu.wpi.teamname.Ddb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SanitationServRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,location,assignedEmployee,typeOfSanitation,urgencyLevel(high med low)

  public SanitationServRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE SanitationRequest ("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(100) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "descriptionOfIssue VARCHAR(100) NOT NULL,"
              + "urgencyLevel VARCHAR(25) NOT NULL,"
              + "PRIMARY KEY(id),"
              + "CONSTRAINT SAN_employee_FK FOREIGN KEY(assignedEmployee) REFERENCES Users(id),"
              // + "CONSTRAINT SAN_location_FK FOREIGN KEY(location) REFERENCES Nodes(nodeID),"
              + "CONSTRAINT SAN_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')),"
              + "CONSTRAINT SAN_urgency_check CHECK (urgencyLevel IN ('Low Priority', 'Medium Priority', 'High Priority')))";
      stmt.executeUpdate(query);
      System.out.println("Sanitation Service Request table created");
    } catch (Exception e) {
      System.out.println("Sanitation Service Request table was not created");
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
      String sanitationType,
      String urgencyLev) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO SecurityRequest (firstName, lastName, contactInfo,"
                  + "location, assignedEmployee, description, urgencyLevel) VALUES(?,?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, assignedEmp);
      stmt.setString(6, sanitationType);
      stmt.setString(7, urgencyLev);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
