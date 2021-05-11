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
}
