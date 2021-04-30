package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Astar.Edge;
import edu.wpi.teamname.Astar.Node;
import edu.wpi.teamname.Ddb.GlobalDb;
import java.io.IOException;
import java.util.Random;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;

public class NodeEditPopup {

  private ObjectProperty<MapController> mapController = new SimpleObjectProperty<MapController>();
  private EdgeEditNodes edgeEditNodes;
  private Node theNode = new Node(); // holds node clicked for popup
  private Circle theCricle = new Circle(); // holds circle clicked for popup
  @FXML private Label nodeName;
  @FXML private JFXButton favoriteBtn;
  public static javafx.stage.Popup popup;
  @FXML private JFXButton cancelBtn;
  @FXML private JFXButton editBtn;
  @FXML private JFXButton addEdgeBtn;
  @FXML private JFXButton deleteBtn;

  @FXML
  private void initialize() {}

  @FXML
  private void Cancel() {
    exitPopup();
  }

  @FXML
  private void changeFavorite() {
    //    LinkedList<String> favNodeList =
    //        mapController
    //            .get()
    //            .getFav(GlobalDb.getConnection()); // update from database everytime we click
    Node n = theNode;
    int setFavorite = 0;

    //    if (!(favNodeList.contains(n.getNodeID()))) {
    //      favoriteBtn.setText("Add to Favorites");
    //      setFavorite = 1;
    //    } else if (favNodeList.contains(n.getNodeID())) {
    //      favoriteBtn.setText("Remove from Favorites");
    //      setFavorite = 0;
    //    }

    //    mapController.get().updateFavorite(GlobalDb.getConnection(), n.getNodeID(), setFavorite);
    // //
    //    mapController.get().redrawMap();
    exitPopup();
  }

  @FXML
  private void EditNode() throws IOException {
    GaussianBlur blur = new GaussianBlur(25);
    App.getPrimaryStage().getScene().getRoot().setEffect(blur);

    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("EditProperties.fxml"));

    Pane root = (Pane) fxmlLoader.load();
    root.setStyle("-fx-background-color: #FFFFFF");

    setupDraggablePopUp(root);

    // tie this controller to popup for easy access of functions like pathfinding
    EditPropertiesController popupController = fxmlLoader.getController();
    popupController.setMapController(this.mapController.get());

    popupController.nodeID.setText(theNode.getNodeID());
    popupController.floor.setText(theNode.getFloor());
    popupController.building.setText(theNode.getBuilding());
    popupController.nodeType.setText(theNode.getNodeType());
    popupController.longName.setText(theNode.getLongName());
    popupController.shortName.setText(theNode.getShortName());

    this.popup = new Popup();
    popupController.setNode(theNode);
    popupController.setCircle(theCricle);
    popupController.setAdd(false);
    popup.getContent().addAll(root);
    popup.isAutoFix();
    popup.show(App.getPrimaryStage());

    exitPopup();
  }

  @FXML
  private void DeleteNode() {
    //    mapController.get().deleteNode(theNode, theCricle);
    exitPopup();
  }

  @FXML
  private void AddEdge() throws IOException {
    //
    Edge theEdge = new Edge(theNode, theNode, 0.0);
    //    Line line = mapController.get().drawEdge(theEdge);

    Random random = new Random(System.currentTimeMillis());
    theEdge.setEdgeID("NEWEDGE"); // String.valueOf(random.nextInt(100000)));

    //    mapController.get().setTempN(theNode);

    GlobalDb.getTables()
        .getEdgeTable()
        .addEntity(
            GlobalDb.getConnection(),
            theEdge.getEdgeID(),
            theEdge.getStartNodeID(),
            theEdge.getEndNodeID());
    //
    //    mapController.get().setIseditedge(false);

    // popup
    GaussianBlur blur = new GaussianBlur(25);
    App.getPrimaryStage().getScene().getRoot().setEffect(blur);

    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("EdgeEditNodes.fxml"));

    Pane root = (Pane) fxmlLoader.load();
    root.setStyle("-fx-background-color: #FFFFFF");

    setupDraggablePopUp(root);

    // tie this controller to popup for easy access of functions like pathfinding
    EdgeEditNodes popupController = fxmlLoader.getController();
    popupController.setMapController(this.mapController.get());

    this.popup = new Popup();
    popupController.setEdge(theEdge);
    //    popupController.setline(line);
    popupController.setN(theNode);
    popupController.setC(theCricle);
    popupController.setAdd(true);
    //    mapController.get().setTempL(line);
    popup.getContent().addAll(root);
    popup.isAutoFix();
    popup.show(App.getPrimaryStage());

    exitPopup();
  }

  private void exitPopup() {
    //    mapController.get().popup.hide();
    if (edgeEditNodes.popup != null) edgeEditNodes.popup.hide();
    App.getPrimaryStage().getScene().getRoot().setEffect(null);
  }

  public void setMapController(MapController mapController) {
    this.mapController.set(mapController);
  }

  public void setNode(Node N) {
    this.theNode = N;
  }

  public void setCricle(Circle theCricle) {
    this.theCricle = theCricle;
  }

  public void setNodeName(String name) {
    nodeName.setText(name);
  }

  public void setupDraggablePopUp(Pane container) {
    ObjectProperty<Point2D> mouseLocation = new SimpleObjectProperty<>();

    container.setOnMousePressed(
        event -> mouseLocation.set(new Point2D(event.getScreenX(), event.getScreenY())));

    container.setOnMouseDragged(
        event -> {
          if (mouseLocation.get() != null) {
            double x = event.getScreenX();
            double deltaX = x - mouseLocation.get().getX();
            double y = event.getScreenY();
            double deltaY = y - mouseLocation.get().getY();
            // in case of 2 or more computer screens this help me to avoid get stuck on 1 screen
            if (Math.abs(popup.getX() - x) > popup.getWidth()) {
              popup.setX(x);
              popup.setY(y);
            } else {
              popup.setX(popup.getX() + deltaX);
              popup.setY(popup.getY() + deltaY);
            }
            mouseLocation.set(new Point2D(x, y));
          }
        });

    container.setOnMouseReleased(event -> mouseLocation.set(null));
  }

  public EdgeEditNodes getEdgeEditNodes() {
    return edgeEditNodes;
  }

  public void setEdgeEditNodes(EdgeEditNodes edgeEditNodes) {
    this.edgeEditNodes = edgeEditNodes;
  }
}
