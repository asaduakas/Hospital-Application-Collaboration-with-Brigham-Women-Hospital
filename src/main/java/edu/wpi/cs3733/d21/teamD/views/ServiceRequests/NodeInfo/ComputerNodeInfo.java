package edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ComputerNodeInfo extends RecursiveTreeObject<ComputerNodeInfo> {

  private SimpleStringProperty id = new SimpleStringProperty();
  public StringProperty status = new SimpleStringProperty();
  public StringProperty firstName = new SimpleStringProperty();
  public StringProperty lastName = new SimpleStringProperty();
  public StringProperty contactInfo = new SimpleStringProperty();
  public StringProperty location = new SimpleStringProperty();
  public StringProperty assignedEmployee = new SimpleStringProperty();
  public StringProperty descriptionOfIssue = new SimpleStringProperty();

  public ComputerNodeInfo(
      String id,
      String status,
      String firstName,
      String lastName,
      String contactInfo,
      String location,
      String assignedEmployee,
      String descriptionOfIssue) {
    this.id = new SimpleStringProperty(id);
    this.status = new SimpleStringProperty(status);
    this.firstName = new SimpleStringProperty(firstName);
    this.lastName = new SimpleStringProperty(lastName);
    this.contactInfo = new SimpleStringProperty(contactInfo);
    this.location = new SimpleStringProperty(location);
    this.assignedEmployee = new SimpleStringProperty(assignedEmployee);
    this.descriptionOfIssue = new SimpleStringProperty(descriptionOfIssue);
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

  public String getFirstName() {
    return firstName.get();
  }

  public StringProperty firstNameProperty() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName.set(firstName);
  }

  public String getLastName() {
    return lastName.get();
  }

  public StringProperty lastNameProperty() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName.set(lastName);
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

  public String getAssignedEmployee() {
    return assignedEmployee.get();
  }

  public StringProperty assignedEmployeeProperty() {
    return assignedEmployee;
  }

  public void setAssignedEmployee(String assignedEmployee) {
    this.assignedEmployee.set(assignedEmployee);
  }

  public String getDescriptionOfIssue() {
    return descriptionOfIssue.get();
  }

  public StringProperty descriptionOfIssueProperty() {
    return descriptionOfIssue;
  }

  public void setDescriptionOfIssue(String descriptionOfIssue) {
    this.descriptionOfIssue.set(descriptionOfIssue);
  }
}