package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.base.IFXLabelFloatControl;
import com.jfoenix.skins.JFXTextFieldSkin;
import com.jfoenix.skins.ValidationPane;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public class EmergencyController implements AllAccessible {
  @FXML JFXButton yesBtn;
  @FXML JFXButton noBtn;
  @FXML StackPane stackPane;

  private DialogFactory dialogFactory;

  @FXML
  public void initialize() {
    stackPane.setPickOnBounds(false);
    dialogFactory = new DialogFactory(stackPane);
  }

  @FXML
  public void popupYes() throws IOException {
    dialogFactory.createOneButtonDialog("", "HELP IS ON YOUR WAY!", "OK", this::closePopup);
  }

  @FXML
  public void close() {
    closePopup();
  }

  public void closePopup() {
    if (HomeController.mainButtons != null) HomeController.mainButtons.setVisible(true);
    ControllerManager.exitPopup();
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
