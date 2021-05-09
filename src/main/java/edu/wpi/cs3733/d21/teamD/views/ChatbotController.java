package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class ChatbotController implements AllAccessible {
  @FXML private Circle chatbotImage;
  @FXML private ImageView send;
  @FXML public JFXButton chatbotExit;

  @FXML
  private void initialize() {
    Image image = new Image("Images/diamondDragon.png");
    chatbotImage.setFill(new ImagePattern(image));
  }

  @FXML
  private void exit() {
    ControllerManager.popup.hide();
  }
}
