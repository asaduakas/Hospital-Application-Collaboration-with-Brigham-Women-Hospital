package edu.wpi.cs3733.d21.teamD.Ddb;

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
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
    } catch (ClassNotFoundException e) {
      System.out.println("Apache Derby Driver not found. Add the classpath to your module.");
      System.out.println("For IntelliJ do the following:");
      System.out.println("File | Project Structure, Modules, Dependency tab");
      System.out.println("Add by clicking on the green plus icon on the right of the window");
      System.out.println(
              "Select JARs or directories. Go to the folder where the database JAR is located");
      System.out.println("Click OK, now you can compile your program and run it.");
      e.printStackTrace();
      return;
    }
    System.out.println("Apache Derby driver registered!");
    try {
      // this should be the only connection established in the ENTIRE application, every other time
      // should use GlobalDB.getConnection()
      connection =
              DriverManager.getConnection("jdbc:derby:myDB;create=true;username=Admin;password=Admin");
      System.out.println("Apache Derby connection established to the embedded DB!");
      // getConnection() gives a warning if there is already a database
      if (connection.getWarnings() == null) { // If there isn't already a populated database

        GlobalDb.getTables()
                .createAllTables(); // might not be doing what we want with the connections, but this
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }
  }

  public static void establishClientCon() {
    tables = new FDatabaseTables();
    try {
      // EmbeddedDriver -- for embedded

      Class.forName("org.apache.derby.jdbc.ClientDriver");
    } catch (ClassNotFoundException e) {
      System.out.println("Apache Derby Driver not found. Add the classpath to your module.");
      System.out.println("For IntelliJ do the following:");
      System.out.println("File | Project Structure, Modules, Dependency tab");
      System.out.println("Add by clicking on the green plus icon on the right of the window");
      System.out.println(
          "Select JARs or directories. Go to the folder where the database JAR is located");
      System.out.println("Click OK, now you can compile your program and run it.");
      e.printStackTrace();
      // return;
    }
    System.out.println("Apache Derby driver registered!");
    try {
      // this should be the only connection established in the ENTIRE application, every other time
      // should use GlobalDB.getConnection()

      connection =
          // jdbc:derby://localhost:1527/MyDbTest;create=true
          // jdbc:derby:myDB;create=true -- this is for embedded
          DriverManager.getConnection(
              "jdbc:derby://localhost:1527/MyDbTest;create=true;username=Admin;password=Admin");
      System.out.println("the client-server connection was established");
      if (connection == null) System.out.println("Apache Derby connection established!");

      // getConnection() gives a warning if there is already a database
      if (connection.getWarnings() == null) { // If there isn't already a populated database

        GlobalDb.getTables()
            .createAllTables(); // might not be doing what we want with the connections, but this
      }
    } catch (SQLException e) {
      try {
        connection =
            DriverManager.getConnection(
                "jdbc:derby:myDB;create=true;username=Admin;password=Admin");
        System.out.println("the embedded database is being used");
        if (connection.getWarnings() == null) { // If there isn't already a populated database
          GlobalDb.getTables().createAllTables();
        }
      } catch (SQLException throwables) {
        System.out.println("Connection failed. Check output console.");
        throwables.printStackTrace();
      }
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }
  }
}
