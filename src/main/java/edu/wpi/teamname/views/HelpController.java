package edu.wpi.teamname.views;

import edu.wpi.teamname.App;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HelpController {

  @FXML
  void okFunction(ActionEvent event) throws IOException {
    // TODO: Close popup
    if (InitPageController.popup != null) {
      InitPageController.popup.hide();
    }
    if (HomeController.popup != null) {
      HomeController.popup.hide();
    }
    App.getPrimaryStage().getScene().getRoot().setEffect(null);
  }
}
