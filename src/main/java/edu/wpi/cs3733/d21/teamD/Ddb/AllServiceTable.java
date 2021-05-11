package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.AllServiceNodeInfo;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;

public class AllServiceTable extends AbsTables {

  public void createTable(Connection conn) {
    try {
      Statement stmt = conn.createStatement();
      String query =
          "CREATE TABLE AllServiceTable("
              + "id INT NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "status VARCHAR(100) NOT NULL,"
              + "assignedTo VARCHAR(450) NOT NULL,"
              + "Stype VARCHAR(5) NOT NULL)";
      stmt.executeUpdate(query);
      System.out.println("AllServiceTable table created");
    } catch (Exception e) {
      System.out.println("AllServiceTable was not created");
      e.printStackTrace();
      return;
    }
  }

  public static void addEntity(
      Connection conn, int id, String location, String status, String employee, String Stype) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement("INSERT INTO AllServiceTable VALUES(?,?,?,?,?)");
      stmt.setInt(1, id);
      stmt.setString(2, location);
      stmt.setString(3, status);
      stmt.setString(4, employee);
      stmt.setString(5, Stype);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void updateEntity(
      Connection conn, String id, String status, String employee, String Stype) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              " UPDATE AllServiceTable SET status = ?, assignedTo = ? WHERE id = ? AND Stype = ?");
      stmt.setString(1, status);
      stmt.setString(2, employee);
      stmt.setString(3, id);
      stmt.setString(4, Stype);
      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public LinkedList<AllServiceNodeInfo> ListServices() throws IOException {
    try {
      String query = "SELECT * FROM AllServiceTable";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      LinkedList<AllServiceNodeInfo> Services = new LinkedList<>();
      while (rs.next()) {
        Services.add(
            new AllServiceNodeInfo(
                rs.getInt("id"),
                rs.getString("status"),
                rs.getString("location"),
                rs.getString("assignedTo"),
                rs.getString("Stype")));
      }
      return Services;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  public void dispAll(Connection conn) {
    Statement stmt = null;

    try {
      stmt = conn.createStatement();
      String query = "SELECT * FROM AllServiceTable";
      ResultSet rs = stmt.executeQuery(query);
      System.out.println("id \t status \t location \t assignedEmployee \t Stype");
      while (rs.next()) {
        // nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName
        System.out.println(
            rs.getInt("id")
                + " \t"
                + rs.getString("status")
                + " \t"
                + rs.getString("location")
                + " \t"
                + rs.getString("assignedTo")
                + " \t"
                + rs.getString("Stype"));
        System.out.println(" ");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
}
