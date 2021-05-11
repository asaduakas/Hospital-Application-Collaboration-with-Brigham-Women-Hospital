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
  public static int totalCount;

  public static boolean noExTransIncomplete;
  public static HashMap<Integer, String> exTransList;

  public static boolean noAudVisIncomplete;
  public static HashMap<Integer, String> audVisList;

  public static boolean noComputerServiceIncomplete;
  public static HashMap<Integer, String> compServiceList;

  public static boolean noFacilitiesIncomplete;
  public static HashMap<Integer, String> facilitiesServiceList;

  public static boolean noFloralDeliveryIncomplete;
  public static HashMap<Integer, String> floralDeliveryList;

  public static boolean noFoodDeliveryIncomplete;
  public static HashMap<Integer, String> foodDeliveryList;

  public static boolean noInternalTransIncomplete;
  public static HashMap<Integer, String> internalTransList;

  public static boolean noLangInterpIncomplete;
  public static HashMap<Integer, String> langInterpList;

  public static boolean noLaundryIncomplete;
  public static HashMap<Integer, String> laundryList;

  public static boolean noMedDeliveryIncomplete;
  public static HashMap<Integer, String> medDeliveryList;

  public static boolean noSantiServiceIncomplete;
  public static HashMap<Integer, String> SantiServiceList;

  public static boolean noSecurityServiceIncomplete;
  public static HashMap<Integer, String> SecurityServiceList;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    noExTransIncomplete =
        FDatabaseTables.getExternalTransportTable().getIncompleteRequest().isEmpty();
    noAudVisIncomplete = FDatabaseTables.getAudVisTable().getIncompleteRequest().isEmpty();
    noComputerServiceIncomplete =
        FDatabaseTables.getCompRequestTable().getIncompleteRequest().isEmpty();
    noFacilitiesIncomplete = FDatabaseTables.getFacilitiesTable().getIncompleteRequest().isEmpty();
    noFloralDeliveryIncomplete =
        FDatabaseTables.getFloralDeliveryTable().getIncompleteRequest().isEmpty();
    noFoodDeliveryIncomplete =
        FDatabaseTables.getFoodDeliveryTable().getIncompleteRequest().isEmpty();
    noInternalTransIncomplete =
        FDatabaseTables.getInternalDeliveryTable().getIncompleteRequest().isEmpty();
    noLangInterpIncomplete =
        FDatabaseTables.getLangInterpreterTable().getIncompleteRequest().isEmpty();
    noLaundryIncomplete = FDatabaseTables.getLaundryRequestTable().getIncompleteRequest().isEmpty();
    noMedDeliveryIncomplete =
        FDatabaseTables.getMedDeliveryTable().getIncompleteRequest().isEmpty();
    noSantiServiceIncomplete =
        FDatabaseTables.getSanitationServiceTable().getIncompleteRequest().isEmpty();
    noSecurityServiceIncomplete =
        FDatabaseTables.getSecurityRequestTable().getIncompleteRequest().isEmpty();

    exTransList = FDatabaseTables.getExternalTransportTable().getIncompleteRequest();
    audVisList = FDatabaseTables.getAudVisTable().getIncompleteRequest();
    compServiceList = FDatabaseTables.getCompRequestTable().getIncompleteRequest();
    facilitiesServiceList = FDatabaseTables.getFacilitiesTable().getIncompleteRequest();
    floralDeliveryList = FDatabaseTables.getFloralDeliveryTable().getIncompleteRequest();
    foodDeliveryList = FDatabaseTables.getFoodDeliveryTable().getIncompleteRequest();
    internalTransList = FDatabaseTables.getInternalDeliveryTable().getIncompleteRequest();
    langInterpList = FDatabaseTables.getLangInterpreterTable().getIncompleteRequest();
    laundryList = FDatabaseTables.getLaundryRequestTable().getIncompleteRequest();
    medDeliveryList = FDatabaseTables.getMedDeliveryTable().getIncompleteRequest();
    SantiServiceList = FDatabaseTables.getSanitationServiceTable().getIncompleteRequest();
    SecurityServiceList = FDatabaseTables.getSecurityRequestTable().getIncompleteRequest();

    textFlow.getChildren().clear();
    scrollPane.setPrefHeight(400);
    putIntoText();

    totalCount =
        exTransList.size()
            + audVisList.size()
            + compServiceList.size()
            + facilitiesServiceList.size()
            + floralDeliveryList.size()
            + foodDeliveryList.size()
            + internalTransList.size()
            + langInterpList.size()
            + laundryList.size()
            + medDeliveryList.size()
            + SantiServiceList.size()
            + SecurityServiceList.size();
  }

  public void putIntoText() {
    if (!noExTransIncomplete) {
      for (int i = 0; i < exTransList.size(); i++) {
        Text infoExTrans = new Text("\t" + exTransList.get(i) + "\n");
        textFlow.getChildren().add(infoExTrans);
      }
    }

    if (!noAudVisIncomplete) {
      for (int i = 0; i < audVisList.size(); i++) {
        Text infoAudVis = new Text("\t" + audVisList.get(i) + "\n");
        textFlow.getChildren().add(infoAudVis);
      }
    }

    if (!noComputerServiceIncomplete) {
      for (int i = 0; i < compServiceList.size(); i++) {
        Text infoCompService = new Text("\t" + compServiceList.get(i) + "\n");
        textFlow.getChildren().add(infoCompService);
      }
    }

    if (!noFacilitiesIncomplete) {
      for (int i = 0; i < facilitiesServiceList.size(); i++) {
        Text infoFacilities = new Text("\t" + facilitiesServiceList.get(i) + "\n");
        textFlow.getChildren().add(infoFacilities);
      }
    }

    if (!noFloralDeliveryIncomplete) {
      for (int i = 0; i < floralDeliveryList.size(); i++) {
        Text infoFloralDelivery = new Text("\t" + floralDeliveryList.get(i) + "\n");
        textFlow.getChildren().add(infoFloralDelivery);
      }
    }

    if (!noFoodDeliveryIncomplete) {
      for (int i = 0; i < foodDeliveryList.size(); i++) {
        Text infoFoodDelivery = new Text("\t" + foodDeliveryList.get(i) + "\n");
        textFlow.getChildren().add(infoFoodDelivery);
      }
    }

    if (!noInternalTransIncomplete) {
      for (int i = 0; i < internalTransList.size(); i++) {
        Text infoInternalTrans = new Text("\t" + internalTransList.get(i) + "\n");
        textFlow.getChildren().add(infoInternalTrans);
      }
    }

    if (!noLangInterpIncomplete) {
      for (int i = 0; i < langInterpList.size(); i++) {
        Text infoLangInterp = new Text("\t" + langInterpList.get(i) + "\n");
        textFlow.getChildren().add(infoLangInterp);
      }
    }

    if (!noLaundryIncomplete) {
      for (int i = 0; i < laundryList.size(); i++) {
        Text infoLaundry = new Text("\t" + laundryList.get(i) + "\n");
        textFlow.getChildren().add(infoLaundry);
      }
    }

    if (!noMedDeliveryIncomplete) {
      for (int i = 0; i < medDeliveryList.size(); i++) {
        Text infoMedDelivery = new Text("\t" + medDeliveryList.get(i) + "\n");
        textFlow.getChildren().add(infoMedDelivery);
      }
    }

    if (!noSantiServiceIncomplete) {
      for (int i = 0; i < SantiServiceList.size(); i++) {
        Text infoSantiService = new Text("\t" + SantiServiceList.get(i) + "\n");
        textFlow.getChildren().add(infoSantiService);
      }
    }

    if (!noSecurityServiceIncomplete) {
      for (int i = 0; i < SecurityServiceList.size(); i++) {
        Text infoSecurity = new Text("\t" + SecurityServiceList.get(i) + "\n");
        textFlow.getChildren().add(infoSecurity);
      }
    }
  }
}
