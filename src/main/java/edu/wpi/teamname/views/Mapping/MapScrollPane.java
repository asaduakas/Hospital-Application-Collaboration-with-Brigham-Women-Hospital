package edu.wpi.teamname.views.Mapping;

import edu.wpi.teamname.App;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MapScrollPane extends ScrollPane {
  private double minimumScale;
  private double maximumScale;

  private double scaleValue;
  private double zoomIntensity = 0.02;
  private ImageView mapImage;
  public AnchorPane mapAnchor;
  private Node zoomNode;

  public MapScrollPane(Image image) {
    super();

    this.scaleValue = minimumScale;
    this.mapImage = new ImageView();
    mapImage.setImage(image);
    this.mapAnchor = new AnchorPane();
    mapAnchor.getChildren().add(mapImage);
    this.zoomNode = new Group(mapAnchor);
    setContent(outerNode(zoomNode));

    setPannable(true);
    setHbarPolicy(ScrollBarPolicy.ALWAYS);
    setVbarPolicy(ScrollBarPolicy.ALWAYS);
    setFitToHeight(true); // center
    setFitToWidth(true); // center

    updateScaleRange();
  }

  public void updateScaleRange() {
    System.out.println(App.getPrimaryStage().getWidth());
    System.out.println(App.getPrimaryStage().getHeight());
    System.out.println();
    this.minimumScale =
        Math.max(
            App.getPrimaryStage().getWidth() / mapImage.getImage().getWidth(),
            App.getPrimaryStage().getHeight() / mapImage.getImage().getHeight());
    this.maximumScale = minimumScale * 10;
    if (scaleValue < minimumScale) scaleValue = minimumScale;
    if (scaleValue > maximumScale) scaleValue = maximumScale;
    updateScale();
  }

  public void setMapImage(Image image) {
    mapImage.setImage(image);
  }

  private Node outerNode(Node node) {
    Node outerNode = centeredNode(node);
    outerNode.setOnScroll(
        e -> {
          e.consume();
          onScroll(e.getTextDeltaY(), new Point2D(e.getX(), e.getY()));
        });
    return outerNode;
  }

  private Node centeredNode(Node node) {
    VBox vBox = new VBox(node);
    vBox.setAlignment(Pos.CENTER);
    return vBox;
  }

  private void updateScale() {
    mapAnchor.setScaleX(scaleValue);
    mapAnchor.setScaleY(scaleValue);
    System.out.println("Scale is " + scaleValue);
  }

  private void onScroll(double wheelDelta, Point2D mousePoint) {
    double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

    Bounds innerBounds = zoomNode.getLayoutBounds();
    Bounds viewportBounds = getViewportBounds();

    // calculate pixel offsets from [0, 1] range
    double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
    double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

    scaleValue = scaleValue * zoomFactor;
    if (scaleValue < minimumScale || scaleValue > maximumScale) {
      scaleValue = scaleValue / zoomFactor;
      return;
    }
    updateScale();
    this.layout(); // refresh ScrollPane scroll positions & target bounds

    // convert target coordinates to zoomTarget coordinates
    Point2D posInZoomTarget = mapAnchor.parentToLocal(zoomNode.parentToLocal(mousePoint));

    // calculate adjustment of scroll position (pixels)
    Point2D adjustment =
        mapAnchor
            .getLocalToParentTransform()
            .deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

    // convert back to [0, 1] range
    // (too large/small values are automatically corrected by ScrollPane)
    Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
    this.setHvalue(
        (valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
    this.setVvalue(
        (valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
  }
}
