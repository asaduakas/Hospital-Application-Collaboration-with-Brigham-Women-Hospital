package edu.wpi.cs3733.d21.teamD.chatbot;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import opennlp.tools.doccat.DoccatModel;
import org.junit.Before;
import org.junit.Test;

public class ChatBotTest {

  private Map<String, String> questionAnswer = new HashMap<>();

  /*
   * Define answers for each given category.
   */
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

    /*
    String[] sentences = bot.SentenceDetect("whats my passwrd?");
    for (String sentence : sentences) {
      System.out.println(sentence);
      String[] tokens = bot.Tokenize(sentence);
      String[] posTags = bot.detectPOSTags(tokens);
      String[] lemmas = bot.lemmatizeTokens(tokens, posTags);
      String category = bot.detectCategory(categoryModel, lemmas);
      System.out.println("-----x-----");
    }
     */
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
        "Hello bot! My username is Curtis! I want to make a request."; // scanner.nextLine();

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
          if (posTags[i].equals("NNP")) {
            answer = answer + " Username inputted is: " + tokens[i] + ".";
            break;
          }
        }
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
}
