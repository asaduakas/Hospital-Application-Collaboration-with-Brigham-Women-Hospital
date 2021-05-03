package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Astar.*;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.Access.AllAccessible;
import edu.wpi.teamname.views.Mapping.Popup.Edit.AddNodeController;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;

public class MapController implements AllAccessible {

  private RoomGraph initialData = new RoomGraph(GlobalDb.getConnection());
  public static LinkedList<NodeUI> NODES = new LinkedList<>();
  private static LinkedList<EdgeUI> EDGES = new LinkedList<>();
  private PathAlgoPicker algorithm = new PathAlgoPicker(new aStar());
  private LinkedList<Edge> thePath = new LinkedList<Edge>();
  private LinkedList<Node> Targets = new LinkedList<Node>();
  public static final double nodeNormalHeight = 30;
  public static final double nodeNormalWidth = 30;
  private String userCategory = "admin";
  public static String currentFloor = "1";
  private boolean isEditor;

  private final Image F1 = new Image("01_thefirstfloor.png");
  public static final Image DEFAULT = new Image("Images/274px-Google_Maps_pin.svg.png");
  public static final Image PARK = new Image("Images/parkingpin.png");
  public static final Image ELEV = new Image("Images/elevatorpin.png");
  public static final Image REST = new Image("Images/restroompins.png");
  public static final Image STAI = new Image("Images/stairspin.png");
  public static final Image DEPT = new Image("Images/deptpins.png");
  public static final Image LABS = new Image("Images/labspin.png");
  public static final Image INFO = new Image("Images/infopin.png");
  public static final Image CONF = new Image("Images/conferencepin.png");
  public static final Image EXIT = new Image("Images/exitpin.png");
  public static final Image RETL = new Image("Images/retailpin.png");
  public static final Image SERV = new Image("Images/service.png");

  @FXML private JFXToggleButton toggleEditor;
  @FXML private AnchorPane mainAnchor;
  @FXML private JFXComboBox FloorOption;
  @FXML private ScrollPane movingMap;
  @FXML private AnchorPane secondaryAnchor;
  @FXML private ImageView TheMap;
  @FXML public static Popup popup;
  @FXML private JFXHamburger mapHam;
  @FXML private JFXDrawer mapDrawer;

  @FXML
  private void initialize() {

    TheMap.setImage(F1);
    initializeNodes();
    initializeEdges();

    drawNodeFloor("1");

    movingMap.setPannable(true);
    movingMap.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    movingMap.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    ToggleListener();
    nodeAddListener();
    cancelListener();
    getNodesToAlignListener();

    mapDrawer.setPickOnBounds(false);

    try {
      AnchorPane menuBtns =
          FXMLLoader.load(getClass().getClassLoader().getResource("MapDrawerView.fxml"));
      mapDrawer.setSidePane(menuBtns);
    } catch (IOException e) {
      e.printStackTrace();
    }

    HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(mapHam);
    burgerTask.setRate(-1);
    mapHam.addEventHandler(
        MouseEvent.MOUSE_PRESSED,
        (e) -> {
          burgerTask.setRate(burgerTask.getRate() * -1);
          burgerTask.play();
          if (mapDrawer.isOpened()) {
            mapDrawer.close();
            TheMap.setLayoutX(0);
          } else {
            mapDrawer.open();
            TheMap.setLayoutX(263);
          }
        });
  }

  // _______________________________________SET UP________________________________________

  private void LoadMap(Image floor, String floorNum) {
    TheMap.setImage(floor);
    LoadingNodesEdges(floorNum);
  }

  private void LoadingNodesEdges(String Floor) {
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

    for (Node N : initialData.getGraphInfo()) {
      ImageView Marker = new ImageView();
      Marker.setFitWidth(nodeNormalWidth);
      Marker.setFitHeight(nodeNormalHeight);
      Marker.setX(N.getXCoord() - nodeNormalWidth / 2);
      Marker.setY(N.getYCoord() - nodeNormalHeight);

      switch (N.getNodeType()) {
        case "PARK":
          Marker.setImage(PARK);
          break;
        case "ELEV":
          Marker.setImage(ELEV);
          break;
        case "REST":
          Marker.setImage(REST);
          break;
        case "STAI":
          Marker.setImage(STAI);
          break;
        case "DEPT":
          Marker.setImage(DEPT);
          break;
        case "LABS":
          Marker.setImage(LABS);
          break;
        case "INFO":
          Marker.setImage(INFO);
          break;
        case "CONF":
          Marker.setImage(CONF);
          break;
        case "EXIT":
          Marker.setImage(EXIT);
          break;
        case "RETL":
          Marker.setImage(RETL);
          break;
        case "SERV":
          Marker.setImage(SERV);
          break;
        default:
          Marker.setImage(DEFAULT);
          break;
      }

      Marker.setOnMouseClicked(
          (MouseEvent e) -> {
            disableListener(e);
          }); // TODO ACTION

      NodeUI Temp = new NodeUI(N, Marker, nodeNormalWidth, nodeNormalHeight);
      pathListener(Temp);
      hoverResize(Temp);
      deleteNodeListener(Temp);
      setupDraggableNodeUI(Temp);
      NODES.add(Temp);
    }
  }

  private void initializeEdges() {

    for (Edge E : initialData.getListOfEdges()) {
      Line L = new Line();
      L.startXProperty().bind(getNodeUIByID(E.getStartNodeID()).simpXcoordProperty());
      L.startYProperty().bind(getNodeUIByID(E.getStartNodeID()).simpYcoordProperty());
      L.endXProperty().bind(getNodeUIByID(E.getEndNodeID()).simpXcoordProperty());
      L.endYProperty().bind(getNodeUIByID(E.getEndNodeID()).simpYcoordProperty());

      L.setOnMouseClicked(
          (MouseEvent e) -> {
            disableListener(e);
          }); // TODO ACTION
      L.setStrokeWidth(3.0);
      L.setStroke(Color.BLACK);
      EdgeUI Temp = new EdgeUI(E, L);
      EDGES.add(Temp);
      deleteEdgeListener(Temp);
    }
  }

  // _______________________________________Draw________________________________________

  private void addNodeUI(NodeUI NUI) {
    secondaryAnchor.getChildren().add(NUI.getI());
  }

  private void addEdgeUI(EdgeUI EUI) {
    secondaryAnchor.getChildren().add(EUI.getL());
  }

  private void drawNodeFloor(String Floor) {
    for (NodeUI NUI : NODES) {
      if (NUI.getN().getFloor().equals(Floor)
          && (!NUI.getN().getNodeType().equals("WALK"))
          && (!NUI.getN().getNodeType().equals("HALL"))) {
        addNodeUI(NUI);
      }
    }
  }

  private void drawEdgeFloor(String Floor) {
    for (EdgeUI EUI : EDGES) {
      if (getNodeUIByID(EUI.getE().getStartNodeID()).getN().getFloor().equals(Floor)) {
        addEdgeUI(EUI);
      }
    }
  }

  public void showPath() {
    if (thePath.isEmpty()) {
      System.out.println("No path to show!");
    } else {
      System.out.println("Path Exists!");
      clearEdges(); // for previous paths
      for (Edge E : thePath) {
        if (initialData.getNodeByID(E.getStartNodeID()).getFloor().equals(currentFloor)
            && initialData.getNodeByID(E.getEndNodeID()).getFloor().equals(currentFloor)) {
          EdgeUI EUI = getEdgeUIByID(E.getEdgeID());
          if (!secondaryAnchor.getChildren().contains(EUI.getL())) addEdgeUI(EUI);
        }
      }
    }
  }

  public void resizeNodeUI(NodeUI N, double factor) {
    if ((N.getI().getFitHeight() * factor <= 2 * N.getSizeHeight())
        && (N.getI().getFitHeight() * factor >= N.getSizeHeight())) {
      N.getI().setFitWidth(N.getI().getFitWidth() * factor);
      N.getI().setFitHeight(N.getI().getFitHeight() * factor);
      N.getI().setX(N.getN().getXCoord() - N.getI().getFitWidth() / 2);
      N.getI().setY(N.getN().getYCoord() - N.getI().getFitHeight());
    }
  }

  // ----------------------------------------RESET-----------------------------------------------

  private void disableListener(MouseEvent e) {}

  private void clearMap() {
    secondaryAnchor.getChildren().remove(0, secondaryAnchor.getChildren().size());
    secondaryAnchor.getChildren().add(TheMap);
    resetNodeSizes();
  }

  private void clearEdges() {
    Line line = new Line(); // for comparison
    secondaryAnchor
        .getChildren()
        .removeIf(
            n -> {
              if (n.getClass() == line.getClass()) return true;
              return false;
            });
  }

  private void resetData() {
    Targets.clear();
  }

  private void resetNodeSizes() {
    for (NodeUI N : NODES) {
      N.getI().setFitWidth(N.getSizeWidth());
      N.getI().setFitHeight(N.getSizeHeight());
      N.getI().setX(N.getN().getXCoord() - N.getI().getFitWidth() / 2);
      N.getI().setY(N.getN().getYCoord() - N.getI().getFitHeight());
    }
  }

  // _______________________________________EDITOR FEATURES________________________________________

  private void deleteNode(NodeUI N) {

    LinkedList<EdgeUI> toRemove = new LinkedList<>();

    for (EdgeUI E : EDGES) {
      if (E.getE().getStartNodeID().equals(N.getN().getNodeID())
          || E.getE().getEndNodeID().equals(N.getN().getNodeID())) {
        toRemove.add(E);
      }
    }

    for (EdgeUI E : toRemove) {
      deleteEdge(E);
    }

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

  public void addNode(NodeUI N) {

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
    pathListener(N);
    hoverResize(N);
    setupDraggableNodeUI(N);
    initialData.getGraphInfo().add(N.getN());
    NODES.add(N);
    addNodeUI(N);
    deleteNodeListener(N);
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

  // For Directory
  public void runPathFindingDirectory(String startNode, String targetNode) throws IOException {
    Node start = initialData.getNodeByID(startNode);
    Node target = initialData.getNodeByID(targetNode);

    algorithm.search(initialData, start, target);
    algorithm.printPathTo();
    algorithm.printEdgeTo();
    thePath = algorithm.getShortestPath().getPathEdges();
    getDirections(thePath);
  }

  public void runPathFindingClick() {
    thePath = algorithm.multiSearch(initialData, Targets).getPathEdges();
    if (thePath.isEmpty()) {
      Targets.clear();
    } else {
      showPath();
    }
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
                  clearMap();
                  LoadingNodesEdges("1");
                  isEditor = true;
                } else {
                  clearMap();
                  drawNodeFloor("1");
                  isEditor = false;
                }
              }
            });
  }

  private void nodeAddListener() {
    secondaryAnchor.setOnMousePressed(
        (MouseEvent E) -> {
          if (E.isAltDown() && isEditor) {
            try {
              FXMLLoader temp = loadPopup("MapPopUps/AddNode.fxml");
              AddNodeController popupController = temp.getController();
              popupController.setMapController(this);
              popupController.setNX(E.getX());
              popupController.setNY(E.getY());
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
  }

  private void cancelListener() {
    mainAnchor.setOnKeyPressed(
        (KeyEvent e) -> {
          KeyCode key = e.getCode();
          if (key == KeyCode.ESCAPE) {
            resetData();
            clearMap();
            drawNodeFloor("1");
            nodesToAlign.clear();
            System.out.println("Just cleared");
          }
        });
  }

  private void deleteNodeListener(NodeUI N) {
    N.getI()
        .setOnMousePressed(
            (MouseEvent E) -> {
              if (E.isControlDown() && isEditor) {
                deleteNode(N);
              }
            });
  }

  private void deleteEdgeListener(EdgeUI E) {
    E.getL()
        .setOnMousePressed(
            (MouseEvent e) -> {
              if (e.isControlDown()) {
                deleteEdge(E);
              }
            });
  }

  private void pathListener(NodeUI N) {
    N.getI()
        .addEventHandler(
            MouseEvent.MOUSE_PRESSED,
            (E) -> {
              if (E.getButton() == MouseButton.SECONDARY) {
                Targets.add(N.getN());
                resizeNodeUI(N, 2);
                if (Targets.size() >= 2) {
                  runPathFindingClick();
                }
              }
            });
  }

  LinkedList<Node> nodesToAlign = new LinkedList<Node>();

  private void getNodesToAlignListener() {
    mainAnchor.setOnKeyPressed(
        (KeyEvent e) -> {
          KeyCode key = e.getCode();
          if (key == KeyCode.SHIFT && isEditor) {
            System.out.println("Shift is down");
            cancelListener();

            for (Node N : this.initialData.getGraphInfo()) {
              NodeUI NUI = getNodeUIByID(N.getNodeID());
              NUI.getI()
                  .addEventHandler(
                      MouseEvent.MOUSE_PRESSED,
                      (M) -> {
                        System.out.println("node clicked");
                        nodesToAlign.add(NUI.getN());

                        if (nodesToAlign.size() > 2) {
                          alignNodes();
                        }
                      });
            }
          }
        });
  }

  private void alignNodes() {
    System.out.println("about to align");

    int totalX = 0;
    int totalY = 0;
    int nodeCount = 0;
    int diffX = 0;
    int diffY = 0;
    int totalXDiff = 0;
    int totalYDiff = 0;
    int tempX = nodesToAlign.getFirst().getXCoord();
    int tempY = nodesToAlign.getFirst().getYCoord();

    for (Node node : nodesToAlign) {
      System.out.println(node.getNodeID());
      System.out.println("X: " + node.getXCoord());
      System.out.println("Y: " + node.getYCoord());
      diffX = tempX - node.getXCoord();
      diffY = tempY - node.getYCoord();
      totalXDiff += Math.abs(diffX);
      totalYDiff += Math.abs(diffY);
      totalX += node.getXCoord();
      totalY += node.getYCoord();
      tempX = node.getXCoord();
      tempY = node.getYCoord();
      nodeCount++;
    }

    int avgX = totalX / nodeCount;
    int avgY = totalY / nodeCount;

    if (totalXDiff > totalYDiff) {
      System.out.println("Align horizontally");
      for (Node node : nodesToAlign) {
        NodeUI NUI = getNodeUIByID(node.getNodeID());
        NUI.getI().setX(node.getXCoord() - NUI.getI().getFitWidth() / 2);
        NUI.getI().setY(avgY - NUI.getI().getFitHeight());
        NUI.setNodeCoord(node.getXCoord(), avgY);
        System.out.println("aligned X: " + NUI.getN().getXCoord());
        System.out.println("aligned Y: " + NUI.getN().getYCoord());
      }
    } else {
      System.out.println("Align vertically");
      for (Node node : nodesToAlign) {
        NodeUI NUI = getNodeUIByID(node.getNodeID());
        NUI.getI().setX(avgX - NUI.getI().getFitWidth() / 2);
        NUI.getI().setY(node.getYCoord() - NUI.getI().getFitHeight());
        NUI.setNodeCoord(avgX, node.getYCoord());
        System.out.println("aligned X: " + NUI.getN().getXCoord());
        System.out.println("aligned Y: " + NUI.getN().getYCoord());
      }
    }
  }

  private void hoverResize(NodeUI N) {
    N.getI()
        .setOnMouseEntered(
            (MouseEvent e) -> {
              resizeNodeUI(N, 2);
              //              if (isEditor) {
              //                try {
              //                  //                  FXMLLoader temp =
              // loadPopup("MapPopUps/AddNode.fxml");
              //                  //                  AddNodeController popupController =
              // temp.getController();
              //                  //                  popupController.setMapController(this);
              //                  //                  popupController.setNX(e.getX());
              //                  //                  popupController.setNY(e.getY());
              //                  FXMLLoader temp = loadPopup("MapPopUps/EditNode.fxml");
              //                  EditNodeController editNodeController = temp.getController();
              //                  editNodeController.setMapController(this);
              //                  editNodeController.setTheNode(N);
              //
              //                } catch (IOException ioException) {
              //                  ioException.printStackTrace();
              //                }
              //              }
            });

    N.getI()
        .setOnMouseExited(
            (MouseEvent e) -> {
              if (Targets.isEmpty()) resizeNodeUI(N, .5);
              //              if (isEditor) {
              //                this.popup.hide();
              //              }
            });
  }

  // ___________________________________Getter and Setter_____________________________________
  public static NodeUI getNodeUIByID(String NodeID) {
    for (NodeUI theNode : NODES) {
      if (theNode.getN().getNodeID().equals(NodeID)) {
        return theNode;
      }
    }
    System.out.println("This node doesn't exist.");
    return null;
  }

  private static EdgeUI getEdgeUIByID(String EdgeID) {
    for (EdgeUI theEdge : EDGES) {
      if (theEdge.getE().getEdgeID().equals(EdgeID)) {
        return theEdge;
      } else if (theEdge.getE().getReverseEdgeID().equals(EdgeID)) {
        return theEdge;
      }
    }
    System.out.println("This Edge doesn't exist.");
    return null;
  }

  // _____________________________________Directions__________________________________________

  @FXML Button dirBtn;
  @FXML TextArea dirText;
  private String endLocation = "";

  private void setEnd(String end) {
    endLocation = end;
  }

  private String getEnd() {
    return endLocation;
  }

  @FXML
  public void getDirections(LinkedList<Edge> edges) {

    if (edges.isEmpty()) {
      System.out.println("No Directions to Give!");
      return;
    }
    // ScaleDown(edges.getFirst().getStartNode());

    dirText.setText("");

    Node start = initialData.getNodeByID(edges.getFirst().getStartNodeID());
    Node end = initialData.getNodeByID(edges.getLast().getEndNodeID());
    dirText.appendText(
        "Directions from " + start.getLongName() + " to " + end.getLongName() + ":\n");
    setEnd(end.getShortName());

    // ScaleDown(edges.getFirst().getEndNode());
    String initialDirection =
        firstMove(
            start.getXCoord(), start.getYCoord(), start.getXCoord(), start.getYCoord(), start, end);

    // used to skip first edge
    int skip = 0;
    for (Edge N : edges) {
      if (skip == 0) {
        skip++;
        continue;
      }
      // ScaleDown(N.getEndNode());
      Node startN = initialData.getNodeByID(N.getStartNodeID());
      Node endN = initialData.getNodeByID(N.getEndNodeID());

      String newDirection =
          evalTurn(
              initialDirection,
              startN.getXCoord(),
              startN.getYCoord(),
              endN.getXCoord(),
              endN.getYCoord(),
              startN,
              endN);
      initialDirection = newDirection;
    }
    dirText.appendText("\nWelcome to " + end.getLongName() + "\n");
  }

  public String evalTurn(
      String currentDirection,
      int startX,
      int startY,
      int endX,
      int endY,
      Node startNode,
      Node endNode) {
    String newDirection = "";
    int deltaX = endX - startX;
    int deltaY = endY - startY;

    System.out.println("start type: " + startNode.getNodeType());
    System.out.println("end type: " + endNode.getNodeType());

    // add handling for changing floors
    if (startNode.getNodeType().equals("ELEV") && endNode.getNodeType().equals("ELEV")) {
      dirText.appendText("Take the elevator towards floor " + endNode.getFloor() + "\n");
      return "In elevator";

    } else if (startNode.getNodeType().equals("ELEV") && !endNode.getNodeType().equals("ELEV")) {
      newDirection = firstMove(startX, startY, endX, endY, startNode, endNode);
      return newDirection;
    } else if (startNode.getNodeType().equals("STAI") && endNode.getNodeType().equals("STAI")) {
      dirText.appendText("Take the stairs towards floor " + endNode.getFloor() + "\n");
      return "In stairs";
    } else if (startNode.getNodeType().equals("STAI") && !endNode.getNodeType().equals("STAI")) {
      newDirection = firstMove(startX, startY, endX, endY, startNode, endNode);
      return newDirection;
    }

    // North
    if ((deltaY < 0) && (Math.abs(deltaY) > Math.abs(deltaX))) {
      newDirection = "North";
    }
    // South
    else if ((deltaY > 0) && (deltaY > Math.abs(deltaX))) {
      newDirection = "South";
    }
    // East
    else if ((deltaX > 0) && (deltaX > Math.abs(deltaY))) {
      newDirection = "East";
    }
    // West
    else if ((deltaX < 0) && (Math.abs(deltaX) > Math.abs(deltaY))) {
      newDirection = "West";
    } else {
      newDirection = "Error";
    }

    // Turn Left
    if ((currentDirection.equals("North") && newDirection.equals("West"))
        || (currentDirection.equals("East") && newDirection.equals("North"))
        || (currentDirection.equals("South") && newDirection.equals("East"))
        || (currentDirection.equals("West") && newDirection.equals("South"))) {
      dirText.appendText("Turn Left towards " + endNode.getLongName() + "\n");

    }
    // Turn Right
    else if ((currentDirection.equals("North") && newDirection.equals("East"))
        || (currentDirection.equals("East") && newDirection.equals("South"))
        || (currentDirection.equals("South") && newDirection.equals("West"))
        || (currentDirection.equals("West") && newDirection.equals("North"))) {
      dirText.appendText("Turn Right towards " + endNode.getLongName() + "\n");
    }
    // Continue Straight
    else if (currentDirection.equals(newDirection)) {
      if (!(startNode.getNodeType().equals("HALL") && endNode.getNodeType().equals("HALL"))) {
        dirText.appendText("Continue Straight towards " + endNode.getLongName() + "\n");
      }
    }

    return newDirection;
  }

  public String firstMove(
      int startX, int startY, int endX, int endY, Node startNode, Node endNode) {
    int deltaX = endX - startX;
    int deltaY = endY - startY;

    // add handling for changing floors
    if (startNode.getNodeType().equals("ELEV") && endNode.getNodeType().equals("ELEV")) {
      dirText.appendText("Take the elevator towards floor " + endNode.getFloor() + "\n");
      return "In elevator";
    } else if (startNode.getNodeType().equals("STAI") && endNode.getNodeType().equals("STAI")) {
      dirText.appendText("Take the stairs towards floor " + endNode.getFloor() + "\n");
      return "In stairs";
    } else {
      // North
      if ((deltaY < 0) && (Math.abs(deltaY) > Math.abs(deltaX))) {
        System.out.println("Head North towards " + endNode.getLongName());
        dirText.appendText("Head North towards " + endNode.getLongName() + "\n");
        return "North";
      }
      // South
      else if ((deltaY > 0) && (deltaY > Math.abs(deltaX))) {
        System.out.println("Head South towards " + endNode.getLongName());
        dirText.appendText("Head South towards " + endNode.getLongName() + "\n");
        return "South";
      }
      // East
      else if ((deltaX > 0) && (deltaX > Math.abs(deltaY))) {
        System.out.println("Head East towards " + endNode.getLongName());
        dirText.appendText("Head East towards " + endNode.getLongName() + "\n");
        return "East";
      }
      // West
      else if ((deltaX < 0) && (Math.abs(deltaX) > Math.abs(deltaY))) {
        System.out.println("Head West towards " + endNode.getLongName());
        dirText.appendText("Head West towards " + endNode.getLongName() + "\n");
        return "West";
      } else {
        System.out.println("Error determining turn direction towards " + endNode.getLongName());
        return "Direction Error";
      }
    }
  }

  @FXML
  public void downloadDirections(ActionEvent event) {
    if (event.getSource() == dirBtn) {

      String name = getEnd().replaceAll(" ", "") + "Directions.txt";

      try {
        FileWriter directions = new FileWriter(name);

        directions.write(dirText.getText());
        directions.close();

        String DialogText = "";
        if (getEnd().equals("")) {
          DialogText = "Select a Start and End Before Downloading Directions";
        } else {
          DialogText = "Your Directions Have Been Downloaded";
        }
        Text header = new Text(DialogText);
        header.setFont(Font.font("System", FontWeight.BOLD, 18));

        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(header);
        layout.setBody(new Text(""));

        StackPane downloadedStackPane = new StackPane();
        mainAnchor.getChildren().add(downloadedStackPane);
        StackPane.setAlignment(downloadedStackPane, Pos.TOP_RIGHT);
        downloadedStackPane.setLayoutY(245);
        downloadedStackPane.setLayoutX(300);
        JFXDialog submitDia =
            new JFXDialog(downloadedStackPane, layout, JFXDialog.DialogTransition.CENTER);

        JFXButton downloadedBtn = new JFXButton("Close");
        downloadedBtn.setPrefHeight(60);
        downloadedBtn.setPrefWidth(120);
        downloadedBtn.setId("downloadedBtn");
        downloadedBtn.setButtonType(JFXButton.ButtonType.FLAT);
        downloadedBtn.setStyle("-fx-background-color: #cdcdcd;");

        downloadedBtn.setOnAction(
            new EventHandler<ActionEvent>() {
              @Override
              public void handle(ActionEvent event) {
                submitDia.close();
              }
            });

        layout.setActions(downloadedBtn);
        submitDia.show();

      } catch (IOException e) {
        System.out.println("Unable to write to directions output file");
      }
    }
  }
  // ______________________________________Popups_____________________________________________

  private FXMLLoader loadPopup(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxml));
    Pane root = (Pane) fxmlLoader.load();

    setupDraggablePopUp(root);

    this.popup = new Popup();
    this.popup.getContent().addAll(root);
    popup.show(App.getPrimaryStage());

    return fxmlLoader;
  }

  public void setupDraggablePopUp(Pane container) {
    ObjectProperty<Point2D> mouseLocation = new SimpleObjectProperty<>();

    container.setOnMousePressed(
        event -> mouseLocation.set(new Point2D(event.getScreenX(), event.getScreenY())));

    container.setOnMouseDragged(
        event -> {
          if (mouseLocation.get() != null) {
            double x = event.getScreenX();
            double deltaX = x - mouseLocation.get().getX();
            double y = event.getScreenY();
            double deltaY = y - mouseLocation.get().getY();
            // in case of 2 or more computer screens this help me to avoid get stuck on 1 screen
            if (Math.abs(popup.getX() - x) > popup.getWidth()) {
              popup.setX(x);
              popup.setY(y);
            } else {
              popup.setX(popup.getX() + deltaX);
              popup.setY(popup.getY() + deltaY);
            }
            mouseLocation.set(new Point2D(x, y));
          }
        });
  }

  public void setupDraggableNodeUI(NodeUI NUI) {

    NUI.getI()
        .setOnMouseDragged(
            event -> {
              if (isEditor) {
                movingMap.setPannable(false);
                Double x = event.getX();
                Double y = event.getY();
                NUI.getI().setX(x - NUI.getI().getFitWidth() / 2);
                NUI.getI().setY(y - NUI.getI().getFitHeight());
                NUI.setNodeCoord(x.intValue(), y.intValue());
                resizeNodeUI(NUI, 2);
              }
            });

    NUI.getI()
        .setOnMouseReleased(
            event -> {
              if (isEditor) {
                Double x = event.getX();
                Double y = event.getY();
                GlobalDb.getTables()
                    .getNodeTable()
                    .updateNodeXCoord(
                        GlobalDb.getConnection(), NUI.getN().getNodeID(), x.intValue());
                GlobalDb.getTables()
                    .getNodeTable()
                    .updateNodeYCoord(
                        GlobalDb.getConnection(), NUI.getN().getNodeID(), y.intValue());
                movingMap.setPannable(true);
                resizeNodeUI(NUI, .5);
              }
            });
  }
}
