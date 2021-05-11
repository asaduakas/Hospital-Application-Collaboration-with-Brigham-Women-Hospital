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


  public static boolean noAudVisIncomplete =
          FDatabaseTables.getAudVisTable().getIncompleteRequest().isEmpty();
  public static HashMap<Integer, String> audVisList =
          FDatabaseTables.getAudVisTable().getIncompleteRequest();


  public static boolean noComputerServiceIncomplete =
          FDatabaseTables.getCompRequestTable().getIncompleteRequest().isEmpty();
  public static HashMap<Integer, String> compServiceList =
          FDatabaseTables.getCompRequestTable().getIncompleteRequest();


  public static boolean noFacilitiesIncomplete =
          FDatabaseTables.getFacilitiesTable().getIncompleteRequest().isEmpty();
  public static HashMap<Integer, String> facilitiesServiceList =
          FDatabaseTables.getFacilitiesTable().getIncompleteRequest();

  public static boolean noFloralDeliveryIncomplete =
          FDatabaseTables.getFloralDeliveryTable().getIncompleteRequest().isEmpty();
  public static HashMap<Integer, String> floralDeliveryList =
          FDatabaseTables.getFloralDeliveryTable().getIncompleteRequest();

  public static boolean noFoodDeliveryIncomplete =
          FDatabaseTables.getFoodDeliveryTable().getIncompleteRequest().isEmpty();
  public static HashMap<Integer, String> foodDeliveryList =
          FDatabaseTables.getFoodDeliveryTable().getIncompleteRequest();

  public static boolean noInternalTransIncomplete =
          FDatabaseTables.getInternalDeliveryTable().getIncompleteRequest().isEmpty();
  public static HashMap<Integer, String> internalTransList =
          FDatabaseTables.getInternalDeliveryTable().getIncompleteRequest();

  public static boolean noLangInterpIncomplete =
          FDatabaseTables.getLangInterpreterTable().getIncompleteRequest().isEmpty();
  public static HashMap<Integer, String> langInterpList =
          FDatabaseTables.getLangInterpreterTable().getIncompleteRequest();

  public static boolean noLaundryIncomplete =
          FDatabaseTables.getLaundryRequestTable().getIncompleteRequest().isEmpty();
  public static HashMap<Integer, String> laundryList =
          FDatabaseTables.getLaundryRequestTable().getIncompleteRequest();

  public static boolean noMedDeliveryIncomplete =
          FDatabaseTables.getMedDeliveryTable().getIncompleteRequest().isEmpty();
  public static HashMap<Integer, String> medDeliveryList =
          FDatabaseTables.getMedDeliveryTable().getIncompleteRequest();

  public static boolean noSantiServiceIncomplete =
          FDatabaseTables.getSanitationServiceTable().getIncompleteRequest().isEmpty();
  public static HashMap<Integer, String> SantiServiceList =
          FDatabaseTables.getSanitationServiceTable().getIncompleteRequest();

  public static boolean noSecurityServiceIncomplete =
          FDatabaseTables.getSecurityRequestTable().getIncompleteRequest().isEmpty();
  public static HashMap<Integer, String> SecurityServiceList =
          FDatabaseTables.getSecurityRequestTable().getIncompleteRequest();

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    scrollPane.setMaxHeight(400);
    putIntoText();
  }

  public void putIntoText() {
    if (!noExTransIncomplete) {
      for (int i = 0; i < exTransList.size(); i++) {
        Text infoExTrans = new Text(exTransList.get(i));
        textFlow.getChildren().add(infoExTrans);
      }
    }

    if (!noAudVisIncomplete) {
      for (int i = 0; i < audVisList.size(); i++) {
        Text infoAudVis = new Text(audVisList.get(i));
        textFlow.getChildren().add(infoAudVis);
      }
    }


    if (!noComputerServiceIncomplete) {
      for (int i = 0; i < compServiceList.size(); i++) {
        Text infoCompService = new Text(compServiceList.get(i));
        textFlow.getChildren().add(infoCompService);
      }
    }

    if (!noFacilitiesIncomplete) {
      for (int i = 0; i < facilitiesServiceList.size(); i++) {
        Text infoFacilities = new Text(facilitiesServiceList.get(i));
        textFlow.getChildren().add(infoFacilities);
      }
    }






  }
}
