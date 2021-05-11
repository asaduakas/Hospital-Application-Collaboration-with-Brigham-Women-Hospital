package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CreditsController implements AllAccessible {
  @FXML private JFXButton backButton;
  @FXML private Label label1;
  @FXML private Label label2;
  @FXML private VBox vbox1;
  @FXML private VBox vbox2;

  @FXML
  private void initialize() {
    vbox1.setAlignment(Pos.CENTER);
    vbox2.setAlignment(Pos.CENTER);
    label1.setText("Machine learning based toolkit for the\n processing of natural language text.");
    label2.setText("Library to send email after COVID-19 survey\n has been filled out.");
  }

  @FXML
  private void closePopup(ActionEvent event) {
    ControllerManager.exitPopup();
  }
}
