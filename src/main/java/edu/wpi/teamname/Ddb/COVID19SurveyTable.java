package edu.wpi.teamname.Ddb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class COVID19SurveyTable extends AbsTables {

  public COVID19SurveyTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE COVID19SurveyResults ("
              + "id VARCHAR(100) NOT NULL,"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(200) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "positiveTestCheck INT DEFAULT 0,"
              + "symptomCheck INT DEFAULT 0,"
              + "closeContactCheck INT DEFAULT 0,"
              + "selfIsolateCheck INT DEFAULT 0,"
              + "feelGoodCheck INT DEFAULT 0,"
              + "PRIMARY KEY(id),"
              + "CONSTRAINT COV_id_FK FOREIGN KEY (id) REFERENCES Users (id),"
              + "CONSTRAINT COV_positiveTestCheck_check CHECK (positiveTestCheck IN (0, 1)),"
              + "CONSTRAINT COV_symptomCheck_check CHECK (symptomCheck IN (0, 1)),"
              + "CONSTRAINT COV_closeContactCheck_check CHECK (closeContactCheck IN (0, 1)),"
              + "CONSTRAINT COV_selfIsolateCheck_check CHECK (selfIsolateCheck IN (0, 1)),"
              + "CONSTRAINT COV_feelGoodCheck_check CHECK (feelGoodCheck IN (0, 1)))";
      stmt.executeUpdate(query);
      System.out.println("COVID-19 Survey Results table created");
    } catch (Exception e) {
      System.out.println("COVID-19 Survey Results table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void addEntity(
      Connection conn,
      String firstName,
      String lastName,
      String contactInfo,
      int positiveTestCheck,
      int symptomCheck,
      int closeContactCheck,
      int selfIsolateCheck,
      int feelGoodCheck) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO COVID19SurveyResults (firstName, lastName, contactInfo, positiveTestCheck, symptomCheck, closeContactCheck, selfIsolateCheck, feelGoodCheck) VALUES(?,?,?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setInt(4, positiveTestCheck);
      stmt.setInt(5, symptomCheck);
      stmt.setInt(6, closeContactCheck);
      stmt.setInt(7, selfIsolateCheck);
      stmt.setInt(8, feelGoodCheck);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendEmail(Connection conn, String id) {
    // use id to get email
    // if any constraint is checked, send email of STAY TF HOME
    // if all good is checked, send email of GO ON PEASANT
  }
}
