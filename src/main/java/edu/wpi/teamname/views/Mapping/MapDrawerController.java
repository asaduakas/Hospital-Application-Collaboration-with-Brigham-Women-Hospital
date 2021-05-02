package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;

public class MapDrawerController {
  @FXML private JFXTreeTableView directoryTreeTable;
  @FXML private JFXTreeTableView directionTreeTable;

  TreeItem<String> directory = new TreeItem<>("Directory");
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

  public void initialize(URL url, ResourceBundle rb) {
    directory
        .getChildren()
        .setAll(
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
  }
}
