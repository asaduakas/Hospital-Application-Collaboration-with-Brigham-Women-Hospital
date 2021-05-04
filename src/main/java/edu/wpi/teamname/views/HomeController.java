package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamname.App;
import edu.wpi.teamname.views.Access.*;
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
          Scene scene = App.getPrimaryStage().getScene();
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
        });
  }

  public void changeChildrenMapView(List<Node> nodeList) {

    //    ScrollPane mapScrollPane = (ScrollPane) nodeList.get(0);
    //
    //    HBox topButtons = (HBox) nodeList.get(1);
    //    topButtons.setAlignment(Pos.TOP_CENTER);
    //    topButtons.setPrefWidth(App.getPrimaryStage().getScene().getWidth() - 20);
    //
    //    mapScrollPane.setMaxHeight(App.getPrimaryStage().getScene().getHeight() - 50);
    //    mapScrollPane.setMaxWidth(App.getPrimaryStage().getScene().getWidth());
    //    double mapPaneRatio = mapScrollPane.getWidth() / mapScrollPane.getHeight();
    //    mapScrollPane.setFitToWidth(true);
    //    mapScrollPane.setFitToHeight(true);
    //    mapScrollPane.setPrefHeight(App.getPrimaryStage().getScene().getWindow().getHeight());
    //    mapScrollPane.setPrefWidth(
    //        App.getPrimaryStage().getScene().getWindow().getHeight() * mapPaneRatio);
    //    if (mapScrollPane.getPrefWidth() <
    // App.getPrimaryStage().getScene().getWindow().getWidth()) {
    //      mapScrollPane.setLayoutX(
    //          (App.getPrimaryStage().getScene().getWindow().getWidth() -
    // mapScrollPane.getPrefWidth())
    //              / 2);
    //    } else {
    //      mapScrollPane.setLayoutX(0);
    //    }
    //
    //    JFXButton findBtn = (JFXButton) nodeList.get(2);
    //    findBtn.setLayoutY(
    //        App.getPrimaryStage().getScene().getWindow().getHeight() - findBtn.getHeight() - 60);
    //    if (mapScrollPane.getPrefWidth() <
    // App.getPrimaryStage().getScene().getWindow().getWidth()) {
    //      findBtn.setLayoutX(
    //          ((App.getPrimaryStage().getScene().getWindow().getWidth() -
    // mapScrollPane.getPrefWidth())
    //                  / 2)
    //              + 25);
    //    } else {
    //      findBtn.setLayoutX(25);
    //    }
    //
    //    JFXButton exitBtn = (JFXButton) nodeList.get(3);
    //    exitBtn.setLayoutY(
    //        App.getPrimaryStage().getScene().getWindow().getHeight() - exitBtn.getHeight() - 60);
    //
    //    if (mapScrollPane.getPrefWidth() <
    // App.getPrimaryStage().getScene().getWindow().getWidth()) {
    //      exitBtn.setLayoutX(
    //          ((App.getPrimaryStage().getScene().getWindow().getWidth() -
    // mapScrollPane.getPrefWidth())
    //                  / 2)
    //              + mapScrollPane.getPrefWidth()
    //              - exitBtn.getWidth()
    //              - 20);
    //    } else {
    //      exitBtn.setLayoutX(
    //          App.getPrimaryStage().getScene().getWindow().getWidth() - exitBtn.getWidth() - 30);
    //    }
    //
    //    Slider slider = (Slider) nodeList.get(4);
    //    //    slider.setLayoutX(App.getPrimaryStage().getScene().getWidth() - exitBtn.getWidth() -
    // 100);
    //    slider.setLayoutY(exitBtn.getLayoutY());
    //
    //    if (mapScrollPane.getPrefWidth() <
    // App.getPrimaryStage().getScene().getWindow().getWidth()) {
    //      slider.setLayoutX(
    //          ((App.getPrimaryStage().getScene().getWindow().getWidth() - slider.getWidth()) /
    // 2));
    //    } else {
    //      slider.setLayoutX(
    //          (App.getPrimaryStage().getScene().getWindow().getWidth() - slider.getWidth()) / 2);
    //    }
    //
    //    JFXButton cancelButton = (JFXButton) nodeList.get(5);
    //    if (mapScrollPane.getPrefWidth() <
    // App.getPrimaryStage().getScene().getWindow().getWidth()) {
    //      cancelButton.setLayoutX(
    //          ((App.getPrimaryStage().getScene().getWindow().getWidth() -
    // mapScrollPane.getPrefWidth())
    //                  / 2)
    //              + mapScrollPane.getPrefWidth()
    //              - exitBtn.getWidth()
    //              - cancelButton.getWidth()
    //              - 50);
    //    } else {
    //      cancelButton.setLayoutX(
    //          App.getPrimaryStage().getScene().getWindow().getWidth()
    //              - exitBtn.getWidth()
    //              - cancelButton.getWidth()
    //              - 50);
    //    }
    //    cancelButton.setLayoutY(exitBtn.getLayoutY());
    //
    //    JFXTextArea textArea = (JFXTextArea) nodeList.get(7);
    //    textArea.setLayoutX(App.getPrimaryStage().getScene().getWidth() - textArea.getWidth());
    //    textArea.setLayoutY(exitBtn.getLayoutY() - textArea.getHeight() - 80);
    //
    //    JFXButton downloadButton = (JFXButton) nodeList.get(6);
    //    downloadButton.setLayoutX(mapScrollPane.getLayoutX());
    //    downloadButton.setLayoutY(findBtn.getLayoutY() - 80);
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
