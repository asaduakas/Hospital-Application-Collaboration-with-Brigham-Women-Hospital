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

  public void initDialogFactory() {
    dialogFactory = new DialogFactory(stackPane);
  }

  // ------------------------------------------------------------------------------------

  @FXML
  private void initialize() {
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
  private void closePopup(ActionEvent event) {
    ControllerManager.exitPopup();
  }
}
