package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.*;
import edu.wpi.teamname.Ddb.FDatabaseTables;
import edu.wpi.teamname.Ddb.GlobalDb;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapDrawerController implements Initializable {
  @FXML private TreeView<String> directoryTreeView;
  @FXML private JFXTextField startField;
  @FXML private JFXTextField endField;
  @FXML private JFXButton findPathButton;
  private ObservableList<CategoryNodeInfo> parkingData =
      FDatabaseTables.getNodeTable().getCategory(GlobalDb.getConnection(), "PAR");
  private ArrayList<String> parkingList =
      FDatabaseTables.getNodeTable().getCategoryTry(GlobalDb.getConnection(), "PARK");

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    TreeItem<String> root = new TreeItem<String>("Directory");
    directoryTreeView.setRoot(root);
    TreeItem<String> parking = new TreeItem<>("Parking");
    TreeItem<String> elevator = new TreeItem<>("Elevator");
    TreeItem<String> restroom = new TreeItem<>("Restroom");
    TreeItem<String> stairs = new TreeItem<>("Stairs");
    TreeItem<String> department = new TreeItem<>("Department");
    TreeItem<String> laboratory = new TreeItem<>("Laboratory");
    TreeItem<String> information = new TreeItem<>("Information");
    TreeItem<String> conference = new TreeItem<>("Conference");
    TreeItem<String> exit = new TreeItem<>("Entrance/Exit");
    TreeItem<String> retail = new TreeItem<>("Retail");
    TreeItem<String> service = new TreeItem<>("Service");

    try {
      ImageView parkImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/parkingpin.png")));
      parkImage.setFitWidth(15);
      parkImage.setFitHeight(15);
      parking.setGraphic(parkImage);

      ImageView elevImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/elevatorpin.png")));
      elevImage.setFitWidth(15);
      elevImage.setFitHeight(15);
      elevator.setGraphic(elevImage);

      ImageView restImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/restroompins.png")));
      restImage.setFitWidth(15);
      restImage.setFitHeight(15);
      restroom.setGraphic(restImage);

      ImageView stairImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/stairspin.png")));
      stairImage.setFitWidth(15);
      stairImage.setFitHeight(15);
      stairs.setGraphic(stairImage);

      ImageView deptImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/deptpins.png")));
      deptImage.setFitWidth(15);
      deptImage.setFitHeight(15);
      department.setGraphic(deptImage);

      ImageView labImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/labspin.png")));
      labImage.setFitWidth(15);
      labImage.setFitHeight(15);
      laboratory.setGraphic(labImage);

      ImageView infoImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/infopin.png")));
      infoImage.setFitWidth(15);
      infoImage.setFitHeight(15);
      information.setGraphic(infoImage);

      ImageView confImage =
          new ImageView(
              new Image(new FileInputStream("src/main/resources/Images/conferencepin.png")));
      confImage.setFitWidth(15);
      confImage.setFitHeight(15);
      conference.setGraphic(confImage);

      ImageView exitImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/exitpin.png")));
      exitImage.setFitWidth(15);
      exitImage.setFitHeight(15);
      exit.setGraphic(exitImage);

      ImageView retailImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/retailpin.png")));
      retailImage.setFitWidth(15);
      retailImage.setFitHeight(15);
      retail.setGraphic(retailImage);

      ImageView servImage =
          new ImageView(new Image(new FileInputStream("src/main/resources/Images/service.png")));
      servImage.setFitWidth(15);
      servImage.setFitHeight(15);
      service.setGraphic(servImage);

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    root.getChildren()
        .addAll(
            parking,
            elevator,
            restroom,
            stairs,
            department,
            laboratory,
            information,
            conference,
            exit,
            retail,
            service);
    for (String parkingSpace : parkingList) {
      TreeItem<String> parkingLocation = new TreeItem<String>(parkingSpace);
      parking.getChildren().add(parkingLocation);
    }

    root.setExpanded(true);
    directoryTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  public void tableSetup() {}
}
