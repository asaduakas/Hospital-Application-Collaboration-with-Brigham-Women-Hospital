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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Popup;

public class ComputerServiceController extends AbsRequest
    implements Initializable, EmployeeAccessible, IRequest {

  @FXML private JFXComboBox<String> staffAssigned;
  @FXML private JFXTextArea issueDescription;
  @FXML public static Popup popup;

  @FXML
  public void popUpAction() throws IOException {
    popup = new Popup();
    //    // ServicePageController.popup.hide();
    //    super.popUpAction("Emergency.fxml");
    ControllerManager.attemptLoadPopupBlur("Emergency.fxml");
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
                .or(issueDescription.textProperty().isEmpty()));

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

      //      // TODO: CHANGE THIS TABLE INFO TYPE
      GlobalDb.getTables()
          .getCompRequestTable()
          .addEntity(
              GlobalDb.getConnection(),
              super.firstName.getText(),
              super.lastName.getText(),
              super.contactInfo.getText(),
              super.locationBox.getValue(),
              staffAssigned.getValue(),
              issueDescription.getText());
    }
  }

  //  private void getInfo()

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    initDialogFactory();
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

    ArrayList<String> longNameList =
        FDatabaseTables.getNodeTable().fetchLongName(GlobalDb.getConnection());
    longNameList.sort(String::compareToIgnoreCase);
    locationBox.getItems().addAll(longNameList);

    ObservableList<String> employeeList =
        FDatabaseTables.getUserTable().fetchEmployee(GlobalDb.getConnection());
    employeeList.sort(String::compareToIgnoreCase);
    staffAssigned.getItems().addAll(employeeList);
  }
}
