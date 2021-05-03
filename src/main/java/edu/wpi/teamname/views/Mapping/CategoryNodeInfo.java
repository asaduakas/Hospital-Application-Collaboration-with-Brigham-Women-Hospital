package edu.wpi.teamname.views.Mapping;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.derby.impl.sql.catalog.SYSTRIGGERSRowFactory;

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
}
