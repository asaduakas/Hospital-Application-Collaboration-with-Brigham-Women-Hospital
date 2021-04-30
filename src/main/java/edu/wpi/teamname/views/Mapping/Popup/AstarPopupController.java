package edu.wpi.teamname.views.Mapping.Popup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Astar.Node;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import edu.wpi.teamname.views.Access.PatientAccessible;
import edu.wpi.teamname.views.AutoCompleteComboBox;
import edu.wpi.teamname.views.HomeController;
import edu.wpi.teamname.views.Mapping.MapController;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class AstarPopupController implements PatientAccessible {
  @FXML Button btn1; // Btn for changing to AstarView
  @FXML Button btn2; // Btn for changing back to homeView

  @FXML public JFXComboBox<String> start_choice; // Selection choice box for start node
  @FXML public JFXComboBox<String> end_choice; // Selection choice box for end node
  @FXML private JFXComboBox<String> start_favorites;
  @FXML private JFXComboBox<String> end_favorites;
  @FXML private StackPane stackPane;
  public String recentStart = "";
  public String recentEnd = "";

  // necessary so that we don't call/create new instances of mapController to do pathfinding
  private ObjectProperty<MapController> mapController = new SimpleObjectProperty<MapController>();

  HashMap<String, String> longNameToNodeID;
  ArrayList<String> longNames;
  HashMap<String, String> favoriteNamesToNodeID;
  ArrayList<String> favoriteNodes;

  @FXML
  private void changeView(ActionEvent event)
      throws
          IOException { // buttons that switch screens will run this method when they are clicked on

    if (event.getSource() == btn1) {
      final String startID = longNameToNodeID.get(start_choice.getValue());
      final String endID = longNameToNodeID.get(end_choice.getValue());
      final String startFavID = favoriteNamesToNodeID.get(start_favorites.getValue());
      final String endFavID = favoriteNamesToNodeID.get(end_favorites.getValue());

      //      if (startID == null || endID == null || startID.equals(endID)) {
      //        // TODO: Add error message for nothing selected in choice box or selecting same node
      // for
      //        return;
      //      }

      // Store longnames so we can display most recent search in bar
      String startName = start_choice.getValue();
      String endName = end_choice.getValue();
      String startFavName = start_favorites.getValue();
      String endFavName = end_favorites.getValue();

      MapController controller = this.mapController.get();

      if ((startID == null && endID == null)
          && (startFavID != null && endFavID != null)
          && (!startFavID.equals(endFavID))) {
        recentStart = startFavName;
        recentEnd = endFavName;
        System.out.println("reached condition 1");
        // controller.runPathFinding(startFavID, endFavID);
        exitPopup();
        // fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("AstarView.fxml"));
        // root = fxmlLoader.load();
        // fxmlLoader.<AstarController>getController().runAStar(startFavID, endFavID);
        FDatabaseTables.getSearchHistoryTable().addEntity(GlobalDb.getConnection(), recentStart, recentEnd);
        HomeController.historyTracker = 1;
        getSearchHistory(GlobalDb.getConnection());
      } else if ((startID != null && endID != null)
          && (startFavID == null && endFavID == null)
          && (!startID.equals(endID))) {
        System.out.println("reached condition 2");
        // controller.runPathFinding(startID, endID);
        exitPopup();
        recentStart = startName;
        recentEnd = endName;
        System.out.println("reached condition 2" + recentStart + recentEnd);
        FDatabaseTables.getSearchHistoryTable().addEntity(GlobalDb.getConnection(), recentStart, recentEnd);
        HomeController.historyTracker = 1;
        getSearchHistory(GlobalDb.getConnection());
        //  fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("AstarView.fxml"));
        // root = fxmlLoader.load();
        // fxmlLoader.<AstarController>getController().runAStar(startID, endID);
      } else if ((startID != null && endID == null)
          && (startFavID == null && endFavID != null)
          && (!startID.equals(endFavID))) {
        recentStart = startName;
        recentEnd = endFavName;
        System.out.println("reached condition 3");
        // controller.runPathFinding(startID, endFavID);
        exitPopup();
        //  fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("AstarView.fxml"));
        // root = fxmlLoader.load();
        // fxmlLoader.<AstarController>getController().runAStar(startID, endFavID);
        FDatabaseTables.getSearchHistoryTable().addEntity(GlobalDb.getConnection(), recentStart, recentEnd);
        HomeController.historyTracker = 1;
        getSearchHistory(GlobalDb.getConnection());
      } else if ((startID == null && endID != null)
          && (startFavID != null && endFavID == null)
          && (!startFavID.equals(endID))) {
        recentStart = startFavName;
        recentEnd = endName;
        System.out.println("reached condition 4");
        // controller.runPathFinding(startFavID, endID);
        exitPopup();
        //  fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("AstarView.fxml"));
        // root = fxmlLoader.load();
        // fxmlLoader.<AstarController>getController().runAStar(startFavID, endID);
        FDatabaseTables.getSearchHistoryTable().addEntity(GlobalDb.getConnection(), recentStart, recentEnd);
        HomeController.historyTracker = 1;
        getSearchHistory(GlobalDb.getConnection());
      } else {
        return;
      }
    } else if (event.getSource() == btn2) {
      exitPopup();
    }
  }

  private void exitPopup() {
    // mapController.get().popup.hide();
    App.getPrimaryStage().getScene().getRoot().setEffect(null);
  }

  @FXML
  public void popupWarning(ActionEvent event) throws IOException {

    Text header = new Text("Warning");
    header.setFont(Font.font("System", FontWeight.BOLD, 18));

    JFXDialogLayout warningLayout = new JFXDialogLayout();
    warningLayout.setHeading(header);
    warningLayout.setBody(new Text("Please fill the required fields"));
    JFXDialog warningDia =
        new JFXDialog(stackPane, warningLayout, JFXDialog.DialogTransition.CENTER);
    JFXButton okBtn = new JFXButton("Close");
    okBtn.setStyle("-fx-background-color: #cdcdcd;");
    okBtn.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            warningDia.close();
          }
        });
    warningLayout.setActions(okBtn);
    warningDia.show();
  }

  @FXML
  public void initialize() throws IOException, NullPointerException {
    HashMap<String, Node> nodeMap = getNodes(GlobalDb.getConnection());
    longNames = new ArrayList<>();
    longNameToNodeID = new HashMap<>();
    favoriteNamesToNodeID = new HashMap<>();
    favoriteNodes = new ArrayList<String>();
    for (String nodeID : nodeMap.keySet()) {
      String longName = nodeMap.get(nodeID).getLongName();
      longNameToNodeID.put(longName, nodeID);
      longNames.add(longName);
    }

    MapController aStar = new MapController();
    Connection readconnection = GlobalDb.getConnection();
    // LinkedList<String> favNodesFromDataBase = aStar.getFav(readconnection);

    //    if (!(favNodesFromDataBase.isEmpty())) {
    //      System.out.println("Im here");
    //      for (String favID : favNodesFromDataBase) {
    //        String favName = nodeMap.get(favID).getLongName();
    //        favoriteNamesToNodeID.put(favName, favID);
    //        System.out.println(favName);
    //        favoriteNodes.add(favName);
    //      }
    //    }
    //
    //    longNames.sort(String::compareToIgnoreCase); // Make sure names are in alphabetical order
    //    start_choice.getItems().addAll(longNames);
    //    end_choice.getItems().addAll(longNames);
    //
    //    start_favorites.getItems().addAll(favoriteNodes);
    //    end_favorites.getItems().addAll(favoriteNodes);
    //
    //    getSearchHistory(GlobalDb.getConnection());
  }

  public HashMap<String, Node> getNodes(Connection conn) {
    Statement stmt = null;

    HashMap<String, Node> nodes = new HashMap<String, Node>();
    try {
      stmt = conn.createStatement();
      String query = "SELECT * FROM Nodes";
      ResultSet rs = stmt.executeQuery(query);
      Node node;
      while (rs.next()) {
        node =
            new Node(
                rs.getString("nodeID"),
                rs.getInt("xcoord"),
                rs.getInt("ycoord"),
                rs.getString("floor"),
                rs.getString("building"),
                rs.getString("nodeType"),
                rs.getString("longName"),
                rs.getString("shortName"));
        nodes.put(rs.getString("nodeID"), node);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    // Tables.dispAll(GlobalDb.getConnection());
    return nodes;
  }

  public void type(ActionEvent actionEvent) {

    AutoCompleteComboBox autoStart = new AutoCompleteComboBox(this.start_choice);
    autoStart.getEntries().addAll(longNames);

    AutoCompleteComboBox autoEnd = new AutoCompleteComboBox(this.end_choice);
    autoEnd.getEntries().addAll(longNames);

    AutoCompleteComboBox autoStartFav = new AutoCompleteComboBox(this.start_favorites);
    autoStartFav.getEntries().addAll(favoriteNodes);

    AutoCompleteComboBox autoEndFav = new AutoCompleteComboBox(this.end_favorites);
    autoEndFav.getEntries().addAll(favoriteNodes);
  }

  public void getSearchHistory(Connection conn) {
    Statement stmt = null;
    ResultSet rs = null;

    try {
      stmt = conn.createStatement();

      String query = "SELECT * FROM SearchHistory";

      rs = stmt.executeQuery(query);

      // conn.setAutoCommit(false);

      while (rs.next()) {
        recentStart = rs.getString("startName");
        recentEnd = rs.getString("endName");
        System.out.println("start-" + rs.getString("startName"));
        System.out.println("end-" + rs.getString("endName"));
      }
      rs.close();

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    // Initialize search menus with most recent searches
    System.out.println(recentStart + "-------------------");
    System.out.println(recentEnd + "----------------------");
    if (HomeController.historyTracker == 1) {
      start_choice.getSelectionModel().select(recentStart);
      end_choice.getSelectionModel().select(recentEnd);
    }
    // start_choice.setPromptText(recentStart);
    // end_choice.setPromptText(recentEnd);
  }

  public void setMapController(MapController mapController) {
    this.mapController.set(mapController);
  }
}
