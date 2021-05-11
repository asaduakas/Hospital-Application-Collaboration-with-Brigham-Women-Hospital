package edu.wpi.cs3733.d21.teamD.views.Mapping.Popup.Edit;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d21.teamD.views.Mapping.MapController;
import edu.wpi.cs3733.d21.teamD.views.Mapping.ServiceNode;
import javafx.fxml.FXML;

public class ServiceRequestInfoController {

  private MapController mapController;
  private ServiceNode Sn;
  @FXML public JFXTextField Stype;
  @FXML public JFXTextField Emp;
  @FXML public JFXTextField Loc;
  @FXML public JFXTextField Status;

  @FXML
  private void initialize() {}

  @FXML
  private void exitpopup() {
    mapController.popup.hide();
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
    Emp.setText(Sn.getInfo().getAssignedEmployee());
    Loc.setText(Sn.getInfo().getLocation());
    Status.setText(Sn.getInfo().getStatus());
  }
}
