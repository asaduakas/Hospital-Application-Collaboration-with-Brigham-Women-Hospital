package edu.wpi.teamname.views.Mapping;

import edu.wpi.teamname.Astar.Node;
import javafx.scene.image.ImageView;

public class NodeUI {
  private Node N;
  private ImageView INode;

  public NodeUI(Node n, ImageView i) {
    N = n;
    INode = i;
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
}
