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
        /* GlobalDb.table.createNodesTable(GlobalDb.connection);
        GlobalDb.table.createEdgesTable(GlobalDb.connection);
        GlobalDb.table.createUserTable(GlobalDb.connection);
        GlobalDb.table.createFavoriteNodeTable(GlobalDb.connection);
        GlobalDb.table.createExternalTransRequestsTable(GlobalDb.connection);
        GlobalDb.table.createFloralRequestsTable(GlobalDb.connection);
        GlobalDb.table.createFoodDeliveryRequestsTable(GlobalDb.connection);
        GlobalDb.table.createLaundryRequestsTable(GlobalDb.connection);
        GlobalDb.table.createSearchHistoryTable(GlobalDb.connection);
        System.out.println("Created tables");*/
        GlobalDb.tables
            .createAllTables(); // might not be doing what we want with the connections, but this
        // could be an easy fix by switching connection to be a parameter
        /* probably don't need this stuff anymore
        GlobalDb.tables.createNodesTable(GlobalDb.connection);
        GlobalDb.tables.createEdgesTable(GlobalDb.connection);
        GlobalDb.tables.createUserTable(GlobalDb.connection);
        GlobalDb.tables.createFavoriteNodeTable(GlobalDb.connection);
        GlobalDb.tables.createExternalTransRequestsTable(GlobalDb.connection);
        GlobalDb.tables.createFloralRequestsTable(GlobalDb.connection);
        GlobalDb.tables.createFoodDeliveryRequestsTable(GlobalDb.connection);
        GlobalDb.tables.createLaundryRequestsTable(GlobalDb.connection);

        GlobalDb.table.populateNodesTable(GlobalDb.connection, "");
        GlobalDb.table.populateEdgesTable(GlobalDb.connection, "");
        GlobalDb.table.populateUserTable(GlobalDb.connection, "");
        GlobalDb.table.populateFavoriteNodeTable(GlobalDb.connection, "");
        GlobalDb.table.populateSearchHistoryTable(GlobalDb.connection, "");
        System.out.println("Populated tables");
        GlobalDb.tables.populateNodesTable(GlobalDb.connection, "");
        GlobalDb.tables.populateEdgesTable(GlobalDb.connection, "");
        GlobalDb.tables.populateUserTable(GlobalDb.connection, "");
        GlobalDb.tables.populateFavoriteNodeTable(GlobalDb.connection, "");
         */

      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }
  }
}
