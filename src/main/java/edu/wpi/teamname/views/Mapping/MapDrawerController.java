package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

public class MapDrawerController {
  @FXML private JFXTreeTableView directoryTreeTable;
  @FXML private JFXTextField startField;
  @FXML private JFXTextField endField;
  @FXML private JFXButton findPathButton;
  @FXML private ObservableList<CategoryNodeInfo> data;
  //  private JFXTreeTableColumn<String, String> column = new JFXTreeTableColumn<>("Directory");

  public void initialize(URL url, ResourceBundle rb) {

    tableSetup();

    //    JFXTreeTableCell

    //    column.setCellValueFactory(
    //        new Callback<TreeTableColumn.CellDataFeatures<String, String>,
    // ObservableValue<String>>() {
    //          @Override
    //          public ObservableValue<String> call(
    //              TreeTableColumn.CellDataFeatures<String, String> param) {
    //            return new SimpleStringProperty(param.getValue().getValue());
    //          }
    //        });

    //        directory
    //            .getChildren()
    //            .setAll(
    //                parking,
    //                elevator,
    //                restroom,
    //                stairs,
    //                department,
    //                laboratory,
    //                information,
    //                conference,
    //                exit,
    //                retail,
    //                service);
    //    TreeItem<CategoryNodeInfo> root = new RecursiveTreeItem<>(CategoryNodeInfo,
    // RecursiveTreeObject::getChildren);
  }

  private ObservableList<CategoryNodeInfo> getData() {
    data = FXCollections.observableArrayList();

    return data;
  }

  public void tableSetup() {
    JFXTreeTableColumn<String, String> column = new JFXTreeTableColumn<>("Directory");
    column.setPrefWidth(248);
    column.setCellValueFactory(new TreeItemPropertyValueFactory<>("directory"));

    directoryTreeTable.getColumns().setAll(column);

    directoryTreeTable.setPlaceholder(new Label("No table here"));
    //    TreeItem<String> locations = new TreeItem<>("Locations");
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

    directoryTreeTable.setRoot(parking);
    directoryTreeTable.setRoot(elevator);
    directoryTreeTable.setRoot(restroom);
    directoryTreeTable.setRoot(stairs);
    directoryTreeTable.setRoot(department);
    directoryTreeTable.setRoot(laboratory);
    directoryTreeTable.setRoot(information);
    directoryTreeTable.setRoot(conference);
    directoryTreeTable.setRoot(exit);
    directoryTreeTable.setRoot(retail);
    directoryTreeTable.setRoot(service);

    JFXTreeTableColumn<String, String> column1 = new JFXTreeTableColumn<String, String>("tester");
    JFXTreeTableColumn<String, String> column2 = new JFXTreeTableColumn<String, String>("tester1");
    // ObservableList<String> employList =
    // FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    column.setCellValueFactory(new TreeItemPropertyValueFactory<>("directory"));

    directoryTreeTable.setEditable(true);
    column1.setEditable(false);
    column2.setEditable(false);

    directoryTreeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    directoryTreeTable.getColumns().setAll(column, column1);

    //    locations
    //        .getChildren()
    //        .setAll(
    //            parking,
    //            elevator,
    //            restroom,
    //            stairs,
    //            department,
    //            laboratory,
    //            information,
    //            conference,
    //            exit,
    //            retail,
    //            service);

    //    directoryTreeTable.setRoot(locations);
  }
}
