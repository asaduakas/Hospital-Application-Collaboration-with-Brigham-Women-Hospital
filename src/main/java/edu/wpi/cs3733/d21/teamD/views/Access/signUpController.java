package edu.wpi.cs3733.d21.teamD.views.Access;

import static edu.wpi.cs3733.d21.teamD.views.Email.sendSignUpEmail;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.Ddb.UsersTable;
import edu.wpi.cs3733.d21.teamD.views.ControllerManager;
import edu.wpi.cs3733.d21.teamD.views.DialogFactory;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class signUpController implements AllAccessible {

  @FXML Button exitButton;
  @FXML Button signUpButton;
  @FXML JFXTextField nameField;
  @FXML JFXTextField usernameField;
  @FXML JFXTextField emailField;
  @FXML JFXPasswordField passwordField;
  @FXML JFXComboBox category;
  @FXML StackPane signUPstackPane;

  private DialogFactory dialogFactory;

  @FXML
  public void initialize() throws IOException {
    dialogFactory = new DialogFactory(signUPstackPane);
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
        UsersTable.addEntity(
            GlobalDb.getConnection(),
            usernameField.getText(),
            passwordField.getText().toString(),
            nameField.getText(),
            categoryName,
            emailField.getText());
        /*InitPageController.popup.hide();
        App.getPrimaryStage().close();
        App takeToInit = new App();
        takeToInit.start(App.getPrimaryStage());
         */
        ControllerManager.exitPopup();

        // Send Email
        String email = emailField.getText();
        String name = nameField.getText();
        String userName = usernameField.getText();
        sendSignUpEmail(email, name, userName);
      }
    }
  }

  @FXML
  public void takeToLogin() {
    ControllerManager.attemptLoadPopup("LoginView.fxml");
  }

  @FXML
  void popupWarning(ActionEvent event) throws IOException {
    dialogFactory.createOneButtonDialog(
        "Error", "Please fill out the required fields.", "Close", () -> {});
  }

  @FXML
  private void exit(ActionEvent event) {
    ControllerManager.exitPopup();
  }
}
