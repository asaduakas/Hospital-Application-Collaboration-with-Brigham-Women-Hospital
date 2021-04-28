package edu.wpi.teamname.views.ServiceRequests.SRControllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public interface IRequest {
  void initialize(URL url, ResourceBundle rb);

  @FXML
  void loadDialog(MouseEvent Event);
}
