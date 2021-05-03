package edu.wpi.teamname.views.Access;

import com.jfoenix.controls.*;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.ControllerManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class signUpController implements AllAccessible {

  @FXML Button exitButton;
  @FXML Button signUpButton;
  @FXML JFXTextField nameField;
  @FXML JFXTextField usernameField;
  @FXML JFXPasswordField passwordField;
  @FXML JFXComboBox category;
  @FXML StackPane signUPstackPane;

  @FXML
  public void initialize() throws IOException {

    signUPstackPane.setPickOnBounds(false);
    ObservableList<String> categoryList =
        FXCollections.observableArrayList("Patient", "Employee", "Admin");
    this.category.getItems().addAll(categoryList);
  }

  @FXML
  public void signUp(ActionEvent event) throws IOException {
    // FXMLLoader fxmlLoader =
    //    new FXMLLoader(getClass().getClassLoader().getResource("HomeView.fxml"));
    // Parent root = fxmlLoader.load();

    String categoryName = (String) category.getValue();
    System.out.println(categoryName);

    // handling empty fields
    if (event.getSource() == signUpButton) {
      if (usernameField.getText().isEmpty()
          || nameField.getText().isEmpty()
          || passwordField.getText().isEmpty()
          || (categoryName == null)) {
        popupWarning(event);
      } else {
        addToDatabase(
            GlobalDb.getConnection(),
            usernameField.getText(),
            passwordField.getText().toString(),
            nameField.getText(),
            categoryName);
        /*
        InitPageController.popup.hide();
        App.getPrimaryStage().close();
        App takeToInit = new App();
        takeToInit.start(App.getPrimaryStage());
         */
        ControllerManager.exitPopup();
      }
    }
  }

  public void addToDatabase(
      Connection conn, String username, String password, String name, String category) {
    PreparedStatement stmnt = null;
    try {
      stmnt =
          conn.prepareStatement(
              "INSERT INTO Users(id, password, firstName, category) VALUES(?,?,?,?)");
      stmnt.setString(1, username);
      stmnt.setString(2, password);
      stmnt.setString(3, name);
      stmnt.setString(4, category);
      stmnt.executeUpdate();
    } catch (SQLException e) {

    }
  }

  @FXML
  public void takeToLogin() {
    /*
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("LoginView.fxml"));
    Parent root = fxmlLoader.load();
    InitPageController.popup.hide();
    InitPageController.popup.getContent().addAll(root.getChildrenUnmodifiable());
    InitPageController.popup.show(App.getPrimaryStage());
    */
    ControllerManager.attemptLoadPopup("LoginView.fxml");
  }

  @FXML
  void popupWarning(ActionEvent event) throws IOException {
    Text header = new Text("Error");
    header.setFont(Font.font("System", FontWeight.BOLD, 18));

    JFXDialogLayout warningLayout = new JFXDialogLayout();
    warningLayout.setHeading(header);
    warningLayout.setBody(new Text("Please fill out the required fields."));

    JFXDialog warningDia =
        new JFXDialog(signUPstackPane, warningLayout, JFXDialog.DialogTransition.CENTER);
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
}
