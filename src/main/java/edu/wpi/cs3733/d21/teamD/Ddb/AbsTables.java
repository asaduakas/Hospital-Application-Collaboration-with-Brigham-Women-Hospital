package edu.wpi.cs3733.d21.teamD.Ddb;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public abstract class AbsTables {

  public abstract void createTable(Connection conn);

  public void clearTable(Connection conn, String tableName) {
    try {
      conn.createStatement().execute("DELETE FROM " + tableName);
      System.out.println(tableName + " table cleared!");
    } catch (Exception e) {
      System.out.println(tableName + "table can't be cleared");
      e.printStackTrace();
      return;
    }
  }

  // public abstract void populateTable(Connection conn, String filePath);

  public void deleteEntity(Connection conn, String tableName, String id) {
    try {
      // System.out.println(id);
      PreparedStatement stmt = conn.prepareStatement("DELETE FROM ? WHERE id =?");
      stmt.setString(1, tableName);
      stmt.setString(2, id);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void writeTableToCSV(
      Connection conn, String tableName, String filePath, int numColumns, String firstLine)
      throws IOException {
    FileWriter fw = new FileWriter(filePath);
    fw.write(firstLine + "\n");
    try {
      Statement stmt = conn.createStatement();
      stmt.executeQuery("SELECT * FROM " + tableName);
      ResultSet rs = stmt.getResultSet();
      while (rs.next()) {
        for (int i = 1; i <= numColumns; i++) {
          fw.write(rs.getString(i));
          if (i != numColumns) {
            fw.write(",");
          }
        }
        fw.write("\n");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    fw.flush();
    fw.close();
  }
}
