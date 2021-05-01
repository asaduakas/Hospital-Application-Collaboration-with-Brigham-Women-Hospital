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
import edu.wpi.teamname.views.HomeController;
import edu.wpi.teamname.views.InitPageController;
import edu.wpi.teamname.views.ServiceRequests.ServicePageController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;

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

  public void goHome() {
    List<Node> childrenList = App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
    VBox buttonBox = (VBox) childrenList.get(2);
    buttonBox.setVisible(true);
    ServicePageController.popup.hide();
    HomeController.popup.hide();
    App.getPrimaryStage().getScene().getRoot().setEffect(null);
  }

  public void popUpAction(String fxml, Popup popup, boolean isCheckRequestDisabled)
      throws IOException {

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

    GaussianBlur blur = new GaussianBlur(25);
    App.getPrimaryStage().getScene().getRoot().setEffect(blur);

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxml));
    Pane root = (Pane) fxmlLoader.load();

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

    if (popup.getContent().size() > 0) {
      popup.getContent().remove(popup.getContent().size() - 1);
    }
    popup.getContent().add(root);
    popup.show(App.getPrimaryStage());
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
    okBtn.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            ServicePageController.popup.hide();
            try {
              popUpAction("ServicePageView.fxml", HomeController.popup, disableRequestStatus);
            } catch (IOException e) {
              e.printStackTrace();
            }
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

  @FXML
  void cancelPage(ActionEvent event) throws IOException {
    Text header = new Text("Exit form?");
    header.setFont(Font.font("System", FontWeight.BOLD, 18));

    JFXDialogLayout layout = new JFXDialogLayout();
    layout.setHeading(header);
    layout.setBody(new Text("This will bring you back to the service" + "\n" + "requests page."));
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
            List<Node> childrenList =
                App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
            VBox buttonBox = (VBox) childrenList.get(2);
            buttonBox.setVisible(false);
            ServicePageController.popup.hide();
            try {
              popUpAction("ServicePageView.fxml", HomeController.popup, disableRequestStatus);
            } catch (IOException e) {
              e.printStackTrace();
            }
            submitDia.close();
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

  public void type(ActionEvent actionEvent) {
    AutoCompleteComboBox locationAutoComplete = new AutoCompleteComboBox(locationBox);
    ArrayList<String> longNameList =
        FDatabaseTables.getNodeTable().fetchLongName(GlobalDb.getConnection());
    locationAutoComplete.getEntries().addAll(longNameList);
  }
}
