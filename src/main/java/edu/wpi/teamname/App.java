package edu.wpi.teamname;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.ControllerManager;
import edu.wpi.teamname.views.SceneSizeChangeListener;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

  private static Stage primaryStage;

  /*
  private boolean isInJar(){
    return this.getClass().getResource("").getProtocol().equals("jar");
  }
   */

  @Override
  public void init() {

    GlobalDb.establishCon();
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    GlobalDb.getTables().getUserTable().dispUsers(GlobalDb.getConnection());
    App.primaryStage = primaryStage;

    ControllerManager.attemptLoadPage(
        "initPageView.fxml",
        fxmlLoader -> {
          Pane root = (Pane) fxmlLoader.getRoot();
          List<Node> childrenList = root.getChildren();
          primaryStage.setMinHeight(135 * 4);
          primaryStage.setMinWidth(240 * 4);
          Scene scene = primaryStage.getScene();
          changeChildrenInitPage(childrenList);
          // Overriding the method inside the object of fullScreenListener
          SceneSizeChangeListener listener =
              new SceneSizeChangeListener(scene, root, childrenList) {
                @Override
                public void changeChildren(List<Node> nodeList) {
                  changeChildrenInitPage(childrenList);
                }
              };
          scene.widthProperty().addListener(listener);
          scene.heightProperty().addListener(listener);
        });
  }

  public void changeChildrenInitPage(List<Node> nodeList) {

    HBox topButtons = (HBox) nodeList.get(4);
    topButtons.setLayoutX(primaryStage.getScene().getWidth() - (topButtons.getWidth() + 26));

    VBox buttons = (VBox) nodeList.get(2);
    buttons.setLayoutX((primaryStage.getScene().getWidth() - buttons.getWidth()) / 2);
    buttons.setLayoutY((primaryStage.getScene().getHeight() - (buttons.getHeight() / 3)) / 2);

    ImageView hospitalImage = (ImageView) nodeList.get(1);
    double newHospitalRatio;
    newHospitalRatio = 1499 / 722;

    hospitalImage.setFitHeight(primaryStage.getScene().getHeight() - 87);
    hospitalImage.setFitWidth(hospitalImage.getFitHeight() * newHospitalRatio);

    hospitalImage.setLayoutX(
        (primaryStage.getScene().getWidth() - hospitalImage.getFitWidth()) / 2);

    JFXButton exitButton = (JFXButton) nodeList.get(5);
    if (hospitalImage.getFitWidth() < App.getPrimaryStage().getScene().getWindow().getWidth()) {
      exitButton.setLayoutX(
          ((App.getPrimaryStage().getScene().getWindow().getWidth() - hospitalImage.getFitWidth())
                  / 2)
              + hospitalImage.getFitWidth()
              - (exitButton.getWidth() + 50));
    } else {
      exitButton.setLayoutX(
          App.getPrimaryStage().getScene().getWidth() - (exitButton.getWidth() + 50));
    }
    exitButton.setLayoutY(
        App.getPrimaryStage().getScene().getHeight() - (exitButton.getHeight() + 50));
  }

  public static Stage getPrimaryStage() {
    return primaryStage;
  }

  public static void setPrimaryStage() {
    primaryStage = new Stage();
  }

  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }
}
