package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.d21.teamD.App;
import edu.wpi.cs3733.d21.teamD.Ddb.FDatabaseTables;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import edu.wpi.cs3733.d21.teamD.views.Access.AllAccessible;
import edu.wpi.cs3733.d21.teamD.views.Access.LoginController;
import edu.wpi.cs3733.d21.teamD.views.Access.UserCategory;
import edu.wpi.cs3733.d21.teamD.views.Mapping.MapController;
import edu.wpi.cs3733.d21.teamD.views.Mapping.MapScrollPane;
import java.io.IOException;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

  @FXML private ImageView hospitalImage;
  @FXML private JFXButton mapEditing;
  @FXML private JFXButton serviceRequest;
  @FXML private JFXButton covidButton;
  @FXML public static Popup popup;
  @FXML public static VBox mainButtons;
  @FXML public StackPane stackPane;
  @FXML private AnchorPane mainPane;
  @FXML private JFXToggleButton dbToggle;
  @FXML private Pane thePane;
  @FXML private JFXButton pageButton;

  @FXML public ImageView chatbotImage;
  private Boolean mouseOnPopup = false;

  public static UserCategory userTypeEnum;
  public static String username = null;
  public static String password = null;
  public static String userCategory;
  public static Boolean disableRequestStatus = false;

  public static SceneSizeChangeListener sizeListener;

  // Used to reset search history for each login
  public static int historyTracker = 0;

  private int index = 0;
  private String dbToggleText[] = {"Embedded Connection", "Remote Connection"};

  @FXML
  public void initialize() {
    initializePages();
    chatbotImage.setOnMousePressed((e) -> chatbotPopUp());
    dbToggle.setOnAction(
        (ActionEvent e) -> {
          index++;
          if (index >= dbToggleText.length) {
            index = 0;
          }
          dbToggle.setText(dbToggleText[index]);
        });
    dbToggleSwitch();
    if (LoginController.getUserCategory() != null) {
      this.userCategory = LoginController.getUserCategory();
    } else {
      this.userCategory = InitPageController.getUserCategory();
    }
    //    ControllerManager.attemptLoadPopupBlur("StatusView.fxml");
    //    ControllerManager.exitPopup();
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
  public void hospitalMapView() {

    JFXSpinner spinner = new JFXSpinner();
    Label loading = new Label("Loading Map");
    Text text = new Text("Dr. Dobby is getting your map!");
    loading.setStyle("-fx-font-weight: Bold; -fx-font-size: 20");
    text.setStyle("-fx-font-size: 20");

    logoutButton.setDisable(true);
    exitButton.setDisable(true);

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
            // task.cancel();
            // GlobalDb.establishCon();
            logoutButton.setDisable(false);
            exitButton.setDisable(false);
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

          //          App.getPrimaryStage().setMaximized(true);
          //          App.getPrimaryStage().close();
          App.getPrimaryStage().setScene(scene);
          App.getPrimaryStage().setMaximized(false);
          App.getPrimaryStage().setMaximized(true);
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

  public void mapViewFromBot(String start, String end) {

    JFXSpinner spinner = new JFXSpinner();
    Label loading = new Label("Loading Map");
    Text text = new Text("Dr. Dobby is getting your map!");
    loading.setStyle("-fx-font-weight: Bold; -fx-font-size: 20");
    text.setStyle("-fx-font-size: 20");

    logoutButton.setDisable(true);
    exitButton.setDisable(true);

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
            // task.cancel();
            // GlobalDb.establishCon();
            logoutButton.setDisable(false);
            exitButton.setDisable(false);
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

          //          App.getPrimaryStage().setMaximized(true);
          //          App.getPrimaryStage().close();
          App.getPrimaryStage().setScene(scene);
          App.getPrimaryStage().setMaximized(false);
          App.getPrimaryStage().setMaximized(true);
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

          if (start != null && end != null) {
            try {
              MapController.drawerController.chatBotPathFinding(start, end);
            } catch (IOException ioException) {
              ioException.printStackTrace();
            }
          }
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
    JFXButton floorBtn = (JFXButton) nodeList.get(6);
    JFXButton helpBtn = (JFXButton) nodeList.get(7);
    ImageView helpImage = (ImageView) nodeList.get(8);
    StackPane stackPane = (StackPane) nodeList.get(9);
    JFXButton testBtn = (JFXButton) nodeList.get(10);
    JFXButton fl1Btn = (JFXButton) nodeList.get(11);
    JFXButton fl2Btn = (JFXButton) nodeList.get(12);
    JFXButton fl3Btn = (JFXButton) nodeList.get(13);
    JFXButton fll1Btn = (JFXButton) nodeList.get(14);
    JFXButton fll2Btn = (JFXButton) nodeList.get(15);
    JFXNodesList csvBtns = (JFXNodesList) nodeList.get(16);

    drawer.setLayoutX(-270);
    drawer.setLayoutY(0);

    exitBtn.setLayoutY(
        App.getPrimaryStage().getScene().getWindow().getHeight() - exitBtn.getHeight() - 60);
    exitBtn.setLayoutX(App.getPrimaryStage().getScene().getWidth() - exitBtn.getWidth() - 40);

    floorBtn.setLayoutY(20);
    floorBtn.setLayoutX(App.getPrimaryStage().getScene().getWidth() - floorBtn.getWidth() - 40);

    fl1Btn.setLayoutY(95);
    fl1Btn.setLayoutX(floorBtn.getLayoutX());
    fl2Btn.setLayoutY(165);
    fl2Btn.setLayoutX(floorBtn.getLayoutX() + 2.5);
    fl3Btn.setLayoutY(235);
    fl3Btn.setLayoutX(floorBtn.getLayoutX() + 2.5);
    fll1Btn.setLayoutY(305);
    fll1Btn.setLayoutX(floorBtn.getLayoutX() + 2.5);
    fll2Btn.setLayoutY(375);
    fll2Btn.setLayoutX(floorBtn.getLayoutX() + 2.5);

    mapEditorBtn.setLayoutX(floorBtn.getLayoutX() - 150);
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

    testBtn.setLayoutY(helpBtn.getLayoutY());
    testBtn.setLayoutX(helpBtn.getLayoutX() + helpBtn.getWidth() + 30);
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

    FDatabaseTables.getExternalTransportTable().getIncompleteRequest();
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
    if (ChatbotController.textBox.getChildren().size() > 0) {
      ChatbotController.textBox.getChildren().clear();
    }
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

  private void dbToggleSwitch() {
    dbToggle
        .selectedProperty()
        .addListener(
            new ChangeListener<Boolean>() {
              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observable,
                  Boolean oldValue,
                  Boolean newValue) {
                if (dbToggle.isSelected()) {
                  System.out.println("Before remote connection established");
                  // drop tables from auto embedded
                  // GlobalDb.getTables().deleteAllTables();
                  GlobalDb.establishClientCon();
                  System.out.println("Remote connection established");
                } else {
                  // GlobalDb.getTables().createAllTables();
                  GlobalDb.establishCon();
                  System.out.println("Switched back to embedded");
                }
              }
            });
  }

  @FXML
  private void initializePages() {
    pageButton.setPrefHeight(60);
    pageButton.setPrefWidth(60);
    pageButton.setStyle(
        "-fx-background-color: #E5E5E5; -fx-background-radius: 60px; -fx-text-fill:  #000000; -fx-font-size: 14; -fx-font-weight: Bold");
    Text pages = new Text("Pages");
    //    pages.setStyle("-fx-text-fill:  #000000; -fx-font-size: 14; -fx-font-weight: Bold");

    pageButton.setText(pages.getText());

    JFXButton aboutButton = new JFXButton("ABOUT");
    aboutButton.setButtonType(JFXButton.ButtonType.FLAT);
    aboutButton.setStyle(
        "-fx-background-color: #C8BEFF; -fx-background-radius: 60px; -fx-font-size: 12");
    aboutButton.setPrefHeight(60);
    aboutButton.setPrefWidth(60);
    aboutButton.setOnAction(this::aboutPage);

    JFXButton creditButton = new JFXButton("CREDITS");
    creditButton.setButtonType(JFXButton.ButtonType.FLAT);
    creditButton.setStyle(
        "-fx-background-color: #AB9CFF; -fx-background-radius: 60px; -fx-font-size: 11");
    creditButton.setPrefHeight(60);
    creditButton.setPrefWidth(60);
    creditButton.setOnAction(this::creditsPage);

    JFXNodesList nodeList = new JFXNodesList();
    nodeList.addAnimatedNode(pageButton);
    nodeList.addAnimatedNode(aboutButton);
    nodeList.addAnimatedNode(creditButton);
    nodeList.setSpacing(20d);
    mainPane.getChildren().add(nodeList);

    nodeList.setRotate(180);
  }

  @FXML
  private void aboutPage(ActionEvent event) {
    ControllerManager.attemptLoadPopupBlur(
        "AboutView.fxml",
        fxmlLoader -> {
          Pane root = (Pane) fxmlLoader.getRoot();
          root.setStyle("-fx-background-color: White");
        });
  }

  @FXML
  private void creditsPage(ActionEvent event) {
    ControllerManager.attemptLoadPopupBlur(
        "CreditsView.fxml",
        fxmlLoader -> {
          Pane root = (Pane) fxmlLoader.getRoot();
          root.setStyle("-fx-background-color: White");
        });
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
