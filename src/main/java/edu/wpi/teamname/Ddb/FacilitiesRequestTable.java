package edu.wpi.teamname.Ddb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class FacilitiesRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,location,assignedEmployee,urgencyLevel(high med
  // low),descriptionOfIssue(a very long string)

  public FacilitiesRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE FacilitiesServiceRequest("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(200) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "urgencyLevel VARCHAR(450) NOT NULL,"
              + "descriptionOfIssue VARCHAR(500) DEFAULT '',"
              + "PRIMARY KEY(id),"
              + "CONSTRAINT FAC_employee_FK FOREIGN KEY (assignedEmployee) REFERENCES Users (id),"
              // + "CONSTRAINT FAC_location_FK FOREIGN KEY (location) REFERENCES Nodes (nodeID),"
              + "CONSTRAINT FAC_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')),"
              + "CONSTRAINT FAC_urgency_check CHECK (urgencyLevel IN ('Low Priority', 'Medium Priority', 'High Priority')))";

      stmt.executeUpdate(query);
      System.out.println("Facilities Service Request table created");
    } catch (Exception e) {
      System.out.println("Facilities Service Request table was not created");
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
      String urgencyLevel,
      String descriptionOfIssue) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO FacilitiesServiceRequest (firstName, lastName, contactInfo, location, assignedEmployee, urgencyLevel, descriptionOfIssue) VALUES(?,?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, assignedEmployee);
      stmt.setString(6, urgencyLevel);
      stmt.setString(7, descriptionOfIssue);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
