package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import edu.wpi.cs3733.d21.teamD.views.Access.LoginController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class InitPageController implements AllAccessible {
  @FXML private JFXButton loginButton;
  @FXML private JFXButton signUpButton;
  @FXML private JFXButton emergencyButton;
  @FXML private JFXButton exitButton;
  @FXML private JFXButton covidButton;
  @FXML public static Popup popup;
  @FXML private JFXButton pageButton;
  @FXML private AnchorPane mainPane;
  private static String userCategory;

  @FXML
  public void InitPageController() {}

  @FXML
  public void initialize() {
    exitButton.setLayoutX(1076);
    exitButton.setLayoutY(613);
    initializePages();
  }

  @FXML
  public void login() throws IOException {
    ControllerManager.attemptLoadPopupBlur("LoginView.fxml");
  }

  @FXML
  public void signUp() throws IOException {
    ControllerManager.attemptLoadPopupBlur("signUpView.fxml");
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
  private void closeApp(ActionEvent event) {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
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

  @FXML
  public void guestLogin() {
    LoginController.userCategory = "Guest";
    ControllerManager.attemptLoadPage(
        "HomeView.fxml", fxmlLoader -> LoginController.start(fxmlLoader.getRoot()));
  }

  public static String getUserCategory() {
    return userCategory;
  }

  @FXML
  private void initializePages() {
    pageButton.setPrefHeight(60);
    pageButton.setPrefWidth(60);
    pageButton.setStyle(
        "-fx-background-color: #E5E5E5; -fx-background-radius: 60px; -fx-text-fill:  #000000; -fx-font-size: 14; -fx-font-weight: Bold");
    Text pages = new Text("Pages");
    //    pages.setStyle("-fx-text-fill:  #000000; -fx-font-size: 14; -fx-font-weight: Bold");

    pageButton.setText(pages.getText());

    JFXButton aboutButton = new JFXButton("ABOUT");
    aboutButton.setButtonType(JFXButton.ButtonType.FLAT);
    aboutButton.setStyle(
        "-fx-background-color: #C8BEFF; -fx-background-radius: 60px; -fx-font-size: 12");
    aboutButton.setPrefHeight(60);
    aboutButton.setPrefWidth(60);
    aboutButton.setOnAction(this::aboutPage);

    JFXButton creditButton = new JFXButton("CREDITS");
    creditButton.setButtonType(JFXButton.ButtonType.FLAT);
    creditButton.setStyle(
        "-fx-background-color: #AB9CFF; -fx-background-radius: 60px; -fx-font-size: 11");
    creditButton.setPrefHeight(60);
    creditButton.setPrefWidth(60);
    creditButton.setOnAction(this::creditsPage);

    JFXNodesList nodeList = new JFXNodesList();
    nodeList.addAnimatedNode(pageButton);
    nodeList.addAnimatedNode(aboutButton);
    nodeList.addAnimatedNode(creditButton);
    nodeList.setSpacing(20d);
    mainPane.getChildren().add(nodeList);

    nodeList.setRotate(180);
  }

  @FXML
  private void aboutPage(ActionEvent event) {
    ControllerManager.attemptLoadPopupBlur(
        "AboutView.fxml",
        fxmlLoader -> {
          Pane root = (Pane) fxmlLoader.getRoot();
          root.setStyle("-fx-background-color: White");
        });
  }

  @FXML
  private void creditsPage(ActionEvent event) {
    ControllerManager.attemptLoadPopupBlur(
        "CreditsView.fxml",
        fxmlLoader -> {
          Pane root = (Pane) fxmlLoader.getRoot();
          root.setStyle("-fx-background-color: White");
        });
  }
}
