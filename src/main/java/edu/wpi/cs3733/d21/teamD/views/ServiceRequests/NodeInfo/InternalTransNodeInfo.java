package edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InternalTransNodeInfo extends RecursiveTreeObject<InternalTransNodeInfo> {

  private SimpleStringProperty id = new SimpleStringProperty();
  public StringProperty status = new SimpleStringProperty();
  public StringProperty firstName = new SimpleStringProperty();
  public StringProperty lastName = new SimpleStringProperty();
  public StringProperty contactInfo = new SimpleStringProperty();
  public StringProperty destination = new SimpleStringProperty();
  public StringProperty assignedEmployee = new SimpleStringProperty();
  public StringProperty typeOfTransport = new SimpleStringProperty();

  public InternalTransNodeInfo(
      String id,
      String status,
      String firstName,
      String lastName,
      String contactInfo,
      String destination,
      String assignedEmployee,
      String typeOfTransport) {
    this.id = new SimpleStringProperty(id);
    this.status = new SimpleStringProperty(status);
    this.firstName = new SimpleStringProperty(firstName);
    this.lastName = new SimpleStringProperty(lastName);
    this.contactInfo = new SimpleStringProperty(contactInfo);
    this.destination = new SimpleStringProperty(destination);
    this.assignedEmployee = new SimpleStringProperty(assignedEmployee);
    this.typeOfTransport = new SimpleStringProperty(typeOfTransport);
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

  public String getDestination() {
    return destination.get();
  }

  public StringProperty destinationProperty() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination.set(destination);
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

  public String getTypeOfTransport() {
    return typeOfTransport.get();
  }

  public StringProperty typeOfTransportProperty() {
    return typeOfTransport;
  }

  public void setTypeOfTransport(String typeOfTransport) {
    this.typeOfTransport.set(typeOfTransport);
  }
}
