package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.Access.*;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javax.swing.*;

public class HomeController extends Application {

  @FXML private JFXButton exitButton; // Btn to exit program
  @FXML private JFXButton logoutButton;

  @FXML private JFXButton mapEditing;
  @FXML private JFXButton serviceRequest;
  @FXML private JFXButton covidButton;
  @FXML public static Popup popup;
  @FXML public static VBox mainButtons;

  public static UserCategory userTypeEnum;
  public static String username = null;
  public static String password = null;
  private static String userCategory;
  public static Boolean disableRequestStatus = false;

  public static SceneSizeChangeListener sizeListener;

  // Used to reset search history for each login
  public static int historyTracker = 0;

  @FXML
  private void logout(ActionEvent event) throws IOException {
    HomeController.username = null;
    HomeController.userTypeEnum = null;
    HomeController.userCategory = null;
    HomeController.historyTracker = 0;
    Stage stage = (Stage) logoutButton.getScene().getWindow();
    stage.setScene(
        new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("LoginView.fxml"))));
    stage.show();
    App.getPrimaryStage().close();
    App takeToInit = new App();
    takeToInit.start(App.getPrimaryStage());
  }

  @FXML
  private void closeApp(ActionEvent event) {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void emergencyView() throws IOException {
    popUpAction("Emergency.fxml", false);
  }

  @FXML
  public void helpView() throws IOException {
    popUpAction("Help.fxml", false);
  }

  @FXML
  public void hospitalMapView() throws IOException {
    if (LoginController.getUserCategory() != null) {
      this.userCategory = LoginController.getUserCategory();
    } else {
      this.userCategory = InitPageController.getUserCategory();
    }

    App.getPrimaryStage().close();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("MapView.fxml"));
    Pane root = (Pane) fxmlLoader.load();

    Scene scene = new Scene(root);
    App.getPrimaryStage().setScene(scene);
    App.getPrimaryStage().setMaximized(true);
    App.getPrimaryStage().show();

    List<Node> childrenList = root.getChildren();

    changeChildrenMapView(childrenList);
    this.sizeListener =
        new SceneSizeChangeListener(scene, root, childrenList) {
          @Override
          public void changeChildren(List<Node> nodeList) {
            changeChildrenMapView(childrenList);
          }
        };

    scene.widthProperty().addListener(sizeListener);
    scene.heightProperty().addListener(sizeListener);
  }

  public void changeChildrenMapView(List<Node> nodeList) {

    ScrollPane mapScrollPane = (ScrollPane) nodeList.get(0);

    HBox topButtons = (HBox) nodeList.get(1);
    topButtons.setAlignment(Pos.TOP_CENTER);
    topButtons.setPrefWidth(App.getPrimaryStage().getScene().getWidth() - 20);

    mapScrollPane.setMaxHeight(App.getPrimaryStage().getScene().getHeight() - 50);
    mapScrollPane.setMaxWidth(App.getPrimaryStage().getScene().getWidth());
    double mapPaneRatio = mapScrollPane.getWidth() / mapScrollPane.getHeight();
    mapScrollPane.setFitToWidth(true);
    mapScrollPane.setFitToHeight(true);
    mapScrollPane.setPrefHeight(App.getPrimaryStage().getScene().getWindow().getHeight());
    mapScrollPane.setPrefWidth(
        App.getPrimaryStage().getScene().getWindow().getHeight() * mapPaneRatio);
    if (mapScrollPane.getPrefWidth() < App.getPrimaryStage().getScene().getWindow().getWidth()) {
      mapScrollPane.setLayoutX(
          (App.getPrimaryStage().getScene().getWindow().getWidth() - mapScrollPane.getPrefWidth())
              / 2);
    } else {
      mapScrollPane.setLayoutX(0);
    }

    JFXButton findBtn = (JFXButton) nodeList.get(2);
    findBtn.setLayoutY(
        App.getPrimaryStage().getScene().getWindow().getHeight() - findBtn.getHeight() - 60);
    if (mapScrollPane.getPrefWidth() < App.getPrimaryStage().getScene().getWindow().getWidth()) {
      findBtn.setLayoutX(
          ((App.getPrimaryStage().getScene().getWindow().getWidth() - mapScrollPane.getPrefWidth())
                  / 2)
              + 25);
    } else {
      findBtn.setLayoutX(25);
    }

    JFXButton exitBtn = (JFXButton) nodeList.get(3);
    exitBtn.setLayoutY(
        App.getPrimaryStage().getScene().getWindow().getHeight() - exitBtn.getHeight() - 60);

    if (mapScrollPane.getPrefWidth() < App.getPrimaryStage().getScene().getWindow().getWidth()) {
      exitBtn.setLayoutX(
          ((App.getPrimaryStage().getScene().getWindow().getWidth() - mapScrollPane.getPrefWidth())
                  / 2)
              + mapScrollPane.getPrefWidth()
              - exitBtn.getWidth()
              - 20);
    } else {
      exitBtn.setLayoutX(
          App.getPrimaryStage().getScene().getWindow().getWidth() - exitBtn.getWidth() - 30);
    }

    Slider slider = (Slider) nodeList.get(4);
    //    slider.setLayoutX(App.getPrimaryStage().getScene().getWidth() - exitBtn.getWidth() - 100);
    slider.setLayoutY(exitBtn.getLayoutY());

    if (mapScrollPane.getPrefWidth() < App.getPrimaryStage().getScene().getWindow().getWidth()) {
      slider.setLayoutX(
          ((App.getPrimaryStage().getScene().getWindow().getWidth() - slider.getWidth()) / 2));
    } else {
      slider.setLayoutX(
          (App.getPrimaryStage().getScene().getWindow().getWidth() - slider.getWidth()) / 2);
    }

    JFXButton cancelButton = (JFXButton) nodeList.get(5);
    if (mapScrollPane.getPrefWidth() < App.getPrimaryStage().getScene().getWindow().getWidth()) {
      cancelButton.setLayoutX(
          ((App.getPrimaryStage().getScene().getWindow().getWidth() - mapScrollPane.getPrefWidth())
                  / 2)
              + mapScrollPane.getPrefWidth()
              - exitBtn.getWidth()
              - cancelButton.getWidth()
              - 50);
    } else {
      cancelButton.setLayoutX(
          App.getPrimaryStage().getScene().getWindow().getWidth()
              - exitBtn.getWidth()
              - cancelButton.getWidth()
              - 50);
    }
    cancelButton.setLayoutY(exitBtn.getLayoutY());

    JFXTextArea textArea = (JFXTextArea) nodeList.get(7);
    textArea.setLayoutX(App.getPrimaryStage().getScene().getWidth() - textArea.getWidth());
    textArea.setLayoutY(exitBtn.getLayoutY() - textArea.getHeight() - 80);

    JFXButton downloadButton = (JFXButton) nodeList.get(6);
    downloadButton.setLayoutX(mapScrollPane.getLayoutX());
    downloadButton.setLayoutY(findBtn.getLayoutY() - 80);
  }

  @FXML
  private void covidSurvey(ActionEvent event) throws IOException {
    GaussianBlur blur = new GaussianBlur(25);
    App.getPrimaryStage().getScene().getRoot().setEffect(blur);

    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("CovidSurveyView.fxml"));
    Parent root = fxmlLoader.load();
    root.setStyle("-fx-background-color: White");

    this.popup = new Popup();
    popup.getContent().addAll(root);
    popup.show(App.getPrimaryStage());
  }

  public void serviceRequestView() throws IOException {

    if (LoginController.getUserCategory() != null) {
      this.userCategory = LoginController.getUserCategory();
    } else {
      this.userCategory = InitPageController.getUserCategory();
    }

    List<Node> childrenList = App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
    mainButtons = (VBox) childrenList.get(2);
    mainButtons.setVisible(false);
    if (userCategory.equalsIgnoreCase("Guest") || userCategory.equalsIgnoreCase("Patient")) {
      // hide and disable the check request button
      this.disableRequestStatus = true;
    } else if (userCategory.equalsIgnoreCase("Admin")
        || userCategory.equalsIgnoreCase("Employee")) {
      this.disableRequestStatus = false;
    }
    popUpAction("ServicePageView.fxml", this.disableRequestStatus);
  }

  public void popUpAction(String fxml, Boolean isCheckRequestDisabled) throws IOException {

    GaussianBlur blur = new GaussianBlur(25);
    App.getPrimaryStage().getScene().getRoot().setEffect(blur);

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxml));
    Pane root = (Pane) fxmlLoader.load();

    if (fxml.equals("servicePageView.fxml")) {
      JFXButton checkStatusButton = (JFXButton) root.getChildren().get(2);
      if (isCheckRequestDisabled) {
        System.out.println("Hello 1");
        checkStatusButton.setVisible(false);
        checkStatusButton.setDisable(true);
      } else {
        System.out.println("Hello 2 ");
        checkStatusButton.setVisible(true);
        checkStatusButton.setDisable(false);
      }
    }

    this.popup = new Popup();
    this.popup.getContent().addAll(root);

    popup.show(App.getPrimaryStage());
  }

  public static boolean controllerPermissible(String controllerName) {
    try {
      Class<?> controllerClass = null;

      if (controllerName == "ServicePageController") {
        controllerClass =
            Class.forName(
                HomeController.class.getPackage().getName() + ".ServiceRequests." + controllerName);
      } else if (controllerName == "MapController") {
        controllerClass =
            Class.forName(
                HomeController.class.getPackage().getName() + ".Mapping." + controllerName);
      }

      if (userTypeEnum == null) {
        System.out.println("User category is null! User data has been lost. PLEASE FIX");
      } else if (PatientAccessible.class.isAssignableFrom(
          controllerClass)) { // If controllerClass implements PatientAccessible
        return true;
      } else if (EmployeeAccessible.class.isAssignableFrom(
          controllerClass)) { // If controllerClass implements EmployeeAccessible
        return (userTypeEnum == userTypeEnum.Employee || userTypeEnum == userTypeEnum.Admin);
      } else if (AdminAccessible.class.isAssignableFrom(
          controllerClass)) { // If controllerClass implements AdminAccessible
        return (userTypeEnum == userTypeEnum.Admin);
      }
      // If we haven't returned by now, the controller doesn't implement permissions correctly.
      System.out.println(
          "The controller " + controllerName + " does not implement permissions! PLEASE FIX");
      return true; // Because I don't want to break people's code
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean validateUser(String username, String password) {
    if ((FDatabaseTables.getUserTable()
        .validateTheUser(GlobalDb.getConnection(), username, password))) {
      return true;
    } else {
      return false;
    }
  }

  public static String getUserCategory() {
    return userCategory;
  }

  public static UserCategory getUserTypeEnum() {
    return userTypeEnum;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}

  //  public boolean validateUser(String username, String password) {
  //    HomeController.username = username;
  //    HomeController.password = password;
  //    System.out.println(username + " in validateUser");
  //    System.out.println(password + " password in validateUser");
  //
  //    try {
  //      Statement statement = GlobalDb.getConnection().createStatement();
  //      String query = "SELECT category, password FROM Users WHERE id = '" + username + "'";
  //      statement.executeQuery(query);
  //      ResultSet rs = statement.getResultSet();
  //
  //      if (rs.next()) { // If there is a user
  //        String pw = rs.getString("password");
  //        System.out.println(pw + " this is rs");
  //        if (!password.equals(pw)) {
  //          return false;
  //        } else {
  //          switch (rs.getString("category")) {
  //            case "patient":
  //              userTypeEnum = UserCategory.Patient;
  //              break;
  //            case "employee":
  //              userTypeEnum = UserCategory.Employee;
  //              break;
  //            case "admin":
  //              userTypeEnum = UserCategory.Admin;
  //              break;
  //          }
  //        }
  //      } else {
  //        System.out.println("User not found in database!");
  //        return false;
  //      }
  //      return true;
  //    } catch (SQLException e) {
  //      e.printStackTrace();
  //      return false;
  //    }
  //  }
