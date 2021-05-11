package edu.wpi.cs3733.d21.teamD.views.Mapping;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import edu.wpi.cs3733.d21.teamD.App;
import edu.wpi.cs3733.d21.teamD.Astar.*;
import edu.wpi.cs3733.d21.teamD.Ddb.FDatabaseTables;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import edu.wpi.cs3733.d21.teamD.views.Access.LoginController;
import edu.wpi.cs3733.d21.teamD.views.Access.UserCategory;
import edu.wpi.cs3733.d21.teamD.views.ControllerManager;
import edu.wpi.cs3733.d21.teamD.views.DialogFactory;
import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.Mapping.Popup.Edit.AddNodeController;
import edu.wpi.cs3733.d21.teamD.views.Mapping.Popup.Edit.EditNodeController;
import edu.wpi.cs3733.d21.teamD.views.SceneSizeChangeListener;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.AllServiceNodeInfo;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javafx.animation.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.util.Duration;

public class MapController implements AllAccessible {

  private int index = 0;
  private String toggleText[] = {"Pathfinding", "Map Editor"};
  private RoomGraph initialData = new RoomGraph(GlobalDb.getConnection());
  public static LinkedList<NodeUI> NODES = new LinkedList<>();
  private static LinkedList<EdgeUI> EDGES = new LinkedList<>();
  public PathAlgoPicker algorithm = new PathAlgoPicker(new aStar());
  public LinkedList<Edge> thePath = new LinkedList<Edge>();
  private LinkedList<Node> Targets = new LinkedList<Node>();
  private LinkedList<NodeUI> NewEdge = new LinkedList<NodeUI>();
  private LinkedList<Node> nodesToAlign = new LinkedList<Node>();
  public static final double nodeNormalHeight = 30;
  public static final double nodeNormalWidth = 30;
  private String userCategory = "admin";
  public static String currentFloor = "1";
  private boolean isEditor = false;
  public boolean isEditNodeProperties = false;
  private boolean isEditStart = false;
  private boolean isEditEnd = false;
  private boolean saveMode = false;
  private boolean alignMode = false;
  private SimpleBooleanProperty startPressed = new SimpleBooleanProperty();
  private SimpleBooleanProperty endPressed = new SimpleBooleanProperty();
  private SimpleBooleanProperty plusPressed = new SimpleBooleanProperty();
  private SimpleBooleanProperty minusPressed = new SimpleBooleanProperty();
  private SimpleBooleanProperty aPressed = new SimpleBooleanProperty();
  private SimpleBooleanProperty pPressed = new SimpleBooleanProperty();
  private SimpleBooleanProperty bPressed = new SimpleBooleanProperty();
  private SimpleBooleanProperty shiftPressed = new SimpleBooleanProperty();
  private SimpleBooleanProperty fPressed = new SimpleBooleanProperty();
  private final double buttonZoomAmount = 10;
  private EdgeUI tempEUI;
  public static boolean mapEditorIsSelected = false;

  private final Image F1 = new Image("01_thefirstfloor.png");
  private final Image F2 = new Image("02_thesecondfloor.png");
  private final Image F3 = new Image("03_thethirdfloor.png");
  private final Image FL1 = new Image("00_thelowerlevel1.png");
  private final Image FL2 = new Image("00_thelowerlevel2.png");
  public static final Image DEFAULT = new Image("Images/walkhallpin.png");
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
  public static final Image favImage = new Image("Images/favIcon_good.png");
  public static final Image blockedNode = new Image("Images/blockedNode.png");
  private Image up = new Image("Images/up-arrow.png");
  private Image down = new Image("Images/redArrow.png");
  private Image endImage = new Image("Images/endingIcon_white.png");
  private Image startImage = new Image("Images/walkingStartIcon_black.png");
  public static SceneSizeChangeListener sizeListener;
  protected DialogFactory dialogFactory;

  @FXML private AnchorPane mainAnchor;
  private MapScrollPane mapScrollPane;
  private AnchorPane secondaryAnchor;
  @FXML public static Popup popup;
  @FXML private JFXHamburger mapHam;
  @FXML private JFXDrawer mapDrawer;
  @FXML private JFXToggleButton toggleEditor;
  @FXML private JFXButton floorBtn;
  @FXML private StackPane stackPane;
  @FXML private JFXButton csv;
  private SimpleStringProperty simpleFloor = new SimpleStringProperty("Floor " + currentFloor);
  //  private JFXButton ChooseFloorBtn = new JFXButton("Floor 1");
  @FXML private JFXButton helpButton;
  @FXML private ImageView helpImage;

  private MapDrawerController drawerController;

  public MapController() {}

  @FXML
  private void initialize() {

    initialData = new RoomGraph(GlobalDb.getConnection());

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

    switchFloor("1");
    for (NodeUI node : NODES) {
      //      System.out.println(node.getN().getNodeID());
    }

    ToggleListener();
    nodeAddListener();
    cancelListener();

    mapDrawer.setPickOnBounds(false);

    try {
      mapDrawerView();
    } catch (IOException e) {
      e.printStackTrace();
    }

    initializeFloorList();

    csvBtns();

    helpButton.addEventHandler(
        MouseEvent.MOUSE_PRESSED,
        (event -> {
          if (helpImage.isVisible()) {
            helpImage.setVisible(false);
          } else {
            helpImage.setVisible(true);
          }
        }));

    HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(mapHam);
    burgerTask.setRate(-1);
    mapHam.addEventHandler(
        MouseEvent.MOUSE_PRESSED,
        (e) -> {
          burgerTask.setRate(burgerTask.getRate() * -1);
          burgerTask.play();
          if (mapDrawer.isOpened()) {
            mapDrawer.close();
            mapDrawer.setLayoutX(-270);
          } else {
            mapDrawer.open();
            mapDrawer.setLayoutX(0);
          }
        });

    toggleEditor.setOnAction(
        (ActionEvent e) -> {
          index++;
          if (index >= toggleText.length) {
            index = 0;
          }
          toggleEditor.setText(toggleText[index]);
        });
    dialogFactory = new DialogFactory(stackPane);
  }

  public void mapDrawerView() throws IOException {
    FXMLLoader loader =
        new FXMLLoader(getClass().getClassLoader().getResource("MapDrawerView.fxml"));
    AnchorPane menuBtns = loader.load();
    this.drawerController = loader.getController();
    drawerController.setMapController(this);
    mapDrawer.setSidePane(menuBtns);
    Pane root = (Pane) loader.getRoot();
    List<javafx.scene.Node> childrenList = root.getChildren();

    if (!(HomeController.userTypeEnum == UserCategory.Admin)) {
      AnchorPane anchorPane = (AnchorPane) childrenList.get(0);
      JFXComboBox<String> algoVersion = (JFXComboBox<String>) anchorPane.getChildren().get(6);
      algoVersion.setVisible(false);
      algoVersion.setDisable(true);
    }

    root.setMinHeight(App.getPrimaryStage().getScene().getHeight());
    Scene scene = App.getPrimaryStage().getScene();
    changeChildrenMapView(childrenList);
    sizeListener =
        new SceneSizeChangeListener(scene, root, childrenList) {
          @Override
          public void changeChildren(List<javafx.scene.Node> nodeList) {
            for (javafx.scene.Node node : nodeList)
              if (node instanceof MapScrollPane) ((MapScrollPane) node).updateScaleRange();
            changeChildrenMapView(childrenList);
          }
        };
    scene.widthProperty().addListener(sizeListener);
    scene.heightProperty().addListener(sizeListener);
  }

  public void changeChildrenMapView(List<javafx.scene.Node> nodeList) {
    AnchorPane secondAnchor = (AnchorPane) nodeList.get(0);
    JFXButton findPathBtn = (JFXButton) secondAnchor.getChildren().get(0);
    JFXTreeView treeView = (JFXTreeView) secondAnchor.getChildren().get(1);
    Label label = (Label) secondAnchor.getChildren().get(2);
    ScrollPane textDirection = (ScrollPane) secondAnchor.getChildren().get(3);
    JFXButton dirBtn = (JFXButton) secondAnchor.getChildren().get(7);

    secondAnchor.setPrefHeight(App.getPrimaryStage().getScene().getHeight());
    treeView.setPrefHeight(secondAnchor.getPrefHeight() / 2.75);
    treeView.setLayoutY(findPathBtn.getLayoutY() + findPathBtn.getHeight() + 30);
    label.setLayoutY(treeView.getLayoutY() + treeView.getPrefHeight() + 10);
    label.setLayoutX(10);
    textDirection.setLayoutY(label.getLayoutY() + label.getHeight() + 40);
    textDirection.setPrefHeight(secondAnchor.getPrefHeight() / 3.3);
    dirBtn.setLayoutY(textDirection.getLayoutY() + textDirection.getPrefHeight() + 20);
  }

  public void goHome() {
    ControllerManager.attemptLoadPage(
        "HomeView.fxml", fxmlLoader -> LoginController.start(fxmlLoader.getRoot()));
  }

  // _______________________________________SET UP________________________________________

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

    NODES.clear();

    for (Node N : initialData.getGraphInfo()) {
      ImageView Marker = new ImageView();
      Marker.setFitWidth(nodeNormalWidth);
      Marker.setFitHeight(nodeNormalHeight);
      Marker.setX(N.getXCoord() - nodeNormalWidth / 2);
      Marker.setY(N.getYCoord() - nodeNormalHeight);
      if (FDatabaseTables.getNodeTable()
          .FavContains(GlobalDb.getConnection(), N.getNodeID(), HomeController.username)) {
        Marker.setImage(favImage);
      } else if (FDatabaseTables.getNodeTable()
          .blockedContains(GlobalDb.getConnection(), N.getNodeID())) {
        Marker.setImage(blockedNode);
      } else {
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
      }

      NodeUI Temp = new NodeUI(N, Marker, nodeNormalWidth, nodeNormalHeight);
      pathListener_AddEdge(Temp);
      hoverResize(Temp);
      deleteNodeListener(Temp);
      setupDraggableNodeUI(Temp);
      NODES.add(Temp);
    }

    initializeFavs();
  }

  private void initializeEdges() {

    EDGES.clear();

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
      L.setStrokeWidth(5);
      L.setStroke(Color.BLACK);
      EdgeUI Temp = new EdgeUI(E, L);
      EDGES.add(Temp);
      deleteEdgeListener(Temp);
      editEdgeListener(Temp);
    }
  }

  private void initializeFloorList() {
    floorBtn.setPrefHeight(55);
    floorBtn.setPrefWidth(55);
    floorBtn.setStyle("-fx-background-color: #17B963; -fx-background-radius: 55px");

    JFXButton Floor1Btn = new JFXButton("1");
    Floor1Btn.setButtonType(JFXButton.ButtonType.FLAT);
    Floor1Btn.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 50px");
    Floor1Btn.setPrefHeight(50);
    Floor1Btn.setPrefWidth(50);
    Floor1Btn.setOnAction(
        (e) -> {
          switchFloor("1");
          floorBtn.setText("1");
        });

    JFXButton Floor2Btn = new JFXButton("2");
    Floor2Btn.setButtonType(JFXButton.ButtonType.FLAT);
    Floor2Btn.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 50px");
    Floor2Btn.setPrefHeight(50);
    Floor2Btn.setPrefWidth(50);
    Floor2Btn.setOnAction(
        (e) -> {
          switchFloor("2");
          floorBtn.setText("2");
        });

    JFXButton Floor3Btn = new JFXButton("3");
    Floor3Btn.setButtonType(JFXButton.ButtonType.FLAT);
    Floor3Btn.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 50px");
    Floor3Btn.setPrefHeight(50);
    Floor3Btn.setPrefWidth(50);
    Floor3Btn.setOnAction(
        (e) -> {
          switchFloor("3");
          floorBtn.setText("3");
        });

    JFXButton FloorL1Btn = new JFXButton("L1");
    FloorL1Btn.setButtonType(JFXButton.ButtonType.FLAT);
    FloorL1Btn.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 50px");
    FloorL1Btn.setPrefHeight(50);
    FloorL1Btn.setPrefWidth(50);
    FloorL1Btn.setOnAction(
        (e) -> {
          switchFloor("L1");
          floorBtn.setText("L1");
        });

    JFXButton FloorL2Btn = new JFXButton("L2");
    FloorL2Btn.setButtonType(JFXButton.ButtonType.FLAT);
    FloorL2Btn.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 50px");
    FloorL2Btn.setPrefHeight(50);
    FloorL2Btn.setPrefWidth(50);
    FloorL2Btn.setOnAction(
        (e) -> {
          switchFloor("L2");
          floorBtn.setText("L2");
        });

    JFXNodesList nodeList = new JFXNodesList();
    nodeList.addAnimatedNode(floorBtn);
    nodeList.addAnimatedNode(Floor3Btn);
    nodeList.addAnimatedNode(Floor2Btn);
    nodeList.addAnimatedNode(Floor1Btn);
    nodeList.addAnimatedNode(FloorL1Btn);
    nodeList.addAnimatedNode(FloorL2Btn);

    nodeList.setSpacing(20d);
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

  public void drawNodeFloor(String Floor) {
    for (NodeUI NUI : NODES) {
      if (NUI.getN().getFloor().equals(Floor)) {
        if (!isEditor) {
          if (((!NUI.getN().getNodeType().equals("WALK"))
                  && (!NUI.getN().getNodeType().equals("HALL")))
              || FDatabaseTables.getNodeTable()
                  .blockedContains(GlobalDb.getConnection(), NUI.getN().getNodeID())) {
            addNodeUI(NUI);
          }
        } else {
          addNodeUI(NUI);
        }
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
    floorBtn.setText("Fl " + currentFloor);
    if (isEditor) {
      drawEdgeFloor(floor);
      drawNodeFloor(floor);
    } else {
      showPath();
    }
  }

  public void showPath() {
    if (thePath.isEmpty()) {
      System.out.println("No path to show!");
      drawNodeFloor(currentFloor);
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
      endPin.setY(endNode.getSimpYcoord() - 30);

      drawNodeFloor(currentFloor);

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
    thePath.clear();
    nodesToAlign.clear();
  }

  private void resetNodeSizes() {
    for (NodeUI N : NODES) {
      if (!Targets.contains(N.getN())) {
        N.getI().setFitWidth(N.getSizeWidth());
        N.getI().setFitHeight(N.getSizeHeight());
        N.getI().setX(N.getN().getXCoord() - N.getI().getFitWidth() / 2);
        N.getI().setY(N.getN().getYCoord() - N.getI().getFitHeight());
      }
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

    FDatabaseTables.getNodeTable()
        .deleteEntity(GlobalDb.getConnection(), "Nodes", N.getN().getNodeID());
    secondaryAnchor.getChildren().remove(N.getI());

    if (this.popup.isShowing()) {
      this.popup.hide();
      isEditNodeProperties = false;
    }

    NODES.remove(N);
  }

  private void deleteEdge(EdgeUI E) {
    if (FDatabaseTables.getEdgeTable().contains(GlobalDb.getConnection(), E.getE().getEdgeID()))
      FDatabaseTables.getEdgeTable()
          .deleteEntity(GlobalDb.getConnection(), "Edges", E.getE().getEdgeID());

    if (FDatabaseTables.getEdgeTable()
        .contains(GlobalDb.getConnection(), E.getE().getReverseEdgeID()))
      FDatabaseTables.getEdgeTable()
          .deleteEntity(GlobalDb.getConnection(), "Edges", E.getE().getReverseEdgeID());

    secondaryAnchor.getChildren().remove(E.getL());
    EDGES.remove(E);
  }

  public void addNode(NodeUI N) {

    FDatabaseTables.getNodeTable()
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
    initialData.getGraphInfo().add(N.getN());
    NODES.add(N);
    pathListener_AddEdge(N);
    hoverResize(N);
    setupDraggableNodeUI(N);
    deleteNodeListener(N);
    addNodeUI(N);
  }

  private void addEdge(EdgeUI E) {
    FDatabaseTables.getEdgeTable()
        .addEntity(
            GlobalDb.getConnection(),
            E.getE().getEdgeID(),
            E.getE().getStartNodeID(),
            E.getE().getEndNodeID());
    addEdgeUI(E);
    EDGES.add(E);
  }

  private void editEdgeStart(Edge E, Node N) {
    String newID = N.getNodeID() + "_" + E.getEndNodeID();
    String reverseNewID = E.getEndNodeID() + "_" + N.getNodeID();

    if (!(FDatabaseTables.getEdgeTable().contains(GlobalDb.getConnection(), newID)
        || FDatabaseTables.getEdgeTable().contains(GlobalDb.getConnection(), reverseNewID))) {

      if (FDatabaseTables.getEdgeTable().contains(GlobalDb.getConnection(), E.getEdgeID())) {
        FDatabaseTables.getEdgeTable()
            .updateEdgeStartNode(GlobalDb.getConnection(), E.getEdgeID(), N.getNodeID());
        FDatabaseTables.getEdgeTable().updateEdgeID(GlobalDb.getConnection(), E.getEdgeID(), newID);
      } else if (FDatabaseTables.getEdgeTable()
          .contains(GlobalDb.getConnection(), E.getReverseEdgeID())) {
        FDatabaseTables.getEdgeTable()
            .updateEdgeStartNode(GlobalDb.getConnection(), E.getReverseEdgeID(), N.getNodeID());
        FDatabaseTables.getEdgeTable()
            .updateEdgeID(GlobalDb.getConnection(), E.getReverseEdgeID(), newID);
      }

      EdgeUI edgeUI = getEdgeUIByID(E.getEdgeID());
      NodeUI nodeUI = getNodeUIByID(N.getNodeID());

      edgeUI.getE().setEdgeID(newID);
      edgeUI.getE().setReverseEdgeID(reverseNewID);
      edgeUI.getE().setStartNodeShortName(N.getShortName());
      edgeUI.getE().setStartNodeID(N.getNodeID());

      edgeUI.getL().startXProperty().unbind();
      edgeUI.getL().startXProperty().bind(nodeUI.simpXcoordProperty());
      edgeUI.getL().startYProperty().unbind();
      edgeUI.getL().startYProperty().bind(nodeUI.simpYcoordProperty());
    }
  }

  private void editEdgeEnd(Edge E, Node N) {
    String newID = E.getStartNodeID() + "_" + N.getNodeID();
    String reverseNewID = N.getNodeID() + "_" + E.getStartNodeID();

    if (!(FDatabaseTables.getEdgeTable().contains(GlobalDb.getConnection(), newID)
        || FDatabaseTables.getEdgeTable().contains(GlobalDb.getConnection(), reverseNewID))) {

      if (FDatabaseTables.getEdgeTable().contains(GlobalDb.getConnection(), E.getEdgeID())) {
        FDatabaseTables.getEdgeTable()
            .updateEdgeEndNode(GlobalDb.getConnection(), E.getEdgeID(), N.getNodeID());
        FDatabaseTables.getEdgeTable().updateEdgeID(GlobalDb.getConnection(), E.getEdgeID(), newID);
      } else if (FDatabaseTables.getEdgeTable()
          .contains(GlobalDb.getConnection(), E.getReverseEdgeID())) {
        FDatabaseTables.getEdgeTable()
            .updateEdgeEndNode(GlobalDb.getConnection(), E.getReverseEdgeID(), N.getNodeID());
        FDatabaseTables.getEdgeTable()
            .updateEdgeID(GlobalDb.getConnection(), E.getReverseEdgeID(), newID);
      }

      EdgeUI edgeUI = getEdgeUIByID(E.getEdgeID());
      NodeUI nodeUI = getNodeUIByID(N.getNodeID());

      edgeUI.getE().setEdgeID(newID);
      edgeUI.getE().setReverseEdgeID(reverseNewID);
      edgeUI.getE().setEndNodeShortName(N.getShortName());
      edgeUI.getE().setEndNodeID(N.getNodeID());

      edgeUI.getL().endXProperty().unbind();
      edgeUI.getL().endXProperty().bind(nodeUI.simpXcoordProperty());
      edgeUI.getL().endYProperty().unbind();
      edgeUI.getL().endYProperty().bind(nodeUI.simpYcoordProperty());
    }
  }

  // _______________________________________Path Finding____________________________________________

  // For Directory
  public LinkedList<Edge> runPathFindingDirectory(LinkedList<Node> DirectoryTargets)
      throws IOException {
    Targets.clear();
    Targets.add(DirectoryTargets.getFirst());
    Targets.add(DirectoryTargets.getLast());
    resetNodeSizes();
    thePath = algorithm.multiSearch(initialData, DirectoryTargets).getPathEdges();
    if (thePath.isEmpty()) {
      Targets.clear();
    } else {
      switchFloor(currentFloor);
      resizeNodeUI(getNodeUIByID(DirectoryTargets.getFirst().getNodeID()), 2);
      resizeNodeUI(getNodeUIByID(DirectoryTargets.getLast().getNodeID()), 2);
      final Node start = DirectoryTargets.getFirst();
      switchFloor(start.getFloor());
      final Node end = getCenteringEnd(DirectoryTargets.getLast());
      System.out.println("End node (for centering) is " + end.getLongName());
      mapScrollPane.centerOnPath(
          start.getXCoord(), start.getYCoord(), end.getXCoord(), end.getYCoord());

      //      algorithm.multiSearch(initialData, DirectoryTargets).printPathEdges();
      drawerController.getDirections(thePath);
    }
    return thePath;
  }

  private Node getCenteringEnd(Node trueEnd) {
    return getNodeUIByID(
            thePath.stream()
                .filter(
                    edge -> {
                      final Node start = getNodeUIByID(edge.getStartNodeID()).getN();
                      final Node end = getNodeUIByID(edge.getEndNodeID()).getN();
                      if (trueEnd.getNodeID().equals(start.getNodeID())
                          || trueEnd.getNodeID().equals(end.getNodeID())) return true;
                      return start.compareFloor(end) != 0;
                    })
                .findFirst()
                .get()
                .getStartNodeID())
        .getN();
  }

  public void runPathFindingClick() {
    thePath = algorithm.multiSearch(initialData, Targets).getPathEdges();
    if (thePath.isEmpty()) {
      resetData();
      clearMap();
      drawNodeFloor(currentFloor);
    } else {
      switchFloor(currentFloor);
      // algorithm.multiSearch(initialData, Targets).printPathEdges();
      drawerController.getDirections(thePath);
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
                  isEditor = true;
                  clearMap();
                  LoadingNodesEdges(currentFloor);
                } else {
                  isEditor = false;
                  if (popup != null && popup.isShowing()) {
                    popup.hide();
                  }
                  clearMap();
                  drawNodeFloor(currentFloor);
                }
              }
            });
  }

  private void nodeAddListener() {
    secondaryAnchor.setOnMousePressed(
        (MouseEvent E) -> {
          if (plusPressed.get() && isEditor) {
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
          if (key == KeyCode.P) {
            pPressed.set(true);
            plusPressed.set(false);
            minusPressed.set(false);
            aPressed.set(false);
            startPressed.setValue(false);
            endPressed.setValue(false);
            shiftPressed.set(false);
            fPressed.set(false);
            bPressed.set(false);
            saveMode = !saveMode;
            alignMode = false;
          }
          if (key == KeyCode.F) {
            pPressed.set(false);
            plusPressed.set(false);
            minusPressed.set(false);
            aPressed.set(false);
            startPressed.setValue(false);
            endPressed.setValue(false);
            shiftPressed.set(false);
            fPressed.set(true);
            bPressed.set(false);
          }
          if (key == KeyCode.B) {
            pPressed.set(false);
            plusPressed.set(false);
            minusPressed.set(false);
            aPressed.set(false);
            startPressed.setValue(false);
            endPressed.setValue(false);
            shiftPressed.set(false);
            fPressed.set(false);
            bPressed.set(true);
          }
          if (key == KeyCode.SHIFT) {
            pPressed.set(false);
            shiftPressed.set(true);
            plusPressed.set(false);
            minusPressed.set(false);
            aPressed.set(false);
            startPressed.setValue(false);
            endPressed.setValue(false);
            fPressed.set(false);
            alignMode = !alignMode;
            saveMode = false;
            bPressed.set(false);
          }
          if (key == KeyCode.EQUALS) {
            pPressed.set(false);
            plusPressed.set(true);
            minusPressed.set(false);
            aPressed.set(false);
            startPressed.setValue(false);
            endPressed.setValue(false);
            shiftPressed.set(false);
            fPressed.set(false);
            bPressed.set(false);
          }
          if (key == KeyCode.OPEN_BRACKET) {
            mapScrollPane.onScroll(
                -buttonZoomAmount,
                new Point2D(
                    mapScrollPane.getMapImage().getFitWidth() / 2,
                    mapScrollPane.getMapImage().getFitHeight() / 2));
          }
          if (key == KeyCode.CLOSE_BRACKET) {
            mapScrollPane.onScroll(
                buttonZoomAmount,
                new Point2D(
                    mapScrollPane.getMapImage().getFitWidth() / 2,
                    mapScrollPane.getMapImage().getFitHeight() / 2));
          }
          if (key == KeyCode.MINUS) {
            pPressed.set(false);
            plusPressed.set(false);
            minusPressed.set(true);
            aPressed.set(false);
            startPressed.setValue(false);
            endPressed.setValue(false);
            shiftPressed.set(false);
            fPressed.set(false);
            bPressed.set(false);
          }
          if (key == KeyCode.A) {
            pPressed.set(false);
            plusPressed.set(false);
            minusPressed.set(false);
            aPressed.set(true);
            startPressed.setValue(false);
            endPressed.setValue(false);
            shiftPressed.set(false);
            fPressed.set(false);
            bPressed.set(false);
          }
          if (key == KeyCode.S) {
            startPressed.setValue(true);
            endPressed.setValue(false);
            pPressed.set(false);
            plusPressed.set(false);
            minusPressed.set(false);
            aPressed.set(false);
            shiftPressed.set(false);
            fPressed.set(false);
            bPressed.set(false);
          }
          if (key == KeyCode.E) {
            endPressed.setValue(true);
            startPressed.setValue(false);
            pPressed.set(false);
            plusPressed.set(false);
            minusPressed.set(false);
            aPressed.set(false);
            shiftPressed.set(false);
            fPressed.set(false);
            bPressed.set(false);
          }
          if (!isEditor) {
            if (key == KeyCode.ESCAPE) {
              resetData();
              clearMap();
              drawNodeFloor(currentFloor);
              System.out.println("Just cleared");
            }
            return;
          }
        });

    mapScrollPane.setOnKeyReleased(
        (event) -> {
          endPressed.setValue(false);
          startPressed.setValue(false);
          pPressed.set(false);
          plusPressed.set(false);
          minusPressed.set(false);
          aPressed.set(false);
          shiftPressed.set(false);
          fPressed.set(false);
          bPressed.set(false);
        });
  }

  private void deleteNodeListener(NodeUI N) {
    N.getI()
        .setOnMousePressed(
            (MouseEvent E) -> {
              if (minusPressed.get() && isEditor) {
                deleteNode(N);
              }

              if (isEditStart && isEditor) {
                editEdgeStart(tempEUI.getE(), N.getN());
                tempEUI.getL().setStroke(Color.BLACK);
                tempEUI = null;
                isEditStart = false;
              }
              if (isEditEnd && isEditor) {
                editEdgeEnd(tempEUI.getE(), N.getN());
                tempEUI.getL().setStroke(Color.BLACK);
                tempEUI = null;
                isEditEnd = false;
              }
            });
  }

  private void deleteEdgeListener(EdgeUI E) {
    E.getL()
        .setOnMousePressed(
            (MouseEvent e) -> {
              if (minusPressed.get() && isEditor) {
                deleteEdge(E);
              }
            });
  }

  private void editEdgeListener(EdgeUI E) {

    E.getL()
        .setOnMouseClicked(
            new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent event) {
                if (startPressed.get()) {
                  isEditStart = true;
                  isEditEnd = false;
                  tempEUI = E;
                  E.getL().setStroke(Color.ROYALBLUE);
                }
                if (endPressed.get()) {
                  isEditEnd = true;
                  isEditStart = false;
                  tempEUI = E;
                  E.getL().setStroke(Color.ORANGERED);
                }
              }
            });

    E.getL()
        .setOnMouseEntered(
            (MouseEvent e) -> {
              if (!isEditStart && !isEditEnd && isEditor) E.getL().setStroke(Color.DARKORANGE);
            });

    E.getL()
        .setOnMouseExited(
            (MouseEvent e) -> {
              if (!isEditStart && !isEditEnd) E.getL().setStroke(Color.BLACK);
            });
  }

  private void pathListener_AddEdge(NodeUI N) {
    N.getI()
        .addEventHandler(
            MouseEvent.MOUSE_PRESSED,
            (E) -> {
              if (E.getButton() == MouseButton.SECONDARY && !isEditor) {
                Targets.add(N.getN());
                resizeNodeUI(N, 2);
                if (Targets.size() >= 2) {
                  runPathFindingClick();
                }
              }
              if (isEditor) {
                if (aPressed.get()) {
                  alignMode = false;
                  saveMode = false;
                  NewEdge.add(N);
                  if (NewEdge.size() == 2) {

                    //                    System.out.println(NewEdge.get(0).getN().getNodeID());
                    //                    System.out.println(NewEdge.get(1).getN().getNodeID());

                    Line L = new Line();

                    L.startXProperty().bind(NewEdge.get(0).simpXcoordProperty());
                    L.startYProperty().bind(NewEdge.get(0).simpYcoordProperty());
                    L.endXProperty().bind(NewEdge.get(1).simpXcoordProperty());
                    L.endYProperty().bind(NewEdge.get(1).simpYcoordProperty());
                    L.setStroke(Color.BLACK);
                    L.setStrokeWidth(5.0);
                    Edge edge =
                        new Edge(
                            NewEdge.get(0).getN(),
                            NewEdge.get(1).getN(),
                            NewEdge.get(0).getN().getMeasuredDistance(NewEdge.get(1).getN()));
                    Edge edge1 =
                        new Edge(
                            NewEdge.get(1).getN(),
                            NewEdge.get(0).getN(),
                            NewEdge.get(0).getN().getMeasuredDistance(NewEdge.get(1).getN()));
                    NewEdge.get(0).getN().addEdge(edge);
                    NewEdge.get(1).getN().addEdge(edge1);
                    EdgeUI temp = new EdgeUI(edge, L);
                    editEdgeListener(temp);
                    deleteEdgeListener(temp);
                    addEdge(temp);
                    NewEdge = new LinkedList<NodeUI>();
                  }
                }
                // Align node
                if (alignMode) {
                  nodesToAlign.add(N.getN());

                  if (nodesToAlign.size() > 2) {
                    alignNodes();
                  }
                }

                if (bPressed.get()) {
                  blockNode(N);
                }
              } // end of isEditor

              if (saveMode) {
                if (N.getN().getNodeType().equals("PARK")) {
                  saveParkingSpot(N);
                }
              }
              if (fPressed.get()) {
                favorite(N);
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
      //      System.out.println(node.getNodeID());
      //      System.out.println("X: " + node.getXCoord());
      //      System.out.println("Y: " + node.getYCoord());
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

  private void initializeFavs() {
    LinkedList<NodeUI> initialFavs = new LinkedList<NodeUI>();
    initialFavs.add(getNodeUIByLongName("Elevator S 01"));
    initialFavs.add(getNodeUIByLongName("Restroom S elevator 1st floor"));
    initialFavs.add(getNodeUIByLongName("Cafe Stairs"));
    initialFavs.add(getNodeUIByLongName("Waiting Room 1 Floor 1"));
    initialFavs.add(getNodeUIByLongName("Connors Center Security Desk Floor 1"));
    initialFavs.add(getNodeUIByLongName("Shattuck Street Lobby Exit"));
    initialFavs.add(getNodeUIByLongName("Emergency Department Entrance"));
    initialFavs.add(getNodeUIByLongName("Parking Garage L2"));
    initialFavs.add(getNodeUIByLongName("Cafe"));

    if (HomeController.username == null) {
      System.out.println("No initial favorites for guests");
    } else {
      for (NodeUI N : initialFavs) {
        if (!FDatabaseTables.getNodeTable()
            .FavContains(GlobalDb.getConnection(), N.getN().getNodeID(), HomeController.username)) {
          FDatabaseTables.getNodeTable()
              .addToFavoriteNodes(
                  GlobalDb.getConnection(),
                  HomeController.username,
                  N.getN().getNodeID(),
                  N.getN().getLongName());
          N.getI().setImage(favImage);
        }
      }
    }
  }

  private void favorite(NodeUI N) {
    if (FDatabaseTables.getNodeTable()
        .FavContains(GlobalDb.getConnection(), N.getN().getNodeID(), HomeController.username)) {
      FDatabaseTables.getNodeTable()
          .deleteFav(GlobalDb.getConnection(), N.getN().getNodeID(), HomeController.username);
      switch (N.getN().getNodeType()) {
        case "PARK":
          N.getI().setImage(PARK);
          break;
        case "ELEV":
          N.getI().setImage(ELEV);
          break;
        case "REST":
          N.getI().setImage(REST);
          break;
        case "STAI":
          N.getI().setImage(STAI);
          break;
        case "DEPT":
          N.getI().setImage(DEPT);
          break;
        case "LABS":
          N.getI().setImage(LABS);
          break;
        case "INFO":
          N.getI().setImage(INFO);
          break;
        case "CONF":
          N.getI().setImage(CONF);
          break;
        case "EXIT":
          N.getI().setImage(EXIT);
          break;
        case "RETL":
          N.getI().setImage(RETL);
          break;
        case "SERV":
          N.getI().setImage(SERV);
          break;
        default:
          N.getI().setImage(DEFAULT);
          break;
      }
    } else {
      if (HomeController.username == null) {
        dialogFactory.createTwoButtonDialog(
            "You're in guest view",
            "Please login or Sign up to add favorite",
            "Sign up",
            () -> {
              ControllerManager.attemptLoadPopupBlur("signUpView.fxml");
            },
            "Just view map",
            () -> {});
      } else {
        FDatabaseTables.getNodeTable()
            .addToFavoriteNodes(
                GlobalDb.getConnection(),
                HomeController.username,
                N.getN().getNodeID(),
                N.getN().getLongName());
        N.getI().setImage(favImage);
      }
    }
    MapDrawerController.favCallStuff();
  }

  private void blockNode(NodeUI N) {
    // TODO: REPLACE FAVE DB NODE STUFF WITH BLOCKED NODE DB
    if (FDatabaseTables.getNodeTable()
        .blockedContains(GlobalDb.getConnection(), N.getN().getNodeID())) {
      FDatabaseTables.getNodeTable().deleteBlocked(GlobalDb.getConnection(), N.getN().getNodeID());
      switch (N.getN().getNodeType()) {
        case "PARK":
          N.getI().setImage(PARK);
          break;
        case "ELEV":
          N.getI().setImage(ELEV);
          break;
        case "REST":
          N.getI().setImage(REST);
          break;
        case "STAI":
          N.getI().setImage(STAI);
          break;
        case "DEPT":
          N.getI().setImage(DEPT);
          break;
        case "LABS":
          N.getI().setImage(LABS);
          break;
        case "INFO":
          N.getI().setImage(INFO);
          break;
        case "CONF":
          N.getI().setImage(CONF);
          break;
        case "EXIT":
          N.getI().setImage(EXIT);
          break;
        case "RETL":
          N.getI().setImage(RETL);
          break;
        case "SERV":
          N.getI().setImage(SERV);
          break;
        default:
          N.getI().setImage(DEFAULT);
          break;
      }
      initialData.getNodeByID(N.getN().getNodeID()).setBlocked(false);
    } else {
      if (HomeController.username == null) { // should never get in here
        dialogFactory.createTwoButtonDialog(
            "You're in guest view",
            "Please login or Sign up to add favorite",
            "Sign up",
            () -> {
              ControllerManager.attemptLoadPopupBlur("signUpView.fxml");
            },
            "Just view map",
            () -> {});
      } else { // TODO: replace with new DB stuff and image
        FDatabaseTables.getNodeTable()
            .addToBlockedNodes(
                GlobalDb.getConnection(), N.getN().getNodeID(), N.getN().getLongName());
        initialData.getNodeByID(N.getN().getNodeID()).setBlocked(true);
        N.getI().setImage(blockedNode);
        N.setSizeHeight(50);
        N.setSizeWidth(50);
      }
    }
    MapDrawerController.blockedCallStuff();
  }

  private void hoverResize(NodeUI N) {
    N.getI().setOnMouseEntered(new NodeEnterHandler(N, this));
    N.getI()
        .setOnMouseExited(
            (MouseEvent e) -> {
              resetNodeSizes();
              if (isEditor) {
                if (!isEditNodeProperties) {
                  this.popup.hide();
                }
              }
            });
  }

  private class NodeEnterHandler implements EventHandler<MouseEvent> {
    private NodeUI N;
    private MapController mapController;

    NodeEnterHandler(NodeUI N, MapController mapController) {
      this.N = N;
      this.mapController = mapController;
    }

    @Override
    public void handle(MouseEvent event) {
      resizeNodeUI(N, 2);
      if (isEditor) {
        if (!isEditNodeProperties && !(isEditEnd || isEditStart)) {
          try {
            FXMLLoader temp = loadPopup("MapPopUps/EditNode.fxml");
            EditNodeController editNodeController = temp.getController();
            editNodeController.setMapController(mapController);
            editNodeController.setTheNode(N);
            Pane root = (Pane) temp.getRoot();
            popup.addEventHandler(
                KeyEvent.KEY_PRESSED,
                (KeyEvent k) -> {
                  KeyCode key = k.getCode();
                  if (key == KeyCode.ALT) {
                    isEditNodeProperties = true;
                  }
                });

            // remove node listeners
            root.addEventHandler(
                MouseEvent.MOUSE_ENTERED,
                (MouseEvent e1) -> {
                  for (NodeUI node : NODES) {
                    node.getI().setOnMouseEntered(mapController::disableListener);
                    node.getI().setOnMouseExited(mapController::disableListener);
                  }
                });

            // return node listeners
            root.addEventHandler(
                MouseEvent.MOUSE_EXITED,
                (MouseEvent e1) -> {
                  mapController.resetNodeSizes();
                  if (!isEditNodeProperties) {
                    popup.hide();
                  }
                  for (NodeUI node : NODES) {
                    // here's that recursive calling
                    node.getI().setOnMouseEntered(new NodeEnterHandler(node, mapController));
                    node.getI()
                        .setOnMouseExited(
                            (MouseEvent e2) -> {
                              mapController.resetNodeSizes();
                              if (isEditor) {
                                if (!isEditNodeProperties) {
                                  mapController.popup.hide();
                                }
                              }
                            });
                  }
                });
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        } // end of edit node popup stuff
      } // end of isEditor
    }
  }

  // ---------------------------------------Animation------------------------------------------

  @FXML
  private void animateEdges() {
    for (Edge e : thePath) {
      EdgeUI edgeUi = getEdgeUIByID(e.getEdgeID());
      Line line = edgeUi.getL();

      line.getStrokeDashArray().setAll(25d, 10d);
      line.setStrokeWidth(5);

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

  public static NodeUI getNodeUIByLongName(String NodeLongName) {
    for (NodeUI theNode : NODES) {
      if (theNode.getN().getLongName().equals(NodeLongName)) {
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

  public String getCurrentFloor() {
    return currentFloor;
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
    dialogFactory.createTwoButtonDialog(
        "Reset CSV",
        "Are you sure to reset the CSV?",
        "Yes",
        () -> {
          FDatabaseTables.getNodeTable().clearTable(GlobalDb.getConnection(), "Nodes");
          FDatabaseTables.getEdgeTable().clearTable(GlobalDb.getConnection(), "Edges");
          FDatabaseTables.getNodeTable().populateTable(GlobalDb.getConnection(), "");
          FDatabaseTables.getEdgeTable().populateTable(GlobalDb.getConnection(), "");
          updateMapFromDB();
        },
        "No",
        () -> {});
  }

  @FXML
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

  @FXML
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

  private void csvBtns() {
    JFXButton importBtn = new JFXButton("Import CSV");
    importBtn.setButtonType(JFXButton.ButtonType.FLAT);
    importBtn.setStyle("-fx-background-color: #C3E4FF");
    importBtn.setOnAction(
        (e) -> {
          importCSV();
        });

    JFXButton exportBtn = new JFXButton("Export CSV");
    exportBtn.setStyle("-fx-background-color: #C3E4FF");
    exportBtn.setOnAction(
        (e) -> {
          exportCSV();
        });

    JFXButton resetBtn = new JFXButton("Reset CSV");
    resetBtn.setStyle("-fx-background-color: #C3E4FF");
    resetBtn.setOnAction(
        (e) -> {
          resetCSV();
        });

    JFXNodesList csvNodeList = new JFXNodesList();
    csvNodeList.addAnimatedNode(csv);
    csvNodeList.addAnimatedNode(importBtn);
    csvNodeList.addAnimatedNode(exportBtn);
    csvNodeList.addAnimatedNode(resetBtn);
    csvNodeList.setSpacing(20d);
    mainAnchor.getChildren().add(csvNodeList);

    csvNodeList.visibleProperty().bind(toggleEditor.selectedProperty());
  }

  @FXML
  private void helpPopUp() {
    helpButton.setOnAction(
        (e) -> {
          helpImage.getImage();
        });
  }

  // _________________________________________Service View_____________________________________

  @FXML
  private void LoadServices() throws IOException {
    clearMap();
    Image I = new Image("Images/Service Icons/exTrans_green.png");

    for (AllServiceNodeInfo S : FDatabaseTables.getAllServiceTable().ListServices()) {

      for (NodeUI N : NODES) {
        if (N.getN().getLongName().equals(S.getLocation())) {
          ImageView Service = new ImageView();
          Service.setX(N.getN().getXCoord());
          Service.setY(N.getN().getYCoord());
          Service.setFitWidth(30);
          Service.setFitHeight(30);

          System.out.println(S.getStatus());

          switch (S.getType()) {
            case "EXT":
              switch (S.getStatus()) {
                case "Complete":
                  I = new Image("Images/Service Icons/exTrans_green.png");
                  break;
                case "In Progress":
                  I = new Image("Images/Service Icons/exTrans_yellow.png");
                  break;
                case "Incomplete":
                  I = new Image("Images/Service Icons/exTrans_red.png");
                  break;
              }
              Service.setImage(I);
              break;
            case "FLOW":
              switch (S.getStatus()) {
                case "Complete":
                  I = new Image("Images/Service Icons/floral_green.png");
                  break;
                case "In Progress":
                  I = new Image("Images/Service Icons/floral_yellow.png");
                  break;
                case "Incomplete":
                  I = new Image("Images/Service Icons/floral_red.png");
                  break;
              }
              Service.setImage(I);
              break;
            case "FOOD":
              switch (S.getStatus()) {
                case "Complete":
                  I = new Image("Images/Service Icons/food_green.png");
                  break;
                case "In Progress":
                  I = new Image("Images/Service Icons/food_yellow.png");
                  break;
                case "Incomplete":
                  I = new Image("Images/Service Icons/food_red.png");
                  break;
              }
              Service.setImage(I);
              break;
            case "LAUN":
              switch (S.getStatus()) {
                case "Complete":
                  I = new Image("Images/Service Icons/laundry_green.png");
                  break;
                case "In Progress":
                  I = new Image("Images/Service Icons/laundry_yellow.png");
                  break;
                case "Incomplete":
                  I = new Image("Images/Service Icons/laundry_red.png");
                  break;
              }
              Service.setImage(I);
              break;
            case "LANG":
              switch (S.getStatus()) {
                case "Complete":
                  I = new Image("Images/Service Icons/translate_green.png");
                  break;
                case "In Progress":
                  I = new Image("Images/Service Icons/translate_yellow.png");
                  break;
                case "Incomplete":
                  I = new Image("Images/Service Icons/translate_red.png");
                  break;
              }
              Service.setImage(I);
              break;
            case "ITRAN":
              switch (S.getStatus()) {
                case "Complete":
                  I = new Image("Images/Service Icons/wheelchair_green.png");
                  break;
                case "In Progress":
                  I = new Image("Images/Service Icons/wheelchair_yellow.png");
                  break;
                case "Incomplete":
                  I = new Image("Images/Service Icons/wheelchair_red.png");
                  break;
              }
              Service.setImage(I);
              break;
            case "SECUR":
              switch (S.getStatus()) {
                case "Complete":
                  I = new Image("Images/Service Icons/security_green.png");
                  break;
                case "In Progress":
                  I = new Image("Images/Service Icons/security_yellow.png");
                  break;
                case "Incomplete":
                  I = new Image("Images/Service Icons/security_red.png");
                  break;
              }
              Service.setImage(I);
              break;
            case "FACIL":
              switch (S.getStatus()) {
                case "Complete":
                  I = new Image("Images/Service Icons/maintenance_green.png");
                  break;
                case "In Progress":
                  I = new Image("Images/Service Icons/maintenance_yellow.png");
                  break;
                case "Incomplete":
                  I = new Image("Images/Service Icons/maintenance_red.png");
                  break;
              }
              Service.setImage(I);
              break;
            case "COMP":
              switch (S.getStatus()) {
                case "Complete":
                  I = new Image("Images/Service Icons/Computer_green.png");
                  break;
                case "In Progress":
                  I = new Image("Images/Service Icons/Computer_yellow.png");
                  break;
                case "Incomplete":
                  I = new Image("Images/Service Icons/Computer_red.png");
                  break;
              }
              Service.setImage(I);
              break;
            case "AUD":
              switch (S.getStatus()) {
                case "Complete":
                  I = new Image("Images/Service Icons/exTrans_green.png");
                  break;
                case "In Progress":
                  I = new Image("Images/Service Icons/exTrans_yellow.png");
                  break;
                case "Incomplete":
                  I = new Image("Images/Service Icons/exTrans_red.png");
                  break;
              }
              I = new Image("Images/Service Icons/exTrans_green.png");
              Service.setImage(I);
              break;
            case "SANI":
              switch (S.getStatus()) {
                case "Complete":
                  I = new Image("Images/Service Icons/sanitization_green.png");
                  break;
                case "In Progress":
                  I = new Image("Images/Service Icons/sanitization_yellow.png");
                  break;
                case "Incomplete":
                  I = new Image("Images/Service Icons/sanitization_red.png");
                  break;
              }
              Service.setImage(I);
              break;
            case "MEDD":
              switch (S.getStatus()) {
                case "Complete":
                  I = new Image("Images/Service Icons/medicine_green.png");
                  break;
                case "In Progress":
                  I = new Image("Images/Service Icons/medicine_yellow.png");
                  break;
                case "Incomplete":
                  I = new Image("Images/Service Icons/medicine_red.png");
                  break;
              }
              Service.setImage(I);
              break;
          }

          if (N.getN().getFloor().equals(currentFloor)) {
            secondaryAnchor.getChildren().add(Service);
          }
        }
      }
    }
  }
}
