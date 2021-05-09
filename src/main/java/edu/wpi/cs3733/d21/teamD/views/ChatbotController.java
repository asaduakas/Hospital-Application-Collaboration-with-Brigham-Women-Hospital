package edu.wpi.cs3733.d21.teamD.views;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javax.swing.text.html.ImageView;

public class ChatbotController {
  @FXML private Circle chatbotImage;
  @FXML private ImageView sendImage;

  @FXML
  private void initialize() {
    Image image = new Image("Images/diamondDragon.png");
    chatbotImage.setFill(new ImagePattern(image));
  }
}
