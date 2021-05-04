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

public class FacilitiesMaintenanceController extends AbsRequest
    implements Initializable, EmployeeAccessible, IRequest {

  @FXML private JFXComboBox<String> urgencyLevel;
  @FXML private JFXComboBox<String> staffAssigned;
  @FXML private JFXTextArea issueDescription;
  @FXML public static Popup popup;

  @FXML
  public void popUpAction() throws IOException {
    popup = new Popup();
    // ServicePageController.popup.hide();
    super.popUpAction("Emergency.fxml");
  }

  @FXML
  public void loadDialog(MouseEvent Event) {
    dialogFactory.createOneButtonDialogWhite(
            "More Information",
            "All fields must be filled out before submitting"
                    + "\n"
                    + "the form. For further assistance, contact IT at"
                    + "\n"
                    + "123-456- 7890. Questions can also be sent to"
                    + "\n"
                    + "diamonddragonsIT@gmail.com. ",
            "Close",
            () -> {});
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
                .or(urgencyLevel.valueProperty().isNull())
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

      //      // TODO - create facilities maintenance table
      //
      GlobalDb.getTables()
          .getFacilitiesTable()
          .addEntity(
              GlobalDb.getConnection(),
              super.firstName.getText(),
              super.lastName.getText(),
              super.contactInfo.getText(),
              super.locationBox.getValue(),
              staffAssigned.getValue(),
              urgencyLevel.getValue(),
              issueDescription.getText());
    }
  }

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

    urgencyLevel.getItems().addAll("Low Priority", "Medium Priority", "High Priority");

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
