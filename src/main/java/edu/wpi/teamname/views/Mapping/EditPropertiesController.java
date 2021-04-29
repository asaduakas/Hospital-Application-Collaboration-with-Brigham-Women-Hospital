package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Astar.Node;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

public class EditPropertiesController {

  private Node theNode = new Node(); // holds node clicked for popup\
  private Circle theCircle = new Circle(); // holds circle clicked for popup

  @FXML public JFXTextField nodeID;
  @FXML public JFXTextField floor;
  @FXML public JFXTextField building;
  @FXML public JFXTextField nodeType;
  @FXML public JFXTextField longName;
  @FXML public JFXTextField shortName;
  private MapController mapController;
  private boolean isAdd = false;

  @FXML
  public void updateNode() {
    //    mapController.updateNode(
    //        theNode,
    //        nodeID.getText(),
    //        floor.getText(),
    //        building.getText(),
    //        nodeType.getText(),
    //        longName.getText(),
    //        shortName.getText());
    exitPopup();
  }

  @FXML
  public void Close() {
    //    if (isAdd) mapController.deleteCircle(theCircle);
    exitPopup();
  }

  private void exitPopup() {

    if (NodeEditPopup.popup != null) {
      NodeEditPopup.popup.hide();
    }
    //    if (mapController.popup != null) {
    //      mapController.popup.hide();
    //    }

    App.getPrimaryStage().getScene().getRoot().setEffect(null);
  }

  public void setMapController(MapController View) {
    this.mapController = View;
  }

  public void setNode(Node N) {
    this.theNode = N;
  }

  public void setCircle(Circle C) {
    this.theCircle = C;
  }

  public void setAdd(boolean add) {
    isAdd = add;
  }
}
