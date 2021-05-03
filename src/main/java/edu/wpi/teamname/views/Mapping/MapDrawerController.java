package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.*;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public class MapDrawerController {
  @FXML private JFXTreeTableView<CategoryNodeInfo> directoryTreeTable;
  @FXML private JFXTextField startField;
  @FXML private JFXTextField endField;
  @FXML private JFXButton findPathButton;
  @FXML private ObservableList<CategoryNodeInfo> data;

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

    tableSetup();

//    JFXTreeTableCell

//    column.setCellValueFactory(
//        new Callback<TreeTableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {
//          @Override
//          public ObservableValue<String> call(
//              TreeTableColumn.CellDataFeatures<String, String> param) {
//            return new SimpleStringProperty(param.getValue().getValue());
//          }
//        });

//    directory
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
//    TreeItem<CategoryNodeInfo> root = new RecursiveTreeItem<>(CategoryNodeInfo, RecursiveTreeObject::getChildren);
  }

  private ObservableList<CategoryNodeInfo> getData(){
    data = FXCollections.observableArrayList();


    return data;
  }

  public void tableSetup(){
    JFXTreeTableColumn<CategoryNodeInfo, String> column = new JFXTreeTableColumn<>("Directory");
    column.setPrefWidth(248);
    directoryTreeTable.getColumns().setAll(column);
  }
}
