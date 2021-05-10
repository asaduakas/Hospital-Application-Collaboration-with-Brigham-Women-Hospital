package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class AboutController implements AllAccessible {
  @FXML private JFXTextArea blurb;
  @FXML private Label teamName;
  @FXML private GridPane gridPane;
  @FXML private Label kushal;
  @FXML private Label veronica;
  @FXML private Label arman;
  @FXML private Label elaine;
  @FXML private Label curtis;
  @FXML private Label yiyi;
  @FXML private Label tia;
  @FXML private Label reagan;
  @FXML private Label patrick;
  @FXML private Label uri;
  @FXML private JFXButton backButton;

  // ------------------------------------------------------------------------------------

  @FXML
  private void initialize() {
    blurb.setEditable(false);

    gridPane.setHalignment(kushal, HPos.CENTER);
    gridPane.setHalignment(veronica, HPos.CENTER);
    gridPane.setHalignment(arman, HPos.CENTER);
    gridPane.setHalignment(elaine, HPos.CENTER);
    gridPane.setHalignment(curtis, HPos.CENTER);
    gridPane.setHalignment(yiyi, HPos.CENTER);
    gridPane.setHalignment(tia, HPos.CENTER);
    gridPane.setHalignment(reagan, HPos.CENTER);
    gridPane.setHalignment(patrick, HPos.CENTER);
    gridPane.setHalignment(uri, HPos.CENTER);
  }

  @FXML
  private void closePopup(ActionEvent event) {
    ControllerManager.exitPopup();
  }
}
