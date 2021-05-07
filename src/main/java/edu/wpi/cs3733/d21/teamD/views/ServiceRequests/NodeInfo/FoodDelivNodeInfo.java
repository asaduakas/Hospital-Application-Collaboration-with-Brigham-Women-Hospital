package edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FoodDelivNodeInfo extends RecursiveTreeObject<FoodDelivNodeInfo> {
  private SimpleStringProperty id = new SimpleStringProperty();
  public StringProperty serviceType = new SimpleStringProperty();
  public StringProperty pFirstName = new SimpleStringProperty();
  public StringProperty pLastName = new SimpleStringProperty();
  public StringProperty contactInfo = new SimpleStringProperty();
  public StringProperty location = new SimpleStringProperty();
  public StringProperty specialNeeds = new SimpleStringProperty();
  public StringProperty assignedEmployee = new SimpleStringProperty();
  public StringProperty status = new SimpleStringProperty();
  public StringProperty createTime = new SimpleStringProperty();
  public StringProperty createDate = new SimpleStringProperty();

  public FoodDelivNodeInfo(
      String id,
      String status,
      String pFirstName,
      String pLastName,
      String contactInfo,
      String location,
      String assignedEmployee,
      String specialNeeds) {
    this.id = new SimpleStringProperty(id);
    this.pFirstName = new SimpleStringProperty(pFirstName);
    this.pLastName = new SimpleStringProperty(pLastName);
    this.contactInfo = new SimpleStringProperty(contactInfo);
    this.location = new SimpleStringProperty(location);
    this.specialNeeds = new SimpleStringProperty(specialNeeds);
    this.assignedEmployee = new SimpleStringProperty(assignedEmployee);
    this.status = new SimpleStringProperty(status);
  }

  public String getId() {
    return id.get();
  }

  public void setId(String id) {
    this.id = new SimpleStringProperty(id);
  }

  public String getServiceType() {
    return serviceType.get();
  }

  public void setServiceType(String serviceType) {
    this.serviceType = new SimpleStringProperty(serviceType);
  }

  public String getpLastName() {
    return pLastName.get();
  }

  public void setpLastName(String pLastName) {
    this.pLastName = new SimpleStringProperty(pLastName);
  }

  public String getContactInfo() {
    return contactInfo.get();
  }

  public void setContactInfo(String contactInfo) {
    this.contactInfo = new SimpleStringProperty(contactInfo);
  }

  public String getStatus() {
    return status.get();
  }

  public void setStatus(String status) {
    this.status = new SimpleStringProperty(status);
  }

  public String getpFirstName() {
    return pFirstName.get();
  }

  public void setpFirstName(String pFirstName) {
    this.pFirstName.set(pFirstName);
  }

  public String getSpecialNeeds() {
    return specialNeeds.get();
  }

  public void setSpecialNeeds(String specialNeeds) {
    this.specialNeeds.set(specialNeeds);
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

  public String getCreateTime() {
    return createTime.get();
  }

  public StringProperty createTimeProperty() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime.set(createTime);
  }

  public String getCreateDate() {
    return createDate.get();
  }

  public StringProperty createDateProperty() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate.set(createDate);
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
}
