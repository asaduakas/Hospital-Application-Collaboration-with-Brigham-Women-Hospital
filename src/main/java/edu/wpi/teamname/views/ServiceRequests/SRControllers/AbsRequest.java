package edu.wpi.teamname.views.ServiceRequests.SRControllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.base.IFXLabelFloatControl;
import com.jfoenix.skins.JFXTextFieldSkin;
import com.jfoenix.skins.ValidationPane;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.Access.AllAccessible;
import edu.wpi.teamname.views.Access.LoginController;
import edu.wpi.teamname.views.AutoCompleteComboBox;
import edu.wpi.teamname.views.ControllerManager;
import edu.wpi.teamname.views.DialogFactory;
import edu.wpi.teamname.views.InitPageController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public abstract class AbsRequest implements AllAccessible {

  @FXML StackPane stackPane1;
  @FXML JFXButton submitButton;
  @FXML JFXTextField firstName;
  @FXML JFXTextField lastName;
  @FXML JFXTextField contactInfo;
  @FXML JFXComboBox<String> locationBox;
  @FXML JFXButton helpButton;
  private static String userCategory;
  public static Boolean disableRequestStatus = false;

  protected DialogFactory dialogFactory;

  public void initDialogFactory() {
    dialogFactory = new DialogFactory(stackPane1);
  }

  @FXML
  public void initialize() {
    initDialogFactory();
  }

  public void goHome() {
    List<Node> childrenList = App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
    VBox buttonBox = (VBox) childrenList.get(2);
    buttonBox.setVisible(true);
    ControllerManager.exitPopup();
  }

  public void popUpAction(String fxml) throws IOException {

    if (LoginController.getUserCategory() != null) {
      this.userCategory = LoginController.getUserCategory();
    } else {
      this.userCategory = InitPageController.getUserCategory();
    }

    if (userCategory.equalsIgnoreCase("Guest") || userCategory.equalsIgnoreCase("Patient")) {
      // hide and disable the check request button
      this.disableRequestStatus = true;
    } else if (userCategory.equalsIgnoreCase("Admin")
        || userCategory.equalsIgnoreCase("Employee")) {
      this.disableRequestStatus = false;
    }

    /*
    if (popup.getContent().size() > 0) {
      popup.getContent().remove(popup.getContent().size() - 1);
    }
    */

    ControllerManager.attemptLoadPopupBlur(
        fxml,
        fxmlLoader -> {
          Pane root = (Pane) fxmlLoader.getRoot();
          JFXButton checkStatusButton = (JFXButton) root.getChildren().get(2);
          if (disableRequestStatus) {
            System.out.println("Hello 3");
            checkStatusButton.setVisible(false);
            checkStatusButton.setDisable(true);
          } else {
            System.out.println("Hello 4 ");
            checkStatusButton.setVisible(true);
            checkStatusButton.setDisable(false);
          }
        });
  }

  @FXML
  void popupWarning(ActionEvent event) throws IOException {
    dialogFactory.createOneButtonDialog("Error", "Please fill out the required fields.",
            "Close", () -> {});
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
    dialogFactory.createTwoButtonDialog("Exit form?",
            "This will bring you back to the service" + "\n" + "requests page.",
            "Yes", () -> {
              List<Node> childrenList =
                      App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
              VBox buttonBox = (VBox) childrenList.get(2);
              buttonBox.setVisible(false);
              // ServicePageController.popup.hide();
              try {
                popUpAction("ServicePageView.fxml");
              } catch (IOException e) {
                e.printStackTrace();
              }
            },
            "No", () -> {});
  }

  public void type(ActionEvent actionEvent) {
    AutoCompleteComboBox locationAutoComplete = new AutoCompleteComboBox(locationBox);
    ArrayList<String> longNameList =
        FDatabaseTables.getNodeTable().fetchLongName(GlobalDb.getConnection());
    locationAutoComplete.getEntries().addAll(longNameList);
  }
}
