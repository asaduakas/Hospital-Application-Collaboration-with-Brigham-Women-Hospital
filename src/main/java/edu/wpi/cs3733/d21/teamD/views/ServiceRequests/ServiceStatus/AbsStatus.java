package edu.wpi.cs3733.d21.teamD.views.ServiceRequests.ServiceStatus;

import edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo.ExtTransNodeInfo;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class AbsStatus {
  @FXML TableColumn<ExtTransNodeInfo, String> pFNCol;
  @FXML TableColumn<ExtTransNodeInfo, String> pLNCol;
  @FXML TableColumn<ExtTransNodeInfo, String> contactInfoCol;
  @FXML TableColumn<ExtTransNodeInfo, String> locationCol;
}
