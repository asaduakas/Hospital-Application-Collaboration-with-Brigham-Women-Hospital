package edu.wpi.teamname.views.ServiceRequests.SRControllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.Access.EmployeeAccessible;
import java.io.IOException;
import java.net.URL;
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

public class ExternalTransportationController extends AbsRequest
    implements Initializable, EmployeeAccessible, IRequest {

  @FXML private JFXComboBox<String> transportComboBox;
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
            "All fields must be filled out before submitting"
                + "\n"
                + "the form. For further assistance, contact IT at"
                + "\n"
                + "123-456- 7890. Questions can also be sent to"
                + "\n"
                + "diamonddragonsIT@gmail.com. "));
    JFXDialog dialog = new JFXDialog(stackPane1, content, JFXDialog.DialogTransition.CENTER);
    JFXButton closeButton = new JFXButton("Close");
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
                .or(super.locationBox.valueProperty().isNull())
                .or(transportComboBox.valueProperty().isNull())
                .or(locationBox.valueProperty().isNull())
                .or(transportComboBox.valueProperty().isNull()));

    if (super.submitButton.isDisabled()) {
      popupWarning(event);
    }

    if (super.submitButton.isDisabled() == false) {

      // Tables.addExTransInfo(connection, super.firstName.getText());

      Text submitText =
          new Text("Your request is submitted." + "\n" + "Would you like to make another request?");
      JFXDialogLayout layout = new JFXDialogLayout();
      layout.setHeading(new Text("Submitted!"));
      layout.setBody(submitText);
      JFXDialog submitDia =
          new JFXDialog(super.stackPane1, layout, JFXDialog.DialogTransition.CENTER);

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

      GlobalDb.getTables()
          .getExternalTransportTable()
          .addEntity(
              GlobalDb.getConnection(),
              "External Transportation",
              super.firstName.getText(),
              super.lastName.getText(),
              super.contactInfo.getText(),
              super.locationBox.getValue(),
              transportComboBox.getValue(),
              staffAssigned.getValue());
    }
  }

  //  private void getInfo()

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    stackPane1.setPickOnBounds(false);
    //    validationPaneFormatter(super.firstName);
    //    validationPaneFormatter(super.lastName);
    //    validationPaneFormatter(super.contactInfo);

    RequiredFieldValidator firstNameValid = new RequiredFieldValidator();
    firstNameValid.setMessage("Enter patient's first name");
    super.firstName.getValidators().add(firstNameValid);
    super.firstName
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) super.firstName.validate();
            });

    RequiredFieldValidator lastNameValid = new RequiredFieldValidator();
    lastNameValid.setMessage("Enter patient's last name");
    super.lastName.getValidators().add(lastNameValid);
    super.lastName
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) super.lastName.validate();
            });

    RequiredFieldValidator contactValid = new RequiredFieldValidator();
    contactValid.setMessage("Enter contact information");
    super.contactInfo.getValidators().add(contactValid);
    super.contactInfo
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) super.contactInfo.validate();
            });

    transportComboBox.getItems().add("Ambulance");
    transportComboBox.getItems().add("Helicopter");
    transportComboBox.getItems().add("Car");

    ArrayList<String> longNameList =
        FDatabaseTables.getNodeTable().fetchLongName(GlobalDb.getConnection());
    longNameList.sort(String::compareToIgnoreCase);
    locationBox.getItems().addAll(longNameList);
    locationBox.getItems().addAll(longNameList);

    ObservableList<String> employeeList =
        FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    employeeList.sort(String::compareToIgnoreCase);
    staffAssigned.getItems().addAll(employeeList);
    locationBox.getItems().addAll(longNameList);
  }
}
