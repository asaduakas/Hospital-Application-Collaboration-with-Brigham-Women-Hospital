package edu.wpi.teamname.views.ServiceRequests.NodeInfo;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FloralDelivNodeInfo extends RecursiveTreeObject<FloralDelivNodeInfo> {

  private SimpleStringProperty id = new SimpleStringProperty();
  public StringProperty status = new SimpleStringProperty();
  public StringProperty pFirstName = new SimpleStringProperty();
  public StringProperty pLastName = new SimpleStringProperty();
  public StringProperty contactInfo = new SimpleStringProperty();
  public StringProperty location = new SimpleStringProperty();
  public StringProperty typeOfFlower = new SimpleStringProperty();
  public StringProperty numOfFlower = new SimpleStringProperty();
  public StringProperty fromFlower = new SimpleStringProperty();
  public StringProperty assignedEmployee = new SimpleStringProperty();

  public FloralDelivNodeInfo(
      String id,
      String status,
      String pFirstName,
      String pLastName,
      String contactInfo,
      String location,
      String typeOfFlower,
      String numOfFlower,
      String fromFlower,
      String assignedEmployee) {
    this.id = new SimpleStringProperty(id);
    this.status = new SimpleStringProperty(status);
    this.pFirstName = new SimpleStringProperty(pFirstName);
    this.pLastName = new SimpleStringProperty(pLastName);
    this.contactInfo = new SimpleStringProperty(contactInfo);
    this.location = new SimpleStringProperty(location);
    this.typeOfFlower = new SimpleStringProperty(typeOfFlower);
    this.numOfFlower = new SimpleStringProperty(numOfFlower);
    this.fromFlower = new SimpleStringProperty(fromFlower);
    this.assignedEmployee = new SimpleStringProperty(assignedEmployee);
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

  public String getStatus() {
    return status.get();
  }

  public StringProperty statusProperty() {
    return status;
  }

  public void setStatus(String status) {
    this.status.set(status);
  }

  public String getpFirstName() {
    return pFirstName.get();
  }

  public StringProperty pFirstNameProperty() {
    return pFirstName;
  }

  public void setpFirstName(String pFirstName) {
    this.pFirstName.set(pFirstName);
  }

  public String getpLastName() {
    return pLastName.get();
  }

  public StringProperty pLastNameProperty() {
    return pLastName;
  }

  public void setpLastName(String pLastName) {
    this.pLastName.set(pLastName);
  }

  public String getContactInfo() {
    return contactInfo.get();
  }

  public StringProperty contactInfoProperty() {
    return contactInfo;
  }

  public void setContactInfo(String contactInfo) {
    this.contactInfo.set(contactInfo);
  }

  public String getLocation() {
    return location.get();
  }

  public StringProperty locationProperty() {
    return location;
  }

  public void setLocation(String location) {
    this.location.set(location);
  }

  public String getTypeOfFlower() {
    return typeOfFlower.get();
  }

  public StringProperty typeOfFlowerProperty() {
    return typeOfFlower;
  }

  public void setTypeOfFlower(String typeOfFlower) {
    this.typeOfFlower.set(typeOfFlower);
  }

  public String getNumOfFlower() {
    return numOfFlower.get();
  }

  public StringProperty numOfFlowerProperty() {
    return numOfFlower;
  }

  public void setNumOfFlower(String numOfFlower) {
    this.numOfFlower.set(numOfFlower);
  }

  public String getFromFlower() {
    return fromFlower.get();
  }

  public StringProperty fromFlowerProperty() {
    return fromFlower;
  }

  public void setFromFlower(String fromFlower) {
    this.fromFlower.set(fromFlower);
  }

  public String getAssignedEmployee() {
    return assignedEmployee.get();
  }

  public StringProperty assignedEmployeeProperty() {
    return assignedEmployee;
  }

  public void setAssignedEmployee(String assignedEmployee) {
    this.assignedEmployee.set(assignedEmployee);
  }
}
