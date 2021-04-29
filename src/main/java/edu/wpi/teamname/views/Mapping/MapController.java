package edu.wpi.teamname.views.Mapping;

import edu.wpi.teamname.Astar.*;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.Access.AdminAccessible;
import java.awt.*;
import java.sql.*;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class MapController implements AdminAccessible {

  private RoomGraph initialData = new RoomGraph(GlobalDb.getConnection());
  private LinkedList<NodeUI> NODES = new LinkedList<>();
  private LinkedList<EdgeUI> EDGES = new LinkedList<>();
  private Image I = new Image("Images/274px-Google_Maps_pin.svg.png");
  private Image F1 = new Image("01_thefirstfloor.png");
  @FXML private AnchorPane mainAnchor;
  @FXML private ScrollPane movingMap;
  @FXML private AnchorPane secondaryAnchor;
  @FXML private ImageView TheMap;

  @FXML
  private void initialize() {
    initializeFloor(F1, "1");
  }

  // _______________________________________SET UP________________________________________

  private void initializeFloor(Image floor, String floorNum) {
    TheMap.setImage(floor);
    movingMap.setPannable(true);
    movingMap.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    movingMap.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    initalizeMap(floorNum);
  }

  private void initalizeMap(String Floor) {
    initializeNodes();
    initializeEdges();

    for (EdgeUI EUI : EDGES) {
      if (initialData.getNodeByID(EUI.getE().getStartNode()).getFloor().equals(Floor)
          || initialData.getNodeByID(EUI.getE().getEndNode()).getFloor().equals(Floor)) {
        addEdgeUI(EUI);
      }
    }
    for (NodeUI NUI : NODES) {
      System.out.println(NUI.getN().getFloor().equals(Floor));
      if (NUI.getN().getFloor().equals(Floor)) {
        addNodeUI(NUI);
      }
    }
  }

  private void initializeNodes() {
    double markerX = 15;
    double markerY = 26.25;

    for (Node N: initialData.getGraphInfo()) {
      ImageView Marker = new ImageView(I);
      Marker.setFitWidth(markerX);
      Marker.setFitHeight(markerY);
      Marker.setX(N.getXCoord() - markerX / 2);
      Marker.setY(N.getYCoord() - markerY);
      Marker.setOnMouseClicked(
          (MouseEvent e) -> {
            disableListener(e);
          }); // TODO ACTION
      NodeUI Temp = new NodeUI(N, Marker);
      NODES.add(Temp);
    }
  }

  private void initializeEdges() {
    for (Edge E : initialData.getListOfEdges()) {
      Line L =
          new Line(
              initialData.getNodeByID(E.getStartNode()).getSimpXcoord(),
              initialData.getNodeByID(E.getStartNode()).getSimpYcoord(),
              initialData.getNodeByID(E.getEndNode()).getSimpXcoord(),
              initialData.getNodeByID(E.getEndNode()).getSimpYcoord());
      L.setOnMouseClicked(
          (MouseEvent e) -> {
            disableListener(e);
          }); // TODO ACTION
      L.setStrokeWidth(3.0);
      L.setStroke(Color.BLACK);
      EdgeUI Temp = new EdgeUI(E, L);
      EDGES.add(Temp);
    }
  }

  private void addNodeUI(NodeUI NUI) {
    secondaryAnchor.getChildren().add(NUI.getI());
  }

  private void addEdgeUI(EdgeUI EUI) {
    secondaryAnchor.getChildren().add(EUI.getL());
  }

  private void disableListener(MouseEvent e) {}

  // _______________________________________EDITOR FEATURES________________________________________

  private void deleteNode (){

  }

  private void deleteEdge (){

  }








}
