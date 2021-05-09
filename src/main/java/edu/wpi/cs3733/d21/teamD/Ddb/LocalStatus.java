package edu.wpi.cs3733.d21.teamD.Ddb;

public class LocalStatus {
    private String Local;
    private String Status;

    public LocalStatus(String local, String status) {
        Local = local;
        Status = status;
    }

    public String getLocal() {return Local;}

    public void setLocal(String local) {Local = local;}

    public String getStatus() {return Status;}

    public void setStatus(String status) {Status = status;}
}
