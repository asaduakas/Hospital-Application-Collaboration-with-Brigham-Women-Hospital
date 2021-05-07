package edu.wpi.cs3733.d21.teamD.Ddb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Scanner;

public class SearchHistoryTable extends AbsTables {

  public void createTable(Connection conn) {
    try {
      Statement stmt = conn.createStatement();
      String query =
          "CREATE TABLE SearchHistory("
              + "startName VARCHAR(50) NOT NULL,"
              + "endName VARCHAR(50) NOT NULL)";
      stmt.executeUpdate(query);
      System.out.println("Search History table created");
    } catch (Exception e) {
      System.out.println("Search History table was not created");
      e.printStackTrace();
      return;
    }
  }

  public void populateTable(Connection conn, String filePath) {
    if (filePath.isEmpty()) filePath = "SearchHistory.csv";
    try {
      Scanner sc = new Scanner(SearchHistoryTable.class.getResourceAsStream("/csv/" + filePath));
      Statement stmt = conn.createStatement();

      sc.nextLine();
      while (sc.hasNextLine()) {
        while (sc.hasNextLine()) {
          try {
            String[] row = sc.nextLine().split(",");
            String query =
                "INSERT INTO SearchHistory VALUES('" + row[0] + "'," + "'" + row[1] + "')";
            stmt.execute(query);
          } catch (Exception e) {
            System.out.println("Search History table did not populate");
            e.printStackTrace();
          }
        }
      }

      System.out.println("Search History table populated");
    } catch (Exception e) {
      System.out.println("Search History table did not populate");
      e.printStackTrace();
      return;
    }
  }

  public void addEntity(Connection conn, String startName, String endName) {
    try {
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO SearchHistory VALUES(?,?)");
      stmt.setString(1, startName);
      stmt.setString(2, endName);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
