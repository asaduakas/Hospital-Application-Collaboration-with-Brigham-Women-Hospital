package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Astar.Node;
import edu.wpi.teamname.Ddb.GlobalDb;
import java.util.LinkedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NodePopup {

  private MapController mapController = null;
  private Node theNode = new Node(); // holds node clicked for popup

  @FXML private Label nodeName;

  @FXML private JFXButton favoriteBtn;
  @FXML private JFXButton cancelBtn;
  @FXML private JFXButton selectStartButton;

  @FXML
  private void initialize() {}

  @FXML
  private void Cancel() {
    exitPopup();
  }

  @FXML
  private void changeFavorite() {
    LinkedList<String> favNodeList =
        mapController.getFav(GlobalDb.getConnection()); // update from database everytime we click
    Node n = theNode;
    int setFavorite = 0;

    if (!(favNodeList.contains(n.getNodeID()))) {
      favoriteBtn.setText("Add to Favorites");
      setFavorite = 1;
    } else if (favNodeList.contains(n.getNodeID())) {
      favoriteBtn.setText("Remove from Favorites");
      setFavorite = 0;
    }

    mapController.updateFavorite(GlobalDb.getConnection(), n.getNodeID(), setFavorite); //
    mapController.redrawMap();
    exitPopup();
  }

  @FXML
  private void selectStart() {
    mapController.setSimpleStartNode(theNode);
    exitPopup();
  }

  private void exitPopup() {
    mapController.popup.hide();
    App.getPrimaryStage().getScene().getRoot().setEffect(null);
  }

  public void setMapController(MapController mapController) {
    this.mapController = mapController;
  }

  public void setNode(Node N) {
    this.theNode = N;
  }

  public void setNodeName(String name) {
    nodeName.setText(name);
  }
}
