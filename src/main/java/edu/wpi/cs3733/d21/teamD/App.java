package edu.wpi.cs3733.d21.teamD;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.views.ControllerManager;
import edu.wpi.cs3733.d21.teamD.views.SceneSizeChangeListener;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

  public static Stage primaryStage;

  /*
  private boolean isInJar(){
    return this.getClass().getResource("").getProtocol().equals("jar");
  }
   */

  @Override
  public void init() {

    GlobalDb.establishCon(); // this will be for embedded
    // if adminClientBool is true --> call establishClientConn()
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    GlobalDb.getTables().getUserTable().dispUsers(GlobalDb.getConnection());

    App.primaryStage = primaryStage;

    ControllerManager.attemptLoadPage("initPageView.fxml", fxmlLoader -> initLoader(fxmlLoader));
  }

  public static void initLoader(FXMLLoader fxmlLoader) {
    Pane root = fxmlLoader.getRoot();
    List<Node> childrenList = root.getChildren();
    System.out.println("this is the initPage children list " + childrenList);
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
  }

  public static void changeChildrenInitPage(List<Node> nodeList) {

    HBox topButtons = (HBox) nodeList.get(4);
    JFXNodesList aboutBtns = (JFXNodesList) nodeList.get(6);
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

    aboutBtns.setLayoutX(hospitalImage.getLayoutX() + aboutBtns.getWidth() + 30);
    System.out.println("this is in the initPage " + hospitalImage.getLayoutX());
    aboutBtns.setLayoutY(App.getPrimaryStage().getScene().getHeight() - 100);
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
