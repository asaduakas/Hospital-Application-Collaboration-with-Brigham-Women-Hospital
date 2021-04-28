package edu.wpi.teamname.Ddb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class FloralDeliveryRequestTable extends AbsTables {
  // id,type,status,pFirstName,pLastName,contactInfo,location,typeOfFlower,numOfFlower,fromFlower,assignedPerson

  public FloralDeliveryRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();

      String query =
          "CREATE TABLE FloralRequests("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "pFirstName VARCHAR(50) NOT NULL,"
              + "pLastName VARCHAR(50) NOT NULL,"
              + "contactInfo VARCHAR(45) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "typeOfFlower VARCHAR(50) NOT NULL,"
              + "numOfFlower INT NOT NULL,"
              + "fromFlower VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "PRIMARY KEY(id),"
              + "CONSTRAINT FLO_employee_FK FOREIGN KEY (assignedEmployee) REFERENCES Users (id),"
              // + "CONSTRAINT FLO_location_FK FOREIGN KEY (location) REFERENCES Nodes(nodeID),"
              + "CONSTRAINT FLO_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')))";
      stmt.executeUpdate(query);
      System.out.println("Floral requests table created");
    } catch (Exception e) {
      System.out.println("Floral requests table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void addEntity(
      Connection conn,
      String pFirstName,
      String pLastName,
      String contactInfo,
      String location,
      String typeOfFlower,
      String numOfFlower,
      String fromFlower,
      String assignedEmployee) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO FloralRequests (pFirstName, pLastName, contactInfo, location, typeOfFlower, numOfFlower, fromFlower, assignedEmployee) VALUES(?,?,?,?,?,?,?,?)");
      stmt.setString(1, pFirstName);
      stmt.setString(2, pLastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, typeOfFlower);
      stmt.setString(6, numOfFlower);
      stmt.setString(7, fromFlower);
      stmt.setString(8, assignedEmployee);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
