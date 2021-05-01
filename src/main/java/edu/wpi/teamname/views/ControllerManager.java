package edu.wpi.teamname.views;

import edu.wpi.teamname.App;
import edu.wpi.teamname.views.Access.*;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;

public class ControllerManager {

  private static Popup popup = null;

  private static boolean isPermissible(Class<?> controllerClass) {
    if (AllAccessible.class.isAssignableFrom(controllerClass)){
      return true;
    }
    if (LoginController.getUserCategory() == null) {
      System.out.println("User category is null! Permission not granted. PLEASE FIX");
      return false;
    }
    if (PatientAccessible.class.isAssignableFrom(controllerClass)
        && LoginController.getUserCategory().equalsIgnoreCase("patient")) {
      return true;
    }
    if (EmployeeAccessible.class.isAssignableFrom(controllerClass)
        && LoginController.getUserCategory().equalsIgnoreCase("employee")) {
      return true;
    }
    if (AdminAccessible.class.isAssignableFrom(controllerClass)
        && LoginController.getUserCategory().equalsIgnoreCase("admin")) {
      return true;
    }
    System.out.println("Load of " + controllerClass.getSimpleName() + " impermissible!");
    return false;
  }

  private static FXMLLoader getLoader(String fxmlName){
    try {
      FXMLLoader fxmlLoader =
              new FXMLLoader(ControllerManager.class.getClassLoader().getResource(fxmlName));
      fxmlLoader.load();
      return fxmlLoader;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void attemptLoadPage(String fxmlName) {
    FXMLLoader fxmlLoader = getLoader(fxmlName);
    if (!isPermissible(fxmlLoader.getController().getClass())) return;
    if (popup != null){
      popup.hide();
      popup = null;
    }
    App.getPrimaryStage().setScene(new Scene(fxmlLoader.getRoot()));
    App.getPrimaryStage().show();
  }

  public static void attemptLoadPopup(String fxmlName) {
    FXMLLoader fxmlLoader = getLoader(fxmlName);
    if (!isPermissible(fxmlLoader.getController().getClass())) return;
    if (popup != null) popup.hide();
    popup = new Popup();
    popup.getContent().addAll((Pane) fxmlLoader.getRoot());
    popup.show(App.getPrimaryStage());
  }

}
