package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.teamname.App;
import edu.wpi.teamname.views.Access.*;
import edu.wpi.teamname.views.Mapping.MapScrollPane;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javax.swing.*;

public class HomeController extends Application implements AllAccessible {

  @FXML private JFXButton exitButton; // Btn to exit program
  @FXML private JFXButton logoutButton;
  @FXML private JFXButton usersBtn;

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
    LoginController.userCategory = null;
    HomeController.historyTracker = 0;
    ControllerManager.attemptLoadPage(
        "initPageView.fxml", fxmlLoader -> App.initLoader(fxmlLoader));
  }

  @FXML
  private void closeApp(ActionEvent event) {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void emergencyView() throws IOException {
    ControllerManager.attemptLoadPopupBlur("Emergency.fxml");
  }

  @FXML
  public void helpView() throws IOException {
    ControllerManager.attemptLoadPopupBlur("Help.fxml");
  }

  @FXML
  public void usersView(ActionEvent event) {
    ControllerManager.attemptLoadPopupBlur(
        "UsersView.fxml",
        fxmlLoader -> ((Pane) fxmlLoader.getRoot()).setStyle("-fx-background-color: White"));
  }

  @FXML
  public void hospitalMapView() throws IOException {
    if (LoginController.getUserCategory() != null) {
      this.userCategory = LoginController.getUserCategory();
    } else {
      this.userCategory = InitPageController.getUserCategory();
    }

    ControllerManager.attemptLoadPage(
        "MapView.fxml",
        fxmlLoader -> {
          Pane root = (Pane) fxmlLoader.getRoot();
          List<Node> childrenList = root.getChildren();
          System.out.println("this is childrenList of the map" + childrenList);
          JFXToggleButton mapEditing = (JFXToggleButton) childrenList.get(4);
          if (!userCategory.equalsIgnoreCase("admin")) {
            mapEditing.setVisible(false);
            mapEditing.setDisable(true);
          }

          Scene scene = App.getPrimaryStage().getScene();
          changeChildrenMapView(childrenList);
          this.sizeListener =
              new SceneSizeChangeListener(scene, root, childrenList) {
                @Override
                public void changeChildren(List<Node> nodeList) {
                  updateMapScrollPane(nodeList);
                  changeChildrenMapView(childrenList);
                }
              };
          scene.widthProperty().addListener(sizeListener);
          scene.heightProperty().addListener(sizeListener);
          updateMapScrollPane(childrenList);
        });
  }

  // Make sure map scroll pane is scaled correctly
  private void updateMapScrollPane(List<Node> nodeList) {
    for (Node node : nodeList)
      if (node instanceof MapScrollPane) ((MapScrollPane) node).updateScaleRange();
  }

  public void changeChildrenMapView(List<Node> nodeList) {

    JFXToggleButton mapEditorBtn = (JFXToggleButton) nodeList.get(4);
    JFXButton exitBtn = (JFXButton) nodeList.get(5);
    JFXNodesList floorBtns = (JFXNodesList) nodeList.get(8);
    JFXNodesList csvBtns = (JFXNodesList) nodeList.get(9);
    JFXButton helpBtn = (JFXButton) nodeList.get(6);

    exitBtn.setLayoutY(
        App.getPrimaryStage().getScene().getWindow().getHeight() - exitBtn.getHeight() - 60);
    exitBtn.setLayoutX(App.getPrimaryStage().getScene().getWidth() - exitBtn.getWidth() - 40);

    floorBtns.setLayoutY(20);
    floorBtns.setLayoutX(App.getPrimaryStage().getScene().getWidth() - floorBtns.getWidth() - 40);

    mapEditorBtn.setLayoutX(floorBtns.getLayoutX() - 150);
    mapEditorBtn.setLayoutY(14);

    csvBtns.setLayoutY(20);
    csvBtns.setLayoutX(mapEditorBtn.getLayoutX() - csvBtns.getWidth() - 20);

    helpBtn.setLayoutY(exitBtn.getLayoutY());
    helpBtn.setLayoutX(20);
  }

  @FXML
  private void covidSurvey(ActionEvent event) {
    ControllerManager.attemptLoadPopupBlur(
        "CovidSurveyView.fxml",
        fxmlLoader -> {
          Pane root = (Pane) fxmlLoader.getRoot();
          root.setStyle("-fx-background-color: White");
        });
  }

  public void serviceRequestView() {
    if (LoginController.getUserCategory() != null) {
      this.userCategory = LoginController.getUserCategory();
    } else {
      this.userCategory = InitPageController.getUserCategory();
    }

    ControllerManager.attemptLoadPopupBlur(
        "ServicePageView.fxml",
        fxmlLoader -> {
          List<Node> childrenList =
              App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
          mainButtons = (VBox) childrenList.get(2);
          mainButtons.setVisible(false);
          boolean disableStatusButton = false;
          if (userCategory.equalsIgnoreCase("Guest") || userCategory.equalsIgnoreCase("Patient")) {
            // hide and disable the check request button
            disableStatusButton = true;
          } else if (userCategory.equalsIgnoreCase("Admin")
              || userCategory.equalsIgnoreCase("Employee")) {
            disableStatusButton = false;
          }
          JFXButton checkStatusButton =
              (JFXButton) ((Pane) fxmlLoader.getRoot()).getChildren().get(2);
          if (disableStatusButton) {
            // System.out.println("Hello 1");
            checkStatusButton.setVisible(false);
            checkStatusButton.setDisable(true);
          } else {
            // System.out.println("Hello 2 ");
            checkStatusButton.setVisible(true);
            checkStatusButton.setDisable(false);
          }
        });
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
