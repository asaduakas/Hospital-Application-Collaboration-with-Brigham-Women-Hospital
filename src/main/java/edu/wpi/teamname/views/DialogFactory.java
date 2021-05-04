package edu.wpi.teamname.views;

import javafx.scene.layout.StackPane;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

public class DialogFactory {

    private StackPane stackPane;

    public DialogFactory(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    private void createDialog(String title, String content, List<Pair<String, Runnable>> buttons, boolean whiteButtons){
        //TODO: Add code
    }

    public void createOneButtonDialog(String title, String content, String buttonName, Runnable buttonAction) {
        createDialog(title, content, Arrays.asList(new Pair<>(buttonName, buttonAction)), false);
    }

    public void createOneButtonDialogWhite(String title, String content, String buttonName, Runnable buttonAction) {
        createDialog(title, content, Arrays.asList(new Pair<>(buttonName, buttonAction)), true);
    }

    public void createTwoButtonDialog(String title, String content, String firstButtonName, Runnable firstButtonAction, String secondButtonName, Runnable secondButtonAction) {
        createDialog(title, content,
                Arrays.asList(
                        new Pair<>(firstButtonName, firstButtonAction),
                        new Pair<>(secondButtonName, secondButtonAction)),
                false);
    }

    public void createTwoButtonDialogWhite(String title, String content, String firstButtonName, Runnable firstButtonAction, String secondButtonName, Runnable secondButtonAction) {
        createDialog(title, content,
                Arrays.asList(
                        new Pair<>(firstButtonName, firstButtonAction),
                        new Pair<>(secondButtonName, secondButtonAction)),
                true);
    }

}
