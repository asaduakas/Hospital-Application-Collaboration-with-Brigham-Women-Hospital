package edu.wpi.teamname.views.Mapping.Popup.Edit;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamname.Astar.Node;
import edu.wpi.teamname.views.Mapping.MapController;
import edu.wpi.teamname.views.Mapping.NodeUI;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddNodeController {

  @FXML public JFXTextField floor;
  @FXML public JFXTextField building;
  @FXML public JFXTextField nodeType;
  @FXML public JFXTextField longName;
  @FXML public JFXTextField shortName;

  double X;
  double Y;

  private Image I = new Image("Images/274px-Google_Maps_pin.svg.png");
  private MapController mapController;

  @FXML
  private void newNodeUI() {
    buildNode();
    buildMarker();
    NodeUI NUI = new NodeUI(buildNode(), buildMarker());
    mapController.addNode(NUI);
    exitpopup();
  }

  private Node buildNode() {
    Node New = new Node();
    Random Rand = new Random();

    New.setXcoord((int) X);
    New.setYcoord((int) Y);
    New.setNodeID(Integer.toString((Rand.nextInt(1000000000))));
    New.setFloor(floor.getText());
    New.setBuilding(building.getText());
    New.setNodeType(nodeType.getText());
    New.setLongName(longName.getText());
    New.setShortName(shortName.getText());
    return New;
  }

  @FXML
  private void exitpopup() {
    mapController.popup.hide();
  }

  private ImageView buildMarker() {
    double markerX = 15;
    double markerY = 26.25;
    ImageView Marker = new ImageView(I);
    Marker.setFitWidth(markerX);
    Marker.setFitHeight(markerY);
    Marker.setX(X - markerX / 2);
    Marker.setY(Y - markerY);
    return Marker;
  }

  public void setNX(double x) {
    X = x;
  }

  public void setNY(double y) {
    Y = y;
  }

  public void setMapController(MapController mapController) {
    this.mapController = mapController;
  }
}
