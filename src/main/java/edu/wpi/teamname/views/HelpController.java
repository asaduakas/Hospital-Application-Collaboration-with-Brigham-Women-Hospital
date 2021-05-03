package edu.wpi.teamname.views;

import edu.wpi.teamname.views.Access.AllAccessible;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HelpController implements AllAccessible {

  @FXML
  void okFunction(ActionEvent event) throws IOException {
    ControllerManager.exitPopup();
  }
}
