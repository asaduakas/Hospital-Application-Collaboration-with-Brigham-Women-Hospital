package edu.wpi.cs3733.d21.teamD.views.Access;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.d21.teamD.App;
import edu.wpi.cs3733.d21.teamD.Ddb.FDatabaseTables;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.views.ControllerManager;
import edu.wpi.cs3733.d21.teamD.views.DialogFactory;
import edu.wpi.cs3733.d21.teamD.views.SceneSizeChangeListener;
import java.io.IOException;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoginController implements AllAccessible {

  @FXML Button exitButton;
  @FXML JFXButton loginButton;
  @FXML JFXTextField usernameField;
  @FXML JFXPasswordField passwordField;
  @FXML StackPane loginStackPane;
  @FXML AnchorPane anchor;
  public static String userCategory;

  private DialogFactory dialogFactory;

  @FXML
  public void initialize() {
    final BooleanProperty focused = new SimpleBooleanProperty();
    dialogFactory = new DialogFactory(loginStackPane);
    loginStackPane.setPickOnBounds(false);

    anchor.addEventHandler(
        KeyEvent.KEY_PRESSED,
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            loginButton.fire();
            e.consume();
          }
        });
  }

  @FXML
  private void login(ActionEvent event) throws IOException {

    if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
      popupWarning(event, "Please fill out the required fields.");
    } else if (!FDatabaseTables.getUserTable()
        .validateTheUser(
            GlobalDb.getConnection(), usernameField.getText(), passwordField.getText())) {
      popupWarning(event, "Incorrect username or password, please try again!");
    } else {
      this.userCategory =
          FDatabaseTables.getUserTable()
              .getCategoryofUser(GlobalDb.getConnection(), usernameField.getText());
      ControllerManager.attemptLoadPage("HomeView.fxml", fxmlLoader -> start(fxmlLoader.getRoot()));
    }
  }

  public static void start(Pane root) {

    // App.getPrimaryStage().close();

    Text userType = new Text("You are logged in as : " + userCategory);

    userType.setFill(Color.WHITE);
    userType.setStyle("-fx-font-size: 20px; -fx-font-weight: Bold");
    root.getChildren().add(userType);

    List<Node> childrenList = root.getChildrenUnmodifiable();

    if (!userCategory.equalsIgnoreCase("admin")) {
      HBox hBox = (HBox) childrenList.get(4);
      JFXButton usersBtn = (JFXButton) ((HBox) hBox).getChildren().get(0);
      usersBtn.setVisible(false);
      usersBtn.setDisable(true);
    }

    Scene scene = App.getPrimaryStage().getScene();

    changeChildrenHomePage(childrenList);
    SceneSizeChangeListener sizeListener =
        new SceneSizeChangeListener(scene, root, childrenList) {
          @Override
          public void changeChildren(List<Node> nodeList) {
            changeChildrenHomePage(childrenList);
          }
        };

    scene.widthProperty().addListener(sizeListener);
    scene.heightProperty().addListener(sizeListener);
  }

  public static void changeChildrenHomePage(List<Node> nodeList) {

    ImageView hospitalImage = (ImageView) nodeList.get(1);
    VBox buttons = (VBox) nodeList.get(2);
    HBox topButtons = (HBox) nodeList.get(4);
    JFXButton logoutButton = (JFXButton) nodeList.get(5);
    JFXButton exitButton = (JFXButton) nodeList.get(6);
    StackPane stackPane = (StackPane) nodeList.get(7);

    topButtons.setLayoutX(
        App.getPrimaryStage().getScene().getWidth() - (topButtons.getWidth() + 26));

    buttons.setLayoutX((App.getPrimaryStage().getScene().getWidth() - buttons.getWidth()) / 2);
    buttons.setLayoutY(
        (App.getPrimaryStage().getScene().getHeight() - (buttons.getHeight() / 3)) / 2);

    double newHospitalRatio;
    newHospitalRatio = 1499 / 722;
    hospitalImage.setFitHeight(App.getPrimaryStage().getScene().getHeight() - 87);
    hospitalImage.setFitWidth(hospitalImage.getFitHeight() * newHospitalRatio);
    hospitalImage.setLayoutX(
        (App.getPrimaryStage().getScene().getWidth() - hospitalImage.getFitWidth()) / 2);

    if (hospitalImage.getFitWidth() < App.getPrimaryStage().getScene().getWindow().getWidth()) {
      logoutButton.setLayoutX(
          ((App.getPrimaryStage().getScene().getWindow().getWidth() - hospitalImage.getFitWidth())
                  / 2)
              + hospitalImage.getFitWidth()
              - (logoutButton.getWidth() * 2 + 80));
      exitButton.setLayoutX(
          ((App.getPrimaryStage().getScene().getWindow().getWidth() - hospitalImage.getFitWidth())
                  / 2)
              + hospitalImage.getFitWidth()
              - (exitButton.getWidth() + 50));
    } else {
      logoutButton.setLayoutX(
          App.getPrimaryStage().getScene().getWidth() - (logoutButton.getWidth() * 2 + 80));
      exitButton.setLayoutX(
          App.getPrimaryStage().getScene().getWidth() - (exitButton.getWidth() + 50));
    }
    logoutButton.setLayoutY(
        App.getPrimaryStage().getScene().getHeight() - (logoutButton.getHeight() + 50));
    exitButton.setLayoutY(
        App.getPrimaryStage().getScene().getHeight() - (exitButton.getHeight() + 50));

    Text userType = (Text) nodeList.get(nodeList.size() - 1);
    userType.setX(exitButton.getLayoutX() - 170);
    userType.setY(App.getPrimaryStage().getScene().getHeight() - 25);

    stackPane.setPrefWidth(500);
    stackPane.setPrefHeight(300);
    stackPane.setLayoutX(
        (App.getPrimaryStage().getScene().getWidth() - stackPane.getPrefWidth()) / 2);
    stackPane.setLayoutY(
        (App.getPrimaryStage().getScene().getHeight() - stackPane.getHeight()) / 2);
    stackPane.setPickOnBounds(false);

    ImageView chatbot = (ImageView) nodeList.get(8);
    //    chatbot.setLayoutY(logoutButton.getLayoutY() - 20);
    //    chatbot.setLayoutX(logoutButton.getLayoutX() - logoutButton.getWidth() - 10);
    chatbot.setLayoutY(logoutButton.getLayoutY() - logoutButton.getWidth());
    chatbot.setLayoutX(exitButton.getLayoutX() + 10);
  }

  @FXML
  public void takeToSignUp() {
    ControllerManager.attemptLoadPopup("signUpView.fxml");
  }

  @FXML
  void popupWarning(ActionEvent event, String text) throws IOException {
    dialogFactory.createOneButtonDialog("Error", text, "Close", () -> {});
  }

  @FXML
  private void exit(ActionEvent event) {
    ControllerManager.exitPopup();
  }

  public static String getUserCategory() {
    return userCategory;
  }

  public static void setUserCategory(String userType) {
    userCategory = userType;
  }
}
