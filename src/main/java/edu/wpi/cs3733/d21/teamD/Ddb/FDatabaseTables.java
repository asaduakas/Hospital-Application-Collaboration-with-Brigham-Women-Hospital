package edu.wpi.cs3733.d21.teamD.Ddb;

public class FDatabaseTables {

  private static AudVisRequestTable audVisTable;
  private static ComputerRequestTable compRequestTable;
  private static EdgesTable edgeTable;
  private static ExtTransRequestTable externalTransportTable;
  private static FacilitiesRequestTable facilitiesTable;
  private static FloralDeliveryRequestTable floralDeliveryTable;
  private static FoodDeliveryRequestTable foodDeliveryTable;
  private static InternalTransRequestTable internalDeliveryTable;
  private static LangInterpreterRequestTable langInterpreterTable;
  private static LaundryRequestTable laundryRequestTable;
  private static MedDeliveryRequestTable medDeliveryTable;
  private static NodesTable nodeTable;
  private static UsersTable userTable;
  private static SearchHistoryTable searchHistoryTable;
  private static SanitationServRequestTable sanitationServiceTable;
  private static SecurityRequestTable securityRequestTable;
  private static COVID19SurveyTable covid19SurveyTable;
  private static EmployeeTable employeeTable;

  public FDatabaseTables() {
    this.audVisTable = new AudVisRequestTable();
    this.compRequestTable = new ComputerRequestTable();
    this.edgeTable = new EdgesTable();
    this.externalTransportTable = new ExtTransRequestTable();
    this.facilitiesTable = new FacilitiesRequestTable();
    this.floralDeliveryTable = new FloralDeliveryRequestTable();
    this.foodDeliveryTable = new FoodDeliveryRequestTable();
    this.internalDeliveryTable = new InternalTransRequestTable();
    this.langInterpreterTable = new LangInterpreterRequestTable();
    this.laundryRequestTable = new LaundryRequestTable();
    this.medDeliveryTable = new MedDeliveryRequestTable();
    this.nodeTable = new NodesTable();
    this.userTable = new UsersTable();
    this.searchHistoryTable = new SearchHistoryTable();
    this.sanitationServiceTable = new SanitationServRequestTable();
    this.securityRequestTable = new SecurityRequestTable();
    this.covid19SurveyTable = new COVID19SurveyTable();
    this.employeeTable = new EmployeeTable();
  }

  public static AudVisRequestTable getAudVisTable() {
    return audVisTable;
  }

  public static ComputerRequestTable getCompRequestTable() {
    return compRequestTable;
  }

  public static NodesTable getNodeTable() {
    return nodeTable;
  }

  public static EdgesTable getEdgeTable() {
    return edgeTable;
  }

  public static ExtTransRequestTable getExternalTransportTable() {
    return externalTransportTable;
  }

  public static FacilitiesRequestTable getFacilitiesTable() {
    return facilitiesTable;
  }

  public static FloralDeliveryRequestTable getFloralDeliveryTable() {
    return floralDeliveryTable;
  }

  public static FoodDeliveryRequestTable getFoodDeliveryTable() {
    return foodDeliveryTable;
  }

  public static InternalTransRequestTable getInternalDeliveryTable() {
    return internalDeliveryTable;
  }

  public static LangInterpreterRequestTable getLangInterpreterTable() {
    return langInterpreterTable;
  }

  public static LaundryRequestTable getLaundryRequestTable() {
    return laundryRequestTable;
  }

  public static MedDeliveryRequestTable getMedDeliveryTable() {
    return medDeliveryTable;
  }

  public static UsersTable getUserTable() {
    return userTable;
  }

  public static SearchHistoryTable getSearchHistoryTable() {
    return searchHistoryTable;
  }

  public static SanitationServRequestTable getSanitationServiceTable() {
    return sanitationServiceTable;
  }

  public static SecurityRequestTable getSecurityRequestTable() {
    return securityRequestTable;
  }

  public static COVID19SurveyTable getCovid19SurveyTable() {
    return covid19SurveyTable;
  }

  public static EmployeeTable getEmployeeTable() {
    return employeeTable;
  }

  // src/main/resources/csv/MapDAllEdges.csv
  // src/main/resources/csv/MapDAllNodes.csv
  public void createAllTables() {
    userTable.createTable(GlobalDb.getConnection());
    userTable.populateTable(GlobalDb.getConnection(), "");
    nodeTable.createTable(GlobalDb.getConnection());
    nodeTable.populateTable(GlobalDb.getConnection(), "");
    nodeTable.createFavoriteNodeTable(GlobalDb.getConnection());
    nodeTable.populateFavoriteNodeTable(GlobalDb.getConnection(), "");
    edgeTable.createTable(GlobalDb.getConnection());
    edgeTable.populateTable(GlobalDb.getConnection(), "");
    searchHistoryTable.createTable(GlobalDb.getConnection());
    audVisTable.createTable(GlobalDb.getConnection());
    compRequestTable.createTable(GlobalDb.getConnection());
    externalTransportTable.createTable(GlobalDb.getConnection());
    facilitiesTable.createTable(GlobalDb.getConnection());
    floralDeliveryTable.createTable(GlobalDb.getConnection());
    foodDeliveryTable.createTable(GlobalDb.getConnection());
    internalDeliveryTable.createTable(GlobalDb.getConnection());
    langInterpreterTable.createTable(GlobalDb.getConnection());
    laundryRequestTable.createTable(GlobalDb.getConnection());
    medDeliveryTable.createTable(GlobalDb.getConnection());
    sanitationServiceTable.createTable(GlobalDb.getConnection());
    covid19SurveyTable.createTable(GlobalDb.getConnection());
    securityRequestTable.createTable(GlobalDb.getConnection());
    // employeeTable.createTable(GlobalDb.getConnection());
  }

  public void deleteAllTables() {
    userTable.clearTable(GlobalDb.getConnection(), "Users");
    nodeTable.clearTable(GlobalDb.getConnection(), "Nodes");
    nodeTable.clearTable(GlobalDb.getConnection(), "FavoriteNodes");
    edgeTable.clearTable(GlobalDb.getConnection(), "Edges");
    searchHistoryTable.clearTable(GlobalDb.getConnection(), "AudVisServiceRequest");
    audVisTable.clearTable(GlobalDb.getConnection(), "AudVisServiceRequest");
    compRequestTable.clearTable(GlobalDb.getConnection(), "ComputerServiceRequest");
    externalTransportTable.clearTable(GlobalDb.getConnection(), "ExternalTransRequests");
    facilitiesTable.clearTable(GlobalDb.getConnection(), "FacilitiesServiceRequest");
    floralDeliveryTable.clearTable(GlobalDb.getConnection(), "FloralRequests");
    foodDeliveryTable.clearTable(GlobalDb.getConnection(), "FoodDeliveryServiceRequest");
    internalDeliveryTable.clearTable(GlobalDb.getConnection(), "InternalTransReq");
    langInterpreterTable.clearTable(GlobalDb.getConnection(), "LangInterpRequest");
    laundryRequestTable.clearTable(GlobalDb.getConnection(), "LaundryRequest");
    medDeliveryTable.clearTable(GlobalDb.getConnection(), "MedicineDelivery");
    sanitationServiceTable.clearTable(GlobalDb.getConnection(), "SanitationRequest");
    covid19SurveyTable.clearTable(GlobalDb.getConnection(), "COVID19SurveyResults");
    securityRequestTable.clearTable(GlobalDb.getConnection(), "SecurityRequest");
  }

  public void createAudVisTable() {
    audVisTable.createTable(GlobalDb.getConnection());
  }

  public void createCompRequestTable() {
    compRequestTable.createTable(GlobalDb.getConnection());
  }

  public void createEdgesTable() {
    edgeTable.createTable(GlobalDb.getConnection());
    edgeTable.populateTable(GlobalDb.getConnection(), "");
  }

  public void createSearchHistoryTable() {
    searchHistoryTable.createTable(GlobalDb.getConnection());
  }

  public void createExternalTransportTable() {
    externalTransportTable.createTable(GlobalDb.getConnection());
  }

  public void createFacilitiesTable() {
    facilitiesTable.createTable(GlobalDb.getConnection());
  }

  public void createFloralDeliveryTable() {
    floralDeliveryTable.createTable(GlobalDb.getConnection());
  }

  public void createFoodDeliveryTable() {
    foodDeliveryTable.createTable(GlobalDb.getConnection());
  }

  public void createInternalTransportTable() {
    internalDeliveryTable.createTable(GlobalDb.getConnection());
  }

  public void createLangInterpreterTable() {
    langInterpreterTable.createTable(GlobalDb.getConnection());
  }

  public void createLaundryRequestTable() {
    laundryRequestTable.createTable(GlobalDb.getConnection());
  }

  public void createMedDeliveryTable() {
    medDeliveryTable.createTable(GlobalDb.getConnection());
  }

  public void createNodesTable() {
    nodeTable.createTable(GlobalDb.getConnection());
    nodeTable.populateTable(GlobalDb.getConnection(), "");
  }

  public void createSanitationServiceTable() {
    sanitationServiceTable.createTable(GlobalDb.getConnection());
  }

  public void createSecurityServiceTable() {
    securityRequestTable.createTable(GlobalDb.getConnection());
  }

  public void createCOVIDTable() {
    covid19SurveyTable.createTable(GlobalDb.getConnection());
  }
}
