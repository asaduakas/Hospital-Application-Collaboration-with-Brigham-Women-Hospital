package edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo;

public class AllServiceNodeInfo {
  public int id;
  public String status;
  public String location;
  public String assignedEmployee;
  public String type;

  public AllServiceNodeInfo(
      int id, String status, String location, String assignedEmployee, String type) {
    this.id = id;
    this.status = status;
    this.location = location;
    this.assignedEmployee = assignedEmployee;
    this.type = type;
  }

  public int getId() {
    return id;
  }

  public String getStatus() {
    return status;
  }

  public String getLocation() {
    return location;
  }

  public String getAssignedEmployee() {
    return assignedEmployee;
  }

  public String getType() {
    return type;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setAssignedEmployee(String assignedEmployee) {
    this.assignedEmployee = assignedEmployee;
  }

  public void setType(String type) {
    this.type = type;
  }
}
