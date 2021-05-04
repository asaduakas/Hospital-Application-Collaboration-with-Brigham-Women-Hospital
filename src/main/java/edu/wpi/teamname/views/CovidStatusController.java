package edu.wpi.teamname.views;

import com.jfoenix.controls.*;
import edu.wpi.teamname.views.ServiceRequests.NodeInfo.*;

public class CovidStatusController {

  //    @FXML
  //    private JFXButton backBtn;
  //
  //    @FXML private JFXButton saveBtn;
  //
  //    @FXML private JFXTreeTableView<COVIDSurveyResultsNodeInfo> COVIDResultsTableView;
  //
  //    @FXML private JFXComboBox<String> typeBox;
  //
  //    @FXML private JFXListView<Label> listView;
  //
  //    @FXML private JFXTextField search;
  //
  //    private ObservableList<COVIDSurveyResultsNodeInfo> COVIDSurveyData;
  //
  //
  //    @Override
  //    public void initialize(URL location, ResourceBundle resources) {
  //
  //        typeBox
  //                .getItems()
  //                .addAll(
  //                        "COVID-19 Survey Results");
  //        typeBox.setOnAction(
  //                e -> {
  //                    try {
  //                        changeTable(typeBox.getValue().toString());
  //                    } catch (IOException ioException) {
  //                        ioException.printStackTrace();
  //                    }
  //                });
  //
  //        Label COVIDsurvey = new Label("COVID-19 Survey Results");
  //
  //        try {
  //            ImageView covidSurveyImage =
  //                    new ImageView(
  //                            new Image(new
  // FileInputStream("src/main/resources/Images/audio-visual.png")));
  //            covidSurveyImage.setFitWidth(35);
  //            covidSurveyImage.setFitHeight(35);
  //            covidSurveyImage.setGraphic(covidSurveyImage);
  //
  //
  //            COVIDResultsTableView
  //                    .getItems()
  //                    .addAll(
  //                            COVIDsurvey);
  //            COVIDResultsTableView.setStyle("-fx-padding: 0; -fx-background-insets: 0");
  //        } catch (FileNotFoundException e) {
  //            e.printStackTrace();
  //        }
  //        COVIDResultsTableView.setOnMouseClicked(
  //                new EventHandler<MouseEvent>() {
  //                    @Override
  //                    public void handle(MouseEvent event) {
  //                        try {
  //
  // changeTable(COVIDResultsTableView.getSelectionModel().getSelectedItem().getText());
  //                        } catch (IOException e) {
  //                            e.printStackTrace();
  //                        }
  //                    }
  //                });
  //    }
  //
  //    @FXML
  //    public void goHome(MouseEvent event) throws IOException {
  //        List<Node> childrenList =
  // App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
  //        VBox buttonBox = (VBox) childrenList.get(2);
  //        buttonBox.setVisible(false);
  //        popUpAction("CovidStatus.fxml");
  //    }
  //
  //
  //    public void changeData(String ServeType) {
  //        FDatabaseTables.getCovid19SurveyTable().changeCOVIDSurveyData(COVIDSurveyData);
  //    }
  //
  //    @FXML
  //    public void saveData(ActionEvent event) {
  //        //    changeData(typeBox.getValue().toString());
  //        changeData(listView.getSelectionModel().getSelectedItem().getText());
  //    }
  //
  //    public void changeTable(String servType) throws IOException {
  //        switch (servType) {
  //            case "External Transportation":
  //                try {
  //                    getCOVIDSurveyData();
  //                    TreeItem<COVIDSurveyResultsNodeInfo> root =
  //                            new RecursiveTreeItem<>(COVIDResultsTableView,
  // RecursiveTreeObject::getChildren);
  //                    root.setExpanded(true);
  //                    COVIDResultsTableView.setRoot(root);
  //                    COVIDResultsTableView.setShowRoot(false);
  //                } catch (IOException e) {
  //                    e.printStackTrace();
  //                }
  //                COVIDSurveyResultsTableSetup();
  //                break;
  //            default:
  //                COVIDResultsTableView.showRootProperty();
  //        }
  //    }
  //
  //    private ObservableList<COVIDSurveyResultsNodeInfo> getCOVIDSurveyData() throws IOException {
  //        COVIDSurveyData = FXCollections.observableArrayList();
  //        FDatabaseTables.getCovid19SurveyTable().addIntoCOVIDSurveyList(COVIDSurveyData);
  //        return COVIDSurveyData;
  //    }
  //
  //
  //    private void COVIDSurveyResultsTableSetup() {
  //        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> idCol =
  //                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("User ID");
  //        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> firstNameCol =
  //                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("User First Name");
  //        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> lastNameCol =
  //                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("User Last Name");
  //        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> contactInfoCol =
  //                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("Contact
  // Information");
  //        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> assignedCol =
  //                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("Assigned To");
  //
  //
  //        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> positiveTestCheckCol =
  //                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("positiveTestCheck");
  //        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> symptomCheckCol =
  //                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("symptomCheck");
  //        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> closeContactCheckCol =
  //                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("closeContactCheck");
  //        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> selfIsolateCol =
  //                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("selfIsolateCheck");
  //        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String> feelGoodCheckCol =
  //                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, String>("feelGoodCheck");
  //
  //
  ////        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, Integer> positiveTestCheckCol =
  ////                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo,
  // Integer>("positiveTestCheck");
  ////        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, Integer> symptomCheckCol =
  ////                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, Integer>("symptomCheck");
  ////        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, Integer> closeContactCheckCol =
  ////                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo,
  // Integer>("closeContactCheck");
  ////        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, Integer> selfIsolateCol =
  ////                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo,
  // Integer>("selfIsolateCheck");
  ////        JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, Integer> feelGoodCheckCol =
  ////                new JFXTreeTableColumn<COVIDSurveyResultsNodeInfo, Integer>("feelGoodCheck");
  //
  //        idCol.setCellValueFactory(
  //                (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
  //                    if (idCol.validateValue(p)) {
  //                        return p.getValue().getValue().id;
  //                    } else {
  //                        return idCol.getComputedValue(p);
  //                    }
  //                });
  //
  //        firstNameCol.setCellValueFactory(
  //                (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
  //                    if (firstNameCol.validateValue(p)) {
  //                        return p.getValue().getValue().firstName;
  //                    } else {
  //                        return firstNameCol.getComputedValue(p);
  //                    }
  //                });
  //        lastNameCol.setCellValueFactory(
  //                (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
  //                    if (lastNameCol.validateValue(p)) {
  //                        return p.getValue().getValue().lastName;
  //                    } else {
  //                        return lastNameCol.getComputedValue(p);
  //                    }
  //                });
  //        contactInfoCol.setCellValueFactory(
  //                (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
  //                    if (contactInfoCol.validateValue(p)) {
  //                        return p.getValue().getValue().contactInfo;
  //                    } else {
  //                        return contactInfoCol.getComputedValue(p);
  //                    }
  //                });
  //
  //        ObservableList<String> emloyeeList =
  //                FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
  //        //    JFXComboBox<String> emList = new JFXComboBox<String>();
  //        assignedCol.setCellValueFactory(
  //                (JFXTreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
  //                    if (assignedCol.validateValue(p)) {
  //                       return p.getValue().getValue().assignedEmployee;
  //                    } else {
  //                        return assignedCol.getComputedValue(p);
  //                    }
  //                });
  //        assignedCol.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(emloyeeList));
  //
  //        positiveTestCheckCol.setCellValueFactory(
  //                (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, Integer> p) -> {
  //                    if (positiveTestCheckCol.validateValue(p)) {
  //                        // return p.getTreeTableColumn().getCellObservableValue(6);
  //                        return p.getValue().getValue().positiveTestCheck;
  //                    } else {
  //                        return positiveTestCheckCol.getComputedValue(p);
  //                    }
  //                });
  //
  //        symptomCheckCol.setCellValueFactory(
  //                (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
  //                    if (symptomCheckCol.validateValue(p)) {
  //                        // return p.getTreeTableColumn().getCellObservableValue(7);
  //                        return p.getValue().getValue().symptomCheck;
  //                    } else {
  //                        return symptomCheckCol.getComputedValue(p);
  //                    }
  //                });
  //
  //        closeContactCheckCol.setCellValueFactory(
  //                (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
  //                    if (closeContactCheckCol.validateValue(p)) {
  //                        // return p.getTreeTableColumn().getCellObservableValue(6);
  //                        return p.getValue().getValue().closeContactCheck;
  //                    } else {
  //                        return closeContactCheckCol.getComputedValue(p);
  //                    }
  //                });
  //
  //        selfIsolateCol.setCellValueFactory(
  //                (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
  //                    if (selfIsolateCol.validateValue(p)) {
  //                        // return p.getTreeTableColumn().getCellObservableValue(7);
  //                        return p.getValue().getValue().selfIsolateCheck;
  //                    } else {
  //                        return selfIsolateCol.getComputedValue(p);
  //                    }
  //                });
  //
  //        feelGoodCheckCol.setCellValueFactory(
  //                (TreeTableColumn.CellDataFeatures<COVIDSurveyResultsNodeInfo, String> p) -> {
  //                    if (feelGoodCheckCol.validateValue(p)) {
  //                        // return p.getTreeTableColumn().getCellObservableValue(8);
  //                        return p.getValue().getValue().feelGoodCheck;
  //                    } else {
  //                        return feelGoodCheckCol.getComputedValue(p);
  //                    }
  //                });
  //        COVIDResultsTableView.setEditable(true);
  //        idCol.setEditable(false);
  //        firstNameCol.setEditable(false);
  //        lastNameCol.setEditable(false);
  //        contactInfoCol.setEditable(false);
  //        assignedCol.setEditable(true);
  //        positiveTestCheckCol.setEditable(false);
  //        symptomCheckCol.setEditable(false);
  //        closeContactCheckCol.setEditable(false);
  //        selfIsolateCol.setEditable(false);
  //        feelGoodCheckCol.setEditable(false);
  //
  //        COVIDResultsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  //
  //        COVIDResultsTableView
  //                .getColumns()
  //                .setAll(
  //                        idCol,
  //                        firstNameCol,
  //                        lastNameCol,
  //                        contactInfoCol,
  //                        assignedCol,
  //                        positiveTestCheckCol,
  //                        symptomCheckCol,
  //                        closeContactCheckCol,
  //                        selfIsolateCol,
  //                        feelGoodCheckCol);
  //
  //        COVIDResultsTableView.setPlaceholder(
  //                new Label("No COVID-19 surveys have been made yet"));
  //    }
}
