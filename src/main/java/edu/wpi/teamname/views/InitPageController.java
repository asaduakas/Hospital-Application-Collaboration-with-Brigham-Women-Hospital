package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamname.App;
import edu.wpi.teamname.views.Access.AllAccessible;
import edu.wpi.teamname.views.Access.LoginController;
import java.awt.*;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
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
    popUpAction("LoginView.fxml");
  }

  @FXML
  public void signUp() throws IOException {
    popUpAction("signUpView.fxml");
  }

  @FXML
  public void emergencyView() throws IOException {
    popUpAction("Emergency.fxml");
  }

  @FXML
  public void helpView() throws IOException {
    popUpAction("Help.fxml");
  }

  public void popUpAction(String fxml) throws IOException {

    GaussianBlur blur = new GaussianBlur(25);
    App.getPrimaryStage().getScene().getRoot().setEffect(blur);

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxml));
    Parent root = fxmlLoader.load();
    this.popup = new Popup();
    popup.getContent().addAll(root);
    popup.show(App.getPrimaryStage());
  }

  @FXML
  private void closeApp(ActionEvent event) {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void covidSurvey(ActionEvent event) throws IOException {
    GaussianBlur blur = new GaussianBlur(25);
    App.getPrimaryStage().getScene().getRoot().setEffect(blur);

    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("CovidSurveyView.fxml"));
    Pane root = (Pane) fxmlLoader.load();
    root.setStyle("-fx-background-color: White");

    this.popup = new Popup();
    popup.getContent().addAll(root);
    popup.isAutoFix();
    popup.show(App.getPrimaryStage());
  }

  @FXML
  public void guestLogin() throws IOException {
    App.getPrimaryStage().close();
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("HomeView.fxml"));
    Pane root = (Pane) fxmlLoader.load();
    this.userCategory = "Guest";
    LoginController.setUserCategory(null);
    LoginController guestLoginController = new LoginController();
    guestLoginController.start(root, this.userCategory);
  }

  public static String getUserCategory() {
    return userCategory;
  }
}
