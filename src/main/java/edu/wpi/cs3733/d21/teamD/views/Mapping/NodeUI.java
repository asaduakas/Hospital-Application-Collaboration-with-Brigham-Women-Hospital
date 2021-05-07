package edu.wpi.cs3733.d21.teamD.views.Mapping;

import edu.wpi.cs3733.d21.teamD.Astar.Node;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;

public class NodeUI {
  private Node N;
  private ImageView INode;

  // For UI
  private SimpleDoubleProperty simpXcoord;
  private SimpleDoubleProperty simpYcoord;

  private double sizeHeight;
  private double sizeWidth;

  public NodeUI(Node n, ImageView i, double sizeWidth, double sizeHeight) {
    N = n;
    INode = i;
    this.sizeHeight = sizeHeight;
    this.sizeWidth = sizeWidth;
    this.simpXcoord = new SimpleDoubleProperty(N.getXCoord());
    this.simpYcoord = new SimpleDoubleProperty(N.getYCoord());
  }

  public Node getN() {
    return N;
  }

  public void setN(Node n) {
    N = n;
  }

  public ImageView getI() {
    return INode;
  }

  public void setI(ImageView i) {
    INode = i;
  }

  public double getSimpXcoord() {
    return simpXcoord.get();
  }

  public SimpleDoubleProperty simpXcoordProperty() {
    return simpXcoord;
  }

  public void setSimpXcoord(double simpXcoord) {
    this.simpXcoord.set(simpXcoord);
  }

  public double getSimpYcoord() {
    return simpYcoord.get();
  }

  public SimpleDoubleProperty simpYcoordProperty() {
    return simpYcoord;
  }

  public void setSimpYcoord(double simpYcoord) {
    this.simpYcoord.set(simpYcoord);
  }

  public double getSizeHeight() {
    return sizeHeight;
  }

  public void setSizeHeight(double sizeHeight) {
    this.sizeHeight = sizeHeight;
  }

  public double getSizeWidth() {
    return sizeWidth;
  }

  public void setSizeWidth(double sizeWidth) {
    this.sizeWidth = sizeWidth;
  }

  public void setNodeCoord(int x, int y) {
    N.setCoords(x, y);
    setSimpXcoord(N.getXCoord());
    setSimpYcoord(N.getYCoord());
  }
}
