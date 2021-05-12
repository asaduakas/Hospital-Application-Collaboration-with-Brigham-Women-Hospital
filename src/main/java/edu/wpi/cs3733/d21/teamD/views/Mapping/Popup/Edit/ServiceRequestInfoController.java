package edu.wpi.cs3733.d21.teamD.views.Mapping.Popup.Edit;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d21.teamD.Ddb.FDatabaseTables;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.Mapping.MapController;
import edu.wpi.cs3733.d21.teamD.views.Mapping.ServiceNode;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class ServiceRequestInfoController {

  private MapController mapController;
  private ServiceNode Sn;
  @FXML public JFXTextField Stype;
  @FXML public JFXComboBox Emp;
  @FXML public JFXTextField Loc;
  @FXML public JFXComboBox Status;

  @FXML
  private void initialize() {

    ObservableList<String> employeeList =
        FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    employeeList.sort(String::compareToIgnoreCase);
    Emp.getItems().addAll(employeeList);

    Status.getItems().add("Complete");
    Status.getItems().add("In Progress");
    Status.getItems().add("Incomplete");

    if (HomeController.userCategory.equals("Admin")) {
      Stype.setEditable(false);
      Emp.setEditable(true);
      Loc.setEditable(false);
      Status.setEditable(true);
    } else {
      Stype.setEditable(false);
      Emp.setEditable(false);
      Loc.setEditable(false);
      Status.setEditable(true);
    }
  }

  @FXML
  private void exitpopup() {
    mapController.popup.hide();
    Sn.getSerivce().setFitWidth(30);
    Sn.getSerivce().setFitHeight(30);
    Sn.getSerivce().setX(Sn.getSerivce().getX() + 15);
    Sn.getSerivce().setY(Sn.getSerivce().getY() + 15);
    MapController.servicePoop = false;
  }

  public void setSn(ServiceNode sn) {
    Sn = sn;
  }

  public void setMapController(MapController mapController) {
    this.mapController = mapController;
  }

  public void Fill() {

    System.out.println(Sn.getInfo().getType());

    Stype.setText(Sn.getInfo().getType());
    Emp.setPromptText(Sn.getInfo().getAssignedEmployee());
    Loc.setText(Sn.getInfo().getLocation());
    Status.setPromptText(Sn.getInfo().getStatus());
  }

  public void Update() throws IOException {

    System.out.println("EMP:" + Emp.getSelectionModel().getSelectedIndex());
    System.out.println("STATUS:" + Status.getSelectionModel().getSelectedIndex());
    if (Emp.getSelectionModel().getSelectedIndex() > -1) {
      String TheEmp = Emp.getItems().get(Emp.getSelectionModel().getSelectedIndex()).toString();
      Sn.getInfo().setAssignedEmployee(TheEmp);

      FDatabaseTables.getAllServiceTable()
          .updateEntity(
              GlobalDb.getConnection(),
              Sn.getInfo().getId(),
              Sn.getInfo().getStatus(),
              Sn.getInfo().assignedEmployee,
              Sn.getInfo().getType());
    }

    if (Status.getSelectionModel().getSelectedIndex() > -1) {
      String Stat = Status.getItems().get(Status.getSelectionModel().getSelectedIndex()).toString();
      Sn.getInfo().setStatus(Stat);

      FDatabaseTables.getAllServiceTable()
          .updateEntity(
              GlobalDb.getConnection(),
              Sn.getInfo().getId(),
              Sn.getInfo().getStatus(),
              Sn.getInfo().assignedEmployee,
              Sn.getInfo().getType());
    }

    switch (Sn.getInfo().getType()) {
      case "EXT":
        FDatabaseTables.getExternalTransportTable()
            .updateEntity(
                GlobalDb.getConnection(),
                Sn.getInfo().getId(),
                Sn.getInfo().getStatus(),
                Sn.getInfo().assignedEmployee);
        break;
      case "FLOW":
        FDatabaseTables.getFloralDeliveryTable()
            .updateEntity(
                GlobalDb.getConnection(),
                Sn.getInfo().getId(),
                Sn.getInfo().getStatus(),
                Sn.getInfo().assignedEmployee);
        break;
      case "FOOD":
        FDatabaseTables.getFoodDeliveryTable()
            .updateEntity(
                GlobalDb.getConnection(),
                Sn.getInfo().getId(),
                Sn.getInfo().getStatus(),
                Sn.getInfo().assignedEmployee);
        break;
      case "LAUN":
        FDatabaseTables.getLaundryRequestTable()
            .updateEntity(
                GlobalDb.getConnection(),
                Sn.getInfo().getId(),
                Sn.getInfo().getStatus(),
                Sn.getInfo().assignedEmployee);
        break;
      case "LANG":
        FDatabaseTables.getLangInterpreterTable()
            .updateEntity(
                GlobalDb.getConnection(),
                Sn.getInfo().getId(),
                Sn.getInfo().getStatus(),
                Sn.getInfo().assignedEmployee);
        break;
      case "ITRAN":
        FDatabaseTables.getInternalDeliveryTable()
            .updateEntity(
                GlobalDb.getConnection(),
                Sn.getInfo().getId(),
                Sn.getInfo().getStatus(),
                Sn.getInfo().assignedEmployee);
        break;
      case "SECUR":
        FDatabaseTables.getSecurityRequestTable()
            .updateEntity(
                GlobalDb.getConnection(),
                Sn.getInfo().getId(),
                Sn.getInfo().getStatus(),
                Sn.getInfo().assignedEmployee);
        break;
      case "FACIL":
        FDatabaseTables.getFacilitiesTable()
            .updateEntity(
                GlobalDb.getConnection(),
                Sn.getInfo().getId(),
                Sn.getInfo().getStatus(),
                Sn.getInfo().assignedEmployee);
        break;
      case "COMP":
        FDatabaseTables.getCompRequestTable()
            .updateEntity(
                GlobalDb.getConnection(),
                Sn.getInfo().getId(),
                Sn.getInfo().getStatus(),
                Sn.getInfo().assignedEmployee);
        break;
      case "AUD":
        FDatabaseTables.getAudVisTable()
            .updateEntity(
                GlobalDb.getConnection(),
                Sn.getInfo().getId(),
                Sn.getInfo().getStatus(),
                Sn.getInfo().assignedEmployee);
        break;
      case "SANI":
        FDatabaseTables.getSanitationServiceTable()
            .updateEntity(
                GlobalDb.getConnection(),
                Sn.getInfo().getId(),
                Sn.getInfo().getStatus(),
                Sn.getInfo().assignedEmployee);
        break;
      case "MEDD":
        FDatabaseTables.getMedDeliveryTable()
            .updateEntity(
                GlobalDb.getConnection(),
                Sn.getInfo().getId(),
                Sn.getInfo().getStatus(),
                Sn.getInfo().assignedEmployee);
        break;
    }

    mapController.clearMap();
    MapController.ServiceView = false;
    mapController.LoadServices();

    exitpopup();
  }
}
