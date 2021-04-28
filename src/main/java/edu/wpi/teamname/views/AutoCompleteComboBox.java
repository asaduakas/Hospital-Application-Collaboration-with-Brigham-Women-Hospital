package edu.wpi.teamname.views;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.*;

public class AutoCompleteComboBox {

  private final SortedSet<String> entries;
  private ContextMenu entriesPopup;
  public ComboBox<String> box;

  public AutoCompleteComboBox(ComboBox<String> box) {
    this.box = box;
    entries = new TreeSet<>();
    entriesPopup = new ContextMenu();
    box.valueProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observableValue, String s, String s2) {
                if (box.getValue().toString().length() == 0) {
                  entriesPopup.hide();
                } else {
                  LinkedList<String> searchResult = new LinkedList<>();

                  final List<String> filteredEntries =
                      entries.stream()
                          .filter(e -> e.toLowerCase().contains(box.getValue().toLowerCase()))
                          .collect(Collectors.toList());
                  searchResult.addAll(filteredEntries);

                  // Code for having autocomplete only look for exact matches instead of also doing
                  // substrings
                  // searchResult.addAll(entries.subSet(box.getValue().toString(),box.getValue().toString() + Character.MAX_VALUE));
                  if (entries.size() > 0) {
                    populatePopup(searchResult);
                    if (!entriesPopup.isShowing()) {
                      entriesPopup.show(box, Side.BOTTOM, 0, 0);
                    }
                  } else {
                    entriesPopup.hide();
                  }
                }
              }
            });

    box.focusedProperty()
        .addListener(
            new ChangeListener<Boolean>() {
              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observableValue,
                  Boolean aBoolean,
                  Boolean aBoolean2) {
                entriesPopup.hide();
              }
            });
  }

  public SortedSet<String> getEntries() {
    return entries;
  }

  private void populatePopup(List<String> searchResult) {
    List<CustomMenuItem> menuItems = new LinkedList<>();
    // If you'd like more entries, modify this line.
    int maxEntries = 10;
    int count = Math.min(searchResult.size(), maxEntries);
    for (int i = 0; i < count; i++) {
      final String result = searchResult.get(i);
      Label entryLabel = new Label(result);
      CustomMenuItem item = new CustomMenuItem(entryLabel, true);
      item.setOnAction(
          new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
              box.setValue(result);
              entriesPopup.hide();
            }
          });
      menuItems.add(item);
    }
    entriesPopup.getItems().clear();
    entriesPopup.getItems().addAll(menuItems);
  }
}
