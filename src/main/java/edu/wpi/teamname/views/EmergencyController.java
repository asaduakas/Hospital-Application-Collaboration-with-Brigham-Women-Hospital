package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.base.IFXLabelFloatControl;
import com.jfoenix.skins.JFXTextFieldSkin;
import com.jfoenix.skins.ValidationPane;
import edu.wpi.teamname.App;
import edu.wpi.teamname.views.ServiceRequests.SRControllers.*;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class EmergencyController {
  @FXML JFXButton yesBtn;
  @FXML JFXButton noBtn;
  @FXML StackPane stackPane;

  @FXML
  public void initialize() {
    stackPane.setPickOnBounds(false);
  }

  @FXML
  public void popupYes(ActionEvent event) throws IOException {
    popupWarning(event, "HELP IS ON YOUR WAY!");
  }

  @FXML
  public void close() {
    closePopup();
  }

  public void closePopup() {
    if (InitPageController.popup != null) {
      InitPageController.popup.hide();
    }
    if (SecurityServicesController.popup != null) {
      HomeController.mainButtons.setVisible(true);
      SecurityServicesController.popup.hide();
    }
    if (AVRequestController.popup != null) {
      HomeController.mainButtons.setVisible(true);
      AVRequestController.popup.hide();
    }
    if (ComputerServiceController.popup != null) {
      HomeController.mainButtons.setVisible(true);
      ComputerServiceController.popup.hide();
    }
    if (ExternalTransportationController.popup != null) {
      HomeController.mainButtons.setVisible(true);
      ExternalTransportationController.popup.hide();
    }
    if (FacilitiesMaintenanceController.popup != null) {
      HomeController.mainButtons.setVisible(true);
      FacilitiesMaintenanceController.popup.hide();
    }
    if (FloralDeliveryController.popup != null) {
      HomeController.mainButtons.setVisible(true);
      FloralDeliveryController.popup.hide();
    }
    if (FoodDeliveryController.popup != null) {
      HomeController.mainButtons.setVisible(true);
      FoodDeliveryController.popup.hide();
    }
    if (InternalTransportationController.popup != null) {
      HomeController.mainButtons.setVisible(true);
      InternalTransportationController.popup.hide();
    }
    if (LanguageInterpreterController.popup != null) {
      HomeController.mainButtons.setVisible(true);
      LanguageInterpreterController.popup.hide();
    }
    if (LaundryController.popup != null) {
      HomeController.mainButtons.setVisible(true);
      LaundryController.popup.hide();
    }
    if (MedicineDeliveryController.popup != null) {
      HomeController.mainButtons.setVisible(true);
      MedicineDeliveryController.popup.hide();
    }
    if (SanitationServiceController.popup != null) {
      HomeController.mainButtons.setVisible(true);
      SanitationServiceController.popup.hide();
    }
    if (HomeController.popup != null) {
      HomeController.popup.hide();
    }
    App.getPrimaryStage().getScene().getRoot().setEffect(null);
  }

  void popupWarning(ActionEvent event, String text) throws IOException {

    JFXDialogLayout warningLayout = new JFXDialogLayout();
    warningLayout.setBody(new Text(text));
    JFXDialog warningDia =
        new JFXDialog(stackPane, warningLayout, JFXDialog.DialogTransition.CENTER);
    JFXButton okBtn = new JFXButton("Close");
    okBtn.setStyle("-fx-background-color: #cdcdcd;");
    okBtn.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            closePopup();
            warningDia.close();
          }
        });
    warningLayout.setActions(okBtn);
    warningDia.show();
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
}
