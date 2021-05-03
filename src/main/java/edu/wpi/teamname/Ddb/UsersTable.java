package edu.wpi.teamname.Ddb;

import edu.wpi.teamname.views.Access.UserCategory;
import edu.wpi.teamname.views.HomeController;
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

  public void populateTable(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "Users.csv";
    try {
      Scanner sc = new Scanner(UsersTable.class.getResourceAsStream("/csv/" + filePath));
      Statement stmt = conn.createStatement();

      sc.nextLine();
      while (sc.hasNextLine()) {
        try {
          String[] row = sc.nextLine().split(",");
          String query =
              "INSERT INTO Users VALUES("
                  + "'"
                  + row[0]
                  + "',"
                  + "'"
                  + row[1]
                  + "',"
                  + "'"
                  + row[2]
                  + "',"
                  + "'"
                  + row[3]
                  + "')";
          stmt.execute(query);
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
      System.out.println("id \tpassword \tname \tcategory");
      while (rs.next()) {
        System.out.println(
            rs.getString("id")
                + " \t"
                + rs.getString("password")
                + " \t"
                + rs.getString("name")
                + " \t"
                + rs.getString("category"));
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
        System.out.println(pw + " this is rs");
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
}
