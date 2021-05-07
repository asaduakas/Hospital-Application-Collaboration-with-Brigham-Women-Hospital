package edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UsersNodeInfo extends RecursiveTreeObject<UsersNodeInfo> {
  public SimpleStringProperty id;
  public StringProperty password;
  public StringProperty name;
  public StringProperty category;

  public UsersNodeInfo(String id, String password, String name, String category) {
    this.id = new SimpleStringProperty(id);
    this.password = new SimpleStringProperty(password);
    this.name = new SimpleStringProperty(name);
    this.category = new SimpleStringProperty(category);
  }

  public String getId() {
    return id.get();
  }

  public SimpleStringProperty idProperty() {
    return id;
  }

  public void setId(String id) {
    this.id.set(id);
  }

  public String getPassword() {
    return password.get();
  }

  public StringProperty passwordProperty() {
    return password;
  }

  public void setPassword(String password) {
    this.password.set(password);
  }

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public String getCategory() {
    return category.get();
  }

  public StringProperty categoryProperty() {
    return category;
  }

  public void setCategory(String category) {
    this.category.set(category);
  }
}
