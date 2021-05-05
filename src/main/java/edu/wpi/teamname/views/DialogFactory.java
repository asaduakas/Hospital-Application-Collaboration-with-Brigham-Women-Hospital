package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class DialogFactory {

  private StackPane stackPane;

  public DialogFactory(StackPane stackPane) {
    this.stackPane = stackPane;
  }

  private void createDialog(
      String title,
      String content,
      List<Pair<String, Runnable>> buttonPairs,
      boolean whiteButtons) {

    JFXDialogLayout layout = new JFXDialogLayout();
    Text header = new Text(title);
    header.setFont(Font.font("System", FontWeight.BOLD, 18));
    layout.setHeading(header);
    layout.setBody(new Text(content));

    JFXDialog dialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.CENTER);
    List<JFXButton> buttons = new ArrayList<>();

    for (Pair<String, Runnable> buttonPair : buttonPairs) {
      JFXButton button = new JFXButton(buttonPair.getKey());
      if (!whiteButtons) button.setStyle("-fx-background-color: #cdcdcd;");
      button.setOnAction(
          e -> {
            dialog.close();
            buttonPair.getValue().run();
          });
      buttons.add(button);
    }

    layout.setActions(buttons);
    dialog.show();
  }

  public void createOneButtonDialog(
      String title, String content, String buttonName, Runnable buttonAction) {
    createDialog(title, content, Arrays.asList(new Pair<>(buttonName, buttonAction)), false);
  }

  public void createOneButtonDialogWhite(
      String title, String content, String buttonName, Runnable buttonAction) {
    createDialog(title, content, Arrays.asList(new Pair<>(buttonName, buttonAction)), true);
  }

  public void createTwoButtonDialog(
      String title,
      String content,
      String firstButtonName,
      Runnable firstButtonAction,
      String secondButtonName,
      Runnable secondButtonAction) {
    createDialog(
        title,
        content,
        Arrays.asList(
            new Pair<>(firstButtonName, firstButtonAction),
            new Pair<>(secondButtonName, secondButtonAction)),
        false);
  }

  public void createTwoButtonDialogWhite(
      String title,
      String content,
      String firstButtonName,
      Runnable firstButtonAction,
      String secondButtonName,
      Runnable secondButtonAction) {
    createDialog(
        title,
        content,
        Arrays.asList(
            new Pair<>(firstButtonName, firstButtonAction),
            new Pair<>(secondButtonName, secondButtonAction)),
        true);
  }
}
