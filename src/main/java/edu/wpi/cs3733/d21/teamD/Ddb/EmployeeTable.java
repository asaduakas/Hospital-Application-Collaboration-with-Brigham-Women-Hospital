package edu.wpi.cs3733.d21.teamD.Ddb;

import java.sql.Connection;
import java.sql.Statement;

public class EmployeeTable extends AbsTables {

  public void createTable(Connection conn) {
    try {
      Statement stmt = conn.createStatement();
      String query =
          "CREATE TABLE Employees("
              + "id VARCHAR(50) NOT NULL,"
              + "assignedRequests VARCHAR(500) DEFAULT '',"
              + "qualifiedRequests VARCHAR(500) DEFAULT '',"
              + "PRIMARY KEY(id),"
              + "CONSTRAINT EMP_id_FK FOREIGN KEY (id) REFERENCES Users(id))";
      stmt.executeUpdate(query);
      System.out.println("Employee table created");
    } catch (Exception e) {
      System.out.println("Employee table was not created");
      e.printStackTrace();
      return;
    }
  }

  // to list this employee in the drop down of a certain service request based on qualifications
  public String[] getQualifiedRequests(Connection conn, String id) {
    // parse the qualifiedRequests attribute with ',' delimiter
    // store them in string array
    // return array
    String[] array = {""};
    return array;
  }

  // assign a service request to a specific employee
  public void assignEmployee(Connection conn, String id, String requestName, int requestID) {
    // find the employee with the id
    // make sure that the employee is qualified to complete the request
    // add the request name and ID to the assignedRequests CSV list
  }

  // add an employee to the table
  public void addEmployee(Connection conn, String userID, String qualifiedRequests) {
    // Insert into Employees (id, qualifiedRequests) VALUES (userID, qualifiedRequests)
  }

  // add qualified requests to an already existing employee
  public void updateQualifiedRequests(Connection conn, String id, String newRequestType) {
    // getQualifiedRequests(id)
    // append newRequestType to String[] of qualified Requests
    // concat array into one string
    // update qualifiedRequests in Employees where Employees.id = id
  }
}
