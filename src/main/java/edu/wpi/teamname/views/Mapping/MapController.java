package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Astar.*;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.Access.AdminAccessible;
import edu.wpi.teamname.views.Access.LoginController;
import edu.wpi.teamname.views.HomeController;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javafx.beans.InvalidationListener;
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
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Popup;

public class MapController implements AdminAccessible {

  @FXML private AnchorPane mainAnchor;
  @FXML private ScrollPane movingMap;
  @FXML private AnchorPane secondaryAnchor;
  @FXML private ImageView TheMap;
  @FXML private ComboBox<String> FloorOption;
  @FXML private ComboBox<String> algoVersion;
  @FXML private JFXToggleButton toggleEditor; // TODO: worry about visibility for admin only
  @FXML private JFXButton formBtn;
  @FXML private JFXButton exitBtn;
  @FXML private JFXButton cancelBtn;
  @FXML public Popup popup;
  @FXML private Slider slider;
  private boolean iseditedge = false;
  private RoomGraph data = new RoomGraph(GlobalDb.getConnection());

  private static String userCategory;

  private PathAlgoPicker algorithm = new PathAlgoPicker(new aStar()); // Strategy Pattern
  private boolean isMapEditing = false; // so we can choose what happens when
  private String currentFloor = "1"; // so we can redraw edges when swapping modes
  private LinkedList<Edge> thePath = new LinkedList<Edge>(); // holds path created by algo

  public enum edgeNodeSelection {
    STARTSELECT,
    ENDSELECT,
    NONE
  }

  private edgeNodeSelection mode = edgeNodeSelection.NONE;

  private Node simpleStartNode = null; // Start node selected for simple navigation
  private Line TempL = null; // Line being edited
  private Edge edgeEdited = null; // Edge being edited
  private Node TempN = null; // Line being edited
  private Edge TempE = null;

  @FXML
  private void initialize() throws IOException {
    this.userCategory = HomeController.getUserCategory();

    GlobalDb.getTables().getNodeTable().dispAll(GlobalDb.getConnection());
    resetData();
    isMapEditing = false;
    mode = edgeNodeSelection.NONE;
    algoVersion.setDisable(true);
    algoVersion.setVisible(false);

    movingMap.setPannable(true);
    movingMap.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    movingMap.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

    FloorOption.getItems().addAll("L2", "L1", "1", "2", "3");
    FloorOption.getSelectionModel()
        .selectedItemProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentFloor = newValue;
                changeFloor(newValue);
              }
            });
    algoVersion.getItems().addAll("A*", "BFS", "DFS");
    algoVersion
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                changePathFinderAlgo(newValue);
              }
            });
    if (this.userCategory.equalsIgnoreCase("Admin")) {
      toggleEditor.setVisible(true);
      toggleEditor.setDisable(false);
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
                    toggleEditor.setText("Map Editor");
                    algoVersion.setDisable(false);
                    algoVersion.setVisible(true);
                    changeToMapEditor();
                  } else {
                    toggleEditor.setText("Path Finding");
                    algoVersion.setDisable(true);
                    algoVersion.setVisible(false);
                    changeToPathFinding();
                  }
                }
              });
    } else {
      toggleEditor.setVisible(false);
      toggleEditor.setDisable(true);
    }

    Image ImageMap = new Image("01_thefirstfloor.png");
    TheMap.setImage(ImageMap);
    prepareSlider();
    slider
        .valueProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Scale scale = new Scale((Double) newValue, (Double) newValue);
                double ratio = (Double) newValue / (Double) oldValue;
                movingMap.setHvalue(movingMap.getHvalue() * ratio);
                movingMap.setVvalue(movingMap.getVvalue() * ratio);
                movingMap.setHmax(movingMap.getHmax() * ratio);
                movingMap.setVmax(movingMap.getVmax() * ratio);
                scale.setPivotX(
                    secondaryAnchor.getWidth() * movingMap.getHvalue() / movingMap.getHmax());
                scale.setPivotY(
                    secondaryAnchor.getHeight() * movingMap.getVvalue() / movingMap.getVmax());
                secondaryAnchor.getTransforms().setAll(scale);
              }
            });

    //    mainAnchor.getChildren().add(slider);

    // setUpThePath();
    showPath();
    cancelBtn.setVisible(false);
    cancelBtn.setDisable(true);
    initialNodes();
  }

  public void resetData() {
    data = null;
    data = new RoomGraph(GlobalDb.getConnection());
  }

  /*----------------------------- BUTTON FUNCTIONS ---------------------------*/
  @FXML
  public void goToForm() throws IOException {
    searchPopUpAction();
  }

  @FXML
  public void exitMap() throws IOException {
    App.getPrimaryStage().close();
    Pane root = (Pane) FXMLLoader.load(getClass().getClassLoader().getResource("HomeView.fxml"));
    LoginController loginController = new LoginController();
    loginController.start(root, userCategory);
  }

  @FXML
  private void ResetTheMap() {
    GlobalDb.getTables().getNodeTable().clearTable(GlobalDb.getConnection(), "Nodes");
    GlobalDb.getTables().getEdgeTable().clearTable(GlobalDb.getConnection(), "Edges");
    GlobalDb.getTables().createNodesTable();
    GlobalDb.getTables().createEdgesTable();
    redrawMap();
  }

  public void searchPopUpAction() throws IOException {
    GaussianBlur blur = new GaussianBlur(25);
    App.getPrimaryStage().getScene().getRoot().setEffect(blur);

    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("AstarPopupView.fxml"));

    Pane root = (Pane) fxmlLoader.load();
    root.setStyle("-fx-background-color: #FFFFFF");

    setupDraggablePopUp(root);

    // tie this controller to popup for easy access of functions like pathfinding
    AstarPopupController popupController = fxmlLoader.getController();
    popupController.setMapController(this);

    this.popup = new Popup();
    popup.getContent().addAll(root);
    popup.isAutoFix();
    popup.show(App.getPrimaryStage());
  }

  // Cancel button for simple nav amd Edge Edit
  @FXML
  public void cancelSimple() {
    cancelBtn.setVisible(false);
    cancelBtn.setDisable(true);
    simpleStartNode = null;
    TempL = null;
    thePath = new LinkedList<>();
    redrawMap();
  }

  /*----------------------------------  ALGO ---------------------------------*/

  public void runPathFinding(String startNode, String targetNode) throws IOException {
    Node start = data.getNodeByID(startNode);
    Node target = data.getNodeByID(targetNode);

    algorithm.search(data, start, target);
    algorithm.printPathTo();
    algorithm.printEdgeTo();
    thePath = algorithm.getShortestPath().getPathEdges();
    redrawMap();
    getDirections(thePath);
  }

  public void changePathFinderAlgo(String algo) {
    if (algo.equals("BFS")) algorithm.setAlgorithm(new singleBFS());
    else if (algo.equals("DFS")) algorithm.setAlgorithm(new singleDFS());
    else algorithm.setAlgorithm(new aStar());
  }

  /*------------------------------ MAP REDRAW ----------------------*/

  private void clearSecondaryAnchorPane() {
    secondaryAnchor
        .getChildren()
        .removeIf(
            (n) -> {
              ImageView image = new ImageView();
              if (n.getClass() == image.getClass()) return false;
              else return true;
            });
  }

  private void changeToMapEditor() {
    isMapEditing = true;
    cancelSimple();
    secondaryAnchor.setOnMouseClicked(new addNodeEventHandler());
    redrawMap();
  }

  private void changeToPathFinding() {
    isMapEditing = false;
    secondaryAnchor.setOnMouseClicked(null);
    redrawMap();
  }

  private void changeFloor(String floor) {
    clearSecondaryAnchorPane();
    Image ImageMap;
    if (floor.equals("L2")) {
      ImageMap = new Image("00_thelowerlevel2.png");
    } else if (floor.equals("L1")) {
      ImageMap = new Image("00_thelowerlevel1.png");
    } else if (floor.equals("2")) {
      ImageMap = new Image("02_thesecondfloor.png");
    } else if (floor.equals("3")) {
      ImageMap = new Image("03_thethirdfloor.png");
    } else {
      ImageMap = new Image("01_thefirstfloor.png");
    }
    TheMap.setImage(ImageMap);

    if (isMapEditing) {
      for (Edge N : data.getListOfEdges()) {
        if (data.getNodeByID(N.getStartNode()).getFloor().equals(floor)
            && data.getNodeByID(N.getEndNode()).getFloor().equals(floor)) drawEdge(N);
      }
    } else {
      showPath();
    }

    for (Node N : data.getGraphInfo()) {
      if (N.getFloor().equals(floor)) drawNode(N);
    }
  }

  public void redrawMap() {
    changeFloor(currentFloor);
  }

  /*------------------------- PREP --------------------------*/

  private void prepareSlider() {
    //    Slider slider = new Slider();
    slider.setMax(1);
    slider.setMin(.2);
    slider.setPrefWidth(300);
    slider.setPrefHeight(-200);
    slider.setLayoutX(350);
    slider.setLayoutY(600);
    slider.setValue(1);
    //    return slider;
  }

  private int xOffset = 0;
  private int yOffset = 18;

  private void ScaleDown(Node N) {
    int xCoord = N.getXCoord();
    int yCoord = N.getYCoord();
    N.setCoords(xCoord / 4 + xOffset, yCoord / 4 + yOffset);
  }

  private void initialNodes() {
    for (Node N : data.getGraphInfo()) {
      if (N.getFloor().equals("1")) drawNode(N);
    }
  }

  /*------------------------------Editing-------------------------*/

  public void deleteNode(Node N, Circle C) {
    secondaryAnchor.getChildren().remove(C);

    GlobalDb.getTables()
        .getNodeTable()
        .deleteEntity(GlobalDb.getConnection(), "Nodes", N.getNodeID());
    C.centerXProperty().setValue(null);
    C.centerYProperty().setValue(null);

    // GlobalDb.getTables().getEdgeTable().getEdgeInfo(GlobalDb.getConnection());
  }

  public void deleteEdge(Edge E, Line L) {
    secondaryAnchor.getChildren().remove(L);
    GlobalDb.getTables()
        .getEdgeTable()
        .deleteEntity(GlobalDb.getConnection(), "Edges", E.getEdgeID());
  }

  public void updateNode(
      Node N,
      String nodeID,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {

    if (N.getNodeID().equals("NEWNODE")) {
      GlobalDb.getTables()
          .getNodeTable()
          .addEntity(
              GlobalDb.getConnection(),
              nodeID,
              N.getXCoord(),
              N.getYCoord(),
              currentFloor,
              building,
              nodeType,
              longName,
              shortName,
              0);
      GlobalDb.getTables().getNodeTable().dispAll(GlobalDb.getConnection());
    } else {
      GlobalDb.getTables()
          .getNodeTable()
          .updateNodeFloor(GlobalDb.getConnection(), N.getNodeID(), floor);
      GlobalDb.getTables()
          .getNodeTable()
          .updateNodeBuilding(GlobalDb.getConnection(), N.getNodeID(), building);
      GlobalDb.getTables()
          .getNodeTable()
          .updateNodeType(GlobalDb.getConnection(), N.getNodeID(), nodeType);
      GlobalDb.getTables()
          .getNodeTable()
          .updateNodeLongName(GlobalDb.getConnection(), N.getNodeID(), longName);
      GlobalDb.getTables()
          .getNodeTable()
          .updateNodeShortName(GlobalDb.getConnection(), N.getNodeID(), shortName);
    }

    N.setNodeID(nodeID);
    N.setFloor(floor);
    N.setBuilding(building);
    N.setNodeType(nodeType);
    N.setLongName(longName);
    N.setShortName(shortName);
  }

  public void editEdgeStart(Node N) {
    TempL.startXProperty().unbind();
    TempL.startYProperty().unbind();
    TempL.startXProperty().bind(N.simpXcoordProperty());
    TempL.startYProperty().bind(N.simpYcoordProperty());
  }

  public void editEdgeEnd(Node N) {
    TempL.endXProperty().unbind();
    TempL.endYProperty().unbind();
    TempL.endXProperty().bind(N.simpXcoordProperty());
    TempL.endYProperty().bind(N.simpYcoordProperty());
  }
  /*--------------------------------- DRAW ----------------------*/
  private Circle drawNode(Node N) {
    Circle Temp = new Circle();
    LinkedList<String> favNodeList =
        getFav(GlobalDb.getConnection()); // update from database everytime we click

    if (!(favNodeList.contains(N.getNodeID()))) {
      Temp.setRadius(8.0);
      Temp.setStroke(Color.RED);
      Temp.setFill(Color.RED);
    } else {
      Temp.setRadius(10.0);
      Temp.setStroke(Color.BLUE);
      Temp.setFill(Color.BLUE);
    }
    Temp.centerXProperty().bindBidirectional(N.simpXcoordProperty());
    Temp.centerYProperty().bindBidirectional(N.simpYcoordProperty());
    Temp.setOnMouseClicked(new nodeEventHandler(N, Temp));
    secondaryAnchor.getChildren().add(Temp);
    return Temp;
  }

  public Line drawEdge(Edge E) {
    Line TempE = new Line();
    TempE.startXProperty().bind(data.getNodeByID(E.getStartNode()).simpXcoordProperty());
    TempE.startYProperty().bind(data.getNodeByID(E.getStartNode()).simpYcoordProperty());
    TempE.endXProperty().bind(data.getNodeByID(E.getEndNode()).simpXcoordProperty());
    TempE.endYProperty().bind(data.getNodeByID(E.getEndNode()).simpYcoordProperty());
    TempE.startXProperty().addListener(new invalidateLine(TempE));
    TempE.startYProperty().addListener(new invalidateLine(TempE));
    TempE.endXProperty().addListener(new invalidateLine(TempE));
    TempE.endYProperty().addListener(new invalidateLine(TempE));
    TempE.setStrokeWidth(4.0);
    TempE.setStroke(Color.RED);
    TempE.setId(E.getStartNode() + "_" + E.getEndNode());
    TempE.setOnMouseClicked(new edgeEventHandler(E, TempE));
    secondaryAnchor.getChildren().add(TempE);
    return TempE;
  }

  public void deleteLine(Line line) {
    secondaryAnchor.getChildren().remove(line);
  }

  public void deleteCircle(Circle circle) {
    secondaryAnchor.getChildren().remove(circle);
  }

  @FXML
  public void showPath() {
    if (thePath.isEmpty()) {
      System.out.println("No path to show!");
    } else {
      System.out.println("Path Exists!");
      for (Edge N : thePath) {
        if (data.getNodeByID(N.getStartNode()).getFloor().equals(currentFloor)
            && data.getNodeByID(N.getEndNode()).getFloor().equals(currentFloor)) drawEdge(N);
      }
    }
  }

  /*--------------------------------- HANDLE FAVORITES --------------------------------------------*/
  public LinkedList<String> getFav(Connection conn) {
    Statement stmt = null;
    LinkedList<String> favNodeList = new LinkedList<String>();
    try {
      stmt = conn.createStatement();
      String query =
          "SELECT nodeID FROM FavoriteNodes WHERE name = '" + HomeController.username + "'";
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {
        favNodeList.add(rs.getString("nodeID"));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return favNodeList;
  }

  public void updateFavorite(Connection conn, String nID, int setFavorite) {
    PreparedStatement stmt = null;
    try {
      if (setFavorite == 1) {
        stmt = conn.prepareStatement("INSERT INTO FavoriteNodes VALUES (?, ?)");
      } else if (setFavorite == 0) {
        stmt = conn.prepareStatement("DELETE FROM FavoriteNodes WHERE name = ? AND nodeID = ?");
      }

      stmt.setString(1, HomeController.username);
      stmt.setString(2, nID);

      stmt.executeUpdate();

      if (setFavorite == 1) {
        System.out.println("the node with nodeID " + nID + " is now a favorite");
      } else {
        System.out.println("the node with nodeID " + nID + " is now NOT a favorite");
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @FXML Button dirBtn;
  @FXML TextArea dirText;

  @FXML
  public void getDirections(LinkedList<Edge> edges) {

    if (edges.isEmpty()) {
      System.out.println("No Directions to Give!");
      return;
    }
    ScaleDown(data.getNodeByID(edges.getFirst().getStartNode()));

    dirText.appendText(
        "Directions from "
            + data.getNodeByID(edges.getFirst().getStartNode()).getLongName()
            + " to "
            + data.getNodeByID(edges.getLast().getEndNode()).getLongName()
            + ":\n");

    ScaleDown(data.getNodeByID(edges.getFirst().getEndNode()));
    String initialDirection =
        firstMove(
            data.getNodeByID(edges.getFirst().getStartNode()).getXCoord(),
            data.getNodeByID(edges.getFirst().getStartNode()).getYCoord(),
            data.getNodeByID(edges.getFirst().getEndNode()).getXCoord(),
            data.getNodeByID(edges.getFirst().getEndNode()).getYCoord(),
            data.getNodeByID(edges.getFirst().getStartNode()).getLongName(),
            data.getNodeByID(edges.getFirst().getEndNode()).getLongName()); // removed directions

    // used to skip first edge
    int skip = 0;
    for (Edge N : edges) {
      if (skip == 0) {
        skip++;
        continue;
      }
      ScaleDown(data.getNodeByID(N.getEndNode()));
      String newDirection =
          evalTurn(
              initialDirection,
              data.getNodeByID(N.getStartNode()).getXCoord(),
              data.getNodeByID(N.getStartNode()).getYCoord(),
              data.getNodeByID(N.getEndNode()).getXCoord(),
              data.getNodeByID(N.getEndNode()).getYCoord(),
              data.getNodeByID(N.getStartNode()).getLongName(),
              data.getNodeByID(N.getEndNode()).getLongName());
      initialDirection = newDirection;
    }
    dirText.appendText(
        "\nWelcome to " + data.getNodeByID(edges.getLast().getEndNode()).getLongName() + "\n");
  }

  public String evalTurn(
      String currentDirection,
      int startX,
      int startY,
      int endX,
      int endY,
      String startNode,
      String endNode) {
    String newDirection = "";
    int deltaX = endX - startX;
    int deltaY = endY - startY;

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
      dirText.appendText("Turn Left towards " + endNode + "\n");

    }
    // Turn Right
    else if ((currentDirection.equals("North") && newDirection.equals("East"))
        || (currentDirection.equals("East") && newDirection.equals("South"))
        || (currentDirection.equals("South") && newDirection.equals("West"))
        || (currentDirection.equals("West") && newDirection.equals("North"))) {
      dirText.appendText("Turn Right towards " + endNode + "\n");
    }
    // Continue Straight
    else if (currentDirection.equals(newDirection)) {
      dirText.appendText("Continue Straight towards " + endNode + "\n");
    }

    return newDirection;
  }

  public String firstMove(
      int startX, int startY, int endX, int endY, String startNode, String endNode) {
    int deltaX = endX - startX;
    int deltaY = endY - startY;

    // North
    if ((deltaY < 0) && (Math.abs(deltaY) > Math.abs(deltaX))) {
      System.out.println("Head North towards " + endNode);
      dirText.appendText("Head North towards " + endNode + "\n");
      return "North";
    }
    // South
    else if ((deltaY > 0) && (deltaY > Math.abs(deltaX))) {
      System.out.println("Head South towards " + endNode);
      dirText.appendText("Head South towards " + endNode + "\n");
      return "South";
    }
    // East
    else if ((deltaX > 0) && (deltaX > Math.abs(deltaY))) {
      System.out.println("Head East towards " + endNode);
      dirText.appendText("Head East towards " + endNode + "\n");
      return "East";
    }
    // West
    else if ((deltaX < 0) && (Math.abs(deltaX) > Math.abs(deltaY))) {
      System.out.println("Head West towards " + endNode);
      dirText.appendText("Head West towards " + endNode + "\n");
      return "West";
    } else {
      System.out.println("Error determining turn direction towards " + endNode);
      return "Direction Error";
    }
  }

  @FXML
  public void downloadDirections(ActionEvent event) {
    if (event.getSource() == dirBtn) {

      try {
        FileWriter directions = new FileWriter("directions.txt");

        directions.write(dirText.getText());
        directions.close();

        String DialogText = "Your Directions Have Been Downloaded";
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

  /*----------------------------- EVENT HANDLERS ------------------------------*/

  private class addNodeEventHandler implements EventHandler<MouseEvent> {

    public addNodeEventHandler() {}

    @Override
    public void handle(MouseEvent eventM) {
      try {
        addNewNode(eventM);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void addNewNode(MouseEvent Event) throws IOException {
    if (Event.isControlDown()) {

      Node temp = new Node();
      temp.setNodeID("NEWNODE");
      temp.setXcoord((int) Event.getX());
      temp.setYcoord((int) Event.getY());
      temp.setSimpXcoord(Event.getX());
      temp.setSimpYcoord(Event.getY());
      Circle circle = drawNode(temp);

      FXMLLoader fxmlLoader =
          new FXMLLoader(getClass().getClassLoader().getResource("EditProperties.fxml"));

      Pane root = fxmlLoader.load();
      root.setStyle("-fx-background-color: #FFFFFF");

      setupDraggablePopUp(root);

      // tie this controller to popup for easy access of functions like pathfinding
      EditPropertiesController popupController = fxmlLoader.getController();
      popupController.setMapController(this);

      this.popup = new Popup();
      popupController.setAdd(true);
      popupController.setCircle(circle);
      popupController.setNode(temp);
      popup.getContent().addAll(root);
      popup.show(App.getPrimaryStage());
    }
  }

  private class edgeEventHandler implements EventHandler<MouseEvent> {

    private Edge E;
    private Line L;

    public edgeEventHandler(Edge E, Line L) {
      this.E = E;
      this.L = L;
    }

    @Override
    public void handle(MouseEvent event) {
      try {
        if (isMapEditing) {
          if (mode == edgeNodeSelection.NONE) {
            edgePopUpAction(E, L);
            TempE = E;
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private class nodeEventHandler implements EventHandler<MouseEvent> {

    private Node N;
    private Circle C;

    public nodeEventHandler(Node N, Circle C) {
      this.N = N;
      this.C = C;
    }

    @Override
    public void handle(MouseEvent event) {
      try {
        if (simpleStartNode != null) {
          if (N != simpleStartNode) runPathFinding(simpleStartNode.getNodeID(), N.getNodeID());
          return;
        }
        if (mode == edgeNodeSelection.STARTSELECT) {
          editEdgeStart(N);
          TempN = N;
          if (!iseditedge) {
            GlobalDb.getTables()
                .getEdgeTable()
                .updateEdgeStartNode(GlobalDb.getConnection(), "NEWEDGE", N.getNodeID());
          } else {
            GlobalDb.getTables()
                .getEdgeTable()
                .updateEdgeStartNode(GlobalDb.getConnection(), TempE.getEdgeID(), N.getNodeID());
          }

          EdgeEditPopup();
          mode = edgeNodeSelection.NONE;
          return;
        }
        if (mode == edgeNodeSelection.ENDSELECT) {
          editEdgeEnd(N);
          mode = edgeNodeSelection.NONE;

          if (!iseditedge) {
            GlobalDb.getTables()
                .getEdgeTable()
                .updateEdgeEndNode(GlobalDb.getConnection(), "NEWEDGE", N.getNodeID());
            GlobalDb.getTables()
                .getEdgeTable()
                .updateEdgeID(
                    GlobalDb.getConnection(), "NEWEDGE", TempN.getNodeID() + "_" + N.getNodeID());
          } else {
            GlobalDb.getTables()
                .getEdgeTable()
                .updateEdgeEndNode(GlobalDb.getConnection(), TempE.getEdgeID(), N.getNodeID());
            GlobalDb.getTables()
                .getEdgeTable()
                .updateEdgeID(
                    GlobalDb.getConnection(),
                    TempE.getEdgeID(),
                    TempN.getNodeID() + "_" + N.getNodeID());
          }

          TempL = null;
          TempN = null;
          edgeEdited = null;
          return;
        }
        nodePopUpAction(N, C);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void nodePopUpAction(Node N, Circle C) throws IOException {
    GaussianBlur blur = new GaussianBlur(25);
    App.getPrimaryStage().getScene().getRoot().setEffect(blur);
    FXMLLoader fxmlLoader;
    if (!isMapEditing) {
      fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("NodePopup.fxml"));
    } else {
      fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("NodeEditPopup.fxml"));
    }

    Pane root = (Pane) fxmlLoader.load();
    root.setStyle("-fx-background-color: #FFFFFF");

    setupDraggablePopUp(root);

    // tie this controller to popup for easy access of functions like pathfinding
    if (!isMapEditing) {
      NodePopup popupController = fxmlLoader.getController();
      popupController.setMapController(this);
      popupController.setNode(N);
      popupController.setNodeName(N.getLongName());
    } else {
      NodeEditPopup popupController = fxmlLoader.getController();
      popupController.setMapController(this);
      popupController.setNode(N);
      popupController.setCricle(C);
      popupController.setNodeName(N.getLongName());
    }

    this.popup = new Popup();

    popup.getContent().addAll(root);
    popup.isAutoFix();
    popup.show(App.getPrimaryStage());
  }

  public void edgePopUpAction(Edge E, Line L) throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("EdgeEditPopup.fxml"));

    Pane root = fxmlLoader.load();
    root.setStyle("-fx-background-color: #FFFFFF");

    setupDraggablePopUp(root);

    // tie this controller to popup for easy access of functions like pathfinding
    EdgePopupController popupController = fxmlLoader.getController();
    popupController.setMapController(this);
    popupController.setEdge(E);
    popupController.setline(L);

    this.popup = new Popup();
    popup.getContent().addAll(root);
    popup.show(App.getPrimaryStage());
  }

  public void EdgeEditPopup() throws IOException {
    GaussianBlur blur = new GaussianBlur(25);
    App.getPrimaryStage().getScene().getRoot().setEffect(blur);

    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("EdgeEditNodes.fxml"));

    Pane root = (Pane) fxmlLoader.load();
    root.setStyle("-fx-background-color: #FFFFFF");

    setupDraggablePopUp(root);

    // tie this controller to popup for easy access of functions like pathfinding
    EdgeEditNodes popupController = fxmlLoader.getController();
    popupController.setMapController(this);

    this.popup = new Popup();
    popupController.setEdge(edgeEdited);
    popupController.setline(TempL);
    popupController.setN(TempN);
    popupController.disableCancel();
    popup.getContent().addAll(root);
    popup.isAutoFix();
    popup.show(App.getPrimaryStage());
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

    container.setOnMouseReleased(event -> mouseLocation.set(null));
  }

  private class invalidateLine implements InvalidationListener {
    private Line line;

    public invalidateLine(Line line) {
      this.line = line;
    }

    @Override
    public void invalidated(javafx.beans.Observable observable) {
      if (TempL == null) secondaryAnchor.getChildren().remove(line);
    }
  }

  /*----------------------- GETTERS AND SETTERS -------------------------*/

  public Line getTempL() {
    return TempL;
  }

  public void setTempL(Line tempL) {
    TempL = tempL;
  }

  public Edge getEdgeEdited() {
    return edgeEdited;
  }

  public void setEdgeEdited(Edge edgeEdited) {
    this.edgeEdited = edgeEdited;
  }

  public void setSimpleStartNode(Node n) {
    simpleStartNode = n;
    cancelBtn.setVisible(true);
    cancelBtn.setDisable(false);
  }

  public edgeNodeSelection getMode() {
    return mode;
  }

  public void setMode(edgeNodeSelection mode) {
    this.mode = mode;
  }

  public boolean isIseditedge() {
    return iseditedge;
  }

  public void setIseditedge(boolean iseditedge) {
    this.iseditedge = iseditedge;
  }

  public Node getTempN() {
    return TempN;
  }

  public void setTempN(Node tempN) {
    TempN = tempN;
  }

  public Edge getTempE() {
    return TempE;
  }

  public void setTempE(Edge tempE) {
    TempE = tempE;
  }
}
