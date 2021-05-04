package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Astar.*;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.Access.AllAccessible;
import edu.wpi.teamname.views.Mapping.Popup.Edit.AddNodeController;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javafx.animation.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
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
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.util.Duration;

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
  private final Image F2 = new Image("02_thesecondfloor.png");
  private final Image F3 = new Image("03_thethirdfloor.png");
  private final Image FL1 = new Image("00_thelowerlevel1.png");
  private final Image FL2 = new Image("00_thelowerlevel2.png");
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
  private Image up = new Image("Images/up-arrow.png");
  private Image down = new Image("Images/redArrow.png");
  private Image endImage = new Image("Images/endingIcon_white.png");
  private Image startImage = new Image("Images/walkingStartIcon_black.png");

  @FXML private AnchorPane mainAnchor;
  @FXML private JFXComboBox FloorOption;
  private MapScrollPane mapScrollPane;
  private AnchorPane secondaryAnchor;
  @FXML private ImageView TheMap;
  @FXML public static Popup popup;
  @FXML private JFXHamburger mapHam;
  @FXML private JFXDrawer mapDrawer;
  @FXML private JFXToggleButton toggleEditor;
  @FXML private StackPane stackPane;
  private SimpleStringProperty simpleFloor = new SimpleStringProperty("Floor " + currentFloor);
  private JFXButton ChooseFloorBtn = new JFXButton("Floor 1");

  @FXML
  private void initialize() {

    mapScrollPane = new MapScrollPane(F1);
    mainAnchor.getChildren().add(mapScrollPane);
    AnchorPane.setTopAnchor(mapScrollPane, 0.0);
    AnchorPane.setBottomAnchor(mapScrollPane, 0.0);
    AnchorPane.setLeftAnchor(mapScrollPane, 0.0);
    AnchorPane.setRightAnchor(mapScrollPane, 0.0);
    secondaryAnchor = mapScrollPane.mapAnchor;
    mapScrollPane.toBack();

    initializeNodes();
    initializeEdges();

    drawNodeFloor("1");

    // mapScrollPane.setPannable(true);
    // mapScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    // mapScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    ToggleListener();
    nodeAddListener();
    cancelListener();
    getNodesToAlignListener();
    recordParkingListener();

    mapDrawer.setPickOnBounds(false);

    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getClassLoader().getResource("MapDrawerView.fxml"));
      AnchorPane menuBtns = loader.load();
      MapDrawerController drawer = loader.getController();
      drawer.setMapController(this);
      mapDrawer.setSidePane(menuBtns);
    } catch (IOException e) {
      e.printStackTrace();
    }

    initializeFloorList();

    HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(mapHam);
    burgerTask.setRate(-1);
    mapHam.addEventHandler(
        MouseEvent.MOUSE_PRESSED,
        (e) -> {
          burgerTask.setRate(burgerTask.getRate() * -1);
          burgerTask.play();
          if (mapDrawer.isOpened()) {
            mapDrawer.close();
            mapDrawer.setLayoutX(-132);
          } else {
            mapDrawer.open();
            mapDrawer.setLayoutX(0);
          }
        });
  }

  // _______________________________________SET UP________________________________________

  private void LoadMap(Image floor, String floorNum) {
    mapScrollPane.setMapImage(floor);
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

  private void initializeFloorList() {
    ChooseFloorBtn.setButtonType(JFXButton.ButtonType.RAISED);
    ChooseFloorBtn.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 20px");

    JFXButton Floor1Btn = new JFXButton("Floor 1");
    Floor1Btn.setButtonType(JFXButton.ButtonType.RAISED);
    Floor1Btn.setOnAction(
        (e) -> {
          switchFloor("1");
        });

    JFXButton Floor2Btn = new JFXButton("Floor 2");
    Floor2Btn.setButtonType(JFXButton.ButtonType.RAISED);
    Floor2Btn.setOnAction(
        (e) -> {
          switchFloor("2");
        });

    JFXButton Floor3Btn = new JFXButton("Floor 3");
    Floor3Btn.setButtonType(JFXButton.ButtonType.RAISED);
    Floor3Btn.setOnAction(
        (e) -> {
          switchFloor("3");
        });

    JFXButton FloorL1Btn = new JFXButton("Floor L1");
    FloorL1Btn.setButtonType(JFXButton.ButtonType.RAISED);
    FloorL1Btn.setOnAction(
        (e) -> {
          switchFloor("L1");
        });

    JFXButton FloorL2Btn = new JFXButton("Floor L2");
    FloorL2Btn.setButtonType(JFXButton.ButtonType.RAISED);
    FloorL2Btn.setOnAction(
        (e) -> {
          switchFloor("L2");
        });

    JFXNodesList nodeList = new JFXNodesList();
    nodeList.addAnimatedNode(ChooseFloorBtn);
    nodeList.addAnimatedNode(FloorL2Btn);
    nodeList.addAnimatedNode(FloorL1Btn);
    nodeList.addAnimatedNode(Floor1Btn);
    nodeList.addAnimatedNode(Floor2Btn);
    nodeList.addAnimatedNode(Floor3Btn);
    nodeList.setSpacing(20d);
    nodeList.setLayoutX(280);
    nodeList.setLayoutY(10);

    mainAnchor.getChildren().add(nodeList);
  }

  // _______________________________________Draw________________________________________

  public void addNodeUI(NodeUI NUI) {
    if (secondaryAnchor.getChildren().contains(NUI.getI())) return;
    secondaryAnchor.getChildren().add(NUI.getI());
  }

  private void addEdgeUI(EdgeUI EUI) {
    if (secondaryAnchor.getChildren().contains(EUI.getL())) return;
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

  public void clearMap() {
    secondaryAnchor.getChildren().removeIf(n -> !n.equals(mapScrollPane.getMapImage()));
    resetNodeSizes();
  }

  public void switchFloor(String floor) {
    clearMap();
    switch (floor) {
      case "2":
        currentFloor = "2";
        mapScrollPane.setMapImage(F2);
        break;
      case "3":
        currentFloor = "3";
        mapScrollPane.setMapImage(F3);
        break;
      case "L2":
        currentFloor = "L2";
        mapScrollPane.setMapImage(FL2);
        break;
      case "L1":
        currentFloor = "L1";
        mapScrollPane.setMapImage(FL1);
        break;
      default:
        currentFloor = "1";
        mapScrollPane.setMapImage(F1);
        break;
    }
    drawNodeFloor(floor);
    ChooseFloorBtn.setText("Floor " + currentFloor);
    if (isEditor) {
      drawEdgeFloor(floor);
    } else {
      showPath();
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
      // Making different icons for starting and ending nodes

      NodeUI startNode = getNodeUIByID(thePath.get(0).getStartNodeID());
      ImageView startPin = new ImageView(startImage);
      startPin.setFitWidth(45);
      startPin.setFitHeight(45);
      startPin.setX(startNode.getSimpXcoord() - 22.5);
      startPin.setY(startNode.getSimpYcoord() - 22.5);

      NodeUI endNode = getNodeUIByID(thePath.get(thePath.size() - 1).getEndNodeID());
      ImageView endPin = new ImageView(endImage);
      endPin.setFitWidth(30);
      endPin.setFitHeight(30);
      endPin.setX(endNode.getSimpXcoord() - 15);
      endPin.setY(endNode.getSimpYcoord() - 15);

      if (startNode.getN().getFloor().equals(currentFloor)) {
        secondaryAnchor.getChildren().add(startPin);
      }
      if (endNode.getN().getFloor().equals(currentFloor)) {
        secondaryAnchor.getChildren().add(endPin);
        scaleAnimation(
            secondaryAnchor.getChildren().get(secondaryAnchor.getChildren().indexOf(endPin)));
      }
      animateEdges();
      animateElevators();
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
      algorithm.multiSearch(initialData, Targets).printPathEdges();
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
                  LoadingNodesEdges(currentFloor);
                  isEditor = true;
                } else {
                  clearMap();
                  drawNodeFloor(currentFloor);
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
    mapScrollPane.setOnKeyPressed(
        (KeyEvent e) -> {
          KeyCode key = e.getCode();
          if (key == KeyCode.ESCAPE) {
            resetData();
            clearMap();
            drawNodeFloor("1");
            nodesToAlign.clear();
            System.out.println("Just cleared");
          }
          if (!isEditor) return;
          switch (key) {
            case R:
              resetCSV();
              break;
            case I:
              importCSV();
              break;
            case E:
              exportCSV();
              break;
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

  private void recordParkingListener() {

    mainAnchor.setOnKeyPressed(
        (KeyEvent e) -> {
          KeyCode key = e.getCode();
          int saveMode = 1;
          if (key == KeyCode.S && !isEditor) {
            System.out.println("S is down");
            if (saveMode == 0) {
              saveMode += 1;
            } else if (saveMode == 1) {
              saveMode -= 1;
              for (Node N : this.initialData.getGraphInfo()) {
                if (N.getNodeType().equals("PARK")) {
                  NodeUI NUI = getNodeUIByID(N.getNodeID());
                  NUI.getI()
                      .addEventHandler(
                          MouseEvent.MOUSE_PRESSED,
                          (M) -> {
                            System.out.println("node clicked");
                            saveParkingSpot(NUI);
                          });
                }
              }
            }
          }
        });
  }

  private void saveParkingSpot(NodeUI N) {
    System.out.println("Saving spot");
    System.out.println(N.getSizeHeight());
    System.out.println(N.getSizeWidth());

    if (N.getSizeHeight() > 30) {
      N.setSizeHeight(N.getSizeHeight() - 60);
      N.setSizeWidth(N.getSizeWidth() - 60);
    } else {
      N.setSizeHeight(N.getSizeHeight() + 60);
      N.setSizeWidth(N.getSizeWidth() + 60);
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

  // ---------------------------------------Animation------------------------------------------

  @FXML
  private void animateEdges() {
    for (Edge e : thePath) {
      EdgeUI edgeUi = getEdgeUIByID(e.getEdgeID());
      Line line = edgeUi.getL();

      line.getStrokeDashArray().setAll(25d, 10d);
      line.setStrokeWidth(2);

      final double maxOffset = line.getStrokeDashArray().stream().reduce(0d, (a, b) -> a + b);

      Timeline timeline;
      System.out.println(edgeUi.getE().getStartNodeID());
      System.out.println(e.getStartNodeID().equals(edgeUi.getE().getStartNodeID()));
      if (e.getStartNodeID().equals(edgeUi.getE().getStartNodeID())) {
        timeline =
            new Timeline(
                new KeyFrame(
                    Duration.seconds(2),
                    new KeyValue(line.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)),
                new KeyFrame(
                    Duration.ZERO,
                    new KeyValue(line.strokeDashOffsetProperty(), maxOffset, Interpolator.LINEAR)));
      } else {
        timeline =
            new Timeline(
                new KeyFrame(
                    Duration.seconds(2),
                    new KeyValue(line.strokeDashOffsetProperty(), maxOffset, Interpolator.LINEAR)),
                new KeyFrame(
                    Duration.ZERO,
                    new KeyValue(line.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)));
      }

      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.play();
    }
  }

  private void goUp() {
    String temp = null;
    switch (currentFloor) {
      case "L2":
        temp = "L1";
        break;
      case "L1":
        temp = "1";
        break;
      case "1":
        temp = "2";
        break;
      case "2":
        temp = "3";
        break;
      default:
        temp = "1";
        break;
    }
    switchFloor(temp);
  }

  private void goDown() {
    String temp = null;
    switch (currentFloor) {
      case "3":
        temp = "2";
        break;
      case "2":
        temp = "1";
        break;
      case "1":
        temp = "L1";
        break;
      case "L1":
        temp = "L2";
        break;
      default:
        temp = "1";
        break;
    }
    switchFloor(temp);
  }

  @FXML
  private void animateElevators() {
    Node endNode = getNodeUIByID(thePath.get(thePath.size() - 1).getEndNodeID()).getN();
    for (int i = 0; i < thePath.size() - 1; i++) {
      Node n = initialData.getNodeByID(thePath.get(i).getStartNodeID());
      Node nodeNext = initialData.getNodeByID(thePath.get(i + 1).getStartNodeID());

      if ((n.getNodeType().equals("ELEV") || n.getNodeType().equals("STAI"))
          && n.getFloor().equals(currentFloor)) {
        ImageView imageView = new ImageView(up);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        imageView.setX(n.getXCoord() - 10);
        imageView.setY(n.getYCoord() - 10);

        if (n.compareFloor(nodeNext) >= 1) {
          imageView.setImage(down);
          secondaryAnchor.getChildren().add(imageView);
          imageView.setOnMousePressed(event -> switchFloor(nodeNext.getFloor()));
        } else if (n.compareFloor(nodeNext) <= -1) {
          secondaryAnchor.getChildren().add(imageView);
          imageView.setOnMousePressed(event -> switchFloor(nodeNext.getFloor()));
        }

        ScaleTransition st = new ScaleTransition(Duration.seconds(1), imageView);
        st.setByX(1.5f);
        st.setByY(1.5f);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
      }
    }
  }

  @FXML
  private void scaleAnimation(javafx.scene.Node n) {
    ScaleTransition st = new ScaleTransition(Duration.seconds(1), n);
    st.setByX(1.2f);
    st.setByY(1.2f);
    st.setCycleCount(Animation.INDEFINITE);
    st.setAutoReverse(true);
    st.play();
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
                mapScrollPane.setPannable(false);
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
                mapScrollPane.setPannable(true);
                resizeNodeUI(NUI, .5);
              }
            });
  }

  /*------------------------  CSV buttons ----------------------------------*/

  boolean resetOpen = false;

  private void updateMapFromDB() {
    initialData = new RoomGraph(GlobalDb.getConnection());
    NODES.clear();
    EDGES.clear();
    initializeNodes();
    initializeEdges();
    switchFloor(currentFloor); // Redraw current floor
  }

  private void resetCSV() {
    if (resetOpen) return;
    resetOpen = true;

    // Creating a vertical box and adding textFields
    Text header = new Text("Are you sure you want to reset CSV?");
    header.setFont(javafx.scene.text.Font.font("System", FontWeight.BOLD, 18));

    JFXDialogLayout layout = new JFXDialogLayout();
    layout.setHeading(header);
    JFXDialog submitDia = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.CENTER);

    // Creating the Submit Button
    JFXButton subBtn = new JFXButton("Submit");
    subBtn.setPrefHeight(20);
    subBtn.setPrefWidth(60);
    subBtn.setId("subBtn");
    subBtn.setButtonType(JFXButton.ButtonType.FLAT);
    subBtn.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            // option
            FDatabaseTables.getNodeTable().clearTable(GlobalDb.getConnection(), "Nodes");
            FDatabaseTables.getEdgeTable().clearTable(GlobalDb.getConnection(), "Edges");
            FDatabaseTables.getNodeTable().populateTable(GlobalDb.getConnection(), "");
            FDatabaseTables.getEdgeTable().populateTable(GlobalDb.getConnection(), "");
            updateMapFromDB();
            submitDia.close();
            resetOpen = false;
          }
        });
    subBtn.setStyle("-fx-background-color: #cdcdcd;");

    // Creating the Cancel Button
    JFXButton canBtn = new JFXButton("Cancel");
    canBtn.setPrefHeight(20);
    canBtn.setPrefWidth(60);
    canBtn.setId("canBtn");
    canBtn.setButtonType(JFXButton.ButtonType.FLAT);
    canBtn.setOnAction(
        e -> {
          submitDia.close();
          resetOpen = false;
        });
    canBtn.setStyle("-fx-background-color: #cdcdcd;");

    layout.setActions(subBtn, canBtn);

    submitDia.show();
  }

  private void importCSV() {
    FileChooser nodeChooser = new FileChooser();
    nodeChooser.setTitle("Import nodes");
    nodeChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
    File selectedNodesFile =
        nodeChooser.showOpenDialog(App.getPrimaryStage().getScene().getWindow());
    if (selectedNodesFile == null) return;

    FileChooser edgeChooser = new FileChooser();
    edgeChooser.setTitle("Import edges");
    edgeChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
    File selectedEdgesFile =
        edgeChooser.showOpenDialog(App.getPrimaryStage().getScene().getWindow());
    if (selectedEdgesFile == null) return;

    String _nodesPath = selectedNodesFile.getAbsolutePath();
    String _edgesPath = selectedEdgesFile.getAbsolutePath();

    FDatabaseTables.getNodeTable().clearTable(GlobalDb.getConnection(), "Nodes");
    FDatabaseTables.getEdgeTable().clearTable(GlobalDb.getConnection(), "Edges");
    FDatabaseTables.getNodeTable().populateNodesTableExternal(GlobalDb.getConnection(), _nodesPath);
    FDatabaseTables.getEdgeTable().populateEdgesTableExternal(GlobalDb.getConnection(), _edgesPath);
    System.out.println("Nodes path entered: " + _nodesPath);
    System.out.println("Edges path entered: " + _edgesPath);

    updateMapFromDB();
  }

  private void exportCSV() {
    FileChooser nodeChooser = new FileChooser();
    nodeChooser.setTitle("Export nodes");
    nodeChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
    File selectedNodesFile =
        nodeChooser.showSaveDialog(App.getPrimaryStage().getScene().getWindow());
    if (selectedNodesFile == null) return;

    FileChooser edgeChooser = new FileChooser();
    nodeChooser.setTitle("Export edges");
    nodeChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
    File selectedEdgesFile =
        nodeChooser.showSaveDialog(App.getPrimaryStage().getScene().getWindow());
    if (selectedEdgesFile == null) return;

    String _nodesPath = selectedNodesFile.getAbsolutePath();
    String _edgesPath = selectedEdgesFile.getAbsolutePath();
    FDatabaseTables.getNodeTable().writeNodesTable(GlobalDb.getConnection(), _nodesPath);
    FDatabaseTables.getEdgeTable().writeEdgesTable(GlobalDb.getConnection(), _edgesPath);
    System.out.println("Nodes path entered: " + _nodesPath);
    System.out.println("Edges path entered: " + _edgesPath);
  }
}
