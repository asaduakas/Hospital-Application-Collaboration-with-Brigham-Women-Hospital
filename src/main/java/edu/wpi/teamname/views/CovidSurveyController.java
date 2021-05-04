package edu.wpi.teamname.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.base.IFXLabelFloatControl;
import com.jfoenix.skins.JFXTextFieldSkin;
import com.jfoenix.skins.ValidationPane;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.Access.AllAccessible;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CovidSurveyController implements Initializable, AllAccessible {

  @FXML private JFXButton submitButton;
  @FXML private JFXButton cancelButton;
  @FXML private ImageView helpImage;
  @FXML private StackPane stackPane1;
  @FXML private JFXTextField firstName;
  @FXML private JFXTextField lastName;
  @FXML private JFXTextField phoneNumber;
  @FXML private JFXCheckBox positiveCheck;
  @FXML private JFXCheckBox symptomCheck;
  @FXML private JFXCheckBox closeContactCheck;
  @FXML private JFXCheckBox isolateCheck;
  @FXML private JFXCheckBox goodCheck;
  @FXML private JFXTextField emailAddress;

  int posBool = 0;
  int sympBool = 0;
  int conBool = 0;
  int isoBool = 0;
  int goodBool = 0;

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
    closeButton.setOnAction(e -> dialog.close());
    content.setActions(closeButton);
    dialog.show();
  }

  @FXML
  void popupWarning(ActionEvent event) throws IOException {
    Text header = new Text("Error");
    header.setFont(Font.font("System", FontWeight.BOLD, 18));

    JFXDialogLayout warningLayout = new JFXDialogLayout();
    warningLayout.setHeading(header);
    warningLayout.setBody(new Text("Please fill out the required fields."));
    JFXDialog warningDia =
        new JFXDialog(stackPane1, warningLayout, JFXDialog.DialogTransition.CENTER);
    JFXButton okBtn = new JFXButton("Close");
    okBtn.setStyle("-fx-background-color: #cdcdcd;");
    okBtn.setOnAction(e -> warningDia.close());
    warningLayout.setActions(okBtn);
    warningDia.show();
  }

  @FXML
  void popupWarningCovid(ActionEvent event) throws IOException {
    Text header = new Text("ATTENTION:");
    header.setFont(Font.font("System", FontWeight.BOLD, 18));

    JFXDialogLayout warningLayout = new JFXDialogLayout();
    warningLayout.setHeading(header);
    warningLayout.setBody(
        new Text(
            "You are not permitted in the hospital, please" + "\n" + "self-isolate for 14 days."));
    JFXDialog warningDia =
        new JFXDialog(stackPane1, warningLayout, JFXDialog.DialogTransition.CENTER);
    JFXButton okBtn = new JFXButton("OK");
    okBtn.setStyle("-fx-background-color: #cdcdcd;");
    okBtn.setOnAction(e -> ControllerManager.exitPopup());
    okBtn.setStyle("-fx-background-color: #cdcdcd;");

    warningLayout.setActions(okBtn);

    warningDia.show();
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
                .or(emailAddress.textProperty().isEmpty()));

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
        Text submitText =
            new Text(
                "Your COVID-19 survey is submitted." + "\n" + "Press OK to return to home screen.");
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Text("Submitted!"));
        layout.setBody(submitText);
        JFXDialog submitDia = new JFXDialog(stackPane1, layout, JFXDialog.DialogTransition.CENTER);
        JFXButton noBtn = new JFXButton("OK");
        noBtn.setPrefHeight(20);
        noBtn.setPrefWidth(60);
        noBtn.setId("noBtn");
        noBtn.setButtonType(JFXButton.ButtonType.FLAT);
        noBtn.setOnAction(e -> ControllerManager.exitPopup());
        noBtn.setStyle("-fx-background-color: #cdcdcd;");

        layout.setActions(noBtn);

        submitDia.show();
      }
    }

    FDatabaseTables.getCovid19SurveyTable()
        .addEntity(
            GlobalDb.getConnection(),
            firstName.getText(),
            lastName.getText(),
            phoneNumber.getText(),
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
      Text submitText =
          new Text(
              "Your COVID-19 survey is submitted." + "\n" + "Press OK to return to home screen.");
      JFXDialogLayout layout = new JFXDialogLayout();
      layout.setHeading(new Text("Submitted!"));
      layout.setBody(submitText);
      JFXDialog submitDia = new JFXDialog(stackPane1, layout, JFXDialog.DialogTransition.CENTER);
      JFXButton noBtn = new JFXButton("OK");
      noBtn.setPrefHeight(20);
      noBtn.setPrefWidth(60);
      noBtn.setId("noBtn");
      noBtn.setButtonType(JFXButton.ButtonType.FLAT);
      noBtn.setOnAction(e -> ControllerManager.exitPopup());
      noBtn.setStyle("-fx-background-color: #cdcdcd;");

      layout.setActions(noBtn);

      submitDia.show();
    }
    submitPage(event);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    stackPane1.setPickOnBounds(false);
    validationPaneFormatter(firstName);
    validationPaneFormatter(lastName);
    validationPaneFormatter(phoneNumber);
    validationPaneFormatter(emailAddress);

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
    phoneNumValid.setMessage("Enter phone number");
    phoneNumber.getValidators().add(phoneNumValid);
    phoneNumber
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) phoneNumber.validate();
            });

    RequiredFieldValidator emailValid = new RequiredFieldValidator();
    emailValid.setMessage("Enter phone number");
    emailAddress.getValidators().add(emailValid);
    emailAddress
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) emailAddress.validate();
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
    Text header = new Text("Exit form?");
    header.setFont(Font.font("System", FontWeight.BOLD, 18));

    JFXDialogLayout layout = new JFXDialogLayout();
    layout.setHeading(header);
    layout.setBody(new Text("This will bring you back to the home" + "\n" + "page."));
    JFXDialog submitDia = new JFXDialog(stackPane1, layout, JFXDialog.DialogTransition.CENTER);

    JFXButton yesBtn = new JFXButton("Yes");
    yesBtn.setPrefHeight(20);
    yesBtn.setPrefWidth(60);
    yesBtn.setId("yesBtn");
    yesBtn.setButtonType(JFXButton.ButtonType.FLAT);
    yesBtn.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            submitDia.close();
            ControllerManager.exitPopup();
          }
        });
    yesBtn.setStyle("-fx-background-color: #cdcdcd;");

    JFXButton noBtn = new JFXButton("No");
    noBtn.setPrefHeight(20);
    noBtn.setPrefWidth(60);
    noBtn.setId("noBtn");
    noBtn.setButtonType(JFXButton.ButtonType.FLAT);
    noBtn.setOnAction(e -> submitDia.close());
    noBtn.setStyle("-fx-background-color: #cdcdcd;");

    layout.setActions(yesBtn, noBtn);

    submitDia.show();
  }
}
