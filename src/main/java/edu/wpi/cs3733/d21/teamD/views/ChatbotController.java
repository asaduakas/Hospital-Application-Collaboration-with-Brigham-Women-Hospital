package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.d21.teamD.App;
import edu.wpi.cs3733.d21.teamD.Ddb.FDatabaseTables;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.Ddb.NodesTable;
import edu.wpi.cs3733.d21.teamD.Ddb.UsersTable;
import edu.wpi.cs3733.d21.teamD.chatbot.chatbot;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import edu.wpi.cs3733.d21.teamD.views.Access.LoginController;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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

  private Boolean navigateCategoryFlag = false;
  private Boolean jokeCategoryFlag = false;
  private Boolean startEndFlag = false;

  public String start = null;
  public String end = null;

  private LinkedList<String> jokeList = new LinkedList<String>();

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

    userIconImg = new Image("Images/userIconBot.png");
    botIconImg = new Image("Images/diamondDragonIcon.png");

    chatAreaScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    chatAreaScroll
        .vvalueProperty()
        .bind(textBox.heightProperty()); // auto scrolling if height of the content increases
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
      if (newUsername != null) {
        if (!FDatabaseTables.getUserTable().contains(GlobalDb.getConnection(), newUsername)) {
          dispMessageBot("user", newUsername);
          this.userMessage.setText(null);
          UsersTable.updateUsername(GlobalDb.getConnection(), HomeController.username, newUsername);
          HomeController.username = newUsername;
          dispMessageBot("bot", "Your username has been updated successfully to: " + newUsername);
          changeUsernameFlag = false;
        } else {
          dispMessageBot("bot", "Sorry this username is already taken!");
        }
      }
    } else if (changePsswdFlag) {
      String newPasswd = this.userMessage.getText();
      if (newPasswd != null) {
        dispMessageBot("user", newPasswd);
        this.userMessage.setText(null);
        UsersTable.updateUserPassword(GlobalDb.getConnection(), HomeController.username, newPasswd);
        dispMessageBot("bot", "Your password has been updated successfully to: " + newPasswd);
        changePsswdFlag = false;
      }
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
        if (HomeController.username != null) {
          dispMessage = HomeController.username + ": " + message + "\n";
        } else {
          dispMessage = "Guest: " + message + "\n";
        }

      } else if (turn.equals("bot")) {
        textPadding = new Insets(0, 100, 0, 20);
        alignment = TextAlignment.LEFT;
        msgBgColor = Color.BLUE;
        icon.setImage(botIconImg);
        icon.setFitWidth(30);
        icon.setFitHeight(30);
        dispMessage = " Dr. Dobby: " + message + "\n";
      }

      TextFlow test = new TextFlow();
      Text dispText = new Text(dispMessage);
      dispText.maxHeight(20);
      dispText.setStyle(
          " -fx-font-style: Italic; -fx-font-weight: Bold; -fx-font-size: 20; -fx-background-color: #093fc6");
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

  public String interpretInput(String input) throws IOException, InterruptedException {

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
      String category = null;
      if (!navigateCategoryFlag) {
        category = bot.detectCategory(categoryModel, lemmas);
      } else if (navigateCategoryFlag) {
        category = "Pathfinding";
      } else if (jokeCategoryFlag) {
        category = "Tell-Joke";
      }

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
      } else if (category.equals("Request-Table")) {
        if ((HomeController.username != null)) {
          if (!HomeController.userCategory.equalsIgnoreCase("Patient")) {
            answer = answer + "Sure thing!";
            ControllerManager.attemptLoadPopupBlur(
                "StatusView.fxml",
                fxmlLoader ->
                    ((Pane) fxmlLoader.getRoot()).setStyle("-fx-background-color: White"));
          }
        }
        if (HomeController.username == null) {
          answer = answer + "Sorry, you need to be logged in first!";
        }
        if (HomeController.userCategory.equalsIgnoreCase("Patient")) {
          answer = answer + "Sorry, you need to be logged in as either an admin or an employee!";
        }
      } else if (category.equals("User-Table")) {
        if (HomeController.userCategory.equalsIgnoreCase("Admin")) {
          ControllerManager.exitPopup();
          ControllerManager.attemptLoadPopupBlur(
              "UsersView.fxml",
              fxmlLoader -> ((Pane) fxmlLoader.getRoot()).setStyle("-fx-background-color: White"));
        } else {
          answer = answer + "Sorry, you need to be logged in as admin to access users-tables";
        }
      } else if (category.equals("Hospital-Map")) {
        ControllerManager.exitPopup();

        ControllerManager.attemptLoadPage(
            "HomeView.fxml",
            fxmlLoader -> {
              LoginController.start(fxmlLoader.getRoot());
              HomeController controller = fxmlLoader.getController();
              controller.hospitalMapView();
            });

      } else if (category.equals("Pathfinding")) {
        answer = getStartEndLoc(tokens, input);
        if ((start != null) && (end != null)) {
          // do the navigation here
          System.out.println(start + " : " + end);

          ControllerManager.exitPopup();
          ControllerManager.attemptLoadPage(
              "HomeView.fxml",
              fxmlLoader -> {
                LoginController.start(fxmlLoader.getRoot());
                HomeController controller = fxmlLoader.getController();
                controller.mapViewFromBot(start, end);
              });
          start = null;
          end = null;
        }
      } else if (category.equals("Emergency")) {
        ControllerManager.attemptLoadPopupBlur("Emergency.fxml");
      } else if (category.equals("Username-Info")) {
        prevCategory = "Username-Change";
        if (HomeController.username == null) {
          answer = "Your username is: Guest";
        } else {
          answer = "Your username is: " + HomeController.username;
        }
      } else if (category.equals("Username-Change")) {
        prevCategory = "Username-Info";
        if (HomeController.username != null) {
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
        if (!(HomeController.username == null)) {
          answer = answer + " What would you like your new password to be?";
          changePsswdFlag = true;
        } else {
          answer = answer + "Sorry, you need to be logged in first!";
        }
      } else if (category.equals("Identity")) {
        answer =
            answer
                + "My name is Dr. Dobby, an AI powered bot at your service. I was created by a wonderful team of experts at WPI on 7th of May";
      } else if (category.equals("Tell-Joke")) {
        // add jokes and select random
        Random rand = new Random();

        jokeList.add(
            "What do you call it when computer science majors make fun of each other?\n"
                + "Cyber boolean");
        jokeList.add("My dog ate my computer science project. It took him a couple bytes.");
        jokeList.add(
            "Why isn't there any beautiful girl in computer science?\n"
                + "Because they're all 0's and 1's");
        jokeList.add("Did you take soft-eng?");
        jokeList.add(
            "How did the computer hackers get away from the scene of the crime?\n"
                + "I think they just ransomware.");
        jokeList.add("What OS do Jedi run their computers on?\n" + "The DagobahSystem.");
        jokeList.add(
            "Why didn't the client tip the server?\n" + "Because they didn't have enough cache!");
        int randomNum = rand.nextInt(((jokeList.size() - 1) - 0) + 1) + 0;
        answer = answer + jokeList.get(randomNum);

      } else if (category.equals("Joke-End")) {
        answer = answer + "Hell I am! After all I was created by the best team in the world!";
      } else {
        answer = answer + " " + questionAnswer.get(category);
      }

      // Print answer back to user
      System.out.println("##### Chat Bot: " + answer);
    }
    return answer;
  }

  public String getStartEndLoc(String[] tokens, String input) throws InterruptedException {
    String botMessage = null;
    ArrayList<String> longNames = NodesTable.fetchLongName(GlobalDb.getConnection());
    ArrayList<String> shortNames = NodesTable.fetchShortName(GlobalDb.getConnection());
    if (!startEndFlag) {
      LinkedList<String> startList = new LinkedList<String>();
      LinkedList<String> endList = new LinkedList<String>();
      Boolean fromFlag = false;
      Boolean toFlag = false;
      for (String token : tokens) {
        if (token.equalsIgnoreCase("from")) {
          fromFlag = true;
        }
        if (token.equalsIgnoreCase("to")) {
          fromFlag = false;
          toFlag = true;
        }
        if (fromFlag) {
          startList.add(token);
        }
        if (toFlag) {
          endList.add(token);
        }
      }
      if (startList.size() != 0 && endList.size() != 0) {
        startList.remove(0);
        endList.remove(0);
      }

      int i = 0;
      int j = 0;
      String tempStart = null;
      String tempEnd = null;
      for (String s : startList) {
        if (i == 0) {
          tempStart = s;
        } else {
          tempStart = tempStart + " " + s;
        }
        i++;
      }
      for (String e : endList) {
        if (j == 0) {
          tempEnd = e;
        } else {
          tempEnd = tempEnd + " " + e;
        }
        j++;
      }

      if (!longNames.contains(tempStart) && longNames.contains(tempEnd)) {
        botMessage = "Please mention your start location! Either it was empty or incorrect!";
        navigateCategoryFlag = true;
        startEndFlag = true;
        end = tempEnd;
      } else if (!longNames.contains(tempEnd) && longNames.contains(tempStart)) {
        botMessage = "Please mention your end location! Either it was empty or incorrect!";
        navigateCategoryFlag = true;
        startEndFlag = true;
        start = tempStart;
      } else if (!longNames.contains(tempStart) && !longNames.contains(tempEnd)) {
        navigateCategoryFlag = true;
        startEndFlag = false;
        botMessage =
            "Please mention your start and end location correctly! Use from: <start loc> to <end loc>";
        // to do: handle both
      } else if (longNames.contains(tempStart) && longNames.contains(tempEnd)) {
        start = tempStart;
        end = tempEnd;
      }

    } else if (startEndFlag) {
      if (start == null) {
        if (longNames.contains(input)) {
          if (input.equals(end)) {
            botMessage = "Please enter a different loc than end location";
          } else {
            start = input;
            navigateCategoryFlag = false;
            startEndFlag = false;
          }

        } else {
          botMessage = "Hmm, I did not found this location in my database! want to try again?";
        }
      } else if (end == null) {
        if (longNames.contains(input)) {
          if (input.equals(start)) {
            botMessage = "Please enter a different loc than start location";
          } else {
            end = input;
            navigateCategoryFlag = false;
            startEndFlag = false;
          }
        } else {
          botMessage = "Hmm, I did not found this location in my database! want to try again?";
        }
      }
    }
    return botMessage;
  }

  public void mapLoader(FXMLLoader fxmlLoader) throws AWTException {

    Pane root = fxmlLoader.getRoot();
    List<Node> childrenList = root.getChildren();
    Scene scene = App.getPrimaryStage().getScene();
    changeChildrenMapPage(childrenList);
    // Overriding the method inside the object of fullScreenListener
    SceneSizeChangeListener listener =
        new SceneSizeChangeListener(scene, root, childrenList) {
          @Override
          public void changeChildren(List<Node> nodeList) {
            changeChildrenMapPage(childrenList);
          }
        };
    scene.widthProperty().addListener(listener);
    scene.heightProperty().addListener(listener);
  }

  public void changeChildrenMapPage(List<Node> nodeList) {
    JFXDrawer drawer = (JFXDrawer) nodeList.get(1);
    JFXToggleButton mapEditorBtn = (JFXToggleButton) nodeList.get(4);
    JFXButton exitBtn = (JFXButton) nodeList.get(5);
    JFXButton helpBtn = (JFXButton) nodeList.get(6);
    ImageView helpImage = (ImageView) nodeList.get(7);
    StackPane stackPane = (StackPane) nodeList.get(8);
    JFXNodesList floorBtns = (JFXNodesList) nodeList.get(10);
    JFXNodesList csvBtns = (JFXNodesList) nodeList.get(11);

    drawer.setLayoutX(-270);
    drawer.setLayoutY(0);

    exitBtn.setLayoutY(
        App.getPrimaryStage().getScene().getWindow().getHeight() - exitBtn.getHeight() - 60);
    exitBtn.setLayoutX(App.getPrimaryStage().getScene().getWidth() - exitBtn.getWidth() - 40);

    floorBtns.setLayoutY(20);
    floorBtns.setLayoutX(App.getPrimaryStage().getScene().getWidth() - floorBtns.getWidth() - 40);

    mapEditorBtn.setLayoutX(floorBtns.getLayoutX() - 150);
    mapEditorBtn.setLayoutY(14);

    csvBtns.setLayoutY(20);
    csvBtns.setLayoutX(mapEditorBtn.getLayoutX() - csvBtns.getWidth() - 20);

    helpBtn.setLayoutY(exitBtn.getLayoutY());
    helpBtn.setLayoutX(20);

    helpImage.setLayoutX(
        (App.getPrimaryStage().getScene().getWidth() - helpImage.getFitWidth()) / 2);
    helpImage.setLayoutY(
        (App.getPrimaryStage().getScene().getHeight() - helpImage.getFitHeight()) / 2);
    helpImage.setVisible(false);

    stackPane.setLayoutX((App.getPrimaryStage().getScene().getWidth() - stackPane.getWidth()) / 2);
    stackPane.setLayoutY(
        (App.getPrimaryStage().getScene().getHeight() - stackPane.getHeight()) / 2);
    stackPane.setPickOnBounds(false);
  }
}
