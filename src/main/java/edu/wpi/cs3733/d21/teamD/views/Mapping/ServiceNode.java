package edu.wpi.cs3733.d21.teamD.views.Mapping;

import edu.wpi.cs3733.d21.teamD.Astar.Node;
import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.AllServiceNodeInfo;
import javafx.scene.image.ImageView;

public class ServiceNode {
  private Node N;
  private ImageView Serivce;
  private AllServiceNodeInfo Info;

  public ServiceNode(Node n, ImageView serivce, AllServiceNodeInfo info) {
    N = n;
    Serivce = serivce;
    Info = info;
  }

  public Node getN() {
    return N;
  }

  public void setN(Node n) {
    N = n;
  }

  public AllServiceNodeInfo getInfo() {
    return Info;
  }

  public void setInfo(AllServiceNodeInfo info) {
    Info = info;
  }

  public ImageView getSerivce() {
    return Serivce;
  }

  public void setSerivce(ImageView serivce) {
    Serivce = serivce;
  }
}
