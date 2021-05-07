package edu.wpi.cs3733.d21.teamD.views.ServiceRequests.NodeInfo;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class COVIDSurveyResultsNodeInfo extends RecursiveTreeObject<COVIDSurveyResultsNodeInfo> {

  public SimpleStringProperty id = new SimpleStringProperty();
  public StringProperty firstName = new SimpleStringProperty();
  public StringProperty lastName = new SimpleStringProperty();
  public StringProperty contactInfo = new SimpleStringProperty();
  public StringProperty assignedEmployee = new SimpleStringProperty();
  public StringProperty status = new SimpleStringProperty();

  public StringProperty positiveTestCheck = new SimpleStringProperty();
  public StringProperty symptomCheck = new SimpleStringProperty();
  public StringProperty closeContactCheck = new SimpleStringProperty();
  public StringProperty selfIsolateCheck = new SimpleStringProperty();
  public StringProperty feelGoodCheck = new SimpleStringProperty();

  //    public IntegerProperty positiveTestCheck = new SimpleIntegerProperty();
  //    public IntegerProperty symptomCheck = new SimpleIntegerProperty();
  //    public IntegerProperty closeContactCheck = new SimpleIntegerProperty();
  //    public IntegerProperty selfIsolateCheck = new SimpleIntegerProperty();
  //    public IntegerProperty feelGoodCheck = new SimpleIntegerProperty();

  public COVIDSurveyResultsNodeInfo(
      String id,
      String firstName,
      String lastName,
      String contactInfo,
      String assignedEmployee,
      String status,
      String positiveTestCheck,
      String symptomCheck,
      String closeContactCheck,
      String selfIsolateCheck,
      String feelGoodCheck) {
    this.id = new SimpleStringProperty(id);
    this.firstName = new SimpleStringProperty(firstName);
    this.lastName = new SimpleStringProperty(lastName);
    this.contactInfo = new SimpleStringProperty(contactInfo);
    this.assignedEmployee = new SimpleStringProperty(assignedEmployee);
    this.status = new SimpleStringProperty(status);
    this.positiveTestCheck = new SimpleStringProperty(positiveTestCheck);
    this.symptomCheck = new SimpleStringProperty(symptomCheck);
    this.closeContactCheck = new SimpleStringProperty(closeContactCheck);
    this.selfIsolateCheck = new SimpleStringProperty(selfIsolateCheck);
    this.feelGoodCheck = new SimpleStringProperty(feelGoodCheck);

    //        this.positiveTestCheck = new SimpleIntegerProperty(positiveTestCheck);
    //        this.symptomCheck = new SimpleIntegerProperty(symptomCheck);
    //        this.closeContactCheck = new SimpleIntegerProperty(closeContactCheck);
    //        this.selfIsolateCheck = new SimpleIntegerProperty(selfIsolateCheck);
    //        this.feelGoodCheck = new SimpleIntegerProperty(feelGoodCheck);
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

  public String getAssignedEmployee() {
    return assignedEmployee.get();
  }

  public StringProperty assignedEmployeeProperty() {
    return assignedEmployee;
  }

  public void setAssignedEmployee(String assignedEmployee) {
    this.assignedEmployee.set(assignedEmployee);
  }

  public String getPositiveTestCheck() {
    return positiveTestCheck.get();
  }

  public StringProperty positiveTestCheckProperty() {
    return positiveTestCheck;
  }

  public void setPositiveTestCheck(String positiveTestCheck) {
    this.positiveTestCheck.set(positiveTestCheck);
  }

  public String getSymptomCheck() {
    return symptomCheck.get();
  }

  public StringProperty symptomCheckProperty() {
    return symptomCheck;
  }

  public void setSymptomCheck(String symptomCheck) {
    this.symptomCheck.set(symptomCheck);
  }

  public String getCloseContactCheck() {
    return closeContactCheck.get();
  }

  public StringProperty closeContactCheckProperty() {
    return closeContactCheck;
  }

  public void setCloseContactCheck(String closeContactCheck) {
    this.closeContactCheck.set(closeContactCheck);
  }

  public String getSelfIsolateCheck() {
    return selfIsolateCheck.get();
  }

  public StringProperty selfIsolateCheckProperty() {
    return selfIsolateCheck;
  }

  public void setSelfIsolateCheck(String selfIsolateCheck) {
    this.selfIsolateCheck.set(selfIsolateCheck);
  }

  public String getFeelGoodCheck() {
    return feelGoodCheck.get();
  }

  public StringProperty feelGoodCheckProperty() {
    return feelGoodCheck;
  }

  public void setFeelGoodCheck(String feelGoodCheck) {
    this.feelGoodCheck.set(feelGoodCheck);
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
}

  //    public int getPositiveTestCheck() {
  //        return positiveTestCheck.get();
  //    }
  //
  //    public IntegerProperty positiveTestCheckProperty() {
  //        return positiveTestCheck;
  //    }
  //
  //    public void setPositiveTestCheck(int positiveTestCheck) {
  //        this.positiveTestCheck.set(positiveTestCheck);
  //    }
  //
  //    public int getSymptomCheck() {
  //        return symptomCheck.get();
  //    }
  //
  //    public IntegerProperty symptomCheckProperty() {
  //        return symptomCheck;
  //    }
  //
  //    public void setSymptomCheck(int symptomCheck) {
  //        this.symptomCheck.set(symptomCheck);
  //    }
  //
  //    public int getCloseContactCheck() {
  //        return closeContactCheck.get();
  //    }
  //
  //    public IntegerProperty closeContactCheckProperty() {
  //        return closeContactCheck;
  //    }
  //
  //    public void setCloseContactCheck(int closeContactCheck) {
  //        this.closeContactCheck.set(closeContactCheck);
  //    }
  //
  //    public int getSelfIsolateCheck() {
  //        return selfIsolateCheck.get();
  //    }
  //
  //    public IntegerProperty selfIsolateCheckProperty() {
  //        return selfIsolateCheck;
  //    }
  //
  //    public void setSelfIsolateCheck(int selfIsolateCheck) {
  //        this.selfIsolateCheck.set(selfIsolateCheck);
  //    }
  //
  //    public int getFeelGoodCheck() {
  //        return feelGoodCheck.get();
  //    }
  //
  //    public IntegerProperty feelGoodCheckProperty() {
  //        return feelGoodCheck;
  //    }
  //
  //    public void setFeelGoodCheck(int feelGoodCheck) {
  //        this.feelGoodCheck.set(feelGoodCheck);
  //    }
