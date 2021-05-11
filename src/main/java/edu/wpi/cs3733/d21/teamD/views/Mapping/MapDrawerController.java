package edu.wpi.cs3733.d21.teamD.views.Mapping;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.d21.teamD.Astar.*;
import edu.wpi.cs3733.d21.teamD.Ddb.FDatabaseTables;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.views.HomeController;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;

public class MapDrawerController implements Initializable {
  @FXML private JFXTreeView<String> directoryTreeView;
  @FXML private JFXTextField startField;
  @FXML private JFXTextField endField;
  @FXML private JFXButton findPathButton;
  @FXML private VBox vBox;
  @FXML private GridPane startGrid;
  @FXML private GridPane endGrid;
  @FXML public JFXComboBox<String> algoVersion;
  @FXML private JFXButton exportBut;
  @FXML private JFXButton importBut;
  @FXML private ScrollPane textScrollPane;
  @FXML JFXButton dirBtn;
  @FXML TextFlow dirText;
  TextArea downloadText = new TextArea();
  private LinkedList<edu.wpi.cs3733.d21.teamD.Astar.Node> Targets = new LinkedList<>();

  private MapController mapController;

  private LinkedList<String> nodesClicked = new LinkedList<>();

  private ArrayList<String> parkingList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "PARK");
  private ArrayList<String> elevList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "ELEV");
  private ArrayList<String> restroomList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "REST");
  private ArrayList<String> stairsList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "STAI");
  private ArrayList<String> departmentList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "DEPT");
  private ArrayList<String> labList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "LABS");
  private ArrayList<String> informationList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "INFO");
  private ArrayList<String> conferenceList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "CONF");
  private ArrayList<String> exitList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "EXIT");
  private ArrayList<String> retailList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "RETL");
  private ArrayList<String> serviceList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "SERV");
  public static ArrayList<String> favList =
      FDatabaseTables.getNodeTable()
          .fetchLongNameFavorites(GlobalDb.getConnection(), HomeController.username);
  public static ArrayList<String> blockedList =
      FDatabaseTables.getNodeTable().fetchLongNameBlocked(GlobalDb.getConnection());
  private Node textDirection;
  private RoomGraph initialData = new RoomGraph(GlobalDb.getConnection());
  public static TreeItem<String> favoriteCell = new TreeItem<>("Favorite");
  public static TreeItem<String> blockedCell = new TreeItem<>("Blocked");

  @FXML
  public void tiasSpecialFunction() throws IOException {
    String startText = startField.getText();
    String endText = endField.getText();
    //    String longName1 = "Neuroscience Waiting Room";
    //    String longName2 = "Emergency Department Entrance";
    //    System.out.println(startText);
    //    System.out.println(endText);

    Targets.add(mapController.getNodeUIByLongName(startText).getN());
    Targets.add(mapController.getNodeUIByLongName(endText).getN());

    mapController.runPathFindingDirectory(Targets);

    recentStart = startText;
    recentEnd = endText;

    setSearchHistory(GlobalDb.getConnection(), recentStart, recentEnd);
    HomeController.historyTracker = 1;
    getSearchHistory(GlobalDb.getConnection());
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    downloadText.setVisible(false);
    textScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    textScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    textScrollPane.setStyle(
        "-fx-background-color: transparent; -fx-padding: 0; -fx-background-insets: 0");
    //    textScrollPane.

    directoryTreeView
        .getSelectionModel()
        .setSelectionMode(SelectionMode.MULTIPLE); // Setting SelectionMode to MULTIPLE

    treeViewSetup();

    EventHandler<MouseEvent> mouseEventHandle =
        (MouseEvent event) -> {
          try {
            handleMouseClicked(event);
          } catch (IOException e) {
            e.printStackTrace();
          }
        };

    directoryTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);

    ArrayList<String> longNames =
        FDatabaseTables.getNodeTable().fetchLongNameNoHall(GlobalDb.getConnection());

    startGrid.setPickOnBounds(false);

    startField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (startGrid.getChildren().size() > 1) {
                startGrid.getChildren().remove(1);
              }
              startGrid.add(longNameMenu(newValue, longNames, startField), 0, 1);
              if (longNameMenu(newValue, longNames, startField).getChildren().size() == 1) {
                startGrid
                    .getChildren()
                    .remove(startGrid.getChildren().get(startGrid.getChildren().size() - 1));
              }
            });

    endField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (endGrid.getChildren().size() > 1) {
                endGrid.getChildren().remove(1);
              }
              endGrid.add(longNameMenu(newValue, longNames, endField), 0, 1);
              if (longNameMenu(newValue, longNames, startField).getChildren().size() == 1) {
                endGrid
                    .getChildren()
                    .remove(endGrid.getChildren().get(endGrid.getChildren().size() - 1));
              }
            });

    algoVersion.getItems().addAll("A*", "BFS", "DFS", "Dijkstra");
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

    favCallStuff();
    blockedCallStuff();
    getSearchHistory(GlobalDb.getConnection());
  }

  public static void favCallStuff() {
    favList.clear();
    if (favList.size()
        != FDatabaseTables.getNodeTable()
            .fetchLongNameFavorites(GlobalDb.getConnection(), HomeController.username)
            .size()) {
      favList =
          FDatabaseTables.getNodeTable()
              .fetchLongNameFavorites(GlobalDb.getConnection(), HomeController.username);
      favoriteCell.getChildren().clear();
      for (String fav : favList) {
        TreeItem<String> favorite = new TreeItem<>(fav);
        favoriteCell.getChildren().add(favorite);
      }
    }
  }

  public static void blockedCallStuff() {
    blockedList.clear();
    if (blockedList.size()
        != FDatabaseTables.getNodeTable().fetchLongNameBlocked(GlobalDb.getConnection()).size()) {
      blockedList = FDatabaseTables.getNodeTable().fetchLongNameBlocked(GlobalDb.getConnection());
      blockedCell.getChildren().clear();
      for (String block : blockedList) {
        TreeItem<String> blocked = new TreeItem<>(block);
        blockedCell.getChildren().add(blocked);
      }
    }
  }

  public static VBox longNameMenu(String str, ArrayList<String> list, JFXTextField textField) {
    VBox nameMenu = new VBox();
    nameMenu.setStyle(
        "-fx-background-color: #ffffff; -fx-border-color: #000000; -fx-border-width: 1");
    nameMenu.setAlignment(Pos.CENTER);
    for (String hmm : list) {
      if (!str.replace(" ", "").isEmpty() && hmm.toUpperCase().contains(str.toUpperCase())) {
        Label text = new Label(hmm);
        text.setOnMouseClicked(
            new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent event) {
                textField.setText(text.getText());
                nameMenu.setVisible(false);
                nameMenu.setDisable(true);
              }
            });
        nameMenu.getChildren().add(text);
      }
      if (nameMenu.getChildren().size() > 5) {
        break;
      }
    }
    return nameMenu;
  }

  public void changePathFinderAlgo(String algo) {
    if (algo.equals("BFS")) mapController.algorithm.setAlgorithm(new singleBFS());
    else if (algo.equals("DFS")) mapController.algorithm.setAlgorithm(new singleDFS());
    else if (algo.equals("Dijkstra")) mapController.algorithm.setAlgorithm(new Dijkstras());
    else mapController.algorithm.setAlgorithm(new aStar());
  }

  public void setMapController(MapController mapController) {
    this.mapController = mapController;
  }

  public void treeViewSetup() {

    TreeItem<String> directoryRoot = new TreeItem<>("Directory");
    TreeItem<String> parking = new TreeItem<>("Parking");
    TreeItem<String> elevator = new TreeItem<>("Elevator");
    TreeItem<String> restroom = new TreeItem<>("Restroom");
    TreeItem<String> stairs = new TreeItem<>("Stairs");
    TreeItem<String> department = new TreeItem<>("Department");
    TreeItem<String> laboratory = new TreeItem<>("Laboratory");
    TreeItem<String> information = new TreeItem<>("Information");
    TreeItem<String> conference = new TreeItem<>("Conference");
    TreeItem<String> exit = new TreeItem<>("Entrance/Exit");
    TreeItem<String> retail = new TreeItem<>("Retail");
    TreeItem<String> service = new TreeItem<>("Service");

    directoryTreeView.setRoot(directoryRoot);

    directoryTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    directoryTreeView.setStyle("-fx-padding: 0; -fx-background-insets: 0");

    try {
      ImageView parkImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/parkingpin.png")));
      parkImage.setFitWidth(15);
      parkImage.setFitHeight(15);
      parking.setGraphic(parkImage);

      ImageView elevImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/elevatorpin.png")));
      elevImage.setFitWidth(15);
      elevImage.setFitHeight(15);
      elevator.setGraphic(elevImage);

      ImageView restImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/restroompins.png")));
      restImage.setFitWidth(15);
      restImage.setFitHeight(15);
      restroom.setGraphic(restImage);

      ImageView stairImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/stairspin.png")));
      stairImage.setFitWidth(15);
      stairImage.setFitHeight(15);
      stairs.setGraphic(stairImage);

      ImageView deptImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/deptpins.png")));
      deptImage.setFitWidth(15);
      deptImage.setFitHeight(15);
      department.setGraphic(deptImage);

      ImageView labImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/labspin.png")));
      labImage.setFitWidth(15);
      labImage.setFitHeight(15);
      laboratory.setGraphic(labImage);

      ImageView infoImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/infopin.png")));
      infoImage.setFitWidth(15);
      infoImage.setFitHeight(15);
      information.setGraphic(infoImage);

      ImageView confImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/conferencepin.png")));
      confImage.setFitWidth(15);
      confImage.setFitHeight(15);
      conference.setGraphic(confImage);

      ImageView exitImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/exitpin.png")));
      exitImage.setFitWidth(15);
      exitImage.setFitHeight(15);
      exit.setGraphic(exitImage);

      ImageView retailImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/retailpin.png")));
      retailImage.setFitWidth(15);
      retailImage.setFitHeight(15);
      retail.setGraphic(retailImage);

      ImageView servImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/service.png")));
      servImage.setFitWidth(15);
      servImage.setFitHeight(15);
      service.setGraphic(servImage);

      ImageView favImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/favIcon_good.png")));
      favImage.setFitWidth(15);
      favImage.setFitHeight(15);
      favoriteCell.setGraphic(favImage);

      ImageView blockedImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/blockedNode.png")));
      blockedImage.setFitWidth(15);
      blockedImage.setFitHeight(15);
      blockedCell.setGraphic(blockedImage);

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    directoryRoot
        .getChildren()
        .addAll(
            parking,
            elevator,
            restroom,
            stairs,
            department,
            laboratory,
            information,
            conference,
            exit,
            retail,
            service,
            favoriteCell,
            blockedCell);

    for (String parkingSpace : parkingList) {
      TreeItem<String> parkingLocation = new TreeItem<String>(parkingSpace);
      parking.getChildren().add(parkingLocation);
    }

    for (String elevs : elevList) {
      TreeItem<String> elevLocation = new TreeItem<String>(elevs);
      elevator.getChildren().add(elevLocation);
    }

    for (String rests : restroomList) {
      TreeItem<String> restLocation = new TreeItem<String>(rests);
      restroom.getChildren().add(restLocation);
    }

    for (String stair : stairsList) {
      TreeItem<String> stairLocation = new TreeItem<String>(stair);
      stairs.getChildren().add(stairLocation);
    }

    for (String dept : departmentList) {
      TreeItem<String> deptLocation = new TreeItem<String>(dept);
      department.getChildren().add(deptLocation);
    }

    for (String lab : labList) {
      TreeItem<String> labLocation = new TreeItem<String>(lab);
      laboratory.getChildren().add(labLocation);
    }

    for (String info : informationList) {
      TreeItem<String> infoLocation = new TreeItem<String>(info);
      information.getChildren().add(infoLocation);
    }

    for (String conf : conferenceList) {
      TreeItem<String> confLocation = new TreeItem<String>(conf);
      conference.getChildren().add(confLocation);
    }

    for (String exits : exitList) {
      TreeItem<String> exitLocation = new TreeItem<String>(exits);
      exit.getChildren().add(exitLocation);
    }

    for (String ret : retailList) {
      TreeItem<String> retailLocation = new TreeItem<String>(ret);
      retail.getChildren().add(retailLocation);
    }

    for (String serv : serviceList) {
      TreeItem<String> serviceLocation = new TreeItem<String>(serv);
      service.getChildren().add(serviceLocation);
    }

    for (String fav : favList) {
      TreeItem<String> favorite = new TreeItem<>(fav);
      favoriteCell.getChildren().add(favorite);
    }

    for (String block : blockedList) {
      TreeItem<String> blocked = new TreeItem<>(block);
      blockedCell.getChildren().add(blocked);
    }

    directoryRoot.setExpanded(true);
    directoryTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    EventHandler<MouseEvent> mouseEventHandle =
        (MouseEvent event) -> {
          try {
            handleMouseClicked(event);
          } catch (IOException e) {
            e.printStackTrace();
          }
        };

    directoryTreeView.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEventHandle);
  }

  private void handleMouseClicked(MouseEvent event) throws IOException {
    Node node = event.getPickResult().getIntersectedNode();
    // Accept clicks only on node cells, and not on empty spaces of the TreeView
    String prev = "";
    if (event.isControlDown()) {
      String clickname =
          (String) ((TreeItem) directoryTreeView.getSelectionModel().getSelectedItem()).getValue();
      for (NodeUI NUI : mapController.NODES) {
        if (NUI.getN().getLongName().equals(clickname)) {
          // System.out.println(NUI.getN().getLongName());
          if (!Targets.contains(NUI.getN())
              && !FDatabaseTables.getNodeTable()
                  .blockedContains(GlobalDb.getConnection(), NUI.getN().getNodeID())) {
            Targets.add(NUI.getN());
          }
          mapController.addNodeUI(NUI);

          if (!endField.getText().equals(NUI.getN().getLongName())) {
            startField.setText(
                mapController.getNodeUIByID(Targets.getFirst().getNodeID()).getN().getLongName());
          }

          if (Targets.size() >= 2) {
            mapController.runPathFindingDirectory(Targets);
            if (!startField
                .getText()
                .equals(
                    mapController
                        .getNodeUIByID(Targets.getLast().getNodeID())
                        .getN()
                        .getLongName())) {
              endField.setText(
                  mapController.getNodeUIByID(Targets.getLast().getNodeID()).getN().getLongName());
            }
            Targets.clear();
          }
        }
      }
    }
    if ((node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null))
        && mapController.thePath.isEmpty()) {
      String name =
          (String) ((TreeItem) directoryTreeView.getSelectionModel().getSelectedItem()).getValue();
      String type = "PARK";
      switch (name) {
        case "Directory":
          type = "ALL";
          nodeRedrawing(type);
          break;
        case "Elevator":
          type = "ELEV";
          nodeRedrawing(type);
          break;
        case "Restroom":
          type = "REST";
          nodeRedrawing(type);
          break;
        case "Stairs":
          type = "STAI";
          nodeRedrawing(type);
          break;
        case "Department":
          type = "DEPT";
          nodeRedrawing(type);
          break;
        case "Laboratory":
          type = "LABS";
          nodeRedrawing(type);
          break;
        case "Information":
          type = "INFO";
          nodeRedrawing(type);
          break;
        case "Conference":
          type = "CONF";
          nodeRedrawing(type);
          break;
        case "Entrance/Exit":
          type = "EXIT";
          nodeRedrawing(type);
          break;
        case "Retail":
          type = "RETL";
          nodeRedrawing(type);
          break;
        case "Service":
          type = "SERV";
          nodeRedrawing(type);
          break;
        case "Parking":
          type = "PARK";
          nodeRedrawing(type);
          break;
        case "Favorite":
          type = "Favorite";
          nodeRedrawing(type);
          break;
        case "Blocked":
          type = "Blocked";
          nodeRedrawing(type);
          break;
        default:
          for (NodeUI NUI : mapController.NODES) {
            if (NUI.getN().getLongName().equals(name)) {
              mapController.addNodeUI(NUI);
            }
          }
          break;
      }
    }
  }

  private void nodeRedrawing(String type) {
    mapController.clearMap();

    if (type.equals("ALL")) {
      for (NodeUI NUI : mapController.NODES) {
        if (NUI.getN().getFloor().equals(mapController.currentFloor)
            && !NUI.getN().getNodeType().equals("HALL")
            && !NUI.getN().getNodeType().equals("WALK")) {
          mapController.addNodeUI(NUI);
        }
      }
    } else if (type.equals("Favorite")) {
      for (NodeUI NUI : mapController.NODES) {
        if (NUI.getN().getFloor().equals(mapController.currentFloor)
            && !NUI.getN().getNodeType().equals("HALL")
            && !NUI.getN().getNodeType().equals("WALK")
            && FDatabaseTables.getNodeTable()
                .FavContains(
                    GlobalDb.getConnection(), NUI.getN().getNodeID(), HomeController.username)) {
          mapController.addNodeUI(NUI);
        }
      }

    } else if (type.equals("Blocked")) {
      for (NodeUI NUI : mapController.NODES) {
        if (NUI.getN().getFloor().equals(mapController.currentFloor)
            && FDatabaseTables.getNodeTable()
                .blockedContains(GlobalDb.getConnection(), NUI.getN().getNodeID())) {
          mapController.addNodeUI(NUI);
        }
      }
    } else {
      for (NodeUI NUI : mapController.NODES) {
        if (NUI.getN().getFloor().equals(mapController.currentFloor)
            && (NUI.getN().getNodeType().equals(type))) {
          mapController.addNodeUI(NUI);
        }
      }
    }
  }

  // _____________________________________Directions__________________________________________

  private String endLocation = "";
  private Font hFont = Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16.0);
  private Font pFont = Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 14.0);

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

    dirText.getChildren().clear();
    downloadText.clear();

    edu.wpi.cs3733.d21.teamD.Astar.Node start =
        initialData.getNodeByID(edges.getFirst().getStartNodeID());
    edu.wpi.cs3733.d21.teamD.Astar.Node end =
        initialData.getNodeByID(edges.getLast().getEndNodeID());
    //    dirText.getChildren().setFont(pFont);

    Text aText =
        new Text("Directions from " + start.getLongName() + " to " + end.getLongName() + ":\n");
    aText.setFont(hFont);
    dirText.getChildren().add(aText);
    downloadText.appendText(
        "Directions from " + start.getLongName() + " to " + end.getLongName() + ":\n");
    // dirText.appendText("Directions from " + start.getLongName() + " to " + end.getLongName() +
    // ":\n");
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
      edu.wpi.cs3733.d21.teamD.Astar.Node startN = initialData.getNodeByID(N.getStartNodeID());
      edu.wpi.cs3733.d21.teamD.Astar.Node endN = initialData.getNodeByID(N.getEndNodeID());

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
    // dirText.setFont(hFont);
    Text endText = new Text("\nWelcome to " + end.getLongName() + "\n");
    endText.setFont(hFont);
    dirText.getChildren().add(endText);
    downloadText.appendText("\nWelcome to " + end.getLongName() + "\n");
    // dirText.appendText("\nWelcome to " + end.getLongName() + "\n");
    // dirText.setPromptText(dirText.getText());
  }

  public String evalTurn(
      String currentDirection,
      int startX,
      int startY,
      int endX,
      int endY,
      edu.wpi.cs3733.d21.teamD.Astar.Node startNode,
      edu.wpi.cs3733.d21.teamD.Astar.Node endNode) {
    String newDirection = "";
    int deltaX = endX - startX;
    int deltaY = endY - startY;

    // add handling for changing floors
    if (startNode.getNodeType().equals("ELEV") && endNode.getNodeType().equals("ELEV")) {
      Text elvText =
          new Text("\t" + "Take the elevator towards floor " + endNode.getFloor() + "\n");
      elvText.setFont(pFont);
      //      dirText.getChildren().add(elvText);
      downloadText.appendText(
          "\t" + "Take the elevator towards floor " + endNode.getFloor() + "\n");
      try {
        ImageView EleImage =
            new ImageView(new Image(new FileInputStream("src/main/resources/Images/elevator.png")));
        EleImage.setFitHeight(30);
        EleImage.setFitWidth(30);
        dirText.getChildren().addAll(EleImage, elvText);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      // dirText.setFont(hFont);
      // dirText.appendText("Take the elevator towards floor " + endNode.getFloor() + "\n");
      return "In elevator";

    } else if (startNode.getNodeType().equals("ELEV") && !endNode.getNodeType().equals("ELEV")) {
      newDirection = firstMove(startX, startY, endX, endY, startNode, endNode);
      return newDirection;
    } else if (startNode.getNodeType().equals("STAI") && endNode.getNodeType().equals("STAI")) {
      //  dirText.setFont(hFont);
      Text text = new Text("\t" + "Take the stairs towards floor " + endNode.getFloor() + "\n");
      text.setFont(pFont);
      downloadText.appendText("\t" + "Take the stairs towards floor " + endNode.getFloor() + "\n");
      try {
        ImageView Image =
            new ImageView(new Image(new FileInputStream("src/main/resources/Images/stairs.png")));
        Image.setFitHeight(30);
        Image.setFitWidth(30);
        dirText.getChildren().addAll(Image, text);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
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
      // dirText.setFont(pFont);
      Text lText = new Text("\tTurn Left towards: \n\t\t" + endNode.getLongName() + "\n");
      lText.setFont(pFont);
      downloadText.appendText("\tTurn Left towards: \n\t\t" + endNode.getLongName() + "\n");
      try {
        ImageView Image =
            new ImageView(new Image(new FileInputStream("src/main/resources/Images/left.png")));
        Image.setFitHeight(30);
        Image.setFitWidth(30);
        dirText.getChildren().addAll(Image, lText);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      // dirText.appendText("\tTurn Left towards: \n\t\t" + endNode.getLongName() + "\n");

    }
    // Turn Right
    else if ((currentDirection.equals("North") && newDirection.equals("East"))
        || (currentDirection.equals("East") && newDirection.equals("South"))
        || (currentDirection.equals("South") && newDirection.equals("West"))
        || (currentDirection.equals("West") && newDirection.equals("North"))) {
      // dirText.setFont(pFont);
      Text rText = new Text("\tTurn Right towards: \n\t\t" + endNode.getLongName() + "\n");
      rText.setFont(pFont);
      downloadText.appendText("\tTurn Right towards: \n\t\t" + endNode.getLongName() + "\n");
      try {
        ImageView Image =
            new ImageView(new Image(new FileInputStream("src/main/resources/Images/right.png")));
        Image.setFitHeight(30);
        Image.setFitWidth(30);
        dirText.getChildren().addAll(Image, rText);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      // dirText.appendText("\tTurn Right towards: \n\t\t" + endNode.getLongName() + "\n");
    }
    // Continue Straight
    else if (currentDirection.equals(newDirection)) {
      if (!(startNode.getNodeType().equals("HALL") && endNode.getNodeType().equals("HALL"))) {
        // dirText.setFont(pFont);
        Text sText = new Text("\tContinue Straight towards: \n\t\t" + endNode.getLongName() + "\n");
        sText.setFont(pFont);
        downloadText.appendText(
            "\tContinue Straight towards: \n\t\t" + endNode.getLongName() + "\n");
        try {
          ImageView Image =
              new ImageView(new Image(new FileInputStream("src/main/resources/Images/up.png")));
          Image.setFitHeight(30);
          Image.setFitWidth(30);
          dirText.getChildren().addAll(Image, sText);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        // dirText.appendText("\tContinue Straight towards: \n\t\t" + endNode.getLongName() + "\n");
      }
    }

    return newDirection;
  }

  public String firstMove(
      int startX,
      int startY,
      int endX,
      int endY,
      edu.wpi.cs3733.d21.teamD.Astar.Node startNode,
      edu.wpi.cs3733.d21.teamD.Astar.Node endNode) {
    int deltaX = endX - startX;
    int deltaY = endY - startY;

    // add handling for changing floors
    if (startNode.getNodeType().equals("ELEV") && endNode.getNodeType().equals("ELEV")) {
      // dirText.setFont(hFont);
      Text text = new Text("Take the elevator towards floor " + endNode.getFloor() + "\n");
      text.setFont(pFont);
      try {
        ImageView EleImage =
            new ImageView(new Image(new FileInputStream("src/main/resources/Images/elevator.png")));
        EleImage.setFitHeight(30);
        EleImage.setFitWidth(30);
        //        dirText.getChildren().addAll(EleImage, text);
        dirText.getChildren().add(EleImage);
        System.out.println("this is adding elevatorImage");
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      return "In elevator";
    } else if (startNode.getNodeType().equals("STAI") && endNode.getNodeType().equals("STAI")) {
      // dirText.setFont(hFont);
      Text text = new Text("Take the stairs towards floor " + endNode.getFloor() + "\n");
      text.setFont(pFont);
      downloadText.appendText("Take the stairs towards floor " + endNode.getFloor() + "\n");
      try {
        ImageView Image =
            new ImageView(new Image(new FileInputStream("src/main/resources/Images/stairs.png")));
        Image.setFitHeight(30);
        Image.setFitWidth(30);
        dirText.getChildren().addAll(Image, text);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      return "In stairs";
    } else {
      // North
      if ((deltaY < 0) && (Math.abs(deltaY) > Math.abs(deltaX))) {
        //        System.out.println("Head North towards " + endNode.getLongName());
        Text text = new Text("\tHead North towards: \n\t\t" + endNode.getLongName() + "\n");
        text.setFont(pFont);
        downloadText.appendText("\tHead North towards: \n\t\t" + endNode.getLongName() + "\n");
        try {
          ImageView Image =
              new ImageView(
                  new Image(new FileInputStream("src/main/resources/Images/north_bg.png")));
          Image.setFitHeight(30);
          Image.setFitWidth(30);
          dirText.getChildren().addAll(Image, text);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        //   dirText.setFont(pFont);
        //        dirText.appendText("\tHead North towards: \n\t\t" + endNode.getLongName() + "\n");
        return "North";
      }
      // South
      else if ((deltaY > 0) && (deltaY > Math.abs(deltaX))) {
        //        System.out.println("Head South towards " + endNode.getLongName());
        Text text = new Text("\tHead South towards: \n\t\t" + endNode.getLongName() + "\n");
        text.setFont(pFont);
        downloadText.appendText("\tHead South towards: \n\t\t" + endNode.getLongName() + "\n");
        try {
          ImageView Image =
              new ImageView(
                  new Image(new FileInputStream("src/main/resources/Images/south_bg.png")));
          Image.setFitHeight(30);
          Image.setFitWidth(30);
          dirText.getChildren().addAll(Image, text);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        return "South";
      }
      // East
      else if ((deltaX > 0) && (deltaX > Math.abs(deltaY))) {
        //        System.out.println("Head East towards " + endNode.getLongName());
        Text text = new Text("\tHead East towards: \n\t\t" + endNode.getLongName() + "\n");
        text.setFont(pFont);
        downloadText.appendText("\tHead East towards: \n\t\t" + endNode.getLongName() + "\n");
        try {
          ImageView Image =
              new ImageView(
                  new Image(new FileInputStream("src/main/resources/Images/east_bg.png")));
          Image.setFitHeight(30);
          Image.setFitWidth(30);
          dirText.getChildren().addAll(Image, text);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        return "East";
      }
      // West
      else if ((deltaX < 0) && (Math.abs(deltaX) > Math.abs(deltaY))) {
        //        System.out.println("Head West towards " + endNode.getLongName());
        Text text = new Text("\tHead West towards: \n\t\t" + endNode.getLongName() + "\n");
        text.setFont(pFont);
        downloadText.appendText("\tHead West towards: \n\t\t" + endNode.getLongName() + "\n");
        try {
          ImageView Image =
              new ImageView(
                  new Image(new FileInputStream("src/main/resources/Images/west_bg.png")));
          Image.setFitHeight(30);
          Image.setFitWidth(30);
          dirText.getChildren().addAll(Image, text);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        return "West";
      } else {
        //        System.out.println("Error determining turn direction towards " +
        // endNode.getLongName());
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

        directions.write(downloadText.getText());

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
        // .getChildren().add(downloadedStackPane);
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

  public void tableSetup() {}

  // -------------Search History-----------------

  public String recentStart = "";
  public String recentEnd = "";

  public void setSearchHistory(Connection conn, String startName, String endName) {

    PreparedStatement stmt = null;
    try {
      System.out.println("set stuff-----" + startName + endName);
      stmt = conn.prepareStatement("INSERT INTO SearchHistory VALUES (?, ?)");
      stmt.setString(1, startName);
      stmt.setString(2, endName);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getSearchHistory(Connection conn) {
    Statement stmt = null;
    ResultSet rs = null;

    try {
      stmt = conn.createStatement();

      String query = "SELECT * FROM SearchHistory";

      rs = stmt.executeQuery(query);

      // conn.setAutoCommit(false);

      while (rs.next()) {
        recentStart = rs.getString("startName");
        recentEnd = rs.getString("endName");
        System.out.println("start-" + rs.getString("startName"));
        System.out.println("end-" + rs.getString("endName"));
      }
      rs.close();

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    // Initialize search menus with most recent searches
    System.out.println(recentStart + "-------------------");
    System.out.println(recentEnd + "----------------------");
    if (HomeController.historyTracker == 1) {
      startField.setText(recentStart);
      endField.setText(recentEnd);
    }
    // start_choice.setPromptText(recentStart);
    // end_choice.setPromptText(recentEnd);
  }
}
