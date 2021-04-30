package edu.wpi.teamname.Ddb;

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
      stmt.setString(5, category);
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

  // TODO: implement this function
  public static void updateUserPassword(Connection conn, String id, String password) {}
}
