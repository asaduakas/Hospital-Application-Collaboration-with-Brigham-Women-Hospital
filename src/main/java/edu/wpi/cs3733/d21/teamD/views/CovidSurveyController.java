package edu.wpi.cs3733.d21.teamD.views;

import static edu.wpi.cs3733.d21.teamD.views.Email.sendPassEmail;

import com.jfoenix.controls.*;
import com.jfoenix.controls.base.IFXLabelFloatControl;
import com.jfoenix.skins.JFXTextFieldSkin;
import com.jfoenix.skins.ValidationPane;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.d21.teamD.Ddb.FDatabaseTables;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class CovidSurveyController implements Initializable, AllAccessible {

  @FXML private JFXButton submitButton;
  @FXML private JFXButton cancelButton;
  @FXML private ImageView helpImage;
  @FXML private StackPane stackPane1;
  @FXML private JFXTextField firstName;
  @FXML private JFXTextField lastName;
  @FXML private JFXTextField phoneNumber;
  @FXML private JFXTextField email;
  @FXML private JFXCheckBox positiveCheck;
  @FXML private JFXCheckBox symptomCheck;
  @FXML private JFXCheckBox closeContactCheck;
  @FXML private JFXCheckBox isolateCheck;
  @FXML private JFXCheckBox goodCheck;

  private DialogFactory dialogFactory;

  String posBool = "No";
  String sympBool = "No";
  String conBool = "No";
  String isoBool = "No";
  String goodBool = "No";

  private String clearance1 = "normalEntrance";
  private String clearance2 = "emergencyEntrance";

  @FXML
  public void loadDialog(MouseEvent Event) {
    dialogFactory.createOneButtonDialogWhite(
        "More Information",
        "For assistance, contact IT at 123-456-"
            + "\n"
            + "7890. Further questions can be sent to"
            + "\n"
            + "diamonddragonsIT@gmail.com. ",
        "Close",
        () -> {});
  }

  @FXML
  void popupWarning(ActionEvent event) throws IOException {
    dialogFactory.createOneButtonDialog(
        "Error", "Please fill out the required fields.", "Close", () -> {});
  }

  @FXML
  void popupWarningCovid(ActionEvent event) throws IOException {
    dialogFactory.createOneButtonDialog(
        "ATTENTION:",
        "You are not permitted in the hospital, please" + "\n" + "self-isolate for 14 days.",
        "OK",
        ControllerManager::exitPopup);
  }

  @FXML
  private void submitPage(ActionEvent event) throws IOException {

    submitButton
        .disableProperty()
        .bind(
            firstName
                .textProperty()
                .isEmpty()
                .or(lastName.textProperty().isEmpty())
                .or(phoneNumber.textProperty().isEmpty())
                .or(email.textProperty().isEmpty())
                .or(
                    (goodCheck.selectedProperty().not())
                        .and(positiveCheck.selectedProperty().not())
                        .and(symptomCheck.selectedProperty().not())
                        .and(closeContactCheck.selectedProperty().not())
                        .and(isolateCheck.selectedProperty().not())));

    if (submitButton.isDisabled()) {
      popupWarning(event);
    }
    if (!submitButton.isDisabled()) {
      if (positiveCheck.isSelected()
          || symptomCheck.isSelected()
          || closeContactCheck.isSelected()
          || isolateCheck.isSelected()) {
        posBool = positiveCheck.isSelected() ? "Yes" : "No";
        sympBool = symptomCheck.isSelected() ? "Yes" : "No";
        conBool = closeContactCheck.isSelected() ? "Yes" : "No";
        isoBool = isolateCheck.isSelected() ? "Yes" : "No";
        FDatabaseTables.getUserTable()
            .changeClearance(GlobalDb.getConnection(), clearance2, HomeController.username);
        FDatabaseTables.getUserTable().dispUsers(GlobalDb.getConnection());
        dialogFactory.createOneButtonDialog(
            "Attention!",
            "You could only proceed to emergency entrance."
                + "\n"
                + "You will be checked by a nurse.",
            "OK",
            ControllerManager::exitPopup);
      } else if (goodCheck.isSelected()) {
        goodBool = goodCheck.isSelected() ? "Yes" : "No";
        FDatabaseTables.getUserTable()
            .changeClearance(GlobalDb.getConnection(), clearance1, HomeController.username);
        dialogFactory.createOneButtonDialog(
            "Submitted!",
            "You may proceed to 75 Lobby entrance." + "\n" + "You will then be checked by a nurse.",
            "OK",
            ControllerManager::exitPopup);
      }
    }

    // Sending email
    Boolean pass = false;
    if (goodBool.equalsIgnoreCase("yes")) {
      pass = true;
    }
    String emailString = email.getText();
    sendPassEmail(emailString, pass);

    FDatabaseTables.getCovid19SurveyTable()
        .addEntity(
            GlobalDb.getConnection(),
            "COVID-19 Survey",
            firstName.getText(),
            lastName.getText(),
            phoneNumber.getText(),
            email.getText(),
            posBool,
            sympBool,
            conBool,
            isoBool,
            goodBool);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    try {
      handleOptions();
    } catch (IOException exception) {
      exception.printStackTrace();
    }

    dialogFactory = new DialogFactory(stackPane1);

    stackPane1.setPickOnBounds(false);
    validationPaneFormatter(firstName);
    validationPaneFormatter(lastName);
    validationPaneFormatter(phoneNumber);
    validationPaneFormatter(email);

    RequiredFieldValidator firstNameValid = new RequiredFieldValidator();
    firstNameValid.setMessage("Enter patient's first name");
    firstName.getValidators().add(firstNameValid);
    firstName
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) firstName.validate();
            });

    RequiredFieldValidator lastNameValid = new RequiredFieldValidator();
    lastNameValid.setMessage("Enter patient's last name");
    lastName.getValidators().add(lastNameValid);
    lastName
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) lastName.validate();
            });

    RequiredFieldValidator phoneNumValid = new RequiredFieldValidator();
    phoneNumber
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) phoneNumber.validate();
              phoneNumValid.setMessage("Enter phone number");
            });
    phoneNumber.getValidators().add(phoneNumValid);
    phoneNumber
        .textProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                  phoneNumber.setText(newValue.replaceAll("[^\\d]", ""));
                  phoneNumValid.setMessage("Please enter number");
                }
              }
            });

    RequiredFieldValidator emailValid = new RequiredFieldValidator();
    emailValid.setMessage("Enter an email address");
    email.getValidators().add(emailValid);
    email
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) email.validate();
            });

    RegexValidator emailValidator = new RegexValidator();
    emailValidator.setRegexPattern(
        "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    email.setValidators(emailValidator);
    email.getValidators().get(1).setMessage("Email is not valid");
    email
        .focusedProperty()
        .addListener(
            new ChangeListener<Boolean>() {
              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observable,
                  Boolean oldValue,
                  Boolean newValue) {
                if (!newValue) {
                  boolean val = email.validate();
                }
              }
            });
  }

  <T extends JFXTextField & IFXLabelFloatControl> void validationPaneFormatter(T jfxTextField) {
    jfxTextField
        .skinProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              JFXTextFieldSkin textFieldSkin = ((JFXTextFieldSkin) newValue);
              ObservableList childs = textFieldSkin.getChildren();
              // Get validation pane.
              // It's always the last child. Be careful no get per type checking -> index can change
              // -> code will fail.
              ValidationPane validationPane = (ValidationPane) childs.get(childs.size() - 1);
              validationPane.setTranslateY(-24);

              // Set validation label to the right.
              // Again node is always first child but code can fail in future.
              StackPane labelStackPane = (StackPane) validationPane.getChildren().get(0);
              labelStackPane.setAlignment(Pos.TOP_RIGHT);
            });
    // Validate also directly on typing or better text change for not override the error label.
    jfxTextField
        .textProperty()
        .addListener((observable, oldValue, newValue) -> jfxTextField.validate());
  }

  @FXML
  void cancelPage(ActionEvent event) throws IOException {
    dialogFactory.createTwoButtonDialog(
        "Exit form?",
        "This will bring you back to the home" + "\n" + "page.",
        "Yes",
        ControllerManager::exitPopup,
        "No",
        () -> {});
  }

  @FXML
  private void handleOptions() throws IOException {
    goodCheck
        .selectedProperty()
        .addListener(
            new ChangeListener<Boolean>() {
              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observable,
                  Boolean oldValue,
                  Boolean newValue) {
                if (newValue) {
                  positiveCheck.setDisable(true);
                  symptomCheck.setDisable(true);
                  closeContactCheck.setDisable(true);
                  isolateCheck.setDisable(true);
                } else {
                  positiveCheck.setDisable(false);
                  symptomCheck.setDisable(false);
                  closeContactCheck.setDisable(false);
                  isolateCheck.setDisable(false);
                }
              }
            });

    positiveCheck
        .selectedProperty()
        .or(symptomCheck.selectedProperty())
        .or(closeContactCheck.selectedProperty())
        .or(isolateCheck.selectedProperty())
        .addListener(
            new ChangeListener<Boolean>() {
              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observable,
                  Boolean oldValue,
                  Boolean newValue) {
                if (newValue) {
                  goodCheck.setDisable(true);
                } else {
                  goodCheck.setDisable(false);
                }
              }
            });
  }
}
