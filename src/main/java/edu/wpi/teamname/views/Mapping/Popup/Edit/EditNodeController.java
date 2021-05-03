package edu.wpi.teamname.views.Mapping.Popup.Edit;

import edu.wpi.teamname.App;
import javafx.fxml.FXML;

public class EditNodeController {

  @FXML
  private void initialize() {}

  @FXML
  private void Cancel() {
    exitPopup();
  }

  private void exitPopup() {
    App.getPrimaryStage().getScene().getRoot().setEffect(null);
  }
}
