package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.base.IFXLabelFloatControl;
import com.jfoenix.skins.JFXTextFieldSkin;
import com.jfoenix.skins.ValidationPane;
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

  int posBool = 0;
  int sympBool = 0;
  int conBool = 0;
  int isoBool = 0;
  int goodBool = 0;

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
                .or(email.textProperty().isEmpty()));

    if (submitButton.isDisabled()) {
      popupWarning(event);
    }
    if (!submitButton.isDisabled()) {
      if (positiveCheck.isSelected()
          || symptomCheck.isSelected()
          || closeContactCheck.isSelected()
          || isolateCheck.isSelected()) {
        if (positiveCheck.isSelected()) {
          posBool = 1;
        }
        if (symptomCheck.isSelected()) {
          sympBool = 1;
        }
        if (closeContactCheck.isSelected()) {
          conBool = 1;
        }
        if (isolateCheck.isSelected()) {
          isoBool = 1;
        }
        popupWarningCovid(event);
      } else {
        goodBool = 1;
        dialogFactory.createOneButtonDialog(
            "Submitted!",
            "Your COVID-19 survey is submitted." + "\n" + "Press OK to return to home screen.",
            "OK",
            ControllerManager::exitPopup);
      }
    }

    FDatabaseTables.getCovid19SurveyTable()
        .addEntity(
            GlobalDb.getConnection(),
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

  @FXML
  private void handleOptions(ActionEvent event) throws IOException {
    if (positiveCheck.isSelected()
        || symptomCheck.isSelected()
        || closeContactCheck.isSelected()
        || isolateCheck.isSelected()) {
      if (positiveCheck.isSelected()) {
        posBool = 1;
      }
      if (symptomCheck.isSelected()) {
        sympBool = 1;
      }
      if (closeContactCheck.isSelected()) {
        conBool = 1;
      }
      if (isolateCheck.isSelected()) {
        isoBool = 1;
      }
      popupWarningCovid(event);
    } else {
      goodBool = 1;
      dialogFactory.createOneButtonDialog(
          "Submitted!",
          "Your COVID-19 survey is submitted." + "\n" + "Press OK to return to home screen.",
          "OK",
          ControllerManager::exitPopup);
    }
    submitPage(event);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
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
    emailValid.setMessage("Enter email");
    email.getValidators().add(emailValid);
    email
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) email.validate();
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
}
