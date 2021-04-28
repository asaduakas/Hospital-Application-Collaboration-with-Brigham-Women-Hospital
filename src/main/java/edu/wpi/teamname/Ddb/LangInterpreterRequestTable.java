package edu.wpi.teamname.Ddb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;

public class LangInterpreterRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,location,assignedEmployee,languageRequested,
  // dateRequested

  public LangInterpreterRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE LangInterpRequest ("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(100) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "languageRequested VARCHAR(100) NOT NULL,"
              + "dateRequested DATE NOT NULL," // YYYY-MM-DD format
              + "PRIMARY KEY(id),"
              + "CONSTRAINT LANG_employee_FK FOREIGN KEY(assignedEmployee) REFERENCES Users(id),"
              + "CONSTRAINT LANG_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')))";
      // + "CONSTRAINT LANG_location_FK FOREIGN KEY(location) REFERENCES Nodes(nodeID))";
      stmt.executeUpdate(query);
      System.out.println("Language Interpreter Request table created");
    } catch (Exception e) {
      System.out.println("Language Interpreter Request table was not created");
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
      String languageRequested,
      LocalDate dateRequested) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO LangInterpRequest (firstName, lastName, contactInfo,"
                  + "location, assignedEmployee, languageRequested, dateRequested) VALUES(?,?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, assignedEmp);
      stmt.setString(6, languageRequested);
      stmt.setDate(7, Date.valueOf(dateRequested));
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
