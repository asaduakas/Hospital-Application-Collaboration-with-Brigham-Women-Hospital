package edu.wpi.teamname.views.Mapping;

import edu.wpi.teamname.App;
import edu.wpi.teamname.Astar.Edge;
import java.io.IOException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Popup;

public class EdgePopupController {

  public javafx.stage.Popup popup;
  private Edge theEdge; // holds node clicked for popup
  private Line line; // holds circle clicked for popup
  private MapController mapController;
  private EdgeEditNodes edgeEditNodes;
  private NodeEditPopup nodeEditPopup;

  @FXML
  private void initialize() {}

  @FXML
  private void Cancel() {
    exitPopup();
  }

  @FXML
  private void DeleteEdge() {
    //    mapController.deleteEdge(theEdge, line);
    exitPopup();
  }

  @FXML
  private void EditEdge() throws IOException {
    //    mapController.setTempL(this.line);
    //    mapController.setEdgeEdited(this.theEdge);
    GaussianBlur blur = new GaussianBlur(25);
    App.getPrimaryStage().getScene().getRoot().setEffect(blur);

    //    mapController.setIseditedge(true);

    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("EdgeEditNodes.fxml"));

    Pane root = (Pane) fxmlLoader.load();
    root.setStyle("-fx-background-color: #FFFFFF");

    setupDraggablePopUp(root);

    // tie this controller to popup for easy access of functions like pathfinding
    EdgeEditNodes popupController = fxmlLoader.getController();
    popupController.setMapController(this.mapController);
    popupController.setEdgePopupController(this);

    this.popup = new Popup();
    popupController.setEdge(theEdge);
    popupController.setline(line);
    popupController.setAdd(false);
    popup.getContent().addAll(root);
    popup.isAutoFix();
    popup.show(App.getPrimaryStage());

    exitPopup();
  }

  private void exitPopup() {
    //    if (mapController.popup != null) mapController.popup.hide();
    if (edgeEditNodes.popup != null) edgeEditNodes.popup.hide();
    if (nodeEditPopup.popup != null) nodeEditPopup.popup.hide();
    App.getPrimaryStage().getScene().getRoot().setEffect(null);
  }

  public void setMapController(MapController View) {
    this.mapController = View;
  }

  public void setEdge(Edge E) {
    this.theEdge = E;
  }

  public void setline(Line L) {
    this.line = L;
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

  public NodeEditPopup getNodeEditPopup() {
    return nodeEditPopup;
  }

  public void setNodeEditPopup(NodeEditPopup nodeEditPopup) {
    this.nodeEditPopup = nodeEditPopup;
  }
}
