package edu.wpi.teamname.Ddb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ComputerRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,location,assignedEmployee,descriptionOfIssue(a very
  // long string)

  public ComputerRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE ComputerServiceRequest("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(200) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "descriptionOfIssue VARCHAR(500) NOT NULL,"
              + "PRIMARY KEY(id),"
              + "CONSTRAINT CSR_employee_FK FOREIGN KEY (assignedEmployee) REFERENCES Users (id),"
              // + "CONSTRAINT CSR_location_FK FOREIGN KEY (location) REFERENCES Nodes (nodeID),"
              + "CONSTRAINT CSR_status_chk CHECK (status IN ('Incomplete', 'Complete', 'In Progress')))";
      stmt.executeUpdate(query);
      System.out.println("Computer Service Request table created");
    } catch (Exception e) {
      System.out.println("Computer Service Request table was not created");
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
      String descriptionOfIssue) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO ComputerServiceRequest (firstName, lastName, contactInfo, location, assignedEmployee, descriptionOfIssue) VALUES(?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, assignedEmployee);
      stmt.setString(6, descriptionOfIssue);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
