package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.COVIDSurveyResultsNodeInfo;
import java.io.IOException;
import java.sql.*;
import javafx.collections.ObservableList;

public class COVID19SurveyTable extends AbsTables {

  public COVID19SurveyTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE COVID19SurveyResults ("
              + "id INT GENERATED ALWAYS AS IDENTITY,"
              + "serviceType VARCHAR(100) DEFAULT 'COVID-19 Survey',"
              + "username VARCHAR(100) NOT NULL,"
              + "firstName VARCHAR(100),"
              + "lastName VARCHAR(200),"
              + "contactInfo VARCHAR(100),"
              + "email VARCHAR(100),"
              + "assignedTo VARCHAR(100) DEFAULT '',"
              + "status VARCHAR(100) DEFAULT 'Disapproved',"
              + "positiveTestCheck VARCHAR(5) DEFAULT 'No' NOT NULL,"
              + "symptomCheck VARCHAR(5) DEFAULT 'No' NOT NULL,"
              + "closeContactCheck VARCHAR(5) DEFAULT 'No' NOT NULL,"
              + "selfIsolateCheck VARCHAR(5) DEFAULT 'No' NOT NULL,"
              + "feelGoodCheck VARCHAR(5) DEFAULT 'No' NOT NULL,"
              + "PRIMARY KEY(id)"
              //              + "CONSTRAINT COV_id_FK FOREIGN KEY (id) REFERENCES Users (id)"
              //              + "CONSTRAINT COV_positiveTestCheck_check CHECK (positiveTestCheck IN
              // (0,1)),"
              //              + "CONSTRAINT COV_symptomCheck_check CHECK (symptomCheck IN (0,1)),"
              //              + "CONSTRAINT COV_closeContactCheck_check CHECK (closeContactCheck IN
              // (0,1)),"
              //              + "CONSTRAINT COV_selfIsolateCheck_check CHECK (selfIsolateCheck IN
              // (0,1)),"
              //              + "CONSTRAINT COV_feelGoodCheck_check CHECK (feelGoodCheck IN (0,1))
              + ")";
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
      String type,
      String username,
      String firstName,
      String lastName,
      String contactInfo,
      String email,
      String positiveTestCheck,
      String symptomCheck,
      String closeContactCheck,
      String selfIsolateCheck,
      String feelGoodCheck) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO COVID19SurveyResults (firstName, lastName, contactInfo, email, positiveTestCheck, symptomCheck, closeContactCheck, selfIsolateCheck, feelGoodCheck, serviceType, username) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, email);
      stmt.setString(5, positiveTestCheck);
      stmt.setString(6, symptomCheck);
      stmt.setString(7, closeContactCheck);
      stmt.setString(8, selfIsolateCheck);
      stmt.setString(9, feelGoodCheck);
      stmt.setString(10, type);
      stmt.setString(11, username);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void sendCovidEmail(Connection conn, String id) {
    // use id to get email
    // if any constraint is checked, send email of STAY TF HOME
    // if all good is checked, send email of GO ON PEASANT
  }

  public void addIntoCOVIDSurveyList(
      ObservableList<COVIDSurveyResultsNodeInfo> COVIDSurveyData, boolean employeeAccess)
      throws IOException {
    PreparedStatement stmt = null;
    Connection conn = GlobalDb.getConnection();
    try {
      if (employeeAccess) {
        stmt =
            conn.prepareStatement(
                "SELECT * FROM COVID19SurveyResults WHERE assignedTo = ? OR assignedTo  IS NULL");
        stmt.setString(1, HomeController.username);
        //        System.out.println(
        //            "this is trying to add data into the employee table " +
        // HomeController.username);
      } else {
        stmt = conn.prepareStatement("SELECT * FROM COVID19SurveyResults");
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        COVIDSurveyData.add(
            new COVIDSurveyResultsNodeInfo(
                rs.getString("serviceType"),
                rs.getString("username"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("email"),
                rs.getString("assignedTo"),
                rs.getString("status"),
                rs.getString("positiveTestCheck"),
                rs.getString("symptomCheck"),
                rs.getString("closeContactCheck"),
                rs.getString("selfIsolateCheck"),
                rs.getString("feelGoodCheck")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    //    return COVIDSurveyData;
  }

  public ObservableList<COVIDSurveyResultsNodeInfo> changeCOVIDSurveyData(
      ObservableList<COVIDSurveyResultsNodeInfo> COVIDSurveyData) {
    for (COVIDSurveyResultsNodeInfo info : COVIDSurveyData) {
      if (!(info.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE COVID19SurveyResults SET status = ?, assignedTo = ? WHERE username = ?");
          stmt.setString(1, info.getStatus());
          stmt.setString(2, info.getAssignedEmployee());
          stmt.setString(3, info.getUsername());
          stmt.executeUpdate();
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return COVIDSurveyData;
  }

  public void dispAll(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query = "SELECT * FROM COVID19SurveyResults";
      ResultSet rs = stmt.executeQuery(query);
      System.out.println(
          "id \tfirstName \tlastName \tcontactInfo \temail \tassignedTo \tstatus "
              + "\tpositiveTestCheck \tsymptomCheck \tcloseContactCheck \tselfIsolateCheck \tfeelGoodCheck");
      while (rs.next()) {
        // id,firstName,lastName,contactInfo,assignedTo,status,posTestCheck,symptomCheck,closeContactCheck,isolateCheck,feelGoodCheck
        System.out.println(
            rs.getString("id")
                + " \t"
                + rs.getString("firstName")
                + " \t"
                + rs.getString("lastName")
                + " \t"
                + rs.getString("contactInfo")
                + " \t"
                + rs.getString("email")
                + " \t"
                + rs.getString("assignedTo")
                + " \t"
                + rs.getString("status")
                + " \t"
                + rs.getString("positiveTestCheck")
                + " \t"
                + rs.getString("symptomCheck")
                + " \t"
                + rs.getString("closeContactCheck")
                + " \t"
                + rs.getString("selfIsolateCheck")
                + " \t"
                + rs.getString("feelGoodCheck"));
        System.out.println(" ");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public String fetchStatus(Connection conn, String username) {
    String status = "disapproved";
    try {
      PreparedStatement stmt =
          conn.prepareStatement("SELECT status FROM COVID19SurveyResults WHERE username = ?");
      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        status = rs.getString("status");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return status;
  }
}
