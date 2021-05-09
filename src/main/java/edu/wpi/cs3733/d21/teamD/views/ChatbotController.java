package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import java.awt.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class ChatbotController implements AllAccessible {
  @FXML private Circle chatbotImage;
  @FXML private ImageView send;
  @FXML public JFXButton chatbotExit;
  @FXML public JFXTextArea userMessage;
  @FXML public TextFlow chatTextFlow;
  public Double messageLayoutY = 0.0;

  @FXML public Image userIconImg;
  @FXML public Image botIconImg;

  @FXML
  private void initialize() {
    Image image = new Image("Images/diamondDragon.png");
    chatbotImage.setFill(new ImagePattern(image));
    if (HomeController.username == null) {
      HomeController.username = "Guest";
    }
    userIconImg = new Image("Images/userIconBot.png");
    botIconImg = new Image("Images/diamondDragonIcon.png");
  }

  @FXML
  public void sendMessage() {
    String userMessage = null;
    if (this.userMessage != null) {
      userMessage = this.userMessage.getText();
    }
    this.userMessage.setText(null);
    dispMessageBot("bot", userMessage);
  }

  public void dispMessageBot(String turn, String message) {
    //    Label dispLabel = new Label();
    String dispMessage = null;
    Insets textPadding = null;
    TextAlignment alignment = null;
    Color msgBgColor = null;
    ImageView icon = new ImageView();
    if (message != null) {
      if (turn.equals("user")) {
        textPadding = new Insets(0, 10, 0, 100);
        alignment = TextAlignment.RIGHT;
        msgBgColor = Color.WHITE;
        icon.setImage(userIconImg);
        icon.setFitWidth(30);
        icon.setFitHeight(30);
        dispMessage = HomeController.username + ": " + message + "\n";
      } else if (turn.equals("bot")) {
        textPadding = new Insets(0, 100, 0, 10);
        alignment = TextAlignment.LEFT;
        msgBgColor = Color.BLUE;
        icon.setImage(botIconImg);
        icon.setFitWidth(30);
        icon.setFitHeight(30);
        dispMessage = " Dr. Dobby: " + message + "\n";
      }

      Text dispText = new Text(dispMessage);
      dispText.maxHeight(20);
      dispText.setStyle(
          " -fx-font-style: Italic; -fx-font-weight: Bold; -fx-font-size: 20; -fx-background-color: #093fc6");
      chatTextFlow.getChildren().addAll(icon, dispText);
      chatTextFlow.setTextAlignment(alignment);
      chatTextFlow.setPadding(textPadding);
      //      chatTextFlow.setStyle("-fx-background-color: White");
    }
  }

  @FXML
  private void exit() {
    ControllerManager.popup.hide();
  }
}
