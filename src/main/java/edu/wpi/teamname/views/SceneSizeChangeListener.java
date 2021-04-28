package edu.wpi.teamname.views;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

public class SceneSizeChangeListener implements ChangeListener<Number> {
  private Scene scene;
  private double ratio;
  private double initHeight;
  private double initWidth;
  private Pane contentPane;
  private List<Node> nodeList;

  public SceneSizeChangeListener(Scene scene, Pane contentPane, List<Node> nodeList) {

    this.scene = scene;
    this.ratio = ratio;
    this.initHeight = initHeight;
    this.initWidth = initWidth;
    this.contentPane = contentPane;
    this.nodeList = nodeList;

    this.initWidth = scene.getWidth();
    this.initHeight = scene.getHeight();
    this.ratio = initWidth / initHeight;
  }

  // Getters
  public Scene getScene() {
    return scene;
  }

  public double getRatio() {
    return ratio;
  }

  public double getInitHeight() {
    return initHeight;
  }

  public double getInitWidth() {
    return initWidth;
  }

  public Pane getContentPane() {
    return contentPane;
  }

  public List<Node> getNodeList() {
    return nodeList;
  }
  // -----  ** -------

  @Override
  public void changed(
      ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

    final double newWidth = scene.getWidth();
    final double newHeight = scene.getHeight();

    double scaleFactor =
        newWidth / newHeight > ratio ? newHeight / initHeight : newWidth / initWidth;

    changeChildren(nodeList);

    if (scaleFactor >= 1) {
      Scale scale = new Scale(scaleFactor, scaleFactor);
      scale.setPivotX(0);
      scale.setPivotY(0);
      scene.getRoot().getTransforms().setAll(scale);

      contentPane.setPrefWidth(newWidth / scaleFactor);
      contentPane.setPrefHeight(newHeight / scaleFactor);

    } else {
      contentPane.setPrefWidth(Math.max(initWidth, newWidth));
      contentPane.setPrefHeight(Math.max(initHeight, newHeight));
    }
    //    allChildrenScale(scaleFactor);
  }

  public void allChildrenScale(Double scaleFactor) {
    Scale scale = new Scale(scaleFactor, scaleFactor);
    scale.setPivotX(0);
    scale.setPivotY(0);
    scene.getRoot().getTransforms().setAll(scale);
  }

  // override this method in every class
  public void changeChildren(List<Node> nodeList) {}
}
