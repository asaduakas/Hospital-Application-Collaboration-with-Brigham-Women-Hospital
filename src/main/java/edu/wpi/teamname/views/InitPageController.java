package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamname.views.Access.AllAccessible;
import edu.wpi.teamname.views.Access.LoginController;
import java.awt.*;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class InitPageController implements AllAccessible {
  @FXML private JFXButton loginButton;
  @FXML private JFXButton signUpButton;
  @FXML private JFXButton emergencyButton;
  @FXML private JFXButton exitButton;
  @FXML private JFXButton covidButton;
  @FXML public static Popup popup;
  private static String userCategory;

  @FXML
  public void InitPageController() {}

  @FXML
  public void initialize() {}

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
}
