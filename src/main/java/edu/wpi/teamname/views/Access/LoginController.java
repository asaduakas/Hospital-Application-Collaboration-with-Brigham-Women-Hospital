package edu.wpi.teamname.views.Access;

import com.jfoenix.controls.*;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.ControllerManager;
import edu.wpi.teamname.views.SceneSizeChangeListener;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javax.swing.*;

public class LoginController implements AllAccessible {

  @FXML Button exitButton;
  @FXML Button loginButton;
  @FXML JFXTextField usernameField;
  @FXML JFXPasswordField passwordField;
  @FXML StackPane loginStackPane;
  public static String userCategory;

  @FXML
  public void initialize() {
    loginStackPane.setPickOnBounds(false);
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

    Scene scene = App.getPrimaryStage().getScene();

    // App.getPrimaryStage().setScene(scene);
    // App.getPrimaryStage().setMaximized(true);
    // App.getPrimaryStage().show();

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

    HBox topButtons = (HBox) nodeList.get(4);
    topButtons.setLayoutX(
        App.getPrimaryStage().getScene().getWidth() - (topButtons.getWidth() + 26));

    VBox buttons = (VBox) nodeList.get(2);
    buttons.setLayoutX((App.getPrimaryStage().getScene().getWidth() - buttons.getWidth()) / 2);
    buttons.setLayoutY(
        (App.getPrimaryStage().getScene().getHeight() - (buttons.getHeight() / 3)) / 2);

    ImageView hospitalImage = (ImageView) nodeList.get(1);
    double newHospitalRatio;
    newHospitalRatio = 1499 / 722;
    hospitalImage.setFitHeight(App.getPrimaryStage().getScene().getHeight() - 87);
    hospitalImage.setFitWidth(hospitalImage.getFitHeight() * newHospitalRatio);
    hospitalImage.setLayoutX(
        (App.getPrimaryStage().getScene().getWidth() - hospitalImage.getFitWidth()) / 2);

    JFXButton logoutButton = (JFXButton) nodeList.get(5);
    JFXButton exitButton = (JFXButton) nodeList.get(6);
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
  }

  @FXML
  public void takeToSignUp() {
    /*
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("signUpView.fxml"));
    Parent root = fxmlLoader.load();
    InitPageController.popup.hide();
    InitPageController.popup.getContent().addAll(root.getChildrenUnmodifiable());
    InitPageController.popup.show(App.getPrimaryStage());
     */
    ControllerManager.attemptLoadPopup("signUpView.fxml");
  }

  @FXML
  void popupWarning(ActionEvent event, String text) throws IOException {
    Text header = new Text("Error");
    header.setFont(Font.font("System", FontWeight.BOLD, 18));

    JFXDialogLayout warningLayout = new JFXDialogLayout();
    warningLayout.setHeading(header);
    warningLayout.setBody(new Text(text));

    JFXDialog warningDia =
        new JFXDialog(loginStackPane, warningLayout, JFXDialog.DialogTransition.CENTER);
    JFXButton okBtn = new JFXButton("Close");
    okBtn.setStyle("-fx-background-color: #cdcdcd;");
    okBtn.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            warningDia.close();
          }
        });
    warningLayout.setActions(okBtn);
    warningDia.show();
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
