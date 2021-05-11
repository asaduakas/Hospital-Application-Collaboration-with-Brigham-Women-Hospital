package edu.wpi.cs3733.d21.teamD.views.ServiceRequests.SRControllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.d21.teamD.Ddb.FDatabaseTables;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.views.Access.EmployeeAccessible;
import edu.wpi.cs3733.d21.teamD.views.ControllerManager;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Popup;

public class FloralDeliveryController extends AbsRequest
    implements Initializable, EmployeeAccessible, IRequest {

  @FXML private JFXTextField typeFlower;
  @FXML private JFXTextField numberFlower;
  @FXML private JFXTextField fromName;
  @FXML private JFXComboBox<String> staffAssigned;
  @FXML public static Popup popup;

  @FXML
  public void popUpAction() throws IOException {
    popup = new Popup();
    //    // ServicePageController.popup.hide();
    //    super.popUpAction("Emergency.fxml");
    ControllerManager.attemptLoadPopupBlur("Emergency.fxml");
  }

  @FXML
  private void popupSubmit(ActionEvent event) throws IOException {
    submitButton
        .disableProperty()
        .bind(
            super.firstName
                .textProperty()
                .isEmpty()
                .or(super.lastName.textProperty().isEmpty())
                .or(super.contactInfo.textProperty().isEmpty())
                .or(super.locationBox.valueProperty().isNull())
                .or(typeFlower.textProperty().isEmpty())
                .or(numberFlower.textProperty().isEmpty())
                .or(numberFlower.textProperty().isEmpty())
                .or(fromName.textProperty().isEmpty()));

    if (super.submitButton.isDisabled()) {
      popupWarning(event);
    }

    if (super.submitButton.isDisabled() == false) {

      // Tables.addExTransInfo(connection, super.firstName.getText());

      dialogFactory.createTwoButtonDialog(
          "Submitted!",
          "Your request is submitted." + "\n" + "Would you like to make another request?",
          "Yes",
          () -> {
            try {
              popUpAction("ServicePageView.fxml");
            } catch (IOException e) {
              e.printStackTrace();
            }
          },
          "No",
          this::goHome);

      GlobalDb.getTables()
          .getFloralDeliveryTable()
          .addEntity(
              GlobalDb.getConnection(),
              super.firstName.getText(),
              super.lastName.getText(),
              super.contactInfo.getText(),
              super.locationBox.getValue(),
              staffAssigned.getValue(),
              typeFlower.getText(),
              numberFlower.getText().toString(),
              fromName.getText());
    }
  }

  public void initialize(URL url, ResourceBundle rb) {
    initDialogFactory();
    stackPane1.setPickOnBounds(false);

    //    validationPaneFormatter(super.firstName);
    //    validationPaneFormatter(super.lastName);
    //    validationPaneFormatter(typeFlower);
    //    validationPaneFormatter(numberFlower);
    //    validationPaneFormatter(super.contactInfo);
    //    validationPaneFormatter(fromName);

    RequiredFieldValidator recNameValid = new RequiredFieldValidator();
    recNameValid.setMessage("Enter first name");
    super.firstName.getValidators().add(recNameValid);
    super.firstName
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) super.firstName.validate();
            });

    RequiredFieldValidator roomNumValid = new RequiredFieldValidator();
    roomNumValid.setMessage("Enter last name");
    super.lastName.getValidators().add(roomNumValid);
    super.lastName
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) super.lastName.validate();
            });

    RequiredFieldValidator typeFlowerValid = new RequiredFieldValidator();
    typeFlowerValid.setMessage("Enter flower type");
    typeFlower.getValidators().add(typeFlowerValid);
    typeFlower
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) typeFlower.validate();
            });

    RequiredFieldValidator numberFlowerValid = new RequiredFieldValidator();
    numberFlower
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) numberFlower.validate();
              numberFlowerValid.setMessage("Please enter number of flowers");
            });
    numberFlower.getValidators().add(numberFlowerValid);
    numberFlower
        .textProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                  numberFlower.setText(newValue.replaceAll("[^\\d]", ""));
                  numberFlowerValid.setMessage("Please enter numbers");
                }
              }
            });

    RequiredFieldValidator fromNameValid = new RequiredFieldValidator();
    fromNameValid.setMessage("Enter name of sender");
    fromName.getValidators().add(fromNameValid);
    fromName
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) fromName.validate();
            });

    RequiredFieldValidator contactInfoValid = new RequiredFieldValidator();
    contactInfoValid.setMessage("Enter contact information");
    super.contactInfo.getValidators().add(contactInfoValid);
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
