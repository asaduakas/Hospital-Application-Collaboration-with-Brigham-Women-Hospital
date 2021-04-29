package edu.wpi.teamname.views.Mapping;

import edu.wpi.teamname.Astar.Edge;
import javafx.scene.shape.Line;

public class EdgeUI {

  private Edge E;
  private Line L;

  public EdgeUI(Edge e, Line l) {
    E = e;
    L = l;
  }

  public Edge getE() {
    return E;
  }

  public void setE(Edge e) {
    E = e;
  }

  public Line getL() {
    return L;
  }

  public void setL(Line l) {
    L = l;
  }
}
