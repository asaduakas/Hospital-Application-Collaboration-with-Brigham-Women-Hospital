package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.Access.UserCategory;
import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.UsersNodeInfo;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UsersTable extends AbsTables {

  public UsersTable() {}

  public void createTable(Connection conn) {
    try {
      Statement stmt = conn.createStatement();
      String query =
          "CREATE TABLE Users("
              + "id VARCHAR(100) NOT NULL,"
              + "password VARCHAR(50) DEFAULT '',"
              + "name VARCHAR(50) DEFAULT '',"
              + "category VARCHAR(8) NOT NULL,"
              + "clearance VARCHAR(30) DEFAULT '',"
              + "email VARCHAR(100) DEFAULT '',"
              + "PRIMARY KEY(id),"
              + "CONSTRAINT USE_role_check CHECK (category IN ('Employee', 'Patient', 'Admin')))";
      stmt.executeUpdate(query);
      System.out.println("User table created");
    } catch (Exception e) {
      System.out.println("User table was not created");
      e.printStackTrace();
      return;
    }
  }

  //  public void populateTable(Connection conn, String filePath) {
  //    if (filePath.isEmpty()) filePath = "Users.csv";
  //    try {
  //      Scanner sc = new Scanner(UsersTable.class.getResourceAsStream("/csv/" + filePath));
  //      Statement stmt = conn.createStatement();
  //
  //      sc.nextLine();
  //      while (sc.hasNextLine()) {
  //        try {
  //          String[] row = sc.nextLine().split(",");
  //          String query =
  //              "INSERT INTO Users VALUES("
  //                  + "'"
  //                  + row[0]
  //                  + "',"
  //                  + "'"
  //                  + row[1]
  //                  + "',"
  //                  + "'"
  //                  + row[2]
  //                  + "',"
  //                  + "'"
  //                  + row[3]
  //                  + "',"
  //                  + "'"
  //                  + row[4]
  //                  + "')";
  //          stmt.execute(query);
  //        } catch (Exception e) {
  //          System.out.println("User table did not populate");
  //          e.printStackTrace();
  //        }
  //      }
  //      conn.commit();
  //      System.out.println("User Table populated");
  //    } catch (Exception e) {
  //      System.out.println("User table did not populate");
  //      e.printStackTrace();
  //      return;
  //    }
  //  }

  public void populateTable(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "Users.csv";
    try {
      Scanner sc = new Scanner(UsersTable.class.getResourceAsStream("/csv/" + filePath));
      Statement stmt = conn.createStatement();

      sc.nextLine();
      while (sc.hasNextLine()) {
        try {
          String[] row = sc.nextLine().split(",");
          PreparedStatement ps = conn.prepareStatement("INSERT INTO Users VALUES (?,?,?,?,NULL,?)");
          ps.setString(1, row[0]);
          ps.setString(2, row[1]);
          ps.setString(3, row[2]);
          ps.setString(4, row[3]);
          ps.setString(5, row[6]);
          //                    ps.setNull(5, );
          ps.executeUpdate();
        } catch (Exception e) {
          System.out.println("User table did not populate");
          e.printStackTrace();
        }
      }
      conn.commit();
      System.out.println("User Table populated");
    } catch (Exception e) {
      System.out.println("User table did not populate");
      e.printStackTrace();
      return;
    }
  }

  public static void addEntity(
      Connection conn, String id, String password, String name, String category) {
    try {
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO Users VALUES(?,?,?,?)");
      stmt.setString(1, id);
      stmt.setString(2, password);
      stmt.setString(3, name);
      stmt.setString(4, category);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void dispUsers(Connection conn) {
    Statement stmt = null;

    try {
      stmt = conn.createStatement();
      String query = "SELECT * FROM Users";
      ResultSet rs = stmt.executeQuery(query);
      System.out.println("id \tpassword \tname \tcategory \tclearance");
      while (rs.next()) {
        System.out.println(
            rs.getString("id")
                + " \t"
                + rs.getString("password")
                + " \t"
                + rs.getString("name")
                + " \t"
                + rs.getString("category")
                + " \t"
                + rs.getString("clearance"));
        System.out.println(" ");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static ObservableList<String> fetchEmployee(Connection conn) {
    ObservableList<String> employees = FXCollections.observableArrayList();
    try {
      PreparedStatement longNameStmt =
          conn.prepareStatement("SELECT id FROM Users WHERE category = 'Employee'");
      ResultSet rs = longNameStmt.executeQuery();
      while (rs.next()) {
        employees.add(rs.getString(1));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return employees;
  }

  public String getCategoryofUser(Connection conn, String username) {
    Statement stmt = null;
    ResultSet rs = null;
    String category = null;
    try {
      stmt = conn.createStatement();
      String query = "SELECT category FROM Users WHERE id = '" + username + "' ";
      rs = stmt.executeQuery(query);

      while (rs.next()) {
        category = rs.getString("category");
        if (rs.getString("username") == null) {
          category = "Guest";
        }
      }

    } catch (SQLException e) {

    }
    return category;
  }

  public boolean validateTheUser(Connection conn, String username, String password) {
    // boolean usernameBool = false;
    // boolean passwordBool = false;

    HomeController.username = username;
    HomeController.password = password;
    System.out.println(username + " in validateUser");
    System.out.println(password + " password in validateUser");

    try {
      Statement statement = GlobalDb.getConnection().createStatement();
      String query = "SELECT category, password FROM Users WHERE id = '" + username + "'";
      statement.executeQuery(query);
      ResultSet rs = statement.getResultSet();

      if (rs.next()) { // If there is a user
        String pw = rs.getString("password");
        //        System.out.println(pw + " this is rs");
        if (!password.equals(pw)) {
          return false;
        } else {
          switch (rs.getString("category")) {
            case "patient":
              HomeController.userTypeEnum = UserCategory.Patient;
              break;
            case "employee":
              HomeController.userTypeEnum = UserCategory.Employee;
              break;
            case "admin":
              HomeController.userTypeEnum = UserCategory.Admin;
              break;
          }
        }
      } else {
        System.out.println("User not found in database!");
        return false;
      }
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  // TODO: implement this function
  public static void updateUserPassword(Connection conn, String id, String password) {}

  public ObservableList<UsersNodeInfo> addIntoEmployeeDataList(
      ObservableList<UsersNodeInfo> employeeData) throws IOException {
    try {
      String query = "SELECT * FROM Users WHERE category = 'Employee'";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        employeeData.add(
            new UsersNodeInfo(
                rs.getString("id"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("clearance")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return employeeData;
  }

  public ObservableList<UsersNodeInfo> addIntoPatientDataList(
      ObservableList<UsersNodeInfo> patientData) throws IOException {
    try {
      String query = "SELECT * FROM Users WHERE category = 'Patient'";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        patientData.add(
            new UsersNodeInfo(
                rs.getString("id"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("clearance")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return patientData;
  }

  public ObservableList<UsersNodeInfo> addIntoAdminDataList(ObservableList<UsersNodeInfo> adminData)
      throws IOException {
    try {
      String query = "SELECT * FROM Users WHERE category = 'Admin'";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        adminData.add(
            new UsersNodeInfo(
                rs.getString("id"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("clearance")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return adminData;
  }

  public ObservableList<UsersNodeInfo> changeUserData(ObservableList<UsersNodeInfo> userData) {
    for (UsersNodeInfo info : userData) {
      PreparedStatement stmt = null;
      try {
        stmt =
            GlobalDb.getConnection().prepareStatement("UPDATE Users SET password = ? WHERE id=?");
        stmt.setString(1, info.getPassword());
        stmt.setString(2, info.getId());
        stmt.executeUpdate();
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
    return userData;
  }

  public void changeClearance(Connection conn, String clearance, String id) {
    try {
      PreparedStatement stmt =
          GlobalDb.getConnection().prepareStatement("UPDATE Users SET clearance = ? WHERE id = ?");
      stmt.setString(1, clearance);
      stmt.setString(2, id);
      stmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public String validateClearance(Connection conn, String id) {
    String clearance = null;
    try {
      PreparedStatement stmt =
          GlobalDb.getConnection().prepareStatement("SELECT clearance FROM Users WHERE id = ?");
      stmt.setString(1, id);
      stmt.execute();
      ResultSet rs = stmt.getResultSet();
      if (rs.next()) {
        String clearanceLevel = rs.getString("clearance");
        if (clearanceLevel.equals("normalEntrance")) {
          clearance = "normalEntrance";
        }
        if (clearanceLevel.equals("emergencyEntrance")) {
          clearance = "emergencyEntrance";
        } else {
          clearance = "prettyGood";
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return clearance;
  }
}
