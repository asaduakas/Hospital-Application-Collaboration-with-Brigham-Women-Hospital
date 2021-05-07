package edu.wpi.cs3733.d21.teamD.views.Mapping;

import edu.wpi.cs3733.d21.teamD.Astar.Edge;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.shape.Line;

public class EdgeUI {

  private Edge E;
  private Line L;

  // for editing columns
  private SimpleStringProperty simpEdgeID;
  private SimpleStringProperty simpStartShort;
  private SimpleStringProperty simpEndShort;

  public EdgeUI(Edge e, Line l) {
    E = e;
    L = l;
    this.simpEdgeID = new SimpleStringProperty(e.getEdgeID());
    this.simpStartShort = new SimpleStringProperty(e.getStartNodeShortName());
    this.simpEndShort = new SimpleStringProperty(e.getEndNodeShortName());
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

  public String getSimpEdgeID() {
    return simpEdgeID.get();
  }

  public SimpleStringProperty simpEdgeIDProperty() {
    return simpEdgeID;
  }

  public void setSimpEdgeID(String simpEdgeID) {
    this.simpEdgeID.set(simpEdgeID);
  }

  public String getSimpStartShort() {
    return simpStartShort.get();
  }

  public SimpleStringProperty simpStartShortProperty() {
    return simpStartShort;
  }

  public void setSimpStartShort(String simpStartShort) {
    this.simpStartShort.set(simpStartShort);
  }

  public String getSimpEndShort() {
    return simpEndShort.get();
  }

  public SimpleStringProperty simpEndShortProperty() {
    return simpEndShort;
  }

  public void setSimpEndShort(String simpEndShort) {
    this.simpEndShort.set(simpEndShort);
  }
}
