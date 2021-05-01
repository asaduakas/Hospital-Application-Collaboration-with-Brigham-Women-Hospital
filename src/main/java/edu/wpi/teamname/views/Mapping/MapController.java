package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.teamname.Astar.*;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.Access.AdminAccessible;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
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
  private PathAlgoPicker algorithm = new PathAlgoPicker(new aStar());
  private LinkedList<Edge> thePath = new LinkedList<Edge>();
  private String userCategory = "admin";

  private Image I = new Image("Images/274px-Google_Maps_pin.svg.png");
  private Image Exit = new Image("Images/exit.png");
  private Image F1 = new Image("01_thefirstfloor.png");

  @FXML private JFXToggleButton toggleEditor;
  @FXML private AnchorPane mainAnchor;
  @FXML private JFXComboBox FloorOption;
  @FXML private ScrollPane movingMap;
  @FXML private AnchorPane secondaryAnchor;
  @FXML private ImageView TheMap;

  @FXML
  private void initialize() {
    TheMap.setImage(F1);
    movingMap.setPannable(true);
    movingMap.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    movingMap.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    ToggleListener();
  }

  // _______________________________________SET UP________________________________________

  private void LoadMap(Image floor, String floorNum) {
    TheMap.setImage(floor);
    LoadingNodesEdges(floorNum);
  }

  private void LoadingNodesEdges(String Floor) {
    initializeNodes();
    initializeEdges();

    for (EdgeUI EUI : EDGES) {
      if (initialData.getNodeByID(EUI.getE().getStartNodeID()).getFloor().equals(Floor)
          || initialData.getNodeByID(EUI.getE().getEndNodeID()).getFloor().equals(Floor)) {
        addEdgeUI(EUI);
      }
    }
    for (NodeUI NUI : NODES) {
      if (NUI.getN().getFloor().equals(Floor)) {
        addNodeUI(NUI);
      }
    }
  }

  private void initializeNodes() {
    double markerX = 15;
    double markerY = 26.25;

    for (Node N : initialData.getGraphInfo()) {
      ImageView Marker = new ImageView(I);
      Marker.setFitWidth(markerX);
      Marker.setFitHeight(markerY);
      Marker.setX(N.getXCoord() - markerX / 2);
      Marker.setY(N.getYCoord() - markerY);

      switch (N.getNodeType()) {
        case "REST":
          Marker.setImage(null);
      }

      Marker.setOnMouseClicked(
          (MouseEvent e) -> {
            disableListener(e);
          }); // TODO ACTION

      NodeUI Temp = new NodeUI(N, Marker);
      pathListener(Temp);
      hoverResize(Temp);
      NODES.add(Temp);
    }
  }

  private void initializeEdges() {
    for (Edge E : initialData.getListOfEdges()) {
      Line L =
          new Line(
              initialData.getNodeByID(E.getStartNodeID()).getSimpXcoord(),
              initialData.getNodeByID(E.getStartNodeID()).getSimpYcoord(),
              initialData.getNodeByID(E.getEndNodeID()).getSimpXcoord(),
              initialData.getNodeByID(E.getEndNodeID()).getSimpYcoord());
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

  private void deleteNode(NodeUI N) {
    GlobalDb.getTables()
        .getNodeTable()
        .deleteEntity(GlobalDb.getConnection(), "Nodes", N.getN().getNodeID());
    secondaryAnchor.getChildren().remove(N.getI());
    NODES.remove(N);
  }

  private void deleteEdge(EdgeUI E) {
    GlobalDb.getTables()
        .getEdgeTable()
        .deleteEntity(GlobalDb.getConnection(), "Tables", E.getE().getEdgeID());
    secondaryAnchor.getChildren().remove(E.getL());
    EDGES.remove(E);
  }

  private void addNode(NodeUI N) {
    GlobalDb.getTables()
        .getNodeTable()
        .addEntity(
            GlobalDb.getConnection(),
            N.getN().getNodeID(),
            N.getN().getXCoord(),
            N.getN().getYCoord(),
            N.getN().getFloor(),
            N.getN().getBuilding(),
            N.getN().getNodeType(),
            N.getN().getLongName(),
            N.getN().getShortName(),
            0);
    addNodeUI(N);
    NODES.add(N);
  }

  private void addEdge(EdgeUI E) {
    GlobalDb.getTables()
        .getEdgeTable()
        .addEntity(
            GlobalDb.getConnection(),
            E.getE().getEdgeID(),
            E.getE().getStartNodeID(),
            E.getE().getEndNodeID());
    addEdgeUI(E);
    EDGES.add(E);
  }

  private void editNode() {} // TODO Implement edit nodes

  private void editEdge() {} // TODO Implement edit edges

  // _______________________________________Path Finding____________________________________________

  public void runPathFinding(String startNode, String targetNode) throws IOException {
    Node start = initialData.getNodeByID(startNode);
    Node target = initialData.getNodeByID(targetNode);

    algorithm.search(initialData, start, target);
    algorithm.printPathTo();
    algorithm.printEdgeTo();
    thePath = algorithm.getShortestPath().getPathEdges();
    // getDirections(thePath);
  }

  // _______________________________________Event Handeler_________________________________________

  private void ToggleListener() {
    toggleEditor
        .selectedProperty()
        .addListener(
            new ChangeListener<Boolean>() {
              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observable,
                  Boolean oldValue,
                  Boolean newValue) {

                if (toggleEditor.isSelected()) {
                  LoadingNodesEdges("1");
                } else {
                  secondaryAnchor.getChildren().remove(0, secondaryAnchor.getChildren().size());
                  secondaryAnchor.getChildren().add(TheMap);
                }
              }
            });
  }

  private void pathListener(NodeUI N) {
    N.getI()
        .setOnMouseClicked(
            (MouseEvent E) -> {
              if (E.isSecondaryButtonDown()) {
                String StartNode = N.getN().getNodeID();
              }
            });
  }

  private void hoverResize(NodeUI N) {
    N.getI()
      .setOnMouseEntered(
          (MouseEvent e) -> {
            N.getI().setFitWidth(N.getI().getFitWidth() * 2);
            N.getI().setFitHeight(N.getI().getFitHeight() * 2);
            N.getI().setX(N.getN().getXCoord() - N.getI().getFitWidth() / 2);
            N.getI().setY(N.getN().getYCoord() - N.getI().getFitHeight());
          });

    N.getI()
      .setOnMouseExited(
          (MouseEvent e) -> {
            N.getI().setFitWidth(N.getI().getFitWidth() / 2);
            N.getI().setFitHeight(N.getI().getFitHeight() / 2);
            N.getI().setX(N.getN().getXCoord() - N.getI().getFitWidth() / 2);
            N.getI().setY(N.getN().getYCoord() - N.getI().getFitHeight());
          });
  }
}
