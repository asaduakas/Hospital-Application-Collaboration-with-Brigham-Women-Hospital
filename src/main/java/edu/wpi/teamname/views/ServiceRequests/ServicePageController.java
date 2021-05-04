package edu.wpi.teamname.views.ServiceRequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.teamname.App;
import edu.wpi.teamname.views.Access.AllAccessible;
import edu.wpi.teamname.views.Access.LoginController;
import edu.wpi.teamname.views.ControllerManager;
import edu.wpi.teamname.views.InitPageController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class ServicePageController implements AllAccessible, Initializable {

  @FXML private JFXButton ExTransBtn;
  @FXML private JFXButton inTransBtn;
  @FXML private JFXButton csBtn;
  @FXML private JFXButton laundryBtn;
  @FXML private JFXButton foodBtn;
  @FXML private JFXButton floralBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton statusBtn;
  @FXML private JFXButton securityBtn;
  @FXML private JFXButton maintenceBtn;
  @FXML private JFXButton languageBtn;
  @FXML private JFXButton audioBtn;
  @FXML private JFXButton sanitizBtn;
  @FXML private JFXButton mediBtn;

  @FXML public static Popup popup;

  private static String userCategory;

  @FXML
  private void goHome(ActionEvent event) throws IOException {
    List<Node> childrenList = App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
    VBox buttonBox = (VBox) childrenList.get(2);
    buttonBox.setVisible(true);
    ControllerManager.exitPopup();
  }

  @FXML
  void changeView(ActionEvent event) throws IOException {
    if (LoginController.getUserCategory() != null) {
      this.userCategory = LoginController.getUserCategory();
    } else {
      this.userCategory = InitPageController.getUserCategory();
    }

    Consumer<FXMLLoader> configureStaffButton =
        fxmlLoader -> {
          Pane root = (Pane) fxmlLoader.getRoot();
          JFXComboBox staff = (JFXComboBox) root.getChildren().get(5);

          if (userCategory.equalsIgnoreCase("admin")) {
            staff.setDisable(false);
            staff.setVisible(true);
          } else {
            staff.setDisable(true);
            staff.setVisible(false);
          }
          root.setStyle("-fx-background-color: White");
        };

    if (event.getSource() == ExTransBtn) {
      ControllerManager.attemptLoadPopupBlur(
          "ExternalTransportationView.fxml", configureStaffButton);
    }
    if (event.getSource() == inTransBtn) {
      ControllerManager.attemptLoadPopupBlur(
          "InternalTransportationView.fxml", configureStaffButton);
    }
    if (event.getSource() == csBtn) {
      ControllerManager.attemptLoadPopupBlur("ComputerServiceView.fxml", configureStaffButton);
    }
    if (event.getSource() == laundryBtn) {
      ControllerManager.attemptLoadPopupBlur("LaundryView.fxml", configureStaffButton);
    }
    if (event.getSource() == foodBtn) {
      ControllerManager.attemptLoadPopupBlur("FoodDeliveryView.fxml", configureStaffButton);
    }
    if (event.getSource() == floralBtn) {
      ControllerManager.attemptLoadPopupBlur("FloralDeliveryView.fxml", configureStaffButton);
    }
    if (event.getSource() == securityBtn) {
      ControllerManager.attemptLoadPopupBlur("SecurityServicesView.fxml", configureStaffButton);
    }
    if (event.getSource() == maintenceBtn) {
      ControllerManager.attemptLoadPopupBlur(
          "FacilitiesMaintenanceView.fxml", configureStaffButton);
    }
    if (event.getSource() == languageBtn) {
      ControllerManager.attemptLoadPopupBlur("LanguageInterpreterView.fxml", configureStaffButton);
    }
    if (event.getSource() == sanitizBtn) {
      ControllerManager.attemptLoadPopupBlur("SanitationView.fxml", configureStaffButton);
    }
    if (event.getSource() == mediBtn) {
      ControllerManager.attemptLoadPopupBlur("MedicineView.fxml", configureStaffButton);
    }
    if (event.getSource() == audioBtn) {
      ControllerManager.attemptLoadPopupBlur("AVRequestView.fxml", configureStaffButton);
    }
  }

  @FXML
  public void statusView(ActionEvent event) {
    ControllerManager.attemptLoadPopupBlur(
        "StatusView.fxml",
        fxmlLoader -> ((Pane) fxmlLoader.getRoot()).setStyle("-fx-background-color: White"));
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {}
}
