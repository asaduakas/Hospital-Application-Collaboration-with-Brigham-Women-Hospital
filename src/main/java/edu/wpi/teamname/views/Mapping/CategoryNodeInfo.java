package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CategoryNodeInfo extends RecursiveTreeObject<CategoryNodeInfo> {
  public StringProperty NodeType = new SimpleStringProperty();
  public StringProperty Dept = new SimpleStringProperty();
  public StringProperty Elev = new SimpleStringProperty();
  public StringProperty InfoType = new SimpleStringProperty();
  public StringProperty EXIT = new SimpleStringProperty();
  public StringProperty Labs = new SimpleStringProperty();
  public StringProperty Rest = new SimpleStringProperty();
  public StringProperty Serv = new SimpleStringProperty();
  public StringProperty Conf = new SimpleStringProperty();
  public StringProperty RetL = new SimpleStringProperty();
  public StringProperty Stai = new SimpleStringProperty();

  public CategoryNodeInfo(String nt) {
    this.NodeType = new SimpleStringProperty(nt);
    //        this.Dept = new SimpleStringProperty(dept);
    //        this.Elev = new SimpleStringProperty(elev);
    //        this.InfoType = new SimpleStringProperty(info);
    //        this.EXIT = new SimpleStringProperty(exit);
    //        this.Labs = new SimpleStringProperty(labs);
    //        this.Rest = new SimpleStringProperty(rest);
    //        this.Serv = new SimpleStringProperty(serv);
    //        this.Conf = new SimpleStringProperty(conf);
    //        this.RetL = new SimpleStringProperty(retL);
    //        this.Stai = new SimpleStringProperty(stai);
  }

  public String getNodeType() {
    return NodeType.get();
  }

  public StringProperty nodeTypeProperty() {
    return NodeType;
  }

  public void setNodeType(String nodeType) {
    this.NodeType.set(nodeType);
  }

  public String getDept() {
    return Dept.get();
  }

  public StringProperty deptProperty() {
    return Dept;
  }

  public void setDept(String dept) {
    this.Dept.set(dept);
  }

  public String getElev() {
    return Elev.get();
  }

  public StringProperty elevProperty() {
    return Elev;
  }

  public void setElev(String elev) {
    this.Elev.set(elev);
  }

  public String getInfoType() {
    return InfoType.get();
  }

  public StringProperty infoTypeProperty() {
    return InfoType;
  }

  public void setInfoType(String infoType) {
    this.InfoType.set(infoType);
  }

  public String getEXIT() {
    return EXIT.get();
  }

  public StringProperty EXITProperty() {
    return EXIT;
  }

  public void setEXIT(String EXIT) {
    this.EXIT.set(EXIT);
  }

  public String getLabs() {
    return Labs.get();
  }

  public StringProperty labsProperty() {
    return Labs;
  }

  public void setLabs(String labs) {
    this.Labs.set(labs);
  }

  public String getRest() {
    return Rest.get();
  }

  public StringProperty restProperty() {
    return Rest;
  }

  public void setRest(String rest) {
    this.Rest.set(rest);
  }

  public String getServ() {
    return Serv.get();
  }

  public StringProperty servProperty() {
    return Serv;
  }

  public void setServ(String serv) {
    this.Serv.set(serv);
  }

  public String getConf() {
    return Conf.get();
  }

  public StringProperty confProperty() {
    return Conf;
  }

  public void setConf(String conf) {
    this.Conf.set(conf);
  }

  public String getRetL() {
    return RetL.get();
  }

  public StringProperty retLProperty() {
    return RetL;
  }

  public void setRetL(String retL) {
    this.RetL.set(retL);
  }

  public String getStai() {
    return Stai.get();
  }

  public StringProperty staiProperty() {
    return Stai;
  }

  public void setStai(String stai) {
    this.Stai.set(stai);
  }
}
