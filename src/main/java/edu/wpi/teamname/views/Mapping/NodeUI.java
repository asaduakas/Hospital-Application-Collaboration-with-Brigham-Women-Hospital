package edu.wpi.teamname.views.Mapping;

import edu.wpi.teamname.Astar.Node;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;

public class NodeUI {
  private Node N;
  private ImageView INode;

  // For UI
  private SimpleDoubleProperty simpXcoord;
  private SimpleDoubleProperty simpYcoord;

  public NodeUI(Node n, ImageView i) {
    N = n;
    INode = i;
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

  public void setNodeCoord(int x, int y){
    N.setCoords(x,y);
    setSimpXcoord(N.getXCoord());
    setSimpYcoord(N.getYCoord());
  }
}
