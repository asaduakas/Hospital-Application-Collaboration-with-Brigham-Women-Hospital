package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.LangInterpNodeInfo;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import javafx.collections.ObservableList;

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
              //              + "CONSTRAINT LANG_employee_FK FOREIGN KEY(assignedEmployee)
              // REFERENCES Users(id),"
              + "CONSTRAINT LANG_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')),"
              + "CONSTRAINT LANG_langRequested_check CHECK (languageRequested IN ('Chinese', 'French', 'German', 'Italian', 'Spanish', 'Portuguese')))";
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
      String assignedEmployee,
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
      stmt.setString(5, assignedEmployee);
      stmt.setString(6, languageRequested);
      stmt.setDate(7, Date.valueOf(dateRequested));
      stmt.executeUpdate();

      FDatabaseTables.getAllServiceTable()
          .addEntity(
              GlobalDb.getConnection(),
              this.getID(GlobalDb.getConnection()),
              location,
              "Incomplete",
              assignedEmployee,
              "LANG");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addIntoLangInterpreterList(
      ObservableList<LangInterpNodeInfo> langInterpData, boolean employeeAccess)
      throws IOException {
    PreparedStatement stmt = null;
    Connection conn = GlobalDb.getConnection();
    try {
      if (employeeAccess) {
        stmt =
            conn.prepareStatement(
                "SELECT * FROM LangInterpRequest WHERE assignedEmployee = ? OR assignedEmployee  IS NULL");
        stmt.setString(1, HomeController.username);
        //        System.out.println(
        //            "this is trying to add data into the employee table " +
        // HomeController.username);
        //        System.out.println("this is getting the userType " + HomeController.userTypeEnum);
      } else {
        stmt = conn.prepareStatement("SELECT * FROM LangInterpRequest");
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        langInterpData.add(
            new LangInterpNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("languageRequested"),
                rs.getString("dateRequested")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
  //
  public ObservableList<LangInterpNodeInfo> changeLangInterData(
      ObservableList<LangInterpNodeInfo> langInterpData) {
    for (LangInterpNodeInfo langInterpInfo : langInterpData) {
      if (!(langInterpInfo.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE LangInterpRequest SET status = ?, assignedEmployee = ? WHERE id=?");
          stmt.setString(1, langInterpInfo.getStatus());
          stmt.setString(2, langInterpInfo.getAssignedEmployee());
          stmt.setString(3, langInterpInfo.getId());
          stmt.executeUpdate();

          AllServiceTable.updateEntity(
              GlobalDb.getConnection(),
              Integer.valueOf(langInterpInfo.getId()),
              langInterpInfo.getStatus(),
              langInterpInfo.getAssignedEmployee(),
              "LANG");

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return langInterpData;
  }

  public int getID(Connection conn) {
    int id = 420;
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT id FROM LangInterpRequest");
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        System.out.println("LOOK HERE:" + id);
        id = rs.getInt(1);
        System.out.println("LOOK HERE:" + id);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    System.out.println();
    return id;
  }

  public HashMap<Integer, String> getIncompleteRequest() {
    Connection conn = GlobalDb.getConnection();
    HashMap<Integer, String> langInterpList = new HashMap<>();
    String id = HomeController.username;
    int i = 0;
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "SELECT location, firstName, lastName, contactInfo FROM LangInterpRequest WHERE status = 'Incomplete' AND assignedEmployee = ?");
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        langInterpList.put(
            i,
            "Language Interpreter"
                + " -- "
                + rs.getString("location")
                + " -- Name: "
                + rs.getString("firstName")
                + " "
                + rs.getString("lastName")
                + " -- Contact: "
                + rs.getString("contactInfo"));
        i++;
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return langInterpList;
  }
}
