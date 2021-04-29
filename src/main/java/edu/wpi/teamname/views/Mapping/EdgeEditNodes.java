package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Astar.Edge;
import edu.wpi.teamname.Astar.Node;
import java.io.IOException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Popup;

public class EdgeEditNodes {

  public Popup popup;
  private Edge theEdge; // holds node clicked for popup
  private Line line; // holds circle clicked for popup
  private MapController mapController;
  private EdgePopupController edgePopupController;
  private Node N;
  private Circle C;
  public static boolean isAdd = false; // isAdd = true if coming from Add edge

  @FXML private JFXButton selectStartBtn;
  @FXML private JFXButton selectEndBtn;
  @FXML private JFXButton cancelBtn;

  @FXML
  private void initialize() {}

  @FXML
  private void Cancel() throws IOException {
    if (!isAdd) {
      GaussianBlur blur = new GaussianBlur(25);
      App.getPrimaryStage().getScene().getRoot().setEffect(blur);
      FXMLLoader fxmlLoader =
          new FXMLLoader(getClass().getClassLoader().getResource("EdgeEditPopup.fxml"));

      Pane root = fxmlLoader.load();
      root.setStyle("-fx-background-color: #FFFFFF");

      setupDraggablePopUp(root);

      // tie this controller to popup for easy access of functions like pathfinding
//      EdgePopupController popupController = fxmlLoader.getController();
//      popupController.setMapController(mapController);
//      popupController.setEdgeEditNodes(this);
//
//      popupController.setEdge(theEdge);
//      popupController.setline(line);

      this.popup = new Popup();
      popup.getContent().addAll(root);
      popup.show(App.getPrimaryStage());
      exitPopup();
    } else {
      // mapController.deleteLine(line);
      GaussianBlur blur = new GaussianBlur(25);
      App.getPrimaryStage().getScene().getRoot().setEffect(blur);
      FXMLLoader fxmlLoader =
          new FXMLLoader(getClass().getClassLoader().getResource("NodeEditPopup.fxml"));

      Pane root = (Pane) fxmlLoader.load();
      root.setStyle("-fx-background-color: #FFFFFF");

      setupDraggablePopUp(root);

//      NodeEditPopup popupController = fxmlLoader.getController();
//      popupController.setMapController(mapController);
//      popupController.setEdgeEditNodes(this);
//      popupController.setNode(N);
//      popupController.setCricle(C);
//      popupController.setNodeName(N.getLongName());

      this.popup = new Popup();

      popup.getContent().addAll(root);
      popup.isAutoFix();
      popup.show(App.getPrimaryStage());
      exitPopup();
    }
  }

  @FXML
  private void selectStart() throws IOException {
    // mapController.setMode(MapController.edgeNodeSelection.STARTSELECT);
    exitPopup();
  }

  @FXML
  private void selectEnd() throws IOException {
    // mapController.setMode(MapController.edgeNodeSelection.ENDSELECT);
    cancelBtn.setDisable(false);
    cancelBtn.setVisible(true);
    exitPopup();
  }

  private void exitPopup() {
//    if (edgePopupController.popup != null) edgePopupController.popup.hide();
//    if (mapController.popup != null) mapController.popup.hide();
//    if (NodeEditPopup.popup != null) NodeEditPopup.popup.hide();
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

  public void disableCancel() {
    cancelBtn.setDisable(true);
    cancelBtn.setVisible(false);
  }

  public boolean isAdd() {
    return isAdd;
  }

  public void setAdd(boolean add) {
    isAdd = add;
  }

  public Edge getTheEdge() {
    return theEdge;
  }

  public void setTheEdge(Edge theEdge) {
    this.theEdge = theEdge;
  }

  public Line getLine() {
    return line;
  }

  public void setLine(Line line) {
    this.line = line;
  }

  public Node getN() {
    return N;
  }

  public void setN(Node n) {
    N = n;
  }

  public Circle getC() {
    return C;
  }

  public void setC(Circle c) {
    C = c;
  }

  public EdgePopupController getEdgePopupController() {
    return edgePopupController;
  }

  public void setEdgePopupController(EdgePopupController edgePopupController) {
    this.edgePopupController = edgePopupController;
  }
}
