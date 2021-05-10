package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.Ddb.UsersTable;
import edu.wpi.cs3733.d21.teamD.chatbot.chatbot;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import opennlp.tools.doccat.DoccatModel;

public class ChatbotController implements AllAccessible {
  @FXML private Circle chatbotImage;
  @FXML private ImageView send;
  @FXML public JFXButton chatbotExit;
  @FXML public JFXTextArea userMessage;
  // public static TextFlow chatTextFlow = new TextFlow();
  public static VBox textBox = new VBox();
  @FXML private ScrollPane chatAreaScroll;
  @FXML private AnchorPane textAnchor;
  public Double messageLayoutY = 0.0;

  @FXML public Image userIconImg;
  @FXML public Image botIconImg;
  private Map<String, String> questionAnswer = new HashMap<>();
  private chatbot bot = new chatbot();
  private Boolean changeUsernameFlag = false;
  private Boolean changePsswdFlag = false;

  @FXML
  private void initialize() {

    questionAnswer.put("greeting", "Hello, how can I help you?");
    questionAnswer.put(
        "product-inquiry",
        "Product is a TipTop mobile phone. It is a smart phone with latest features like touch screen, bluetooth etc.");
    questionAnswer.put("price-inquiry", "Price is $300");
    questionAnswer.put("conversation-continue", "What else can I help you with?");
    questionAnswer.put("conversation-complete", "Nice chatting with you. Bye.");
    questionAnswer.put("Username-Info", "That's a cool username");
    questionAnswer.put("Navigation", "I will navigate you shortly.");

    Image image = new Image("Images/diamondDragon.png");
    // textAnchor.getChildren().add(chatTextFlow);
    textBox.setPrefWidth(chatAreaScroll.getPrefWidth());
    textBox.setPrefHeight(chatAreaScroll.getPrefHeight());
    textAnchor.getChildren().add(textBox);
    textAnchor.maxWidthProperty().bind(chatAreaScroll.widthProperty());
    // chatAreaScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    chatbotImage.setFill(new ImagePattern(image));
    //    chatTextFlow.setPrefWidth(chatAreaScroll.getPrefWidth());
    //    chatTextFlow.setPrefHeight(chatAreaScroll.getPrefHeight());
    if (HomeController.username == null) {
      HomeController.username = "Guest";
    }
    userIconImg = new Image("Images/userIconBot.png");
    botIconImg = new Image("Images/diamondDragonIcon.png");
  }

  @FXML
  public void sendMessage() throws IOException, InterruptedException {
    if (!changeUsernameFlag && !changePsswdFlag) {
      String userMessage = null;
      if (this.userMessage != null) {
        userMessage = this.userMessage.getText();
        this.userMessage.setText(null);
        dispMessageBot("user", userMessage);
        dispMessageBot("bot", interpretInput(userMessage));
      }
    } else if (changeUsernameFlag) {
      String newUsername = this.userMessage.getText();
      HomeController.username = newUsername;
      dispMessageBot("user", newUsername);
      this.userMessage.setText(null);
      UsersTable.updateUsername(GlobalDb.getConnection(), HomeController.username, newUsername);
      dispMessageBot("bot", "Your username has been updated successfully to: " + newUsername);
      changeUsernameFlag = false;
    } else if (changePsswdFlag) {
      String newPasswd = this.userMessage.getText();
      dispMessageBot("user", newPasswd);
      this.userMessage.setText(null);
      UsersTable.updateUserPassword(GlobalDb.getConnection(), HomeController.username, newPasswd);
      dispMessageBot("bot", "Your password has been updated successfully to: " + newPasswd);
      changePsswdFlag = false;
    }
  }

  public void dispMessageBot(String turn, String message) throws InterruptedException {
    //    Label dispLabel = new Label();
    String dispMessage = null;
    Insets textPadding = null;
    TextAlignment alignment = null;
    Color msgBgColor = null;
    ImageView icon = new ImageView();
    if (message != null) {
      if (turn.equals("user")) {
        textPadding = new Insets(0, 40, 0, 100);
        alignment = TextAlignment.RIGHT;
        msgBgColor = Color.WHITE;
        icon.setImage(userIconImg);
        icon.setFitWidth(30);
        icon.setFitHeight(30);
        dispMessage = HomeController.username + ": " + message + "\n";
      } else if (turn.equals("bot")) {
        textPadding = new Insets(0, 100, 0, 20);
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
      chatAreaScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      TextFlow test = new TextFlow();
      test.setPrefWidth(chatAreaScroll.getPrefWidth());
      test.getChildren().addAll(icon, dispText);
      test.setTextAlignment(alignment);
      test.setPadding(textPadding);
      textBox.getChildren().add(test);
    }
  }

  @FXML
  private void exit() {
    ControllerManager.popup.hide();
  }

  public String interpretInput(String input) throws IOException {

    DoccatModel categoryModel = bot.trainCategorizerModel();

    // Break users chat input into sentences using sentence detection.
    String[] sentences = bot.SentenceDetect(input);

    String answer = "";
    String prevCategory = " ";
    //    boolean conversationComplete = false;

    // Loop through sentences.
    for (String sentence : sentences) {

      // Separate words from each sentence using tokenizer.
      String[] tokens = bot.Tokenize(sentence);

      // Tag separated words with POS tags to understand their gramatical structure.
      String[] posTags = bot.detectPOSTags(tokens);

      // Lemmatize each word so that its easy to categorize.
      String[] lemmas = bot.lemmatizeTokens(tokens, posTags);

      // Determine BEST category using lemmatized tokens used a mode that we trained
      // at start.
      String category = bot.detectCategory(categoryModel, lemmas);
      // Get predefined answer from given category & add to answer.
      if (category.equals("Arbitrary")) {
        category = prevCategory;
      }
      if (category.equals("Audio-Visual")) {
        ControllerManager.attemptLoadPopupBlur("AVRequestView.fxml");
      } else if (category.equals("Computer-Request")) {
        ControllerManager.attemptLoadPopupBlur("ComputerServiceView.fxml");
      } else if (category.equals("Ex-Trans")) {
        ControllerManager.attemptLoadPopupBlur("ExternalTransportationView.fxml");
      } else if (category.equals("Facilities-Request")) {
        ControllerManager.attemptLoadPopupBlur("FacilitiesMaintenanceView.fxml");
      } else if (category.equals("Floral-Delivery")) {
        ControllerManager.attemptLoadPopupBlur("FloralDeliveryView.fxml");
      } else if (category.equals("Food-Delivery")) {
        ControllerManager.attemptLoadPopupBlur("FoodDeliveryView.fxml");
      } else if (category.equals("Internal-Trans")) {
        ControllerManager.attemptLoadPopupBlur("InternalTransportationView.fxml");
      } else if (category.equals("Lang-Interpret")) {
        ControllerManager.attemptLoadPopupBlur("LanguageInterpreterView.fxml");
      } else if (category.equals("Laundry-Request")) {
        ControllerManager.attemptLoadPopupBlur("LaundryView.fxml");
      } else if (category.equals("Med-Delivery")) {
        ControllerManager.attemptLoadPopupBlur("MedicineView.fxml");
      } else if (category.equals("Sanitation-Request")) {
        ControllerManager.attemptLoadPopupBlur("SanitationView.fxml");
      } else if (category.equals("Security-Request")) {
        ControllerManager.attemptLoadPopupBlur("SecurityServicesView.fxml");
      } else if (category.equals("Username-Info")) {
        prevCategory = "Username-Change";
        answer = "Your username is: " + HomeController.username;
      } else if (category.equals("Username-Change")) {
        prevCategory = "Username-Info";
        if (!(HomeController.username.equals("Guest"))) {
          answer = answer + " What would you like your new username to be?";
          changeUsernameFlag = true;
        } else {
          answer = answer + "Sorry, you need to be logged in first!";
        }
      } else if (category.equals("Password-Info")) {
        prevCategory = "Password-Change";
        // need to read the password from the database
        //        answer = "Your username is: " + HomeController.username;
      } else if (category.equals("Password-Change")) {
        prevCategory = "Password-Info";
        if (!(HomeController.username.equals("Guest"))) {
          answer = answer + " What would you like your new password to be?";
          changePsswdFlag = true;
        } else {
          answer = answer + "Sorry, you need to be logged in first!";
        }
      } else {
        answer = answer + " " + questionAnswer.get(category);
      }

      // Print answer back to user
      System.out.println("##### Chat Bot: " + answer);
    }
    return answer;
  }
}
