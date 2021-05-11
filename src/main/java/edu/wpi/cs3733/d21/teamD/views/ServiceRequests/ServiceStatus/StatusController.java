package edu.wpi.cs3733.d21.teamD.views.ServiceRequests.ServiceStatus;

import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.d21.teamD.App;
import edu.wpi.cs3733.d21.teamD.Ddb.FDatabaseTables;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.views.Access.AdminAccessible;
import edu.wpi.cs3733.d21.teamD.views.Access.EmployeeAccessible;
import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.*;
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

public class StatusController extends AbsRequest
    implements Initializable, IRequestStatus, EmployeeAccessible, AdminAccessible {

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

  @FXML private JFXTreeTableView<COVIDSurveyResultsNodeInfo> COVIDResultsTableView;

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

  public ObservableList<LaundryNodeInfo> laundryData;

  private ObservableList<MedDelivNodeInfo> medDelivData;

  private ObservableList<SanitationNodeInfo> sanitationData;

  private ObservableList<SecurityRequestNodeInfo> securityData;

  private ObservableList<COVIDSurveyResultsNodeInfo> COVIDSurveyData;

  // Pane pane = (Pane) FXMLLoader.load(getClass().getClassLoader().getResource("StatusView"));

  private ObservableList<Node> tables = FXCollections.observableArrayList();

  private final boolean isEmployee = HomeController.userCategory.equalsIgnoreCase("employee");
  private ObservableList<String> emloyeeList =
      FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    if (isEmployee) {
      emloyeeList.clear();
      emloyeeList.addAll(HomeController.username, "");
    } else {
      emloyeeList.add(0, "");
    }

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
            "Security Service",
            "Hospital Entry Request");
    typeBox.setOnAction(
        e -> {
          try {
            changeTable(typeBox.getValue().toString());
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
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
    Label covid = new Label("Hospital Entry Request");

    try {
      ImageView avImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/audio-visual.png")));
      avImage.setFitWidth(35);
      avImage.setFitHeight(35);
      av.setGraphic(avImage);

      ImageView csImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/Computer.png")));
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

      ImageView covidImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/covid_black.png")));
      covidImage.setFitWidth(40);
      covidImage.setFitHeight(40);
      covid.setGraphic(covidImage);

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
              security,
              covid);
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
        COVIDResultsTableView.setVisible(false);
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
        COVIDResultsTableView.setVisible(false);
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
        COVIDResultsTableView.setVisible(false);
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
        COVIDResultsTableView.setVisible(false);
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
        COVIDResultsTableView.setVisible(false);
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
        COVIDResultsTableView.setVisible(false);
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
        COVIDResultsTableView.setVisible(false);
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
        COVIDResultsTableView.setVisible(false);
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
        COVIDResultsTableView.setVisible(false);
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
        COVIDResultsTableView.setVisible(false);
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
        COVIDResultsTableView.setVisible(false);
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
        COVIDResultsTableView.setVisible(false);
        securityTable.setVisible(true);
        securityTableSetup();
        break;
      case "Hospital Entry Request":
        getCOVIDSurveyData();
        TreeItem<COVIDSurveyResultsNodeInfo> root13 =
            new RecursiveTreeItem<>(COVIDSurveyData, RecursiveTreeObject::getChildren);
        root13.setExpanded(true);
        COVIDResultsTableView.setRoot(root13);
        COVIDResultsTableView.setShowRoot(false);
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
        securityTable.setVisible(false);
        COVIDResultsTableView.setVisible(true);
        COVIDSurveyResultsTableSetup();
        break;
      default:
        tableView.showRootProperty();
    }
  }

  @FXML
  public void goHome(MouseEvent event) throws IOException {
    List<Node> childrenList = App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
    VBox buttonBox = (VBox) childrenList.get(2);
    buttonBox.setVisible(false);
    // ServicePageController.popup.hide();
    popUpAction("ServicePageView.fxml");
    //    HomeController.popup.hide();
    // App.getPrimaryStage().getScene().getRoot().setEffect(null);
    //    return ExTransData;
  }

  private ObservableList<ExtTransNodeInfo> getExTransData() throws IOException {
    ExTransData = FXCollections.observableArrayList();
    FDatabaseTables.getExternalTransportTable()
        .addIntoExTransDataList(
            ExTransData, HomeController.userCategory.equalsIgnoreCase("employee"));
    return ExTransData;
  }

  private ObservableList<FoodDelivNodeInfo> getFoodData() throws IOException {
    foodData = FXCollections.observableArrayList();
    FDatabaseTables.getFoodDeliveryTable()
        .addIntoFoodDelivDataList(
            foodData, HomeController.userCategory.equalsIgnoreCase("employee"));
    return foodData;
  }

  private ObservableList<AudVisNodeInfo> getAudVisData() throws IOException {
    audVisData = FXCollections.observableArrayList();
    FDatabaseTables.getAudVisTable()
        .addIntoAudVisDataList(
            audVisData, HomeController.userCategory.equalsIgnoreCase("employee"));
    return audVisData;
  }

  private ObservableList<ComputerNodeInfo> getComputerData() throws IOException {
    computerData = FXCollections.observableArrayList();
    FDatabaseTables.getCompRequestTable()
        .addIntoComputerDataList(
            computerData, HomeController.userCategory.equalsIgnoreCase("employee"));
    return computerData;
  }

  private ObservableList<FacilitiesNodeInfo> getFacilitiesData() throws IOException {
    facilitiesData = FXCollections.observableArrayList();
    FDatabaseTables.getFacilitiesTable()
        .addIntoFacilitiesDataList(
            facilitiesData, HomeController.userCategory.equalsIgnoreCase("employee"));
    return facilitiesData;
  }

  private ObservableList<FloralDelivNodeInfo> getFloralData() throws IOException {
    floralData = FXCollections.observableArrayList();
    FDatabaseTables.getFloralDeliveryTable()
        .addIntoFloralDeliveryList(
            floralData, HomeController.userCategory.equalsIgnoreCase("employee"));
    return floralData;
  }

  private ObservableList<InternalTransNodeInfo> getInternalTransData() throws IOException {
    internalTransData = FXCollections.observableArrayList();
    FDatabaseTables.getInternalDeliveryTable()
        .addIntoInternalTransList(
            internalTransData, HomeController.userCategory.equalsIgnoreCase("employee"));
    return internalTransData;
  }

  private ObservableList<LangInterpNodeInfo> getLangInterpData() throws IOException {
    langInterpData = FXCollections.observableArrayList();
    FDatabaseTables.getLangInterpreterTable()
        .addIntoLangInterpreterList(
            langInterpData, HomeController.userCategory.equalsIgnoreCase("employee"));
    return langInterpData;
  }

  private ObservableList<LaundryNodeInfo> getLaundryData() throws IOException {
    laundryData = FXCollections.observableArrayList();
    FDatabaseTables.getLaundryRequestTable()
        .addIntoLaundServiceList(
            laundryData, HomeController.userCategory.equalsIgnoreCase("employee"));
    return laundryData;
  }

  private ObservableList<MedDelivNodeInfo> getMedDelivData() throws IOException {
    medDelivData = FXCollections.observableArrayList();
    FDatabaseTables.getMedDeliveryTable()
        .addIntoMedDeliveryList(
            medDelivData, HomeController.userCategory.equalsIgnoreCase("employee"));
    return medDelivData;
  }

  private ObservableList<SanitationNodeInfo> getSanitationData() throws IOException {
    sanitationData = FXCollections.observableArrayList();
    FDatabaseTables.getSanitationServiceTable()
        .addIntoSanitationList(
            sanitationData, HomeController.userCategory.equalsIgnoreCase("employee"));
    return sanitationData;
  }

  private ObservableList<SecurityRequestNodeInfo> getSecurityData() throws IOException {
    securityData = FXCollections.observableArrayList();
    FDatabaseTables.getSecurityRequestTable()
        .addIntoSanitationList(
            securityData, HomeController.userCategory.equalsIgnoreCase("employee"));
    return securityData;
  }

  private ObservableList<COVIDSurveyResultsNodeInfo> getCOVIDSurveyData() throws IOException {
    COVIDSurveyData = FXCollections.observableArrayList();
    FDatabaseTables.getCovid19SurveyTable()
        .addIntoCOVIDSurveyList(
            COVIDSurveyData, HomeController.userCategory.equalsIgnoreCase("employee"));
    //        System.out.println("this is inside the status controller for usertype ");
    return COVIDSurveyData;
    //        return
    // FDatabaseTables.getCovid19SurveyTable().addIntoCOVIDSurveyList(COVIDSurveyData);
  }

  @FXML
  public void saveData(ActionEvent event) {
    //    changeData(typeBox.getValue().toString());
    changeData(listView.getSelectionModel().getSelectedItem().getText());
  }

  public void changeData(String ServeType) {
    switch (ServeType) {
      case "External Transportation":
        FDatabaseTables.getExternalTransportTable().changeExTransData(ExTransData);
        break;
      case "Food Delivery":
        FDatabaseTables.getFoodDeliveryTable().changeFoodDelivData(foodData);
        break;
      case "Audio/Visual":
        FDatabaseTables.getAudVisTable().changeAudVisData(audVisData);
        break;
      case "Computer Service":
        FDatabaseTables.getCompRequestTable().changeComputerData(computerData);
        break;
      case "Facilities Maintenance":
        FDatabaseTables.getFacilitiesTable().changeFacilitiesData(facilitiesData);
        break;
      case "Floral Delivery":
        FDatabaseTables.getFloralDeliveryTable().changeFloralDelivData(floralData);
        break;
      case "Internal Transportation":
        FDatabaseTables.getInternalDeliveryTable().changeInternalTransData(internalTransData);
        break;
      case "Language Interpreter":
        FDatabaseTables.getLangInterpreterTable().changeLangInterData(langInterpData);
        break;
      case "Laundry Service":
        FDatabaseTables.getLaundryRequestTable().changeLaundServiceData(laundryData);
        break;
      case "Medicine Delivery":
        FDatabaseTables.getMedDeliveryTable().changeMedDeliveryData(medDelivData);
        break;
      case "Sanitation Service":
        FDatabaseTables.getSanitationServiceTable().changeSanitationData(sanitationData);
        break;
      case "Security Service":
        FDatabaseTables.getSecurityRequestTable().changeSecurityData(securityData);
        break;
      case "Hospital Entry Request":
        FDatabaseTables.getCovid19SurveyTable().changeCOVIDSurveyData(COVIDSurveyData);
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

    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<SecurityRequestNodeInfo, String> p) -> {
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

    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<SanitationNodeInfo, String> p) -> {
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
        (JFXTreeTableColumn.CellDataFeatures<SanitationNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    sanitationTable.setEditable(true);
    fNCol.setEditable(false);
    lNCol.setEditable(false);
    contactInfoCol.setEditable(false);

    sanitationTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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
    sanitationTable.setPlaceholder(new Label("No request of security service has been made yet"));
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

    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<MedDelivNodeInfo, String> p) -> {
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

    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<LaundryNodeInfo, String> p) -> {
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

    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<LangInterpNodeInfo, String> p) -> {
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

    //    JFXComboBox<String> emList = new JFXComboBox<String>();
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<InternalTransNodeInfo, String> p) -> {
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
    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<FloralDelivNodeInfo, String> p) -> {
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
    ObservableList<String> employeeList =
        FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
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
    ObservableList<String> employeeList =
        FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
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
    ObservableList<String> employeeList =
        FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
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

  private void COVIDSurveyResultsTableSetup() {
    JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> idCol =
        new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("Request Type");
    JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> firstNameCol =
        new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("User First Name");
    JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> lastNameCol =
        new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("User Last Name");
    JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> contactInfoCol =
        new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("Contact Information");
    JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> emailCol =
        new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("Email");
    JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> assignedCol =
        new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("Assigned To");

    JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> positiveTestCheckCol =
        new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("positiveTest");
    JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> symptomCheckCol =
        new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("symptom");
    JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> closeContactCheckCol =
        new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("closeContact");
    JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> selfIsolateCol =
        new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("selfIsolate");
    JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> feelGoodCheckCol =
        new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("feelGood");
    JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> statusCol =
        new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("Status");

    idCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
          if (idCol.validateValue(p)) {
            return p.getValue().getValue().type;
          } else {
            return idCol.getComputedValue(p);
          }
        });

    firstNameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
          if (firstNameCol.validateValue(p)) {
            return p.getValue().getValue().firstName;
          } else {
            return firstNameCol.getComputedValue(p);
          }
        });
    lastNameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
          if (lastNameCol.validateValue(p)) {
            return p.getValue().getValue().lastName;
          } else {
            return lastNameCol.getComputedValue(p);
          }
        });
    contactInfoCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
          if (contactInfoCol.validateValue(p)) {
            return p.getValue().getValue().contactInfo;
          } else {
            return contactInfoCol.getComputedValue(p);
          }
        });

    emailCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
          if (emailCol.validateValue(p)) {
            return p.getValue().getValue().email;
          } else {
            return emailCol.getComputedValue(p);
          }
        });

    assignedCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
          if (assignedCol.validateValue(p)) {
            return p.getValue().getValue().assignedEmployee;
          } else {
            return assignedCol.getComputedValue(p);
          }
        });
    assignedCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(emloyeeList));

    positiveTestCheckCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
          if (positiveTestCheckCol.validateValue(p)) {
            // return p.getTreeTableColumn().getCellObservableValue(6);
            return p.getValue().getValue().positiveTestCheck;
          } else {
            return positiveTestCheckCol.getComputedValue(p);
          }
        });

    symptomCheckCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
          if (symptomCheckCol.validateValue(p)) {
            // return p.getTreeTableColumn().getCellObservableValue(7);
            return p.getValue().getValue().symptomCheck;
          } else {
            return symptomCheckCol.getComputedValue(p);
          }
        });

    closeContactCheckCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
          if (closeContactCheckCol.validateValue(p)) {
            // return p.getTreeTableColumn().getCellObservableValue(6);
            return p.getValue().getValue().closeContactCheck;
          } else {
            return closeContactCheckCol.getComputedValue(p);
          }
        });

    selfIsolateCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
          if (selfIsolateCol.validateValue(p)) {
            // return p.getTreeTableColumn().getCellObservableValue(7);
            return p.getValue().getValue().selfIsolateCheck;
          } else {
            return selfIsolateCol.getComputedValue(p);
          }
        });

    feelGoodCheckCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
          if (feelGoodCheckCol.validateValue(p)) {
            // return p.getTreeTableColumn().getCellObservableValue(8);
            return p.getValue().getValue().feelGoodCheck;
          } else {
            return feelGoodCheckCol.getComputedValue(p);
          }
        });

    ObservableList<String> statusList = FXCollections.observableArrayList();
    statusList.addAll("Approved", "Disapproved", "Inconclusive");
    statusCol.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
          if (statusCol.validateValue(p)) {
            return p.getValue().getValue().status;
          } else {
            return statusCol.getComputedValue(p);
          }
        });
    statusCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));

    COVIDResultsTableView.setEditable(true);
    idCol.setEditable(false);
    firstNameCol.setEditable(false);
    lastNameCol.setEditable(false);
    contactInfoCol.setEditable(false);
    emailCol.setEditable(false);
    assignedCol.setEditable(true);
    statusCol.setEditable(true);
    positiveTestCheckCol.setEditable(false);
    symptomCheckCol.setEditable(false);
    closeContactCheckCol.setEditable(false);
    selfIsolateCol.setEditable(false);
    feelGoodCheckCol.setEditable(false);

    COVIDResultsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    COVIDResultsTableView.getColumns()
        .setAll(
            idCol,
            firstNameCol,
            lastNameCol,
            contactInfoCol,
            emailCol,
            assignedCol,
            statusCol,
            positiveTestCheckCol,
            symptomCheckCol,
            closeContactCheckCol,
            selfIsolateCol,
            feelGoodCheckCol);

    COVIDResultsTableView.setPlaceholder(new Label("No COVID-19 surveys have been made yet"));
  }
}
