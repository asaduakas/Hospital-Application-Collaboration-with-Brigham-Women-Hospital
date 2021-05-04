package edu.wpi.teamname.views.Mapping.Popup.Edit;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamname.Astar.Node;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.Mapping.MapController;
import edu.wpi.teamname.views.Mapping.NodeUI;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class EditNodeController {

  @FXML public JFXComboBox FloorBox;
  @FXML public JFXComboBox NodeType;
  @FXML public JFXTextField building;
  @FXML public JFXTextField longName;
  @FXML public JFXTextField shortName;

  double X;
  double Y;

  private MapController mapController;
  private NodeUI theNode;

  @FXML
  private void initialize() {

    FloorBox.setPromptText("Floor " + MapController.currentFloor);
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
  }

  @FXML
  private void editNodeUI() {
    String building = theNode.getN().getBuilding();
    String floor = theNode.getN().getFloor();
    String type = theNode.getN().getNodeType();
    String shortName = theNode.getN().getShortName();
    String longName = theNode.getN().getLongName();

    if (NodeType.getValue() != null) type = (String) NodeType.getValue();
    if (this.building.getText() != null) building = this.building.getText();
    if (FloorBox.getValue() != null) floor = (String) FloorBox.getValue();
    if (this.shortName != null) shortName = this.shortName.getText();
    if (this.longName != null) longName = this.longName.getText();

    String nID = theNode.getN().getNodeID();
    FDatabaseTables.getNodeTable().updateNodeBuilding(GlobalDb.getConnection(), nID, building);
    FDatabaseTables.getNodeTable().updateNodeFloor(GlobalDb.getConnection(), nID, floor);
    FDatabaseTables.getNodeTable().updateNodeType(GlobalDb.getConnection(), nID, type);
    FDatabaseTables.getNodeTable().updateNodeShortName(GlobalDb.getConnection(), nID, shortName);
    FDatabaseTables.getNodeTable().updateNodeLongName(GlobalDb.getConnection(), nID, longName);

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
        type = (String) NodeType.getValue();
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

  private ImageView buildMarker(Node N) {
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

  public void setNX(double x) {
    X = x;
  }

  public void setNY(double y) {
    Y = y;
  }

  public void setTheNode(NodeUI N) {
    theNode = N;
    String type = "";
    switch (N.getN().getNodeType()) {
      case "PARK":
        type = "Parking";
        break;
      case "ELEV":
        type = "Elevator";
        break;
      case "REST":
        type = "Restroom";
        break;
      case "STAI":
        type = "Stairs";
        break;
      case "DEPT":
        type = "Department";
        break;
      case "LABS":
        type = "Laboratory";
        break;
      case "INFO":
        type = "Information";
        break;
      case "CONF":
        type = "Conference";
        break;
      case "EXIT":
        type = "Exit";
        break;
      case "RETL":
        type = "Retail";
        break;
      case "SERV":
        type = "Service";
        break;
      default:
        type = N.getN().getNodeType();
        break;
    }
    NodeType.setPromptText(type);

    building.setPromptText(N.getN().getBuilding());
    shortName.setPromptText(N.getN().getShortName());
    longName.setPromptText(N.getN().getLongName());
  }

  public void setMapController(MapController mapController) {
    this.mapController = mapController;
  }
}
