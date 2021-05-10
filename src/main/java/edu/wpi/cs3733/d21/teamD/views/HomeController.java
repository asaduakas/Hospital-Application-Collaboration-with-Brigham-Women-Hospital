package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.d21.teamD.App;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import edu.wpi.cs3733.d21.teamD.views.Access.LoginController;
import edu.wpi.cs3733.d21.teamD.views.Access.UserCategory;
import edu.wpi.cs3733.d21.teamD.views.Mapping.MapScrollPane;
import java.io.IOException;
import java.util.List;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class HomeController implements AllAccessible {

  @FXML private JFXButton exitButton; // Btn to exit program
  @FXML private JFXButton logoutButton;
  @FXML private JFXButton usersBtn;

  @FXML private JFXButton mapEditing;
  @FXML private JFXButton serviceRequest;
  @FXML private JFXButton covidButton;
  @FXML public static Popup popup;
  @FXML public static VBox mainButtons;
  @FXML public StackPane stackPane;
  @FXML private AnchorPane mainPane;
  @FXML private Pane thePane;

  @FXML public ImageView chatbotImage;
  private Boolean mouseOnPopup = false;

  public static UserCategory userTypeEnum;
  public static String username = null;
  public static String password = null;
  private static String userCategory;
  public static Boolean disableRequestStatus = false;

  public static SceneSizeChangeListener sizeListener;

  // Used to reset search history for each login
  public static int historyTracker = 0;

  @FXML
  private void initialize() {
    chatbotImage.setOnMousePressed((e) -> chatbotPopUp());
  }

  @FXML
  private void logout(ActionEvent event) throws IOException {
    HomeController.username = null;
    HomeController.userTypeEnum = null;
    HomeController.userCategory = null;
    LoginController.userCategory = null;
    HomeController.historyTracker = 0;
    ControllerManager.attemptLoadPage(
        "initPageView.fxml", fxmlLoader -> App.initLoader(fxmlLoader));
  }

  @FXML
  private void closeApp(ActionEvent event) {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void emergencyView() throws IOException {
    ControllerManager.attemptLoadPopupBlur("Emergency.fxml");
  }

  @FXML
  public void helpView() throws IOException {
    ControllerManager.attemptLoadPopupBlur("Help.fxml");
  }

  @FXML
  public void usersView(ActionEvent event) {
    ControllerManager.attemptLoadPopupBlur(
        "UsersView.fxml",
        fxmlLoader -> ((Pane) fxmlLoader.getRoot()).setStyle("-fx-background-color: White"));
  }

  @FXML
  public void hospitalMapView(ActionEvent event) throws Exception {

    JFXSpinner spinner = new JFXSpinner();
    Label loading = new Label("Loading Map");
    Text text = new Text("Press ESC to cancel load map");
    loading.setStyle("-fx-font-weight: Bold; -fx-font-size: 20");
    text.setStyle("-fx-font-size: 20");

    spinner.setMaxHeight(150);
    spinner.setMaxWidth(150);
    stackPane.setStyle("-fx-background-color: #ffffff");
    stackPane.getChildren().addAll(loading, spinner, text);
    stackPane.setMargin(text, new Insets(200, 0, 0, 20));

    Task<Parent> task =
        new Task<Parent>() {
          @Override
          protected Parent call() throws Exception {
            FXMLLoader fxmlLoader =
                new FXMLLoader(getClass().getClassLoader().getResource("MapView.fxml"));
            Pane root = fxmlLoader.load();
            return root;
          }
        };

    mainPane.addEventHandler(
        KeyEvent.KEY_PRESSED,
        e -> {
          if (e.getCode() == KeyCode.ESCAPE) {
            System.out.println("cancelled");
            task.cancel();
          }
        });

    task.setOnSucceeded(
        e -> {
          stackPane.setVisible(false);

          if (LoginController.getUserCategory() != null) {
            this.userCategory = LoginController.getUserCategory();
          } else {
            this.userCategory = InitPageController.getUserCategory();
          }

          Pane root = (Pane) task.getValue();
          Scene scene = new Scene(root);

          App.getPrimaryStage().setMaximized(true);
          App.getPrimaryStage().close();
          App.getPrimaryStage().setScene(scene);
          App.getPrimaryStage().show();

          List<Node> childrenList = root.getChildren();
          System.out.println("this is childrenList of the map" + childrenList);
          JFXToggleButton mapEditing = (JFXToggleButton) childrenList.get(4);
          if (!userCategory.equalsIgnoreCase("admin")) {
            mapEditing.setVisible(false);
            mapEditing.setDisable(true);
          }

          changeChildrenMapView(childrenList);
          this.sizeListener =
              new SceneSizeChangeListener(scene, root, childrenList) {
                @Override
                public void changeChildren(List<Node> nodeList) {
                  updateMapScrollPane(nodeList);
                  changeChildrenMapView(childrenList);
                }
              };
          scene.widthProperty().addListener(sizeListener);
          scene.heightProperty().addListener(sizeListener);
          updateMapScrollPane(childrenList);
        });

    Thread thread = new Thread(task);
    thread.start();

    task.setOnFailed(e -> task.getException().printStackTrace());
    task.setOnCancelled(
        ee -> {
          task.cancel();
          stackPane.setVisible(false);
          stackPane.setDisable(true);
        });
  }

  // Make sure map scroll pane is scaled correctly
  private void updateMapScrollPane(List<Node> nodeList) {
    for (Node node : nodeList)
      if (node instanceof MapScrollPane) ((MapScrollPane) node).updateScaleRange();
  }

  public void changeChildrenMapView(List<Node> nodeList) {

    JFXDrawer drawer = (JFXDrawer) nodeList.get(1);
    JFXToggleButton mapEditorBtn = (JFXToggleButton) nodeList.get(4);
    JFXButton exitBtn = (JFXButton) nodeList.get(5);
    JFXButton helpBtn = (JFXButton) nodeList.get(6);
    ImageView helpImage = (ImageView) nodeList.get(7);
    StackPane stackPane = (StackPane) nodeList.get(8);
    JFXNodesList floorBtns = (JFXNodesList) nodeList.get(9);
    JFXNodesList csvBtns = (JFXNodesList) nodeList.get(10);

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

  @FXML
  private void covidSurvey(ActionEvent event) {
    ControllerManager.attemptLoadPopupBlur(
        "CovidSurveyView.fxml",
        fxmlLoader -> {
          Pane root = (Pane) fxmlLoader.getRoot();
          root.setStyle("-fx-background-color: White");
        });
  }

  public void serviceRequestView() {
    if (LoginController.getUserCategory() != null) {
      this.userCategory = LoginController.getUserCategory();
    } else {
      this.userCategory = InitPageController.getUserCategory();
    }

    ControllerManager.attemptLoadPopupBlur(
        "ServicePageView.fxml",
        fxmlLoader -> {
          List<Node> childrenList =
              App.getPrimaryStage().getScene().getRoot().getChildrenUnmodifiable();
          mainButtons = (VBox) childrenList.get(2);
          mainButtons.setVisible(false);
          boolean disableStatusButton = false;
          if (userCategory.equalsIgnoreCase("Guest") || userCategory.equalsIgnoreCase("Patient")) {
            // hide and disable the check request button
            disableStatusButton = true;
          } else if (userCategory.equalsIgnoreCase("Admin")
              || userCategory.equalsIgnoreCase("Employee")) {
            disableStatusButton = false;
          }
          JFXButton checkStatusButton =
              (JFXButton) ((Pane) fxmlLoader.getRoot()).getChildren().get(2);
          if (disableStatusButton) {
            // System.out.println("Hello 1");
            checkStatusButton.setVisible(false);
            checkStatusButton.setDisable(true);
          } else {
            // System.out.println("Hello 2 ");
            checkStatusButton.setVisible(true);
            checkStatusButton.setDisable(false);
          }
        });
  }

  @FXML
  public void chatbotPopUp() {
    ControllerManager.attemptLoadPopup(
        "ChatbotView.fxml",
        (fxmlLoader) -> {
          ControllerManager.popup.setX(
              chatbotImage.getLayoutX()
                  - ControllerManager.popup.getWidth()
                  - chatbotImage.getFitWidth() / 2);
          ControllerManager.popup.setY(
              chatbotImage.getLayoutY()
                  - ControllerManager.popup.getHeight()
                  + (chatbotImage.getFitHeight()));
        });

    //    ControllerManager.popup.setAutoHide(true);
  }

  public static String getUserCategory() {
    return userCategory;
  }

  public static UserCategory getUserTypeEnum() {
    return userTypeEnum;
  }

  //  @Override
  //  public void start(Stage primaryStage) throws Exception {}
}
