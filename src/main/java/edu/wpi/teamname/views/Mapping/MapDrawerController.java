package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.*;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class MapDrawerController implements Initializable {
  @FXML private TreeView<String> directoryTreeView;
  @FXML private JFXTextField startField;
  @FXML private JFXTextField endField;
  @FXML private JFXButton findPathButton;
  private ObservableList<CategoryNodeInfo> parkingData =
      FDatabaseTables.getNodeTable().getCategory(GlobalDb.getConnection(), "PAR");
  private ArrayList<String> parkingList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "PARK");

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

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    TreeItem<String> root = new TreeItem<String>("Directory");
    directoryTreeView.setRoot(root);
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

    root.getChildren()
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

    root.setExpanded(true);
    directoryTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  public void tableSetup() {}
}
