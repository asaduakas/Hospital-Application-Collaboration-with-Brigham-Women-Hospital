package edu.wpi.teamname.views.Mapping.Popup.Edit;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Astar.Node;
import edu.wpi.teamname.views.Mapping.MapController;
import edu.wpi.teamname.views.Mapping.NodeUI;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EditNodeController {

  @FXML public JFXComboBox FloorBox;
  @FXML public JFXComboBox NodeType;
  @FXML public JFXTextField building;
  @FXML public JFXTextField nodeType;
  @FXML public JFXTextField longName;
  @FXML public JFXTextField shortName;
  private NodeUI theNode;

  double X;
  double Y;

  private Image I = new Image("Images/274px-Google_Maps_pin.svg.png");
  private MapController mapController;

  @FXML
  private void initialize() {
    FloorBox.setPromptText("Floor " + theNode.getN().getFloor());
    FloorBox.getItems().addAll("L2", "L1", "1", "2", "3");
    NodeType.getItems()
        .addAll(
            "Parking",
            "Elevator",
            "Restroom",
            "Stairs",
            "Department",
            "Laboratory",
            "Information",
            "Conference",
            "Exit",
            "Retail",
            "Service");

    building.setText(theNode.getN().getBuilding());
    nodeType.setText(theNode.getN().getNodeType());
    longName.setText(theNode.getN().getLongName());
    shortName.setText(theNode.getN().getShortName());
  }

  @FXML
  private void editNodeUI(NodeUI NUI) {
    Node node = buildNode();

    NUI.setI(editMarker(NUI.getN()));

    mapController.addNode(NUI);
    exitpopup();
  }

  private Node buildNode() {
    Node New = new Node();
    Random Rand = new Random();

    New.setXcoord((int) X);
    New.setYcoord((int) Y);
    New.setNodeID(Integer.toString((Rand.nextInt(1000000000))));

    while (MapController.NODES.contains(
        MapController.getNodeUIByID(New.getNodeID()))) { // make sure no repetition
      New.setNodeID(Integer.toString((Rand.nextInt(1000000000))));
    }

    if (FloorBox.getValue() != null) { // set floor to current floor unless stated
      New.setFloor((String) FloorBox.getValue());
    } else {
      New.setFloor(MapController.currentFloor);
    }

    New.setBuilding(building.getText());

    String type = "";
    switch ((String) NodeType.getValue()) {
      case "Parking":
        type = "PARK";
        break;
      case "Elevator":
        type = "ELEV";
        break;
      case "Restroom":
        type = "REST";
        break;
      case "Stairs":
        type = "STAI";
        break;
      case "Department":
        type = "DEPT";
        break;
      case "Laboratory":
        type = "LABS";
        break;
      case "Information":
        type = "INFO";
        break;
      case "Conference":
        type = "CONF";
        break;
      case "Exit":
        type = "EXIT";
        break;
      case "Retail":
        type = "RETL";
        break;
      case "Service":
        type = "SERV";
        break;
      default:
        break;
    }
    New.setNodeType(type);

    New.setLongName(longName.getText());
    New.setShortName(shortName.getText());
    return New;
  }

  @FXML
  private void exitpopup() {
    mapController.popup.hide();
  }

  private ImageView editMarker(Node N) {
    ImageView Marker = new ImageView();
    switch (N.getNodeType()) {
      case "PARK":
        Marker.setImage(MapController.PARK);
        break;
      case "ELEV":
        Marker.setImage(MapController.ELEV);
        break;
      case "REST":
        Marker.setImage(MapController.REST);
        break;
      case "STAI":
        Marker.setImage(MapController.STAI);
        break;
      case "DEPT":
        Marker.setImage(MapController.DEPT);
        break;
      case "LABS":
        Marker.setImage(MapController.LABS);
        break;
      case "INFO":
        Marker.setImage(MapController.INFO);
        break;
      case "CONF":
        Marker.setImage(MapController.CONF);
        break;
      case "EXIT":
        Marker.setImage(MapController.EXIT);
        break;
      case "RETL":
        Marker.setImage(MapController.RETL);
        break;
      case "SERV":
        Marker.setImage(MapController.SERV);
        break;
      default:
        break;
    }
    Marker.setFitWidth(MapController.nodeNormalWidth);
    Marker.setFitHeight(MapController.nodeNormalHeight);
    Marker.setX(X - MapController.nodeNormalWidth / 2);
    Marker.setY(Y - MapController.nodeNormalHeight);
    return Marker;
  }

  public void setTheNode(NodeUI N) {
    theNode = N;
  }

  public void setMapController(MapController mapController) {
    this.mapController = mapController;
  }

  @FXML
  private void Cancel() {
    exitPopup();
  }

  private void exitPopup() {
    App.getPrimaryStage().getScene().getRoot().setEffect(null);
  }
}
