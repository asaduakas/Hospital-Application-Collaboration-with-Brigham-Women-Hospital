package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuDrawerController {

  @FXML private JFXButton mapBtn;
  @FXML private JFXButton serviceBtn;

  @FXML
  private void changeView(ActionEvent event)
      throws
          IOException { // buttons that switch screens will run this method when they are clicked on
    Stage stage = null;
    Parent root = null;

    if (event.getSource() == serviceBtn) {
      if (!HomeController.controllerPermissible("ServicePageController")) return;
      stage = (Stage) serviceBtn.getScene().getWindow();
      root = FXMLLoader.load(getClass().getClassLoader().getResource("ServicePageView.fxml"));
    }
    if (event.getSource() == mapBtn) {
      if (!HomeController.controllerPermissible("MapController")) return;
      stage = (Stage) mapBtn.getScene().getWindow();
      root = FXMLLoader.load(getClass().getClassLoader().getResource("MapView.fxml"));
    }

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
