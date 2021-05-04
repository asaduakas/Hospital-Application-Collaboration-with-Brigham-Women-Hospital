package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.*;
import edu.wpi.teamname.Astar.Dijkstras;
import edu.wpi.teamname.Astar.aStar;
import edu.wpi.teamname.Astar.singleBFS;
import edu.wpi.teamname.Astar.singleDFS;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.VBox;
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

  @FXML
  public void tiasSpecialFunction() throws IOException {
    String startText = startField.getText();
    String endText = endField.getText();
    //    String longName1 = "Neuroscience Waiting Room";
    //    String longName2 = "Emergency Department Entrance";
    System.out.println(startText);
    System.out.println(endText);
    String startNode = mapController.getNodeUIByLongName(startText).getN().getNodeID();
    String endNode = mapController.getNodeUIByLongName(endText).getN().getNodeID();
    mapController.runPathFindingDirectory(startNode, endNode);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    directoryTreeView
        .getSelectionModel()
        .setSelectionMode(SelectionMode.MULTIPLE); // Setting SelectionMode to MULTIPLE

    JFXTextArea textDirection = new JFXTextArea();
    Label textDirectionLabel = new Label("Text Direction");

    treeViewSetup();

    EventHandler<MouseEvent> mouseEventHandle =
        (MouseEvent event) -> {
          handleMouseClicked(event);
        };

    directoryTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);

    ArrayList<String> longNames =
        FDatabaseTables.getNodeTable().fetchLongNameNoHall(GlobalDb.getConnection());
    String[] str = {"Hi", "Hello", "adkfljafiowe"};

    startGrid.setPickOnBounds(false);

    startField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (startGrid.getChildren().size() > 1) {
                startGrid.getChildren().remove(1);
              }
              startGrid.add(longNameMenu(newValue, longNames, startField), 0, 1);
            });

    endField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (endGrid.getChildren().size() > 1) {
                endGrid.getChildren().remove(1);
              }
              endGrid.add(longNameMenu(newValue, longNames, endField), 0, 1);
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

  private void handleMouseClicked(MouseEvent event) {
    Node node = event.getPickResult().getIntersectedNode();
    // Accept clicks only on node cells, and not on empty spaces of the TreeView
    if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
      String name =
          (String) ((TreeItem) directoryTreeView.getSelectionModel().getSelectedItem()).getValue();
      System.out.println("Nodsssssssssssssssssssssssssssssssse click: " + name);
      String type = "PARK";
      switch (name) {
        case "Directory":
          System.out.println(mapController.getCurrentFloor());
          mapController.drawNodeFloor(mapController.getCurrentFloor());
          type = "ALL";
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
        case "Entrance/Exit":
          type = "EXIT";
          break;
        case "Retail":
          type = "RETL";
          break;
        case "Service":
          type = "SERV";
          break;
        case "Parking":
          type = "PARK";
          break;
          //        default:
          //          for (NodeUI nodess : mapController.NODES) {
          //            if (nodess.getN().getLongName().equals(name)) {
          //              mapController.clearMap();
          //              mapController.addNode(nodess);
          //            }
          //          }
      }
      nodeRedrawing(type);
      if (mapController.getNodeUIByLongName(name) != null) {
        if (nodesClicked.size() >= 2) {
          nodesClicked.clear();
        }
        if (!(nodesClicked.contains(name))) nodesClicked.add(name);
        for (String nodeName : nodesClicked) {
          if ((nodesClicked.size() == 1) && !(nodesClicked.size() == 0)) {
            String firstClicked = nodesClicked.getFirst();
            startField.setText(firstClicked);
          } else {
            String lastClicked = nodesClicked.getLast();
            endField.setText(lastClicked);
          }
        }
      }
      System.out.println(nodesClicked.size());
    }
  }

  private void nodeRedrawing(String type) {
    mapController.clearMap();

    if (type == "ALL") {
      mapController.drawNodeFloor(mapController.getCurrentFloor());
    } else {
      for (NodeUI NUI : mapController.NODES) {
        if (NUI.getN().getFloor().equals(mapController.currentFloor)
            && (NUI.getN().getNodeType().equals(type))) {
          mapController.addNodeUI(NUI);
        }
      }
    }
  }

  public void changePathFinderAlgo(String algo) {
    if (algo.equals("BFS")) mapController.algorithm.setAlgorithm(new singleBFS());
    else if (algo.equals("DFS")) mapController.algorithm.setAlgorithm(new singleDFS());
    else if (algo.equals("Dijkstra")) mapController.algorithm.setAlgorithm(new Dijkstras());
    else mapController.algorithm.setAlgorithm(new aStar());
  }

//  public void setMapController(MapController mapController) {
//    this.mapController = mapController;
//  }

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
    directoryTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    //    if (parking.isExpanded()) {
    //      MapController.NODES.
    //    }

    EventHandler<MouseEvent> mouseEventHandle =
        (MouseEvent event) -> {
          handleMouseClicked(event);
        };

    directoryTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
  }

  private void handleMouseClicked(MouseEvent event) {
    Node node = event.getPickResult().getIntersectedNode();
    // Accept clicks only on node cells, and not on empty spaces of the TreeView
    if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
      String name =
          (String) ((TreeItem) directoryTreeView.getSelectionModel().getSelectedItem()).getValue();
      System.out.println("Node click: " + name);
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
          mapController.clearMap();
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
        if (NUI.getN().getFloor().equals(mapController.currentFloor)) {
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

  public void setMapController(MapController mapController) {
    this.mapController = mapController;
  }

  public void tableSetup() {}
}
