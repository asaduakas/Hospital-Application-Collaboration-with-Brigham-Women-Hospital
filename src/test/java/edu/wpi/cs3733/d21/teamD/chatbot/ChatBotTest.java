package edu.wpi.cs3733.d21.teamD.chatbot;

import static org.junit.Assert.*;

import edu.wpi.cs3733.d21.teamD.App;
import edu.wpi.cs3733.d21.teamD.Ddb.FDatabaseTables;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.Ddb.UsersTable;
import edu.wpi.cs3733.d21.teamD.Testing.JavaFXThreadingRule;
import edu.wpi.cs3733.d21.teamD.views.ControllerManager;
import edu.wpi.cs3733.d21.teamD.views.HomeController;
import edu.wpi.cs3733.d21.teamD.views.SceneSizeChangeListener;
import java.io.IOException;
import java.util.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import opennlp.tools.doccat.DoccatModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ChatBotTest {

  private Map<String, String> questionAnswer = new HashMap<>();
  private static Popup popup = null;

  @Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

  //  @BeforeClass
  //  public static void initApp() {
  //    App.launch(App.class, new String[0]);
  //  }

  @Before
  public void setup() {
    questionAnswer.put("greeting", "Hello, how can I help you?");
    questionAnswer.put(
        "product-inquiry",
        "Product is a TipTop mobile phone. It is a smart phone with latest features like touch screen, bluetooth etc.");
    questionAnswer.put("price-inquiry", "Price is $300");
    questionAnswer.put("conversation-continue", "What else can I help you with?");
    questionAnswer.put("conversation-complete", "Nice chatting with you. Bye.");
    questionAnswer.put("Username-Info", "That's a cool username");
    questionAnswer.put("Navigation", "I will navigate you shortly.");
  }

  @Test
  public void testChatBot() throws IOException {
    chatbot bot = new chatbot();
    DoccatModel categoryModel = bot.trainCategorizerModel();

    //    modelTraining trainUsernameModel = new modelTraining();
    //    trainUsernameModel.usernameModelTraining();

    LinkedList<String> username = bot.findUserName("my username is wwong");
    System.out.println("--------------------------------------------------------------------");
    System.out.println(username);
  }

  @Test
  public void testChatBot2() throws IOException {
    chatbot bot = new chatbot();
    DoccatModel categoryModel = bot.trainCategorizerModel();
    // Take chat inputs from console (user) in a loop.
    Scanner scanner = new Scanner(System.in);

    // while (true) {

    // Get chat input from user.
    System.out.println("##### You:");
    String userInput =
        "Hello bot! what is my password? Can you help me change my password"; // scanner.nextLine();

    // Break users chat input into sentences using sentence detection.
    String[] sentences = bot.SentenceDetect(userInput);

    String answer = "";
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
      if (category.equals("Username-Info")) {
        for (int i = 0; i < tokens.length; i++) {
          if ((posTags[i].equals("NNP")
              || posTags[i].equals("NN") && !tokens[i].equals("username"))) {
            answer = answer + " Username inputted is: " + HomeController.username + ".";
            break;
          }
        }
      }
      if (category.equals("Username-Change")) {
        GlobalDb.establishCon();
        FDatabaseTables.getUserTable();
        FDatabaseTables.getUserTable().dispUsers(GlobalDb.getConnection());
        // get the login username
        // HomeController.getUsername
        answer = answer + " What would you like your new username to be?";
        // take the input from textfield fxxml, which will be new username, replace "elaine" with
        // whatever the new username is in below function
        // write to the database
        UsersTable.updateUsername(GlobalDb.getConnection(), HomeController.username, "elaine");
      }
      if (category.equals("Password-Change")) {
        answer = answer + " What would you like your new password to be?";
        // take the input from the textfield
        // update the new passwd
        // write to database
        UsersTable.updateUserPassword(
            GlobalDb.getConnection(), HomeController.username, "newPasswd");
      } else {
        answer = answer + " " + questionAnswer.get(category);
      }

      // If category conversation-complete, we will end chat conversation.
      //      if ("conversation-complete".equals(category)) {
      //        conversationComplete = true;
      //      }
    }

    // Print answer back to user. If conversation is marked as complete, then end
    // loop & program.
    System.out.println("##### Chat Bot: " + answer);
    //    if (conversationComplete) {
    //      break;
    //    }
    //    }
  }

  @Test
  public void navigationTest() throws IOException, InterruptedException {

    GlobalDb.establishCon();
    System.out.println("Starting Up");

    App.primaryStage = new Stage();

    ControllerManager.attemptLoadPage(
        "initPageView.fxml",
        (fxmlLoader) -> {
          Pane root = fxmlLoader.getRoot();
          List<Node> childrenList = root.getChildren();
          App.primaryStage.setMinHeight(135 * 4);
          App.primaryStage.setMinWidth(240 * 4);
          Scene scene = App.primaryStage.getScene();
          App.changeChildrenInitPage(childrenList);
          // Overriding the method inside the object of fullScreenListener
          SceneSizeChangeListener listener =
              new SceneSizeChangeListener(scene, root, childrenList) {
                @Override
                public void changeChildren(List<Node> nodeList) {
                  App.changeChildrenInitPage(childrenList);
                }
              };
          scene.widthProperty().addListener(listener);
          scene.heightProperty().addListener(listener);
        });

    chatbot bot = new chatbot();
    DoccatModel categoryModel = bot.trainCategorizerModel();

    System.out.println("##### You:");
    String userInput =
        "Hello bot! My username is Kushal! I want to make a request."; // scanner.nextLine();

    // Break users chat input into sentences using sentence detection.
    String[] sentences = bot.SentenceDetect(userInput);

    String answer = "";
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
      if (category.equals("Navigation")) {
        ControllerManager.attemptLoadPage("ServicePageView.fxml");
      }
      if (category.equals("Username-Info")) {
        for (int i = 0; i < tokens.length; i++) {
          if ((posTags[i].equals("NNP")
              || posTags[i].equals("NN") && !tokens[i].equals("username"))) {
            answer = answer + " Username inputted is: " + tokens[i] + ".";
            break;
          }
        }
      } else {
        answer = answer + " " + questionAnswer.get(category);
      }

      // Print answer back to user
      System.out.println("##### Chat Bot: " + answer);
    }
  }
}
