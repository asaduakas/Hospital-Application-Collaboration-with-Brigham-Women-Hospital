package edu.wpi.cs3733.d21.teamD.views;

import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HelpController implements AllAccessible {

  @FXML
  void okFunction(ActionEvent event) throws IOException {
    ControllerManager.exitPopup();
  }
}
