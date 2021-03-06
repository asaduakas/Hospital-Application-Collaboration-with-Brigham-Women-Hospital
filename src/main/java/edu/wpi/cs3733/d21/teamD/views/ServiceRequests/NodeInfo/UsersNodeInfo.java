package edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UsersNodeInfo extends RecursiveTreeObject<UsersNodeInfo> {
  public SimpleStringProperty id;
  public StringProperty password;
  public StringProperty name;
  public StringProperty category;
  public StringProperty clearance;
  public StringProperty email;

  public UsersNodeInfo(
      String id, String password, String name, String category, String clearance, String email) {
    this.id = new SimpleStringProperty(id);
    this.password = new SimpleStringProperty(password);
    this.name = new SimpleStringProperty(name);
    this.category = new SimpleStringProperty(category);
    this.clearance = new SimpleStringProperty(clearance);
    this.email = new SimpleStringProperty(email);
  }

  public String getEmail() {
    return email.get();
  }

  public StringProperty emailProperty() {
    return email;
  }

  public void setEmail(String email) {
    this.email.set(email);
  }

  public String getClearance() {
    return clearance.get();
  }

  public StringProperty clearanceProperty() {
    return clearance;
  }

  public void setClearance(String clearance) {
    this.clearance.set(clearance);
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
