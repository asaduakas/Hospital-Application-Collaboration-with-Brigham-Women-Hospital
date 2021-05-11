package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.d21.teamD.App;
import edu.wpi.cs3733.d21.teamD.Ddb.FDatabaseTables;
import edu.wpi.cs3733.d21.teamD.views.Access.AdminAccessible;
import edu.wpi.cs3733.d21.teamD.views.Access.EmployeeAccessible;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.UsersNodeInfo;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.SRControllers.AbsRequest;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.SRControllers.IRequestStatus;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class UsersController extends AbsRequest
    implements Initializable, IRequestStatus, EmployeeAccessible, AdminAccessible {

  @FXML private JFXButton backBtn;

  @FXML private JFXButton saveBtn;

  @FXML private JFXTreeTableView<UsersNodeInfo> employeeTable;

  @FXML private JFXTreeTableView<UsersNodeInfo> patientTable;

  @FXML private JFXTreeTableView<UsersNodeInfo> adminTable;

  @FXML private JFXComboBox<String> typeBox;

  @FXML private JFXListView<Label> listView;

  @FXML private JFXTextField search;

  private ObservableList<UsersNodeInfo> employeeData;

  private ObservableList<UsersNodeInfo> patientData;

  private ObservableList<UsersNodeInfo> adminData;

  private ObservableList<Node> tables = FXCollections.observableArrayList();

  private void inComTableSetup() {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    typeBox.getItems().addAll("Employees", "Patients", "Administrators");
    typeBox.setOnAction(
        e -> {
          try {
            changeTable(typeBox.getValue().toString());
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        });

    Label emp = new Label("Employees");
    Label pat = new Label("Patients");
    Label ad = new Label("Administrators");
    try {
      ImageView empImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/employeeIcon.png")));
      empImage.setFitWidth(35);
      empImage.setFitHeight(35);
      emp.setGraphic(empImage);

      ImageView patImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/patientIcon.png")));
      patImage.setFitWidth(40);
      patImage.setFitHeight(40);
      pat.setGraphic(patImage);

      ImageView adImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/adminIcon_black.png")));
      adImage.setFitHeight(40);
      adImage.setFitWidth(40);
      ad.setGraphic(adImage);

      listView.getItems().addAll(emp, pat, ad);
      listView.setStyle("-fx-padding: 0; -fx-background-insets: 0");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    listView.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            try {
              changeTable(listView.getSelectionModel().getSelectedItem().getText());
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
  }

  public void changeTable(String servType) throws IOException {
    switch (servType) {
      case "Employees":
        try {
          getEmployeeData();
          TreeItem<UsersNodeInfo> root =
              new RecursiveTreeItem<>(employeeData, RecursiveTreeObject::getChildren);
          root.setExpanded(true);
          employeeTable.setRoot(root);
          employeeTable.setShowRoot(false);
        } catch (IOException e) {
          e.printStackTrace();
        }
        patientTable.setVisible(false);
        adminTable.setVisible(false);
        employeeTable.setVisible(true);
        employeeTableSetup();
        break;
      case "Patients":
        getPatientData();
        TreeItem<UsersNodeInfo> root2 =
            new RecursiveTreeItem<>(patientData, RecursiveTreeObject::getChildren);
        root2.setExpanded(true);
        patientTable.setRoot(root2);
        patientTable.setShowRoot(false);
        employeeTable.setVisible(false);
        adminTable.setVisible(false);
        patientTable.setVisible(true);
        patientTableSetup();
        break;
      case "Administrators":
        getAdminData();
        TreeItem<UsersNodeInfo> root3 =
            new RecursiveTreeItem<>(adminData, RecursiveTreeObject::getChildren);
        root3.setExpanded(true);
        adminTable.setRoot(root3);
        adminTable.setShowRoot(false);
        patientTable.setVisible(false);
        employeeTable.setVisible(false);
        adminTable.setVisible(true);
        adminTableSetup();
        break;
      default:
        employeeTable.showRootProperty();
    }
  }

  @FXML
  public void goHome(MouseEvent e) {
    List<Node> childrenList = App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
    VBox buttonBox = (VBox) childrenList.get(2);
    buttonBox.setVisible(true);
    ControllerManager.exitPopup();
  }

  private ObservableList<UsersNodeInfo> getEmployeeData() throws IOException {
    employeeData = FXCollections.observableArrayList();
    return FDatabaseTables.getUserTable().addIntoEmployeeDataList(employeeData);
  }

  private ObservableList<UsersNodeInfo> getPatientData() throws IOException {
    patientData = FXCollections.observableArrayList();
    // FDatabaseTables.getUserTable().addIntoPatientDataList(patientData);
    return FDatabaseTables.getUserTable().addIntoPatientDataList(patientData);
  }

  private ObservableList<UsersNodeInfo> getAdminData() throws IOException {
    adminData = FXCollections.observableArrayList();
    // FDatabaseTables.getUserTable().addIntoAdminDataList(adminData);
    return FDatabaseTables.getUserTable().addIntoAdminDataList(adminData);
  }

  @FXML
  public void saveData(ActionEvent event) {
    //    changeData(typeBox.getValue().toString());
    changeData(listView.getSelectionModel().getSelectedItem().getText());
  }

  public void changeData(String ServeType) {
    switch (ServeType) {
      case "Employees":
        FDatabaseTables.getUserTable().changeUserData(employeeData);
        break;
      case "Patients":
        FDatabaseTables.getUserTable().changeUserData(patientData);
        break;
      case "Administrators":
        FDatabaseTables.getUserTable().changeUserData(adminData);
        break;
    }
  }

  private void employeeTableSetup() {
    JFXTreeTableColumn<UsersNodeInfo, String> categoryCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("Category");
    JFXTreeTableColumn<UsersNodeInfo, String> idCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("UserID");
    JFXTreeTableColumn<UsersNodeInfo, String> passCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("Password");
    JFXTreeTableColumn<UsersNodeInfo, String> nameCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("Full Name");

    categoryCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (categoryCol.validateValue(p)) {
            return p.getValue().getValue().category;
          } else {
            return categoryCol.getComputedValue(p);
          }
        });

    idCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (idCol.validateValue(p)) {
            return p.getValue().getValue().id;
          } else {
            return idCol.getComputedValue(p);
          }
        });
    passCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (passCol.validateValue(p)) {
            return p.getValue().getValue().password;
          } else {
            return passCol.getComputedValue(p);
          }
        });
    nameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (nameCol.validateValue(p)) {
            return p.getValue().getValue().name;
          } else {
            return nameCol.getComputedValue(p);
          }
        });
    employeeTable.setEditable(true);
    categoryCol.setEditable(false);
    idCol.setEditable(false);
    nameCol.setEditable(false);

    employeeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    employeeTable.getColumns().setAll(categoryCol, idCol, passCol, nameCol);

    employeeTable.setPlaceholder(new Label("There are currently no employees"));
  }

  private void patientTableSetup() {
    JFXTreeTableColumn<UsersNodeInfo, String> categoryCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("Category");
    JFXTreeTableColumn<UsersNodeInfo, String> idCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("UserID");
    JFXTreeTableColumn<UsersNodeInfo, String> passCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("Password");
    JFXTreeTableColumn<UsersNodeInfo, String> nameCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("Full Name");
    JFXTreeTableColumn<UsersNodeInfo, String> clearanceCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("COVID-19 Clearance");

    categoryCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (categoryCol.validateValue(p)) {
            return p.getValue().getValue().category;
          } else {
            return categoryCol.getComputedValue(p);
          }
        });
    idCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (idCol.validateValue(p)) {
            return p.getValue().getValue().id;
          } else {
            return idCol.getComputedValue(p);
          }
        });
    passCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (passCol.validateValue(p)) {
            return p.getValue().getValue().password;
          } else {
            return passCol.getComputedValue(p);
          }
        });
    nameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (nameCol.validateValue(p)) {
            return p.getValue().getValue().name;
          } else {
            return nameCol.getComputedValue(p);
          }
        });
    clearanceCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (clearanceCol.validateValue(p)) {
            return p.getValue().getValue().clearance;
          } else {
            return clearanceCol.getComputedValue(p);
          }
        });
    clearanceCol.setCellFactory(
        ComboBoxTreeTableCell.forTreeTableColumn("Cleared", "emergencyEntrance", "normalEntrance"));

    patientTable.setEditable(true);
    categoryCol.setEditable(false);
    idCol.setEditable(false);
    nameCol.setEditable(false);

    patientTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    patientTable.getColumns().setAll(categoryCol, idCol, passCol, nameCol, clearanceCol);

    patientTable.setPlaceholder(new Label("There are currently no patients"));
  }

  private void adminTableSetup() {
    JFXTreeTableColumn<UsersNodeInfo, String> categoryCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("Category");
    JFXTreeTableColumn<UsersNodeInfo, String> idCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("UserID");
    JFXTreeTableColumn<UsersNodeInfo, String> passCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("Password");
    JFXTreeTableColumn<UsersNodeInfo, String> nameCol =
        new JFXTreeTableColumn<UsersNodeInfo, String>("Full Name");

    categoryCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (categoryCol.validateValue(p)) {
            return p.getValue().getValue().category;
          } else {
            return categoryCol.getComputedValue(p);
          }
        });

    idCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (idCol.validateValue(p)) {
            return p.getValue().getValue().id;
          } else {
            return idCol.getComputedValue(p);
          }
        });

    passCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (passCol.validateValue(p)) {
            return p.getValue().getValue().password;
          } else {
            return passCol.getComputedValue(p);
          }
        });

    nameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UsersNodeInfo, String> p) -> {
          if (nameCol.validateValue(p)) {
            return p.getValue().getValue().name;
          } else {
            return nameCol.getComputedValue(p);
          }
        });

    adminTable.setEditable(true);
    categoryCol.setEditable(false);
    idCol.setEditable(false);
    nameCol.setEditable(false);

    adminTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    adminTable.getColumns().setAll(categoryCol, idCol, passCol, nameCol);

    adminTable.setPlaceholder(new Label("There are currently no administrators"));
  }
}
