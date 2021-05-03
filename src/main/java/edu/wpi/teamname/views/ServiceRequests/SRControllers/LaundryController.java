package edu.wpi.teamname.views.ServiceRequests.SRControllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.Access.EmployeeAccessible;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Popup;

public class LaundryController extends AbsRequest
    implements Initializable, EmployeeAccessible, IRequest {

  @FXML private JFXComboBox<String> staffAssigned;
  @FXML public static Popup popup;

  @FXML
  public void popUpAction() throws IOException {
    popup = new Popup();
    // ServicePageController.popup.hide();
    super.popUpAction("Emergency.fxml");
  }

  @FXML
  public void loadDialog(MouseEvent Event) {
    JFXDialogLayout content = new JFXDialogLayout();
    content.setHeading(new Text("More Information"));
    content.setBody(
        new Text(
            "For assistance, contact IT at 123-456-"
                + "\n"
                + "7890. Further questions can be sent to"
                + "\n"
                + "diamonddragonsIT@gmail.com. "));
    JFXDialog dialog = new JFXDialog(stackPane1, content, JFXDialog.DialogTransition.CENTER);
    JFXButton closeButton = new JFXButton("Close");
    closeButton.setStyle("-fx-background-color: #cdcdcd;");
    closeButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dialog.close();
          }
        });
    content.setActions(closeButton);
    dialog.show();
  }

  @FXML
  private void submitPage(ActionEvent event) throws IOException {
    submitButton
        .disableProperty()
        .bind(
            super.firstName
                .textProperty()
                .isEmpty()
                .or(super.lastName.textProperty().isEmpty())
                .or(super.contactInfo.textProperty().isEmpty())
                .or(super.locationBox.valueProperty().isNull()));

    if (super.submitButton.isDisabled()) {
      popupWarning(event);
    }

    if (super.submitButton.isDisabled() == false) {

      Text submitText =
          new Text("Your request is submitted." + "\n" + "Would you like to make another request?");
      JFXDialogLayout layout = new JFXDialogLayout();
      layout.setHeading(new Text("Submitted!"));
      layout.setBody(submitText);
      JFXDialog submitDia = new JFXDialog(stackPane1, layout, JFXDialog.DialogTransition.CENTER);

      JFXButton okBtn = new JFXButton("Yes");
      okBtn.setPrefHeight(20);
      okBtn.setPrefWidth(60);
      okBtn.setId("yesBtn");
      okBtn.setStyle("-fx-background-color: #cdcdcd;");
      okBtn.setButtonType(JFXButton.ButtonType.FLAT);
      okBtn.setOnAction(
          new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              // ServicePageController.popup.hide();
              try {
                popUpAction("ServicePageView.fxml");
              } catch (IOException e) {
                e.printStackTrace();
              }
              submitDia.close();
            }
          });

      JFXButton noBtn = new JFXButton("No");
      noBtn.setPrefHeight(20);
      noBtn.setPrefWidth(60);
      noBtn.setId("noBtn");
      noBtn.setButtonType(JFXButton.ButtonType.FLAT);
      noBtn.setOnAction(
          new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              goHome();
              submitDia.close();
            }
          });
      noBtn.setStyle("-fx-background-color: #cdcdcd;");

      layout.setActions(okBtn, noBtn);

      submitDia.show();

      Connection connection = null;
      try {
        connection =
            DriverManager.getConnection(
                "jdbc:derby:myDB;create=true;username=Admin;password=Admin");
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }

      GlobalDb.getTables()
          .getLaundryRequestTable()
          .addEntity(
              GlobalDb.getConnection(),
              super.firstName.getText(),
              super.lastName.getText(),
              super.contactInfo.getText(),
              super.locationBox.getValue(),
              staffAssigned.getValue());
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    stackPane1.setPickOnBounds(false);
    //    validationPaneFormatter(super.firstName);
    //    validationPaneFormatter(super.lastName);
    //    validationPaneFormatter(super.contactInfo);

    RequiredFieldValidator nameValid = new RequiredFieldValidator();
    nameValid.setMessage("Enter patient's name");
    super.firstName.getValidators().add(nameValid);
    super.firstName
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) super.firstName.validate();
            });

    RequiredFieldValidator roomValid = new RequiredFieldValidator();
    roomValid.setMessage("Enter patient's room number");
    super.lastName.getValidators().add(roomValid);
    super.lastName
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) super.lastName.validate();
            });

    RequiredFieldValidator floorValid = new RequiredFieldValidator();
    floorValid.setMessage("Enter patient's floor number");
    super.contactInfo.getValidators().add(floorValid);
    super.contactInfo
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) super.contactInfo.validate();
            });

    ObservableList<String> employeeList =
        FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    employeeList.sort(String::compareToIgnoreCase);
    staffAssigned.getItems().addAll(employeeList);
    ArrayList<String> longNameList =
        FDatabaseTables.getNodeTable().fetchLongName(GlobalDb.getConnection());
    longNameList.sort(String::compareToIgnoreCase);
    locationBox.getItems().addAll(longNameList);
  }
}
