package edu.wpi.teamname.Ddb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GlobalDb {
  private static Connection connection;
  private static FDatabaseTables tables;

  public static Connection getConnection() {
    return connection;
  }

  public static FDatabaseTables getTables() { // this is probably broken right here, don't know
    return tables;
  }

  public static void establishCon() {
    tables = new FDatabaseTables();
    try {
      // this should be the only connection established in the ENTIRE application, every other time
      // should use GlobalDB.getConnection()
      connection =
          DriverManager.getConnection("jdbc:derby:myDB;create=true;username=Admin;password=Admin");
      System.out.println("Apache Derby connection established!");
      // getConnection() gives a warning if there is already a database
      if (connection.getWarnings() == null) { // If there isn't already a populated database

        GlobalDb.tables
            .createAllTables(); // might not be doing what we want with the connections, but this
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }
  }
}
