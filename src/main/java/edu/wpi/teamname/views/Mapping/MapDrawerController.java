package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.*;
import edu.wpi.teamname.Astar.*;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MapDrawerController implements Initializable {
  @FXML private JFXTreeView<String> directoryTreeView;
  @FXML private JFXTextField startField;
  @FXML private JFXTextField endField;
  @FXML private JFXButton findPathButton;
  @FXML private VBox vBox;
  @FXML private GridPane startGrid;
  @FXML private GridPane endGrid;
  @FXML private JFXComboBox<String> algoVersion;
  @FXML private JFXButton exportBut;
  @FXML private JFXButton importBut;
  @FXML JFXButton dirBtn;
  @FXML JFXTextArea dirText;
  private LinkedList<edu.wpi.teamname.Astar.Node> Targets = new LinkedList<>();
  //  private ObservableList<CategoryNodeInfo> parkingData =
  //      FDatabaseTables.getNodeTable().getCategory(GlobalDb.getConnection(), "PAR");


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
  private Node textDirection;
  private RoomGraph initialData = new RoomGraph(GlobalDb.getConnection());

  @FXML
  public void tiasSpecialFunction() throws IOException {
    String startText = startField.getText();
    String endText = endField.getText();
    //    String longName1 = "Neuroscience Waiting Room";
    //    String longName2 = "Emergency Department Entrance";
    System.out.println(startText);
    System.out.println(endText);

    Targets.add(mapController.getNodeUIByLongName(startText).getN());
    Targets.add(mapController.getNodeUIByLongName(endText).getN());

    mapController.runPathFindingDirectory(Targets);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {

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

  //  private void handleMouseClicked(MouseEvent event) {
  //    Node node = event.getPickResult().getIntersectedNode();
  //    // Accept clicks only on node cells, and not on empty spaces of the TreeView
  //    if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() !=
  // null)) {
  //      String name =
  //          (String) ((TreeItem)
  // directoryTreeView.getSelectionModel().getSelectedItem()).getValue();
  //      System.out.println("Nodsssssssssssssssssssssssssssssssse click: " + name);
  //      String type = "PARK";
  //      switch (name) {
  //        case "Directory":
  //          System.out.println(mapController.getCurrentFloor());
  //          mapController.drawNodeFloor(mapController.getCurrentFloor());
  //          type = "ALL";
  //          break;
  //        case "Elevator":
  //          type = "ELEV";
  //          break;
  //        case "Restroom":
  //          type = "REST";
  //          break;
  //        case "Stairs":
  //          type = "STAI";
  //          break;
  //        case "Department":
  //          type = "DEPT";
  //          break;
  //        case "Laboratory":
  //          type = "LABS";
  //          break;
  //        case "Information":
  //          type = "INFO";
  //          break;
  //        case "Conference":
  //          type = "CONF";
  //          break;
  //        case "Entrance/Exit":
  //          type = "EXIT";
  //          break;
  //        case "Retail":
  //          type = "RETL";
  //          break;
  //        case "Service":
  //          type = "SERV";
  //          break;
  //        case "Parking":
  //          type = "PARK";
  //          break;
  //          //        default:
  //          //          for (NodeUI nodess : mapController.NODES) {
  //          //            if (nodess.getN().getLongName().equals(name)) {
  //          //              mapController.clearMap();
  //          //              mapController.addNode(nodess);
  //          //            }
  //          //          }
  //      }
  //      nodeRedrawing(type);
  //      if (mapController.getNodeUIByLongName(name) != null) {
  //        if (nodesClicked.size() >= 2) {
  //          nodesClicked.clear();
  //        }
  //        if (!(nodesClicked.contains(name))) nodesClicked.add(name);
  //        for (String nodeName : nodesClicked) {
  //          if ((nodesClicked.size() == 1) && !(nodesClicked.size() == 0)) {
  //            String firstClicked = nodesClicked.getFirst();
  //            startField.setText(firstClicked);
  //          } else {
  //            String lastClicked = nodesClicked.getLast();
  //            endField.setText(lastClicked);
  //          }
  //        }
  //      }
  //      System.out.println(nodesClicked.size());
  //    }
  //  }

  //  private void nodeRedrawing(String type) {
  //    mapController.clearMap();
  //
  //    if (type == "ALL") {
  //      mapController.drawNodeFloor(mapController.getCurrentFloor());
  //    } else {
  //      for (NodeUI NUI : mapController.NODES) {
  //        if (NUI.getN().getFloor().equals(mapController.currentFloor)
  //            && (NUI.getN().getNodeType().equals(type))) {
  //          mapController.addNodeUI(NUI);
  //        }
  //      }
  //    }
  //  }

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
            service);

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

    directoryTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
  }

  private void handleMouseClicked(MouseEvent event) throws IOException {
    Node node = event.getPickResult().getIntersectedNode();
    // Accept clicks only on node cells, and not on empty spaces of the TreeView
    String prev = "";
    if (event.isControlDown()) {
      mapController.clearMap();
      String clickname =
          (String) ((TreeItem) directoryTreeView.getSelectionModel().getSelectedItem()).getValue();
      for (NodeUI NUI : mapController.NODES) {
        if (NUI.getN().getLongName().equals(clickname)) {
          System.out.println(NUI.getN().getLongName());
          Targets.add(NUI.getN());
          mapController.addNodeUI(NUI);

          if (Targets.size() >= 2) {
            LinkedList<Edge> ThePath = mapController.runPathFindingDirectory(Targets);
            startField.setText(
                mapController.getNodeUIByID(ThePath.get(0).getStartNodeID()).getN().getLongName());
            endField.setText(
                mapController
                    .getNodeUIByID(ThePath.get(ThePath.size() - 1).getEndNodeID())
                    .getN()
                    .getLongName());
          }
        }
      }
    }
    if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
      String name =
          (String) ((TreeItem) directoryTreeView.getSelectionModel().getSelectedItem()).getValue();
      //      System.out.println("Node click: " + name);
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
    } else {
      for (NodeUI NUI : mapController.NODES) {
        if (NUI.getN().getFloor().equals(mapController.currentFloor)
            && (NUI.getN().getNodeType().equals(type))) {
          mapController.addNodeUI(NUI);
        }
      }
    }
  }



  // --------------------------------Text Direction-----------------------------------------

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

    edu.wpi.teamname.Astar.Node start = initialData.getNodeByID(edges.getFirst().getStartNodeID());
    edu.wpi.teamname.Astar.Node end = initialData.getNodeByID(edges.getLast().getEndNodeID());
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
      edu.wpi.teamname.Astar.Node startN = initialData.getNodeByID(N.getStartNodeID());
      edu.wpi.teamname.Astar.Node endN = initialData.getNodeByID(N.getEndNodeID());

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
      edu.wpi.teamname.Astar.Node startNode,
      edu.wpi.teamname.Astar.Node endNode) {
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
      int startX,
      int startY,
      int endX,
      int endY,
      edu.wpi.teamname.Astar.Node startNode,
      edu.wpi.teamname.Astar.Node endNode) {
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
        //        mainAnchor.getChildren().add(downloadedStackPane);
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
}
