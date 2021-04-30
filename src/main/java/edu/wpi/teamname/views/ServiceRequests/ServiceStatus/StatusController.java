package edu.wpi.teamname.views.ServiceRequests.ServiceStatus;

import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.HomeController;
import edu.wpi.teamname.views.ServiceRequests.NodeInfo.*;
import edu.wpi.teamname.views.ServiceRequests.NodeInfo.ExtTransNodeInfo;
import edu.wpi.teamname.views.ServiceRequests.SRControllers.AbsRequest;
import edu.wpi.teamname.views.ServiceRequests.SRControllers.IRequestStatus;
import edu.wpi.teamname.views.ServiceRequests.ServicePageController;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
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

public class StatusController extends AbsRequest implements Initializable, IRequestStatus {

  @FXML private JFXButton backBtn;

  @FXML private JFXButton saveBtn;

  @FXML private JFXTreeTableView<ExtTransNodeInfo> tableView;

  @FXML private JFXTreeTableView<FoodDelivNodeInfo> foodTable;

  @FXML private JFXTreeTableView<AudVisNodeInfo> audVisTable;

  @FXML private JFXTreeTableView<ComputerNodeInfo> computerTable;

  @FXML private JFXTreeTableView<FacilitiesNodeInfo> facilitiesTable;

  @FXML private JFXTreeTableView<FloralDelivNodeInfo> floralTable;

  @FXML private JFXTreeTableView<InternalTransNodeInfo> internalTransTable;

  @FXML private JFXTreeTableView<LangInterpNodeInfo> langInterpTable;

  @FXML private JFXTreeTableView<LaundryNodeInfo> laundryTable;

  @FXML private JFXTreeTableView<MedDelivNodeInfo> medDelivTable;

  @FXML private JFXTreeTableView<SanitationNodeInfo> sanitationTable;

  @FXML private JFXTreeTableView<SecurityRequestNodeInfo> securityTable;

  @FXML private JFXTreeTableView<ComputerNodeInfo> comTableView;

  @FXML private JFXComboBox<String> typeBox;

  @FXML private JFXListView<Label> listView;

  @FXML private JFXTextField search;

  private ObservableList<ExtTransNodeInfo> ExTransData;

  private ObservableList<FoodDelivNodeInfo> foodData;

  private ObservableList<AudVisNodeInfo> audVisData;

  private ObservableList<ComputerNodeInfo> computerData;

  private ObservableList<FacilitiesNodeInfo> facilitiesData;

  private ObservableList<FloralDelivNodeInfo> floralData;

  private ObservableList<InternalTransNodeInfo> internalTransData;

  private ObservableList<LangInterpNodeInfo> langInterpData;

  private ObservableList<LaundryNodeInfo> laundryData;

  private ObservableList<MedDelivNodeInfo> medDelivData;

  private ObservableList<SanitationNodeInfo> sanitationData;

  private ObservableList<SecurityRequestNodeInfo> securityData;

  // Pane pane = (Pane) FXMLLoader.load(getClass().getClassLoader().getResource("StatusView"));

  private ObservableList<Node> tables = FXCollections.observableArrayList();

  private void inComTableSetup() {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    typeBox
        .getItems()
        .addAll(
            "Audio/Visual",
            "Computer Service",
            "External Transportation",
            "Facilities Maintenance",
            "Floral Delivery",
            "Food Delivery",
            "Internal Transportation",
            "Language Interpreter",
            "Laundry Service",
            "Medicine Delivery",
            "Sanitation Service",
            "Security Service");
    typeBox.setOnAction(
        e -> {
          changeTable(typeBox.getValue().toString());
        });

    Label av = new Label("Audio/Visual");
    Label cs = new Label("Computer Service");
    Label exTrans = new Label("External Transportation");
    Label faciMainten = new Label("Facilities Maintenance");
    Label floral = new Label("Floral Delivery");
    Label food = new Label("Food Delivery");
    Label inTrans = new Label("Internal Transportation");
    Label language = new Label("Language Interpreter");
    Label laundry = new Label("Laundry Service");
    Label med = new Label("Medicine Delivery");
    Label sanitize = new Label("Sanitation Service");
    Label security = new Label("Security Service");
    try {
      ImageView avImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/audio-visual.png")));
      avImage.setFitWidth(35);
      avImage.setFitHeight(35);
      av.setGraphic(avImage);

      ImageView csImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/Computer_colored.png")));
      csImage.setFitWidth(40);
      csImage.setFitHeight(40);
      cs.setGraphic(csImage);

      ImageView exTransImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/exTransIcon.png")));
      exTransImage.setFitHeight(40);
      exTransImage.setFitWidth(40);
      exTrans.setGraphic(exTransImage);

      ImageView faciMaintenImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/maintenance.png")));
      faciMaintenImage.setFitWidth(40);
      faciMaintenImage.setFitHeight(40);
      faciMainten.setGraphic(faciMaintenImage);

      ImageView floralImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/floralIcon.png")));
      floralImage.setFitWidth(40);
      floralImage.setFitHeight(40);
      floral.setGraphic(floralImage);

      ImageView foodImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/foodIcon.png")));
      foodImage.setFitWidth(40);
      foodImage.setFitHeight(40);
      food.setGraphic(foodImage);

      ImageView inTransImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/wheelchair.png")));
      inTransImage.setFitWidth(40);
      inTransImage.setFitHeight(40);
      inTrans.setGraphic(inTransImage);

      ImageView languageImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/Translate.png")));
      languageImage.setFitWidth(32);
      languageImage.setFitHeight(32);
      language.setGraphic(languageImage);

      ImageView laundryImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/laundryIcon.png")));
      laundryImage.setFitWidth(40);
      laundryImage.setFitHeight(40);
      laundry.setGraphic(laundryImage);

      ImageView medImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/medicine.png")));
      medImage.setFitWidth(40);
      medImage.setFitHeight(40);
      med.setGraphic(medImage);

      ImageView sanitizeImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/sanitatize.png")));
      sanitizeImage.setFitWidth(40);
      sanitizeImage.setFitHeight(40);
      sanitize.setGraphic(sanitizeImage);

      ImageView securityImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/Security.png")));
      securityImage.setFitWidth(40);
      securityImage.setFitHeight(40);
      security.setGraphic(securityImage);

      listView
          .getItems()
          .addAll(
              av,
              cs,
              exTrans,
              faciMainten,
              floral,
              food,
              inTrans,
              language,
              laundry,
              med,
              sanitize,
              security);
      listView.setStyle("-fx-padding: 0; -fx-background-insets: 0");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    listView.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            changeTable(listView.getSelectionModel().getSelectedItem().getText());
          }
        });
  }

  public void changeTable(String servType) {
    // Tables.dispExTransRequestsTable(GlobalDb.getConnection());
    switch (servType) {
      case "External Transportation":
        try {
          getExTransData();
          TreeItem<ExtTransNodeInfo> root =
              new RecursiveTreeItem<>(ExTransData, RecursiveTreeObject::getChildren);
          root.setExpanded(true);
          tableView.setRoot(root);
          tableView.setShowRoot(false);
        } catch (IOException e) {
          e.printStackTrace();
        }
        foodTable.setVisible(false);
        audVisTable.setVisible(false);
        computerTable.setVisible(false);
        facilitiesTable.setVisible(false);
        floralTable.setVisible(false);
        internalTransTable.setVisible(false);
        langInterpTable.setVisible(false);
        laundryTable.setVisible(false);
        medDelivTable.setVisible(false);
        sanitationTable.setVisible(false);
        securityTable.setVisible(false);
        tableView.setVisible(true);
        ExTransTableSetup();
        break;
      case "Food Delivery":
        getFoodData();
        TreeItem<FoodDelivNodeInfo> root2 =
            new RecursiveTreeItem<>(foodData, RecursiveTreeObject::getChildren);
        root2.setExpanded(true);
        foodTable.setRoot(root2);
        foodTable.setShowRoot(false);
        tableView.setVisible(false);
        audVisTable.setVisible(false);
        computerTable.setVisible(false);
        facilitiesTable.setVisible(false);
        floralTable.setVisible(false);
        internalTransTable.setVisible(false);
        langInterpTable.setVisible(false);
        laundryTable.setVisible(false);
        medDelivTable.setVisible(false);
        sanitationTable.setVisible(false);
        securityTable.setVisible(false);
        foodTable.setVisible(true);
        foodDeliTableSetup();
        break;
      case "Audio/Visual":
        getAudVisData();
        TreeItem<AudVisNodeInfo> root3 =
            new RecursiveTreeItem<>(audVisData, RecursiveTreeObject::getChildren);
        root3.setExpanded(true);
        audVisTable.setRoot(root3);
        audVisTable.setShowRoot(false);
        tableView.setVisible(false);
        foodTable.setVisible(false);
        computerTable.setVisible(false);
        facilitiesTable.setVisible(false);
        floralTable.setVisible(false);
        internalTransTable.setVisible(false);
        langInterpTable.setVisible(false);
        laundryTable.setVisible(false);
        medDelivTable.setVisible(false);
        sanitationTable.setVisible(false);
        securityTable.setVisible(false);
        audVisTable.setVisible(true);
        audVisTableSetup();
        break;
      case "Computer Service":
        getComputerData();
        TreeItem<ComputerNodeInfo> root4 =
            new RecursiveTreeItem<>(computerData, RecursiveTreeObject::getChildren);
        root4.setExpanded(true);
        computerTable.setRoot(root4);
        computerTable.setShowRoot(false);
        audVisTable.setVisible(false);
        tableView.setVisible(false);
        foodTable.setVisible(false);
        facilitiesTable.setVisible(false);
        floralTable.setVisible(false);
        internalTransTable.setVisible(false);
        langInterpTable.setVisible(false);
        laundryTable.setVisible(false);
        medDelivTable.setVisible(false);
        sanitationTable.setVisible(false);
        securityTable.setVisible(false);
        computerTable.setVisible(true);
        computerTableSetup();
        break;
      case "Facilities Maintenance":
        getFacilitiesData();
        TreeItem<FacilitiesNodeInfo> root5 =
            new RecursiveTreeItem<>(facilitiesData, RecursiveTreeObject::getChildren);
        root5.setExpanded(true);
        facilitiesTable.setRoot(root5);
        facilitiesTable.setShowRoot(false);
        audVisTable.setVisible(false);
        tableView.setVisible(false);
        foodTable.setVisible(false);
        floralTable.setVisible(false);
        internalTransTable.setVisible(false);
        langInterpTable.setVisible(false);
        laundryTable.setVisible(false);
        medDelivTable.setVisible(false);
        sanitationTable.setVisible(false);
        securityTable.setVisible(false);
        computerTable.setVisible(false);
        facilitiesTable.setVisible(true);
        facilitiesTableSetup();
        break;
      case "Floral Delivery":
        getFloralData();
        TreeItem<FloralDelivNodeInfo> root6 =
            new RecursiveTreeItem<>(floralData, RecursiveTreeObject::getChildren);
        root6.setExpanded(true);
        floralTable.setRoot(root6);
        floralTable.setShowRoot(false);
        audVisTable.setVisible(false);
        tableView.setVisible(false);
        foodTable.setVisible(false);
        internalTransTable.setVisible(false);
        langInterpTable.setVisible(false);
        laundryTable.setVisible(false);
        medDelivTable.setVisible(false);
        sanitationTable.setVisible(false);
        securityTable.setVisible(false);
        computerTable.setVisible(false);
        facilitiesTable.setVisible(false);
        floralTable.setVisible(true);
        floralTableSetup();
        break;
      case "Internal Transportation":
        getInternalTransData();
        TreeItem<InternalTransNodeInfo> root7 =
            new RecursiveTreeItem<>(internalTransData, RecursiveTreeObject::getChildren);
        root7.setExpanded(true);
        internalTransTable.setRoot(root7);
        internalTransTable.setShowRoot(false);
        audVisTable.setVisible(false);
        tableView.setVisible(false);
        foodTable.setVisible(false);
        langInterpTable.setVisible(false);
        laundryTable.setVisible(false);
        medDelivTable.setVisible(false);
        sanitationTable.setVisible(false);
        securityTable.setVisible(false);
        computerTable.setVisible(false);
        facilitiesTable.setVisible(false);
        floralTable.setVisible(false);
        internalTransTable.setVisible(true);
        internalTransTableSetup();
        break;
      case "Language Interpreter":
        getLangInterpData();
        TreeItem<LangInterpNodeInfo> root8 =
            new RecursiveTreeItem<>(langInterpData, RecursiveTreeObject::getChildren);
        root8.setExpanded(true);
        langInterpTable.setRoot(root8);
        langInterpTable.setShowRoot(false);
        audVisTable.setVisible(false);
        tableView.setVisible(false);
        foodTable.setVisible(false);
        laundryTable.setVisible(false);
        medDelivTable.setVisible(false);
        sanitationTable.setVisible(false);
        securityTable.setVisible(false);
        computerTable.setVisible(false);
        facilitiesTable.setVisible(false);
        floralTable.setVisible(false);
        internalTransTable.setVisible(false);
        langInterpTable.setVisible(true);
        langInterpTableSetup();
        break;
      case "Laundry Service":
        getLaundryData();
        TreeItem<LaundryNodeInfo> root9 =
            new RecursiveTreeItem<>(laundryData, RecursiveTreeObject::getChildren);
        root9.setExpanded(true);
        laundryTable.setRoot(root9);
        laundryTable.setShowRoot(false);
        audVisTable.setVisible(false);
        tableView.setVisible(false);
        foodTable.setVisible(false);
        medDelivTable.setVisible(false);
        sanitationTable.setVisible(false);
        securityTable.setVisible(false);
        computerTable.setVisible(false);
        facilitiesTable.setVisible(false);
        floralTable.setVisible(false);
        internalTransTable.setVisible(false);
        langInterpTable.setVisible(false);
        laundryTable.setVisible(true);
        laundryTableSetup();
        break;
      case "Medicine Delivery":
        getMedDelivData();
        TreeItem<MedDelivNodeInfo> root10 =
            new RecursiveTreeItem<>(medDelivData, RecursiveTreeObject::getChildren);
        root10.setExpanded(true);
        medDelivTable.setRoot(root10);
        medDelivTable.setShowRoot(false);
        audVisTable.setVisible(false);
        tableView.setVisible(false);
        foodTable.setVisible(false);
        sanitationTable.setVisible(false);
        securityTable.setVisible(false);
        computerTable.setVisible(false);
        facilitiesTable.setVisible(false);
        floralTable.setVisible(false);
        internalTransTable.setVisible(false);
        langInterpTable.setVisible(false);
        laundryTable.setVisible(false);
        medDelivTable.setVisible(true);
        medDelivTableSetup();
        break;
      case "Sanitation Service":
        getSanitationData();
        TreeItem<SanitationNodeInfo> root11 =
            new RecursiveTreeItem<>(sanitationData, RecursiveTreeObject::getChildren);
        root11.setExpanded(true);
        sanitationTable.setRoot(root11);
        sanitationTable.setShowRoot(false);
        audVisTable.setVisible(false);
        tableView.setVisible(false);
        foodTable.setVisible(false);
        securityTable.setVisible(false);
        computerTable.setVisible(false);
        facilitiesTable.setVisible(false);
        floralTable.setVisible(false);
        internalTransTable.setVisible(false);
        langInterpTable.setVisible(false);
        laundryTable.setVisible(false);
        medDelivTable.setVisible(false);
        sanitationTable.setVisible(true);
        sanitationTableSetup();
        break;
      case "Security Service":
        getSecurityData();
        TreeItem<SecurityRequestNodeInfo> root12 =
            new RecursiveTreeItem<>(securityData, RecursiveTreeObject::getChildren);
        root12.setExpanded(true);
        securityTable.setRoot(root12);
        securityTable.setShowRoot(false);
        audVisTable.setVisible(false);
        tableView.setVisible(false);
        foodTable.setVisible(false);
        computerTable.setVisible(false);
        facilitiesTable.setVisible(false);
        floralTable.setVisible(false);
        internalTransTable.setVisible(false);
        langInterpTable.setVisible(false);
        laundryTable.setVisible(false);
        medDelivTable.setVisible(false);
        sanitationTable.setVisible(false);
        securityTable.setVisible(true);
        securityTableSetup();
        break;
      default:
        tableView.showRootProperty();
    }
  }

  private ObservableList<ExtTransNodeInfo> getExTransData() throws IOException {

    ExTransData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM ExternalTransRequests";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        ExTransData.add(
            new ExtTransNodeInfo(
                rs.getString("id"),
                rs.getString("serviceType"),
                rs.getString("pFirstName"),
                rs.getString("pLastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("transType"),
                rs.getString("assignedTo"),
                rs.getString("status")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return ExTransData;
  }

  @FXML
  public void goHome(MouseEvent event) throws IOException {
    List<Node> childrenList = App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
    VBox buttonBox = (VBox) childrenList.get(2);
    buttonBox.setVisible(false);
    ServicePageController.popup.hide();
    popUpAction("ServicePageView.fxml", HomeController.popup, disableRequestStatus);
    //    HomeController.popup.hide();
    // App.getPrimaryStage().getScene().getRoot().setEffect(null);
    //    return ExTransData;
  }

  private ObservableList<FoodDelivNodeInfo> getFoodData() {
    foodData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM FoodDeliveryServiceRequest";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        foodData.add(
            new FoodDelivNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("specialNeeds")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return foodData;
  }

  private ObservableList<AudVisNodeInfo> getAudVisData() {
    audVisData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM AudVisServiceRequest";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        audVisData.add(
            new AudVisNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("descriptionOfProblem")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return audVisData;
  }

  private ObservableList<ComputerNodeInfo> getComputerData() {
    computerData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM ComputerServiceRequest";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        computerData.add(
            new ComputerNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("descriptionOfIssue")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return computerData;
  }

  private ObservableList<FacilitiesNodeInfo> getFacilitiesData() {
    facilitiesData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM FacilitiesServiceRequest";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        facilitiesData.add(
            new FacilitiesNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("urgencyLevel"),
                rs.getString("descriptionOfIssue")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return facilitiesData;
  }

  private ObservableList<FloralDelivNodeInfo> getFloralData() {
    floralData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM FloralRequests";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        floralData.add(
            new FloralDelivNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("pFirstName"),
                rs.getString("pLastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("typeOfFlower"),
                rs.getString("numOfFlower"),
                rs.getString("fromFlower"),
                rs.getString("assignedEmployee")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return floralData;
  }

  private ObservableList<InternalTransNodeInfo> getInternalTransData() {
    internalTransData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM InternalTransReq";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        internalTransData.add(
            new InternalTransNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("destination"),
                rs.getString("assignedEmployee"),
                rs.getString("typeOfTransport")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return internalTransData;
  }

  private ObservableList<LangInterpNodeInfo> getLangInterpData() {
    langInterpData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM LangInterpRequest";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        langInterpData.add(
            new LangInterpNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("languageRequested"),
                rs.getString("dateRequested")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return langInterpData;
  }

  private ObservableList<LaundryNodeInfo> getLaundryData() {
    laundryData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM LaundryRequest";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        laundryData.add(
            new LaundryNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return laundryData;
  }

  private ObservableList<MedDelivNodeInfo> getMedDelivData() {
    medDelivData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM MedicineDelivery";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        medDelivData.add(
            new MedDelivNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("typeOfMedicine"),
                rs.getString("dropOffDate")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return medDelivData;
  }

  private ObservableList<SanitationNodeInfo> getSanitationData() {
    sanitationData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM SanitationRequest";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        sanitationData.add(
            new SanitationNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("description"),
                rs.getString("urgencyLevel")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return sanitationData;
  }

  private ObservableList<SecurityRequestNodeInfo> getSecurityData() {
    securityData = FXCollections.observableArrayList();
    try {
      String query = "SELECT * FROM SecurityRequest";
      ResultSet rs = GlobalDb.getConnection().createStatement().executeQuery(query);
      while (rs.next()) {
        securityData.add(
            new SecurityRequestNodeInfo(
                rs.getString("id"),
                rs.getString("status"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("contactInfo"),
                rs.getString("location"),
                rs.getString("assignedEmployee"),
                rs.getString("urgencyLevel"),
                rs.getString("description")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return securityData;
  }

  @FXML
  public void saveData(ActionEvent event) {
    //    changeData(typeBox.getValue().toString());
    changeData(listView.getSelectionModel().getSelectedItem().getText());
  }

  public void changeData(String ServeType) {
    switch (ServeType) {
      case "External Transportation":
        for (ExtTransNodeInfo info : ExTransData) {
          if (!(info.getStatus().isEmpty())) {
            PreparedStatement stmt = null;
            try {
              stmt =
                  GlobalDb.getConnection()
                      .prepareStatement(
                          "UPDATE ExternalTransRequests SET status = ?, assignedTo = ? WHERE id=?");
              stmt.setString(1, info.getStatus());
              stmt.setString(2, info.getAssignedTo());
              stmt.setString(3, info.getId());
              stmt.executeUpdate();
            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
          }
        }
        break;
      case "Food Delivery":
        for (FoodDelivNodeInfo foodInfo : foodData) {
          if (!(foodInfo.getStatus().isEmpty())) {
            PreparedStatement stmt = null;
            try {
              stmt =
                  GlobalDb.getConnection()
                      .prepareStatement(
                          "UPDATE FoodDeliveryServiceRequest SET status = ?, assignedEmployee = ?, specialNeeds = ? WHERE id=?");
              stmt.setString(1, foodInfo.getStatus());
              stmt.setString(2, foodInfo.getAssignedEmployee());
              stmt.setString(3, foodInfo.getSpecialNeeds());
              stmt.setString(4, foodInfo.getId());
              stmt.executeUpdate();

            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
          }
        }
        break;
      case "Audio/Visual":
        for (AudVisNodeInfo audVisInfo : audVisData) {
          if (!(audVisInfo.getStatus().isEmpty())) {
            PreparedStatement stmt = null;
            try {
              stmt =
                  GlobalDb.getConnection()
                      .prepareStatement(
                          "UPDATE AudVisServiceRequest SET status = ?, assignedEmployee = ? WHERE id=?");
              stmt.setString(1, audVisInfo.getStatus());
              stmt.setString(2, audVisInfo.getAssignedEmployee());
              stmt.setString(3, audVisInfo.getId());
              stmt.executeUpdate();

            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
          }
        }
        break;
      case "Computer Service":
        for (ComputerNodeInfo computerInfo : computerData) {
          if (!(computerInfo.getStatus().isEmpty())) {
            PreparedStatement stmt = null;
            try {
              stmt =
                  GlobalDb.getConnection()
                      .prepareStatement(
                          "UPDATE ComputerServiceRequest SET status = ?, assignedEmployee = ? WHERE id=?");
              stmt.setString(1, computerInfo.getStatus());
              stmt.setString(2, computerInfo.getAssignedEmployee());
              stmt.setString(3, computerInfo.getId());
              stmt.executeUpdate();

            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
          }
        }
        break;
      case "Facilities Maintenance":
        for (FacilitiesNodeInfo facilitiesInfo : facilitiesData) {
          if (!(facilitiesInfo.getStatus().isEmpty())) {
            PreparedStatement stmt = null;
            try {
              stmt =
                  GlobalDb.getConnection()
                      .prepareStatement(
                          "UPDATE FacilitiesServiceRequest SET status = ?, assignedEmployee = ? WHERE id=?");
              stmt.setString(1, facilitiesInfo.getStatus());
              stmt.setString(2, facilitiesInfo.getAssignedEmployee());
              stmt.setString(3, facilitiesInfo.getId());
              stmt.executeUpdate();

            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
          }
        }
        break;
      case "Floral Delivery":
        for (FloralDelivNodeInfo floralInfo : floralData) {
          if (!(floralInfo.getStatus().isEmpty())) {
            PreparedStatement stmt = null;
            try {
              stmt =
                  GlobalDb.getConnection()
                      .prepareStatement(
                          "UPDATE FloralRequests SET status = ?, assignedEmployee = ? WHERE id=?");
              stmt.setString(1, floralInfo.getStatus());
              stmt.setString(2, floralInfo.getAssignedEmployee());
              stmt.setString(3, floralInfo.getId());
              stmt.executeUpdate();

            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
          }
        }
        break;
      case "Internal Transportation":
        for (InternalTransNodeInfo intTransInfo : internalTransData) {
          if (!(intTransInfo.getStatus().isEmpty())) {
            PreparedStatement stmt = null;
            try {
              stmt =
                  GlobalDb.getConnection()
                      .prepareStatement(
                          "UPDATE InternalTransReq SET status = ?, assignedEmployee = ? WHERE id=?");
              stmt.setString(1, intTransInfo.getStatus());
              stmt.setString(2, intTransInfo.getAssignedEmployee());
              stmt.setString(3, intTransInfo.getId());
              stmt.executeUpdate();

            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
          }
        }
        break;
      case "Language Interpreter":
        for (LangInterpNodeInfo langInterpInfo : langInterpData) {
          if (!(langInterpInfo.getStatus().isEmpty())) {
            PreparedStatement stmt = null;
            try {
              stmt =
                  GlobalDb.getConnection()
                      .prepareStatement(
                          "UPDATE LangInterpRequest SET status = ?, assignedEmployee = ? WHERE id=?");
              stmt.setString(1, langInterpInfo.getStatus());
              stmt.setString(2, langInterpInfo.getAssignedEmployee());
              stmt.setString(3, langInterpInfo.getId());
              stmt.executeUpdate();

            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
          }
        }
        break;
      case "Laundry Service":
        for (LaundryNodeInfo laundryInfo : laundryData) {
          if (!(laundryInfo.getStatus().isEmpty())) {
            PreparedStatement stmt = null;
            try {
              stmt =
                  GlobalDb.getConnection()
                      .prepareStatement(
                          "UPDATE LaundryRequest SET status = ?, assignedEmployee = ? WHERE id=?");
              stmt.setString(1, laundryInfo.getStatus());
              stmt.setString(2, laundryInfo.getAssignedEmployee());
              stmt.setString(3, laundryInfo.getId());
              stmt.executeUpdate();

            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
          }
        }
        break;
      case "Medicine Delivery":
        for (MedDelivNodeInfo medDelInfo : medDelivData) {
          if (!(medDelInfo.getStatus().isEmpty())) {
            PreparedStatement stmt = null;
            try {
              stmt =
                  GlobalDb.getConnection()
                      .prepareStatement(
                          "UPDATE MedicineDelivery SET status = ?, assignedEmployee = ? WHERE id=?");
              stmt.setString(1, medDelInfo.getStatus());
              stmt.setString(2, medDelInfo.getAssignedEmployee());
              stmt.setString(3, medDelInfo.getId());
              stmt.executeUpdate();

            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
          }
        }
        break;
      case "Sanitation Service":
        for (SanitationNodeInfo sanitationInfo : sanitationData) {
          if (!(sanitationInfo.getStatus().isEmpty())) {
            PreparedStatement stmt = null;
            try {
              stmt =
                  GlobalDb.getConnection()
                      .prepareStatement(
                          "UPDATE SanitationRequest SET status = ?, assignedEmployee = ? WHERE id=?");
              stmt.setString(1, sanitationInfo.getStatus());
              stmt.setString(2, sanitationInfo.getAssignedEmployee());
              stmt.setString(3, sanitationInfo.getId());
              stmt.executeUpdate();

            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
          }
        }
        break;
      case "Security Service":
        for (SecurityRequestNodeInfo securityInfo : securityData) {
          if (!(securityInfo.getStatus().isEmpty())) {
            PreparedStatement stmt = null;
            try {
              stmt =
                  GlobalDb.getConnection()
                      .prepareStatement(
                          "UPDATE SanitationRequest SET status = ?, assignedEmployee = ? WHERE id=?");
              stmt.setString(1, securityInfo.getStatus());
              stmt.setString(2, securityInfo.getAssignedEmployee());
              stmt.setString(3, securityInfo.getId());
              stmt.executeUpdate();

            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
          }
        }
        break;
    }
  }

  private void ExTransTableSetup() {
    JFXTreeTableColumn<ExtTransNodeInfo, String> typeCol =
        new JFXTreeTableColumn<ExtTransNodeInfo, String>("Service Type");
    JFXTreeTableColumn<ExtTransNodeInfo, String> pFNCol =
        new JFXTreeTableColumn<ExtTransNodeInfo, String>("Patient First Name");
    JFXTreeTableColumn<ExtTransNodeInfo, String> pLNCol =
        new JFXTreeTableColumn<ExtTransNodeInfo, String>("Patient Last Name");
    JFXTreeTableColumn<ExtTransNodeInfo, String> contactInfoCol =
        new JFXTreeTableColumn<ExtTransNodeInfo, String>("Contact Info");
    JFXTreeTableColumn<ExtTransNodeInfo, String> locationCol =
        new JFXTreeTableColumn<ExtTransNodeInfo, String>("Location");
    JFXTreeTableColumn<ExtTransNodeInfo, String> assignedCol =
        new JFXTreeTableColumn<ExtTransNodeInfo, String>("Assigned To");
    JFXTreeTableColumn<ExtTransNodeInfo, String> statusCol =
        new JFXTreeTableColumn<ExtTransNodeInfo, String>("Status");

    typeCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ExtTransNodeInfo, String> p) -> {
          if (typeCol.validateValue(p)) {
            return p.getValue().getValue().serviceType;
          } else {
            return typeCol.getComputedValue(p);
          }
        });

    pFNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ExtTransNodeInfo, String> p) -> {
          if (pFNCol.validateValue(p)) {
            return p.getValue().getValue().pFirstName;
          } else {
            return pFNCol.getComputedValue(p);
          }
        });
    pLNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ExtTransNodeInfo, String> p) -> {
          if (pLNCol.validateValue(p)) {
            return p.getValue().getValue().pLastName;
          } else {
            return pLNCol.getComputedValue(p);
          }
        });
    contactInfoCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ExtTransNodeInfo, String> p) -> {
          if (contactInfoCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactInfoCol.getComputedValue(p);
          }
        });
    locationCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ExtTransNodeInfo, String> p) -> {
          if (locationCol.validateValue(p)) {
            return p.getValue().getValue().location;
          } else {
            return locationCol.getComputedValue(p);
          }
        });

    ObservableList<String> emloyeeList = FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<ExtTransNodeInfo, String> p) -> {
          if (assignedCol.validateValue(p)) {
            return p.getValue().getValue().assignedTo;
          } else {
            return assignedCol.getComputedValue(p);
          }
        });
    assignedCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(emloyeeList));

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("In Progress", "Complete", "Incomplete");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<ExtTransNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));
    //    statusCol.setCellFactory(
    //        (TreeTableColumn<ExtTransNodeInfo, String> p) -> {
    //          TreeTableCell<ExtTransNodeInfo, String> cell =
    //              new TreeTableCell<ExtTransNodeInfo, String>() {
    //                // @Override
    //                protected void changeColor(String status, boolean empty) {
    //                  TreeTableRow<ExtTransNodeInfo> row = getTreeTableRow();
    //                  if (status.equals("Incomplete")) {
    //                    row.setStyle("-fx-background-color: Red");
    //                  } else if (status.equals("In progress")) {
    //                    row.setStyle("-fx-background-color: Orange");
    //                    //            setText("In progress");
    //                  } else if (status.equals("Complete")) {
    //                    row.setStyle("-fx-background-color: Blue");
    //                  }
    //                }
    //              };
    //          return cell;
    //        });

    tableView.setEditable(true);
    typeCol.setEditable(false);
    pFNCol.setEditable(false);
    pLNCol.setEditable(false);
    contactInfoCol.setEditable(false);

    JFXTreeTableColumn<ExtTransNodeInfo, String> transTypeCol =
        new JFXTreeTableColumn<ExtTransNodeInfo, String>("Transportation Type");

    transTypeCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ExtTransNodeInfo, String> p) -> {
          if (transTypeCol.validateValue(p)) {
            return p.getValue().getValue().transType;
          } else {
            return transTypeCol.getComputedValue(p);
          }
        });

    tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    tableView
        .getColumns()
        .setAll(
            typeCol,
            pFNCol,
            pLNCol,
            contactInfoCol,
            locationCol,
            transTypeCol,
            assignedCol,
            statusCol);

    tableView.setPlaceholder(
        new Label("No request of external transportation service has been made yet"));
  }

  private void foodDeliTableSetup() {
    JFXTreeTableColumn<FoodDelivNodeInfo, String> typeCol =
        new JFXTreeTableColumn<FoodDelivNodeInfo, String>("Service Type");
    JFXTreeTableColumn<FoodDelivNodeInfo, String> pFNCol =
        new JFXTreeTableColumn<FoodDelivNodeInfo, String>("Patient First Name");
    JFXTreeTableColumn<FoodDelivNodeInfo, String> pLNCol =
        new JFXTreeTableColumn<FoodDelivNodeInfo, String>("Patient Last Name");
    JFXTreeTableColumn<FoodDelivNodeInfo, String> contactInfoCol =
        new JFXTreeTableColumn<FoodDelivNodeInfo, String>("Contact Info");
    JFXTreeTableColumn<FoodDelivNodeInfo, String> locationCol =
        new JFXTreeTableColumn<FoodDelivNodeInfo, String>("Location");
    JFXTreeTableColumn<FoodDelivNodeInfo, String> assignedCol =
        new JFXTreeTableColumn<FoodDelivNodeInfo, String>("Assigned To");
    JFXTreeTableColumn<FoodDelivNodeInfo, String> statusCol =
        new JFXTreeTableColumn<FoodDelivNodeInfo, String>("Status");

    typeCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FoodDelivNodeInfo, String> p) -> {
          if (typeCol.validateValue(p)) {
            return p.getValue().getValue().serviceType;
          } else {
            return typeCol.getComputedValue(p);
          }
        });

    pFNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FoodDelivNodeInfo, String> p) -> {
          if (pFNCol.validateValue(p)) {
            return p.getValue().getValue().pFirstName;
          } else {
            return pFNCol.getComputedValue(p);
          }
        });
    pLNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FoodDelivNodeInfo, String> p) -> {
          if (pLNCol.validateValue(p)) {
            return p.getValue().getValue().pLastName;
          } else {
            return pLNCol.getComputedValue(p);
          }
        });
    contactInfoCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FoodDelivNodeInfo, String> p) -> {
          if (contactInfoCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactInfoCol.getComputedValue(p);
          }
        });
    locationCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FoodDelivNodeInfo, String> p) -> {
          if (locationCol.validateValue(p)) {
            return p.getValue().getValue().location;
          } else {
            return locationCol.getComputedValue(p);
          }
        });
    ObservableList<String> emloyeeList = FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<FoodDelivNodeInfo, String> p) -> {
          if (assignedCol.validateValue(p)) {
            return p.getValue().getValue().assignedEmployee;
          } else {
            return assignedCol.getComputedValue(p);
          }
        });
    assignedCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(emloyeeList));

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("In Progress", "Complete", "Incomplete");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<FoodDelivNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    JFXTreeTableColumn<FoodDelivNodeInfo, String> needsCol =
        new JFXTreeTableColumn<FoodDelivNodeInfo, String>("Special Needs");
    JFXTreeTableColumn<FoodDelivNodeInfo, String> timeCol =
        new JFXTreeTableColumn<FoodDelivNodeInfo, String>("Time Created");
    JFXTreeTableColumn<FoodDelivNodeInfo, String> dateCol =
        new JFXTreeTableColumn<FoodDelivNodeInfo, String>("Date Created");

    needsCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FoodDelivNodeInfo, String> p) -> {
          if (needsCol.validateValue(p)) {
            return p.getValue().getValue().specialNeeds;
          } else {
            return needsCol.getComputedValue(p);
          }
        });
    needsCol.setCellFactory(
        (TreeTableColumn<FoodDelivNodeInfo, String> p) -> {
          return new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
        });
    needsCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<FoodDelivNodeInfo, String> p) -> {
          p.getTreeTableView()
              .getTreeItem(p.getTreeTablePosition().getRow())
              .getValue()
              .specialNeeds
              .set(p.getNewValue());
        });

    timeCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FoodDelivNodeInfo, String> p) -> {
          if (timeCol.validateValue(p)) {
            return p.getValue().getValue().createTime;
          } else {
            return timeCol.getComputedValue(p);
          }
        });
    dateCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FoodDelivNodeInfo, String> p) -> {
          if (dateCol.validateValue(p)) {
            return p.getValue().getValue().createDate;
          } else {
            return dateCol.getComputedValue(p);
          }
        });

    foodTable.setEditable(true);
    typeCol.setEditable(false);
    pFNCol.setEditable(false);
    pLNCol.setEditable(false);
    contactInfoCol.setEditable(false);

    foodTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    foodTable
        .getColumns()
        .setAll(pFNCol, pLNCol, contactInfoCol, locationCol, needsCol, assignedCol, statusCol);

    foodTable.setPlaceholder(new Label("No request of food delivery service has been made yet"));
  }

  private void securityTableSetup() {

    JFXTreeTableColumn<SecurityRequestNodeInfo, String> fNCol =
        new JFXTreeTableColumn<SecurityRequestNodeInfo, String>("First Name");
    JFXTreeTableColumn<SecurityRequestNodeInfo, String> lNCol =
        new JFXTreeTableColumn<SecurityRequestNodeInfo, String>("Last Name");
    JFXTreeTableColumn<SecurityRequestNodeInfo, String> contactInfoCol =
        new JFXTreeTableColumn<SecurityRequestNodeInfo, String>("Contact Info");
    JFXTreeTableColumn<SecurityRequestNodeInfo, String> locationCol =
        new JFXTreeTableColumn<SecurityRequestNodeInfo, String>("Location");
    JFXTreeTableColumn<SecurityRequestNodeInfo, String> urgencyCol =
        new JFXTreeTableColumn<SecurityRequestNodeInfo, String>("Urgency Level");
    JFXTreeTableColumn<SecurityRequestNodeInfo, String> descriptCol =
        new JFXTreeTableColumn<SecurityRequestNodeInfo, String>("Description");
    JFXTreeTableColumn<SecurityRequestNodeInfo, String> assignedCol =
        new JFXTreeTableColumn<SecurityRequestNodeInfo, String>("Assigned Employee");
    JFXTreeTableColumn<SecurityRequestNodeInfo, String> statusCol =
        new JFXTreeTableColumn<SecurityRequestNodeInfo, String>("Status");

    fNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<SecurityRequestNodeInfo, String> p) -> {
          if (fNCol.validateValue(p)) {
            return p.getValue().getValue().firstName;
          } else {
            return fNCol.getComputedValue(p);
          }
        });
    lNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<SecurityRequestNodeInfo, String> p) -> {
          if (lNCol.validateValue(p)) {
            return p.getValue().getValue().lastName;
          } else {
            return lNCol.getComputedValue(p);
          }
        });
    contactInfoCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<SecurityRequestNodeInfo, String> p) -> {
          if (contactInfoCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactInfoCol.getComputedValue(p);
          }
        });
    locationCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<SecurityRequestNodeInfo, String> p) -> {
          if (locationCol.validateValue(p)) {
            return p.getValue().getValue().location;
          } else {
            return locationCol.getComputedValue(p);
          }
        });
    ObservableList<String> urgencyList = FXCollections.observableArrayList();
    urgencyList.addAll("High Priority", "Medium Priority", "Low Priority");
    urgencyCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<SecurityRequestNodeInfo, String> p) -> {
          if (urgencyCol.validateValue(p)) {
            return p.getValue().getValue().urgencyLevel;
          } else {
            return urgencyCol.getComputedValue(p);
          }
        });
    descriptCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<SecurityRequestNodeInfo, String> p) -> {
          if (descriptCol.validateValue(p)) {
            return p.getValue().getValue().description;
          } else {
            return descriptCol.getComputedValue(p);
          }
        });
    urgencyCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(urgencyList));

    ObservableList<String> employeeList = FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<SecurityRequestNodeInfo, String> p) -> {
          if (assignedCol.validateValue(p)) {
            return p.getValue().getValue().assignedEmployee;
          } else {
            return assignedCol.getComputedValue(p);
          }
        });
    assignedCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(employeeList));

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("In Progress", "Complete", "Incomplete");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<SecurityRequestNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    securityTable.setEditable(true);
    fNCol.setEditable(false);
    lNCol.setEditable(false);
    contactInfoCol.setEditable(false);

    securityTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    securityTable
        .getColumns()
        .setAll(
            fNCol,
            lNCol,
            contactInfoCol,
            locationCol,
            urgencyCol,
            descriptCol,
            assignedCol,
            statusCol);
    securityTable.setPlaceholder(new Label("No request of security service has been made yet"));
  }

  private void sanitationTableSetup() {

    JFXTreeTableColumn<SanitationNodeInfo, String> fNCol =
        new JFXTreeTableColumn<SanitationNodeInfo, String>("First Name");
    JFXTreeTableColumn<SanitationNodeInfo, String> lNCol =
        new JFXTreeTableColumn<SanitationNodeInfo, String>("Last Name");
    JFXTreeTableColumn<SanitationNodeInfo, String> contactInfoCol =
        new JFXTreeTableColumn<SanitationNodeInfo, String>("Contact Info");
    JFXTreeTableColumn<SanitationNodeInfo, String> locationCol =
        new JFXTreeTableColumn<SanitationNodeInfo, String>("Location");
    JFXTreeTableColumn<SanitationNodeInfo, String> urgencyCol =
        new JFXTreeTableColumn<SanitationNodeInfo, String>("Urgency Level");
    JFXTreeTableColumn<SanitationNodeInfo, String> descriptCol =
        new JFXTreeTableColumn<SanitationNodeInfo, String>("Description");
    JFXTreeTableColumn<SanitationNodeInfo, String> assignedCol =
        new JFXTreeTableColumn<SanitationNodeInfo, String>("Assigned Employee");
    JFXTreeTableColumn<SanitationNodeInfo, String> statusCol =
        new JFXTreeTableColumn<SanitationNodeInfo, String>("Status");

    fNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<SanitationNodeInfo, String> p) -> {
          if (fNCol.validateValue(p)) {
            return p.getValue().getValue().firstName;
          } else {
            return fNCol.getComputedValue(p);
          }
        });
    lNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<SanitationNodeInfo, String> p) -> {
          if (lNCol.validateValue(p)) {
            return p.getValue().getValue().lastName;
          } else {
            return lNCol.getComputedValue(p);
          }
        });
    contactInfoCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<SanitationNodeInfo, String> p) -> {
          if (contactInfoCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactInfoCol.getComputedValue(p);
          }
        });
    locationCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<SanitationNodeInfo, String> p) -> {
          if (locationCol.validateValue(p)) {
            return p.getValue().getValue().location;
          } else {
            return locationCol.getComputedValue(p);
          }
        });
    ObservableList<String> urgencyList = FXCollections.observableArrayList();
    urgencyList.addAll("High Priority", "Medium Priority", "Low Priority");
    urgencyCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<SanitationNodeInfo, String> p) -> {
          if (urgencyCol.validateValue(p)) {
            return p.getValue().getValue().urgencyLevel;
          } else {
            return urgencyCol.getComputedValue(p);
          }
        });
    descriptCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<SanitationNodeInfo, String> p) -> {
          if (descriptCol.validateValue(p)) {
            return p.getValue().getValue().description;
          } else {
            return descriptCol.getComputedValue(p);
          }
        });
    urgencyCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(urgencyList));

    ObservableList<String> employeeList = FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<SanitationNodeInfo, String> p) -> {
          if (assignedCol.validateValue(p)) {
            return p.getValue().getValue().assignedEmployee;
          } else {
            return assignedCol.getComputedValue(p);
          }
        });
    assignedCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(employeeList));

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("In Progress", "Complete", "Incomplete");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<SanitationNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    securityTable.setEditable(true);
    fNCol.setEditable(false);
    lNCol.setEditable(false);
    contactInfoCol.setEditable(false);

    securityTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    sanitationTable
        .getColumns()
        .setAll(
            fNCol,
            lNCol,
            contactInfoCol,
            locationCol,
            urgencyCol,
            descriptCol,
            assignedCol,
            statusCol);

    sanitationTable.setPlaceholder(new Label("No request of sanitation service has been made yet"));
  }

  private void medDelivTableSetup() {

    JFXTreeTableColumn<MedDelivNodeInfo, String> fNCol =
        new JFXTreeTableColumn<MedDelivNodeInfo, String>("First Name");
    JFXTreeTableColumn<MedDelivNodeInfo, String> lNCol =
        new JFXTreeTableColumn<MedDelivNodeInfo, String>("Last Name");
    JFXTreeTableColumn<MedDelivNodeInfo, String> contactInfoCol =
        new JFXTreeTableColumn<MedDelivNodeInfo, String>("Contact Info");
    JFXTreeTableColumn<MedDelivNodeInfo, String> locationCol =
        new JFXTreeTableColumn<MedDelivNodeInfo, String>("Location");
    JFXTreeTableColumn<MedDelivNodeInfo, String> typeofMedCol =
        new JFXTreeTableColumn<MedDelivNodeInfo, String>("Type of Medicine");
    JFXTreeTableColumn<MedDelivNodeInfo, String> dateCol =
        new JFXTreeTableColumn<MedDelivNodeInfo, String>("Drop Off Date");
    JFXTreeTableColumn<MedDelivNodeInfo, String> assignedCol =
        new JFXTreeTableColumn<MedDelivNodeInfo, String>("Assigned Employee");
    JFXTreeTableColumn<MedDelivNodeInfo, String> statusCol =
        new JFXTreeTableColumn<MedDelivNodeInfo, String>("Status");

    fNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedDelivNodeInfo, String> p) -> {
          if (fNCol.validateValue(p)) {
            return p.getValue().getValue().firstName;
          } else {
            return fNCol.getComputedValue(p);
          }
        });
    lNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedDelivNodeInfo, String> p) -> {
          if (lNCol.validateValue(p)) {
            return p.getValue().getValue().lastName;
          } else {
            return lNCol.getComputedValue(p);
          }
        });
    contactInfoCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedDelivNodeInfo, String> p) -> {
          if (contactInfoCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactInfoCol.getComputedValue(p);
          }
        });
    locationCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedDelivNodeInfo, String> p) -> {
          if (locationCol.validateValue(p)) {
            return p.getValue().getValue().location;
          } else {
            return locationCol.getComputedValue(p);
          }
        });
    typeofMedCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedDelivNodeInfo, String> p) -> {
          if (typeofMedCol.validateValue(p)) {
            return p.getValue().getValue().typeOfMedicine;
          } else {
            return typeofMedCol.getComputedValue(p);
          }
        });
    dateCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedDelivNodeInfo, String> p) -> {
          if (dateCol.validateValue(p)) {
            return p.getValue().getValue().dropOffDate;
          } else {
            return dateCol.getComputedValue(p);
          }
        });

    ObservableList<String> employeeList = FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<MedDelivNodeInfo, String> p) -> {
          if (assignedCol.validateValue(p)) {
            return p.getValue().getValue().assignedEmployee;
          } else {
            return assignedCol.getComputedValue(p);
          }
        });
    assignedCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(employeeList));

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("In Progress", "Complete", "Incomplete");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<MedDelivNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    medDelivTable.setEditable(true);
    fNCol.setEditable(false);
    lNCol.setEditable(false);
    contactInfoCol.setEditable(false);

    medDelivTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    medDelivTable
        .getColumns()
        .setAll(
            fNCol,
            lNCol,
            contactInfoCol,
            locationCol,
            typeofMedCol,
            dateCol,
            assignedCol,
            statusCol);

    medDelivTable.setPlaceholder(
        new Label("No request of medicine delivery service has been made yet"));
  }

  private void laundryTableSetup() {

    JFXTreeTableColumn<LaundryNodeInfo, String> fNCol =
        new JFXTreeTableColumn<LaundryNodeInfo, String>("First Name");
    JFXTreeTableColumn<LaundryNodeInfo, String> lNCol =
        new JFXTreeTableColumn<LaundryNodeInfo, String>("Last Name");
    JFXTreeTableColumn<LaundryNodeInfo, String> contactInfoCol =
        new JFXTreeTableColumn<LaundryNodeInfo, String>("Contact Info");
    JFXTreeTableColumn<LaundryNodeInfo, String> locationCol =
        new JFXTreeTableColumn<LaundryNodeInfo, String>("Location");
    JFXTreeTableColumn<LaundryNodeInfo, String> assignedCol =
        new JFXTreeTableColumn<LaundryNodeInfo, String>("Assigned Employee");
    JFXTreeTableColumn<LaundryNodeInfo, String> statusCol =
        new JFXTreeTableColumn<LaundryNodeInfo, String>("Status");

    fNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<LaundryNodeInfo, String> p) -> {
          if (fNCol.validateValue(p)) {
            return p.getValue().getValue().firstName;
          } else {
            return fNCol.getComputedValue(p);
          }
        });
    lNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<LaundryNodeInfo, String> p) -> {
          if (lNCol.validateValue(p)) {
            return p.getValue().getValue().lastName;
          } else {
            return lNCol.getComputedValue(p);
          }
        });
    contactInfoCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<LaundryNodeInfo, String> p) -> {
          if (contactInfoCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactInfoCol.getComputedValue(p);
          }
        });
    locationCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<LaundryNodeInfo, String> p) -> {
          if (locationCol.validateValue(p)) {
            return p.getValue().getValue().location;
          } else {
            return locationCol.getComputedValue(p);
          }
        });

    ObservableList<String> employeeList = FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<LaundryNodeInfo, String> p) -> {
          if (assignedCol.validateValue(p)) {
            return p.getValue().getValue().assignedEmployee;
          } else {
            return assignedCol.getComputedValue(p);
          }
        });
    assignedCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(employeeList));

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("In Progress", "Complete", "Incomplete");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<LaundryNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    laundryTable.setEditable(true);
    fNCol.setEditable(false);
    lNCol.setEditable(false);
    contactInfoCol.setEditable(false);

    laundryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    laundryTable
        .getColumns()
        .setAll(fNCol, lNCol, contactInfoCol, locationCol, assignedCol, statusCol);

    laundryTable.setPlaceholder(new Label("No request of laundry service has been made yet"));
  }

  private void langInterpTableSetup() {

    JFXTreeTableColumn<LangInterpNodeInfo, String> fNCol =
        new JFXTreeTableColumn<LangInterpNodeInfo, String>("First Name");
    JFXTreeTableColumn<LangInterpNodeInfo, String> lNCol =
        new JFXTreeTableColumn<LangInterpNodeInfo, String>("Last Name");
    JFXTreeTableColumn<LangInterpNodeInfo, String> contactInfoCol =
        new JFXTreeTableColumn<LangInterpNodeInfo, String>("Contact Info");
    JFXTreeTableColumn<LangInterpNodeInfo, String> locationCol =
        new JFXTreeTableColumn<LangInterpNodeInfo, String>("Location");
    JFXTreeTableColumn<LangInterpNodeInfo, String> languageCol =
        new JFXTreeTableColumn<LangInterpNodeInfo, String>("Language Requested");
    JFXTreeTableColumn<LangInterpNodeInfo, String> dateCol =
        new JFXTreeTableColumn<LangInterpNodeInfo, String>("Date Requested");
    JFXTreeTableColumn<LangInterpNodeInfo, String> assignedCol =
        new JFXTreeTableColumn<LangInterpNodeInfo, String>("Assigned Employee");
    JFXTreeTableColumn<LangInterpNodeInfo, String> statusCol =
        new JFXTreeTableColumn<LangInterpNodeInfo, String>("Status");

    fNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<LangInterpNodeInfo, String> p) -> {
          if (fNCol.validateValue(p)) {
            return p.getValue().getValue().firstName;
          } else {
            return fNCol.getComputedValue(p);
          }
        });
    lNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<LangInterpNodeInfo, String> p) -> {
          if (lNCol.validateValue(p)) {
            return p.getValue().getValue().lastName;
          } else {
            return lNCol.getComputedValue(p);
          }
        });
    contactInfoCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<LangInterpNodeInfo, String> p) -> {
          if (contactInfoCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactInfoCol.getComputedValue(p);
          }
        });
    locationCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<LangInterpNodeInfo, String> p) -> {
          if (locationCol.validateValue(p)) {
            return p.getValue().getValue().location;
          } else {
            return locationCol.getComputedValue(p);
          }
        });
    languageCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<LangInterpNodeInfo, String> p) -> {
          if (languageCol.validateValue(p)) {
            return p.getValue().getValue().languageRequested;
          } else {
            return languageCol.getComputedValue(p);
          }
        });
    dateCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<LangInterpNodeInfo, String> p) -> {
          if (dateCol.validateValue(p)) {
            return p.getValue().getValue().dateRequested;
          } else {
            return dateCol.getComputedValue(p);
          }
        });

    ObservableList<String> employeeList = FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<LangInterpNodeInfo, String> p) -> {
          if (assignedCol.validateValue(p)) {
            return p.getValue().getValue().assignedEmployee;
          } else {
            return assignedCol.getComputedValue(p);
          }
        });
    assignedCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(employeeList));

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("In Progress", "Complete", "Incomplete");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<LangInterpNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    langInterpTable.setEditable(true);
    fNCol.setEditable(false);
    lNCol.setEditable(false);
    contactInfoCol.setEditable(false);

    langInterpTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    langInterpTable
        .getColumns()
        .setAll(
            fNCol,
            lNCol,
            contactInfoCol,
            locationCol,
            languageCol,
            dateCol,
            assignedCol,
            statusCol);

    langInterpTable.setPlaceholder(
        new Label("No request of language interpreter service has been made yet"));
  }

  private void internalTransTableSetup() {

    JFXTreeTableColumn<InternalTransNodeInfo, String> fNCol =
        new JFXTreeTableColumn<InternalTransNodeInfo, String>("First Name");
    JFXTreeTableColumn<InternalTransNodeInfo, String> lNCol =
        new JFXTreeTableColumn<InternalTransNodeInfo, String>("Last Name");
    JFXTreeTableColumn<InternalTransNodeInfo, String> contactInfoCol =
        new JFXTreeTableColumn<InternalTransNodeInfo, String>("Contact Info");
    JFXTreeTableColumn<InternalTransNodeInfo, String> locationCol =
        new JFXTreeTableColumn<InternalTransNodeInfo, String>("Destination");
    JFXTreeTableColumn<InternalTransNodeInfo, String> typeTransCol =
        new JFXTreeTableColumn<InternalTransNodeInfo, String>("Type of Transport");
    JFXTreeTableColumn<InternalTransNodeInfo, String> assignedCol =
        new JFXTreeTableColumn<InternalTransNodeInfo, String>("Assigned Employee");
    JFXTreeTableColumn<InternalTransNodeInfo, String> statusCol =
        new JFXTreeTableColumn<InternalTransNodeInfo, String>("Status");

    fNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<InternalTransNodeInfo, String> p) -> {
          if (fNCol.validateValue(p)) {
            return p.getValue().getValue().firstName;
          } else {
            return fNCol.getComputedValue(p);
          }
        });
    lNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<InternalTransNodeInfo, String> p) -> {
          if (lNCol.validateValue(p)) {
            return p.getValue().getValue().lastName;
          } else {
            return lNCol.getComputedValue(p);
          }
        });
    contactInfoCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<InternalTransNodeInfo, String> p) -> {
          if (contactInfoCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactInfoCol.getComputedValue(p);
          }
        });
    locationCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<InternalTransNodeInfo, String> p) -> {
          if (locationCol.validateValue(p)) {
            return p.getValue().getValue().destination;
          } else {
            return locationCol.getComputedValue(p);
          }
        });
    ObservableList<String> transportList = FXCollections.observableArrayList();
    transportList.addAll("Stretcher", "Wheelchair", "Patient can walk");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<InternalTransNodeInfo, String> p) -> {
          if (typeTransCol.validateValue(p)) {
            return p.getValue().getValue().typeOfTransport;
          } else {
            return typeTransCol.getComputedValue(p);
          }
        });
    typeTransCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(transportList));

    ObservableList<String> employeeList = FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<InternalTransNodeInfo, String> p) -> {
          if (assignedCol.validateValue(p)) {
            return p.getValue().getValue().assignedEmployee;
          } else {
            return assignedCol.getComputedValue(p);
          }
        });
    assignedCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(employeeList));

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("In Progress", "Complete", "Incomplete");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<InternalTransNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    internalTransTable.setEditable(true);
    fNCol.setEditable(false);
    lNCol.setEditable(false);
    contactInfoCol.setEditable(false);

    internalTransTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    internalTransTable
        .getColumns()
        .setAll(fNCol, lNCol, contactInfoCol, locationCol, typeTransCol, assignedCol, statusCol);

    internalTransTable.setPlaceholder(
        new Label("No request of internal transportation service has been made yet"));
  }

  private void floralTableSetup() {

    JFXTreeTableColumn<FloralDelivNodeInfo, String> fNCol =
        new JFXTreeTableColumn<FloralDelivNodeInfo, String>("First Name");
    JFXTreeTableColumn<FloralDelivNodeInfo, String> lNCol =
        new JFXTreeTableColumn<FloralDelivNodeInfo, String>("Last Name");
    JFXTreeTableColumn<FloralDelivNodeInfo, String> contactInfoCol =
        new JFXTreeTableColumn<FloralDelivNodeInfo, String>("Contact Info");
    JFXTreeTableColumn<FloralDelivNodeInfo, String> locationCol =
        new JFXTreeTableColumn<FloralDelivNodeInfo, String>("Location");
    JFXTreeTableColumn<FloralDelivNodeInfo, String> flowerCol =
        new JFXTreeTableColumn<FloralDelivNodeInfo, String>("Type of Flower");
    JFXTreeTableColumn<FloralDelivNodeInfo, String> numFlowerCol =
        new JFXTreeTableColumn<FloralDelivNodeInfo, String>("Number of Flower");
    JFXTreeTableColumn<FloralDelivNodeInfo, String> fromCol =
        new JFXTreeTableColumn<FloralDelivNodeInfo, String>("From");
    JFXTreeTableColumn<FloralDelivNodeInfo, String> assignedCol =
        new JFXTreeTableColumn<FloralDelivNodeInfo, String>("Assigned Employee");
    JFXTreeTableColumn<FloralDelivNodeInfo, String> statusCol =
        new JFXTreeTableColumn<FloralDelivNodeInfo, String>("Status");

    fNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FloralDelivNodeInfo, String> p) -> {
          if (fNCol.validateValue(p)) {
            return p.getValue().getValue().pFirstName;
          } else {
            return fNCol.getComputedValue(p);
          }
        });
    lNCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FloralDelivNodeInfo, String> p) -> {
          if (lNCol.validateValue(p)) {
            return p.getValue().getValue().pLastName;
          } else {
            return lNCol.getComputedValue(p);
          }
        });
    contactInfoCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FloralDelivNodeInfo, String> p) -> {
          if (contactInfoCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactInfoCol.getComputedValue(p);
          }
        });
    locationCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FloralDelivNodeInfo, String> p) -> {
          if (locationCol.validateValue(p)) {
            return p.getValue().getValue().location;
          } else {
            return locationCol.getComputedValue(p);
          }
        });
    flowerCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<FloralDelivNodeInfo, String> p) -> {
          if (flowerCol.validateValue(p)) {
            return p.getValue().getValue().typeOfFlower;
          } else {
            return flowerCol.getComputedValue(p);
          }
        });
    numFlowerCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<FloralDelivNodeInfo, String> p) -> {
          if (numFlowerCol.validateValue(p)) {
            return p.getValue().getValue().numOfFlower;
          } else {
            return numFlowerCol.getComputedValue(p);
          }
        });
    fromCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<FloralDelivNodeInfo, String> p) -> {
          if (fromCol.validateValue(p)) {
            return p.getValue().getValue().fromFlower;
          } else {
            return fromCol.getComputedValue(p);
          }
        });
    ObservableList<String> employeeList = FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<FloralDelivNodeInfo, String> p) -> {
          if (assignedCol.validateValue(p)) {
            return p.getValue().getValue().assignedEmployee;
          } else {
            return assignedCol.getComputedValue(p);
          }
        });
    assignedCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(employeeList));

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("In Progress", "Complete", "Incomplete");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<FloralDelivNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    floralTable.setEditable(true);
    fNCol.setEditable(false);
    lNCol.setEditable(false);
    contactInfoCol.setEditable(false);

    floralTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    floralTable
        .getColumns()
        .setAll(
            fNCol,
            lNCol,
            contactInfoCol,
            locationCol,
            flowerCol,
            numFlowerCol,
            fromCol,
            assignedCol,
            statusCol);

    floralTable.setPlaceholder(
        new Label("No request of floral delivery service has been made yet"));
  }

  private void audVisTableSetup() {
    JFXTreeTableColumn<AudVisNodeInfo, String> FirstNameCol =
        new JFXTreeTableColumn<AudVisNodeInfo, String>("First Name");
    JFXTreeTableColumn<AudVisNodeInfo, String> LastNameCol =
        new JFXTreeTableColumn<AudVisNodeInfo, String>("Last Name");
    JFXTreeTableColumn<AudVisNodeInfo, String> contactCol =
        new JFXTreeTableColumn<AudVisNodeInfo, String>("Contact Information");
    JFXTreeTableColumn<AudVisNodeInfo, String> locationCol =
        new JFXTreeTableColumn<AudVisNodeInfo, String>("Location");
    // JFXTreeTableColumn<AudVisNodeInfo, String> urgencyCol = new
    // JFXTreeTableColumn<AudVisNodeInfo, String>("Urgency Level");
    JFXTreeTableColumn<AudVisNodeInfo, String> descripCol =
        new JFXTreeTableColumn<AudVisNodeInfo, String>("Description of Issue");
    JFXTreeTableColumn<AudVisNodeInfo, String> assignedEmployCol =
        new JFXTreeTableColumn<AudVisNodeInfo, String>("Assigned Employee");
    JFXTreeTableColumn<AudVisNodeInfo, String> statusCol =
        new JFXTreeTableColumn<AudVisNodeInfo, String>("Request Status");

    FirstNameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<AudVisNodeInfo, String> p) -> {
          if (FirstNameCol.validateValue(p)) {
            return p.getValue().getValue().firstName;
          } else {
            return FirstNameCol.getComputedValue(p);
          }
        });

    LastNameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<AudVisNodeInfo, String> p) -> {
          if (LastNameCol.validateValue(p)) {
            return p.getValue().getValue().lastName;
          } else {
            return LastNameCol.getComputedValue(p);
          }
        });
    contactCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<AudVisNodeInfo, String> p) -> {
          if (contactCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactCol.getComputedValue(p);
          }
        });
    locationCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<AudVisNodeInfo, String> p) -> {
          if (locationCol.validateValue(p)) {
            return p.getValue().getValue().location;
          } else {
            return locationCol.getComputedValue(p);
          }
        });
    descripCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<AudVisNodeInfo, String> p) -> {
          if (descripCol.validateValue(p)) {
            return p.getValue().getValue().descriptionOfProblem;
          } else {
            return descripCol.getComputedValue(p);
          }
        });
    ObservableList<String> employeeList = FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedEmployCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<AudVisNodeInfo, String> p) -> {
          if (assignedEmployCol.validateValue(p)) {
            return p.getValue().getValue().assignedEmployee;
          } else {
            return assignedEmployCol.getComputedValue(p);
          }
        });
    assignedEmployCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(employeeList));

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("In Progress", "Complete", "Incomplete");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<AudVisNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    audVisTable.setEditable(true);
    FirstNameCol.setEditable(false);
    LastNameCol.setEditable(false);
    contactCol.setEditable(false);
    locationCol.setEditable(false);
    descripCol.setEditable(false);

    audVisTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    audVisTable
        .getColumns()
        .setAll(
            FirstNameCol,
            LastNameCol,
            contactCol,
            locationCol,
            descripCol,
            assignedEmployCol,
            statusCol);

    audVisTable.setPlaceholder(new Label("No request of audio/visual service has been made yet"));
  }

  private void computerTableSetup() {
    JFXTreeTableColumn<ComputerNodeInfo, String> FirstNameCol =
        new JFXTreeTableColumn<ComputerNodeInfo, String>("First Name");
    JFXTreeTableColumn<ComputerNodeInfo, String> LastNameCol =
        new JFXTreeTableColumn<ComputerNodeInfo, String>("Last Name");
    JFXTreeTableColumn<ComputerNodeInfo, String> contactCol =
        new JFXTreeTableColumn<ComputerNodeInfo, String>("Contact Information");
    JFXTreeTableColumn<ComputerNodeInfo, String> locationCol =
        new JFXTreeTableColumn<ComputerNodeInfo, String>("Location");
    // JFXTreeTableColumn<AudVisNodeInfo, String> urgencyCol = new
    // JFXTreeTableColumn<AudVisNodeInfo, String>("Urgency Level");
    JFXTreeTableColumn<ComputerNodeInfo, String> descripCol =
        new JFXTreeTableColumn<ComputerNodeInfo, String>("Description of Issue");
    JFXTreeTableColumn<ComputerNodeInfo, String> assignedEmployCol =
        new JFXTreeTableColumn<ComputerNodeInfo, String>("Assigned Employee");
    JFXTreeTableColumn<ComputerNodeInfo, String> statusCol =
        new JFXTreeTableColumn<ComputerNodeInfo, String>("Request Status");

    FirstNameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ComputerNodeInfo, String> p) -> {
          if (FirstNameCol.validateValue(p)) {
            return p.getValue().getValue().firstName;
          } else {
            return FirstNameCol.getComputedValue(p);
          }
        });

    LastNameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ComputerNodeInfo, String> p) -> {
          if (LastNameCol.validateValue(p)) {
            return p.getValue().getValue().lastName;
          } else {
            return LastNameCol.getComputedValue(p);
          }
        });
    contactCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ComputerNodeInfo, String> p) -> {
          if (contactCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactCol.getComputedValue(p);
          }
        });
    locationCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ComputerNodeInfo, String> p) -> {
          if (locationCol.validateValue(p)) {
            return p.getValue().getValue().location;
          } else {
            return locationCol.getComputedValue(p);
          }
        });
    descripCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ComputerNodeInfo, String> p) -> {
          if (descripCol.validateValue(p)) {
            return p.getValue().getValue().descriptionOfIssue;
          } else {
            return descripCol.getComputedValue(p);
          }
        });
    ObservableList<String> employeeList = FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedEmployCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<ComputerNodeInfo, String> p) -> {
          if (assignedEmployCol.validateValue(p)) {
            return p.getValue().getValue().assignedEmployee;
          } else {
            return assignedEmployCol.getComputedValue(p);
          }
        });
    assignedEmployCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(employeeList));

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("In Progress", "Complete", "Incomplete");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<ComputerNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    computerTable.setEditable(true);
    FirstNameCol.setEditable(false);
    LastNameCol.setEditable(false);
    contactCol.setEditable(false);
    locationCol.setEditable(false);
    descripCol.setEditable(false);

    computerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    computerTable
        .getColumns()
        .setAll(
            FirstNameCol,
            LastNameCol,
            contactCol,
            locationCol,
            descripCol,
            assignedEmployCol,
            statusCol);

    computerTable.setPlaceholder(new Label("No request of computer service has been made yet"));
  }

  private void facilitiesTableSetup() {
    JFXTreeTableColumn<FacilitiesNodeInfo, String> FirstNameCol =
        new JFXTreeTableColumn<FacilitiesNodeInfo, String>("First Name");
    JFXTreeTableColumn<FacilitiesNodeInfo, String> LastNameCol =
        new JFXTreeTableColumn<FacilitiesNodeInfo, String>("Last Name");
    JFXTreeTableColumn<FacilitiesNodeInfo, String> contactCol =
        new JFXTreeTableColumn<FacilitiesNodeInfo, String>("Contact Information");
    JFXTreeTableColumn<FacilitiesNodeInfo, String> locationCol =
        new JFXTreeTableColumn<FacilitiesNodeInfo, String>("Location");
    JFXTreeTableColumn<FacilitiesNodeInfo, String> urgencyCol =
        new JFXTreeTableColumn<FacilitiesNodeInfo, String>("Urgency Level");
    JFXTreeTableColumn<FacilitiesNodeInfo, String> descripCol =
        new JFXTreeTableColumn<FacilitiesNodeInfo, String>("Description of Issue");
    JFXTreeTableColumn<FacilitiesNodeInfo, String> assignedEmployCol =
        new JFXTreeTableColumn<FacilitiesNodeInfo, String>("Assigned Employee");
    JFXTreeTableColumn<FacilitiesNodeInfo, String> statusCol =
        new JFXTreeTableColumn<FacilitiesNodeInfo, String>("Request Status");

    FirstNameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FacilitiesNodeInfo, String> p) -> {
          if (FirstNameCol.validateValue(p)) {
            return p.getValue().getValue().firstName;
          } else {
            return FirstNameCol.getComputedValue(p);
          }
        });

    LastNameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FacilitiesNodeInfo, String> p) -> {
          if (LastNameCol.validateValue(p)) {
            return p.getValue().getValue().lastName;
          } else {
            return LastNameCol.getComputedValue(p);
          }
        });
    contactCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FacilitiesNodeInfo, String> p) -> {
          if (contactCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactCol.getComputedValue(p);
          }
        });
    locationCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FacilitiesNodeInfo, String> p) -> {
          if (locationCol.validateValue(p)) {
            return p.getValue().getValue().location;
          } else {
            return locationCol.getComputedValue(p);
          }
        });

    ObservableList<String> urgencyList = FXCollections.observableArrayList();
    urgencyList.addAll("High Priority", "Medium Priority", "Low Priority");
    urgencyCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FacilitiesNodeInfo, String> p) -> {
          if (urgencyCol.validateValue(p)) {
            return p.getValue().getValue().urgencyLevel;
          } else {
            return urgencyCol.getComputedValue(p);
          }
        });
    descripCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<FacilitiesNodeInfo, String> p) -> {
          if (descripCol.validateValue(p)) {
            return p.getValue().getValue().descriptionOfIssue;
          } else {
            return descripCol.getComputedValue(p);
          }
        });
    ObservableList<String> employeeList = FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedEmployCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<FacilitiesNodeInfo, String> p) -> {
          if (assignedEmployCol.validateValue(p)) {
            return p.getValue().getValue().assignedEmployee;
          } else {
            return assignedEmployCol.getComputedValue(p);
          }
        });
    assignedEmployCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(employeeList));

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("In Progress", "Complete", "Incomplete");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<FacilitiesNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    facilitiesTable.setEditable(true);
    FirstNameCol.setEditable(false);
    LastNameCol.setEditable(false);
    contactCol.setEditable(false);
    locationCol.setEditable(false);
    descripCol.setEditable(false);
    urgencyCol.setEditable(false);

    facilitiesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    facilitiesTable
        .getColumns()
        .setAll(
            FirstNameCol,
            LastNameCol,
            contactCol,
            locationCol,
            urgencyCol,
            descripCol,
            assignedEmployCol,
            statusCol);

    facilitiesTable.setPlaceholder(
        new Label("No request of facilities maintenance service has been made yet"));
  }
}
