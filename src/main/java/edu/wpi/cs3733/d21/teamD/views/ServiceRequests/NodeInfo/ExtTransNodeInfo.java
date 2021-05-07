package edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ExtTransNodeInfo extends RecursiveTreeObject<ExtTransNodeInfo> {

  private SimpleStringProperty id = new SimpleStringProperty();
  public StringProperty serviceType = new SimpleStringProperty();
  public StringProperty pFirstName = new SimpleStringProperty();
  public StringProperty pLastName = new SimpleStringProperty();
  public StringProperty contactInfo = new SimpleStringProperty();
  public StringProperty location = new SimpleStringProperty();
  public StringProperty transType = new SimpleStringProperty();
  public StringProperty assignedTo = new SimpleStringProperty();
  public StringProperty status = new SimpleStringProperty();

  public SimpleStringProperty idProperty() {
    return id;
  }

  public ExtTransNodeInfo(
      String id,
      String serviceType,
      String pFirstName,
      String pLastName,
      String contactInfo,
      String location,
      String transType,
      String assignedTo,
      String status) {
    this.id = new SimpleStringProperty(id);
    this.serviceType = new SimpleStringProperty(serviceType);
    this.pFirstName = new SimpleStringProperty(pFirstName);
    this.pLastName = new SimpleStringProperty(pLastName);
    this.contactInfo = new SimpleStringProperty(contactInfo);
    this.location = new SimpleStringProperty(location);
    this.transType = new SimpleStringProperty(transType);
    this.assignedTo = new SimpleStringProperty(assignedTo);
    this.status = new SimpleStringProperty(status);
  }

  public void setId(String id) {
    this.id.set(id);
  }

  public void setServiceType(String serviceType) {
    // this.serviceType = new SimpleStringProperty(serviceType);
    this.serviceType.set(serviceType);
  }

  public void setPFirstName(String pFirstName) {
    this.pFirstName.set(pFirstName);
  }

  public void setPLastName(String pLastName) {
    this.pLastName = new SimpleStringProperty(pLastName);
    // this.pLastName.set(pLastName);
  }

  public void setContactInfo(String contactInfo) {
    // this.contactInfo = new SimpleStringProperty(contactInfo);
    this.contactInfo.set(contactInfo);
  }

  public void setLocation(String location) {
    // this.location = new SimpleStringProperty(location);
    this.location.set(location);
  }

  public void setTransType(String transType) {
    this.transType.set(transType);
  }

  public void setAssignedTo(String assignedTo) {
    this.assignedTo.set(assignedTo);
  }

  public void setStatus(String status) {
    this.status.set(status);
  }

  public String getId() {
    return id.get();
  }

  public String getServiceType() {
    return serviceType.get();
  }

  public String getPFirstName() {
    return pFirstName.get();
  }

  public String getPLastName() {
    return pLastName.get();
  }

  public String getContactInfo() {
    return contactInfo.get();
  }

  public String getLocation() {
    return location.get();
  }

  public String getTransType() {
    return transType.get();
  }

  public String getAssignedTo() {
    return assignedTo.get();
  }

  public String getStatus() {
    return status.get();
  }
}
