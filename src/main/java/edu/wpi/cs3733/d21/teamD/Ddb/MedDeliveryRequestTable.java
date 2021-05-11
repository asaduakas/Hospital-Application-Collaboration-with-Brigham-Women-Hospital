package edu.wpi.cs3733.d21.teamD.Ddb;

import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.MedDelivNodeInfo;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import javafx.collections.ObservableList;

public class MedDeliveryRequestTable extends AbsTables {
  // id,status,firstName,lastName,contactInfo,location,assignedEmployee,typeOfMedicine,dropOffDate

  public MedDeliveryRequestTable() {}

  public void createTable(Connection conn) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query =
          "CREATE TABLE MedicineDelivery("
              + "id INT GENERATED ALWAYS AS IDENTITY NOT NULL,"
              + "status VARCHAR(100) DEFAULT 'Incomplete',"
              + "firstName VARCHAR(100) NOT NULL,"
              + "lastName VARCHAR(100) NOT NULL,"
              + "contactInfo VARCHAR(100) NOT NULL,"
              + "location VARCHAR(100) NOT NULL,"
              + "assignedEmployee VARCHAR(100) DEFAULT '',"
              + "typeOfMedicine VARCHAR(100) NOT NULL,"
              + "dropOffDate DATE NOT NULL," // YYYY-MM-DD format
              + "PRIMARY KEY(id),"
              //              + "CONSTRAINT MED_employee_FK FOREIGN KEY(assignedEmployee) REFERENCES
              // Users(id),"
              + "CONSTRAINT MED_status_check CHECK (status IN ('Incomplete', 'Complete', 'In Progress')))";
      // + "CONSTRAINT MED_location_FK FOREIGN KEY(location) REFERENCES Nodes(nodeID))";
      stmt.executeUpdate(query);
      System.out.println("Medication Delivery Request table created");
    } catch (Exception e) {
      System.out.println("Medication Delivery Request table was not created");
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
      String medicineType,
      LocalDate dropOffDate) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              "INSERT INTO MedicineDelivery (firstName, lastName, contactInfo, location,"
                  + "typeOfMedicine, dropOffDate) VALUES(?,?,?,?,?,?)");
      stmt.setString(1, firstName);
      stmt.setString(2, lastName);
      stmt.setString(3, contactInfo);
      stmt.setString(4, location);
      stmt.setString(5, medicineType);
      stmt.setDate(6, Date.valueOf(dropOffDate));
      stmt.executeUpdate();

      FDatabaseTables.getAllServiceTable()
          .addEntity(
              GlobalDb.getConnection(),
              this.getID(GlobalDb.getConnection()),
              location,
              "Incomplete",
              assignedEmp,
              "MEDD");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addIntoMedDeliveryList(
      ObservableList<MedDelivNodeInfo> medDelivData, boolean employeeAccess) throws IOException {
    PreparedStatement stmt = null;
    Connection conn = GlobalDb.getConnection();
    try {
      if (employeeAccess) {
        stmt =
            conn.prepareStatement(
                "SELECT * FROM MedicineDelivery WHERE assignedEmployee = ? OR assignedEmployee = ''");
        stmt.setString(1, HomeController.username);
        //        System.out.println(
        //            "this is trying to add data into the employee table " +
        // HomeController.username);
        //        System.out.println("this is getting the userType " + HomeController.userTypeEnum);
      } else {
        stmt = conn.prepareStatement("SELECT * FROM MedicineDelivery");
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        medDelivData.add(
            new MedDelivNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("typeOfMedicine"),
                rs.getString("dropOffDate")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public ObservableList<MedDelivNodeInfo> changeMedDeliveryData(
      ObservableList<MedDelivNodeInfo> medDelivData) {
    for (MedDelivNodeInfo medDelInfo : medDelivData) {
      if (!(medDelInfo.getStatus().isEmpty())) {
        PreparedStatement stmt = null;
        try {
          stmt =
              GlobalDb.getConnection()
                  .prepareStatement(
                      "UPDATE MedicineDelivery SET status = ?, assignedEmployee = ? WHERE id=?");
          stmt.setString(1, medDelInfo.getStatus());
          stmt.setString(2, medDelInfo.getAssignedEmployee());
          stmt.setString(3, medDelInfo.getId());
          stmt.executeUpdate();

          AllServiceTable.updateEntity(
              GlobalDb.getConnection(),
              medDelInfo.getId(),
              medDelInfo.getStatus(),
              medDelInfo.getAssignedEmployee(),
              "MEDD");

        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    }
    return medDelivData;
  }

  public int getID(Connection conn) {
    int id = 420;
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT id FROM MedicineDelivery");
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
}
