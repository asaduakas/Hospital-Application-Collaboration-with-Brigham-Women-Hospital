package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class AboutController implements AllAccessible {
  @FXML private JFXTextArea blurb;
  @FXML private Label teamName;
  @FXML private GridPane gridPane;
  @FXML private Label kushal;
  @FXML private Circle kushalCircle;
  @FXML private Label veronica;
  @FXML private Circle veronicaCircle;
  @FXML private Label arman;
  @FXML private Circle armanCircle;
  @FXML private Label elaine;
  @FXML private Circle elaineCircle;
  @FXML private Label curtis;
  @FXML private Circle curtisCircle;
  @FXML private Label yiyi;
  @FXML private Circle yiyiCircle;
  @FXML private Label tia;
  @FXML private Circle tiaCircle;
  @FXML private Label reagan;
  @FXML private Circle reaganCircle;
  @FXML private Label patrick;
  @FXML private Circle patrickCircle;
  @FXML private Label uri;
  @FXML private Circle uriCircle;
  @FXML private JFXButton backButton;
  @FXML private StackPane stackPane;

  DialogFactory dialogFactory;

  // ------------------------------------------------------------------------------------

  @FXML
  private void initialize() {
    dialogFactory = new DialogFactory(stackPane);
    stackPane.setPickOnBounds(false);

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
    gridPane.setHalignment(kushalCircle, HPos.CENTER);
    gridPane.setHalignment(veronicaCircle, HPos.CENTER);
    gridPane.setHalignment(armanCircle, HPos.CENTER);
    gridPane.setHalignment(elaineCircle, HPos.CENTER);
    gridPane.setHalignment(curtisCircle, HPos.CENTER);
    gridPane.setHalignment(yiyiCircle, HPos.CENTER);
    gridPane.setHalignment(tiaCircle, HPos.CENTER);
    gridPane.setHalignment(reaganCircle, HPos.CENTER);
    gridPane.setHalignment(patrickCircle, HPos.CENTER);
    gridPane.setHalignment(uriCircle, HPos.CENTER);

    Image kImage = new Image("Images/kushal.png");
    ImagePattern kPattern = new ImagePattern(kImage);
    kushalCircle.setFill(kPattern);

    Image vImage = new Image("Images/veronica.png");
    ImagePattern vPattern = new ImagePattern(vImage);
    veronicaCircle.setFill(vPattern);

    Image aImage = new Image("Images/arman.png");
    ImagePattern aPattern = new ImagePattern(aImage);
    armanCircle.setFill(aPattern);

    Image eImage = new Image("Images/elain.png");
    ImagePattern ePattern = new ImagePattern(eImage);
    elaineCircle.setFill(ePattern);

    Image cImage = new Image("Images/curtis.png");
    ImagePattern cPattern = new ImagePattern(cImage);
    curtisCircle.setFill(cPattern);

    Image yImage = new Image("Images/yiyi.png");
    ImagePattern yPattern = new ImagePattern(yImage);
    yiyiCircle.setFill(yPattern);

    Image tImage = new Image("Images/tia.png");
    ImagePattern tPattern = new ImagePattern(tImage);
    tiaCircle.setFill(tPattern);

    Image rImage = new Image("Images/reagan.png");
    ImagePattern rPattern = new ImagePattern(rImage);
    reaganCircle.setFill(rPattern);

    Image pImage = new Image("Images/patrick.png");
    ImagePattern pPattern = new ImagePattern(pImage);
    patrickCircle.setFill(pPattern);

    Image uImage = new Image("Images/uri.png");
    ImagePattern uPattern = new ImagePattern(uImage);
    uriCircle.setFill(uPattern);
  }

  @FXML
  private void loadDialogK() {
    dialogFactory.createOneButtonDialogWhite(
        "About Kushal",
        "Kushal is an international student from India, "
            + "\n"
            + "part of the class of 2022, and is majoring in RBE. "
            + "\n"
            + "He likes to play snooker and a big time business "
            + "\n"
            + "ideation enthusiast!",
        "OK",
        () -> {});
  }

  @FXML
  private void loadDialogV() {
    dialogFactory.createOneButtonDialogWhite(
        "About Veronica",
        "Veronica is a Computer Science major "
            + "\n"
            + "graduating as a member of the class of "
            + "\n"
            + "2022. She is from Norwood, MA and loves"
            + "\n"
            + "to travel.",
        "OK",
        () -> {});
  }

  @FXML
  private void loadDialogA() {
    dialogFactory.createOneButtonDialogWhite(
        "About Arman",
        "Arman is an international student from "
            + "\n"
            + "Kazakhstan and is part of the class of 2023. He "
            + "\n"
            + "is majoring in Computer Science.",
        "OK",
        () -> {});
  }

  @FXML
  private void loadDialogE() {
    dialogFactory.createOneButtonDialogWhite(
        "About Elaine",
        "Elaine is a part of the class of 2022, majoring"
            + "\n"
            + " in Computer Science and Professional Writing, "
            + "\n"
            + "and is from New York City! A fun fact about Elaine "
            + "\n"
            + "is that she is ambidextrous.",
        "OK",
        () -> {});
  }

  @FXML
  private void loadDialogC() {
    dialogFactory.createOneButtonDialogWhite(
        "About Curtis",
        "Curtis is part of the class of 2023"
            + "\n"
            + " and is majoring in Robotics Engineering. "
            + "\n"
            + "He is from Massachusetts and enjoys playing "
            + "\n"
            + "badminton in his free time.",
        "OK",
        () -> {});
  }

  @FXML
  private void loadDialogY() {
    dialogFactory.createOneButtonDialogWhite(
        "About Yiyi",
        "Yiyi is part of the class of 2023"
            + "\n"
            + " and is majoring in Robotics Engineering "
            + "\n"
            + "and Mathematics. She loves driving whenever "
            + "\n"
            + "she can.",
        "OK",
        () -> {});
  }

  @FXML
  private void loadDialogT() {
    dialogFactory.createOneButtonDialogWhite(
        "About Tia",
        "Tia is part of the class of 2023"
            + "\n"
            + " and is majoring in Computer Science "
            + "\n"
            + "and Game Design. She enjoys crocheting "
            + "\n"
            + "and knitting in her free time.",
        "OK",
        () -> {});
  }

  @FXML
  private void loadDialogR() {
    dialogFactory.createOneButtonDialogWhite(
        "About Reagan",
        "Reagan is part of the class of 2023, "
            + "\n"
            + "majoring in Computer Science, and is "
            + "\n"
            + "from Pelham, NH! He is part of the Christian"
            + "\n"
            + " Bible Fellowship and enjoys playing the drums!",
        "OK",
        () -> {});
  }

  @FXML
  private void loadDialogP() {
    dialogFactory.createOneButtonDialogWhite(
        "About Patrick",
        "Patrick is a part of the class of 2023, "
            + "\n"
            + "majoring in Computer Science and Robotics "
            + "\n"
            + "Engineering, and is from Londonderry, NH! He"
            + "\n"
            + " is a member of the football and track teams,"
            + "\n"
            + " and his favorite movie series is Harry Potter!",
        "OK",
        () -> {});
  }

  @FXML
  private void loadDialogU() {
    dialogFactory.createOneButtonDialogWhite(
        "About Uri",
        "Uri is part of the class of 2023, is"
            + "\n"
            + " majoring in Computer Science, and is "
            + "\n"
            + "from Waltham, MA.",
        "OK",
        () -> {});
  }

  @FXML
  private void closePopup(ActionEvent event) {
    ControllerManager.exitPopup();
  }
}
