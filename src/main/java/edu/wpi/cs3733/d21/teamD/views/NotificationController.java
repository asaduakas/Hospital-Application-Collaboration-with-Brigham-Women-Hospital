package edu.wpi.cs3733.d21.teamD.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d21.teamD.Ddb.FDatabaseTables;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class NotificationController implements Initializable {

  @FXML private Pane anotherPane;
  @FXML private JFXButton closeBtn;
  @FXML private ScrollPane scrollPane;
  @FXML private TextFlow textFlow;

  public static boolean noExTransIncomplete =
      FDatabaseTables.getExternalTransportTable().getIncompleteRequest().isEmpty();
  public static HashMap<Integer, String> exTransList =
      FDatabaseTables.getExternalTransportTable().getIncompleteRequest();

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    scrollPane.setMaxHeight(400);
    putIntoText();
  }

  public void putIntoText() {
    if (!noExTransIncomplete) {
      for (int i = 0; i < exTransList.size(); i++) {
        Text info = new Text(exTransList.get(i));
        textFlow.getChildren().add(info);
      }
    }
  }
}
