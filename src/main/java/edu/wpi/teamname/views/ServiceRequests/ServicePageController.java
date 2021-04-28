package edu.wpi.teamname.views.ServiceRequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.teamname.App;
import edu.wpi.teamname.views.Access.EmployeeAccessible;
import edu.wpi.teamname.views.Access.LoginController;
import edu.wpi.teamname.views.Access.UserCategory;
import edu.wpi.teamname.views.HomeController;
import edu.wpi.teamname.views.InitPageController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class ServicePageController implements EmployeeAccessible, Initializable {

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

  private static UserCategory userTypeEnum;
  private static String userCategory;
  public static Boolean disableStaffAssigned = false;

  @FXML
  private void goHome(ActionEvent event) throws IOException {
    List<Node> childrenList = App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
    VBox buttonBox = (VBox) childrenList.get(2);
    buttonBox.setVisible(true);
    HomeController.popup.hide();
    if (ServicePageController.popup != null) {
      ServicePageController.popup.hide();
    }
    App.getPrimaryStage().getScene().getRoot().setEffect(null);
  }

  @FXML
  void changeView(ActionEvent event) throws IOException {

    if (LoginController.getUserCategory() != null) {
      this.userCategory = LoginController.getUserCategory();
    } else {
      this.userCategory = InitPageController.getUserCategory();
    }

    if (userCategory.equalsIgnoreCase("Guest") || userCategory.equalsIgnoreCase("patient")) {
      this.disableStaffAssigned = true;
    } else if (userCategory.equalsIgnoreCase("admin")) {
      this.disableStaffAssigned = false;
    }

    if (event.getSource() == ExTransBtn) {
      HomeController.popup.hide();
      popUpAction("ExternalTransportationView.fxml", disableStaffAssigned);
    }
    if (event.getSource() == inTransBtn) {
      HomeController.popup.hide();
      popUpAction("InternalTransportationView.fxml", disableStaffAssigned);
    }
    if (event.getSource() == csBtn) {
      System.out.println("in cs btn if");
      HomeController.popup.hide();
      popUpAction("ComputerServiceView.fxml", disableStaffAssigned);
    }
    if (event.getSource() == laundryBtn) {
      HomeController.popup.hide();
      popUpAction("LaundryView.fxml", disableStaffAssigned);
    }
    if (event.getSource() == foodBtn) {
      HomeController.popup.hide();
      popUpAction("FoodDeliveryView.fxml", disableStaffAssigned);
    }
    if (event.getSource() == floralBtn) {
      HomeController.popup.hide();
      popUpAction("FloralDeliveryView.fxml", disableStaffAssigned);
    }
    if (event.getSource() == securityBtn) {
      HomeController.popup.hide();
      popUpAction("SecurityServicesView.fxml", disableStaffAssigned);
    }
    if (event.getSource() == maintenceBtn) {
      HomeController.popup.hide();
      popUpAction("FacilitiesMaintenanceView.fxml", disableStaffAssigned);
    }

    if (event.getSource() == languageBtn) {
      HomeController.popup.hide();
      popUpAction("LanguageInterpreterView.fxml", disableStaffAssigned);
    }
    if (event.getSource() == sanitizBtn) {
      HomeController.popup.hide();
      popUpAction("SanitationView.fxml", disableStaffAssigned);
    }
    if (event.getSource() == mediBtn) {
      HomeController.popup.hide();
      popUpAction("MedicineView.fxml", disableStaffAssigned);
    }
    if (event.getSource() == audioBtn) {
      HomeController.popup.hide();
      popUpAction("AVRequestView.fxml", disableStaffAssigned);
    }
  }

  @FXML
  public void statusView(ActionEvent event) throws IOException {
    if (event.getSource() == statusBtn) {
      HomeController.popup.hide();
      GaussianBlur blur = new GaussianBlur(25);
      App.getPrimaryStage().getScene().getRoot().setEffect(blur);

      FXMLLoader fxmlLoader =
          new FXMLLoader(getClass().getClassLoader().getResource("StatusView.fxml"));
      Pane root = (Pane) fxmlLoader.load();

      root.setStyle("-fx-background-color: White");

      this.popup = new Popup();
      popup.getContent().addAll(root);
      popup.show(App.getPrimaryStage());
    }
  }

  public void popUpAction(String fxml, boolean showAssignedTo) throws IOException {

    GaussianBlur blur = new GaussianBlur(25);
    App.getPrimaryStage().getScene().getRoot().setEffect(blur);

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxml));
    Pane root = (Pane) fxmlLoader.load();
    JFXComboBox staff = (JFXComboBox) root.getChildren().get(5);

    if (showAssignedTo) {
      staff.setDisable(true);
      staff.setVisible(false);
    } else {
      staff.setDisable(false);
      staff.setVisible(true);
    }

    root.setStyle("-fx-background-color: White");

    this.popup = new Popup();
    popup.getContent().addAll(root);
    popup.show(App.getPrimaryStage());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {}
}
