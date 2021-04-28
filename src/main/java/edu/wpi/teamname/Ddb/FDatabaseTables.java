package edu.wpi.teamname.Ddb;

public class FDatabaseTables {

  private AudVisRequestTable audVisTable;
  private ComputerRequestTable compRequestTable;
  private EdgesTable edgeTable;
  private ExtTransRequestTable externalTransportTable;
  private FacilitiesRequestTable facilitiesTable;
  private FloralDeliveryRequestTable floralDeliveryTable;
  private FoodDeliveryRequestTable foodDeliveryTable;
  private InternalTransRequestTable internalDeliveryTable;
  private LangInterpreterRequestTable langInterpreterTable;
  private LaundryRequestTable laundryRequestTable;
  private MedDeliveryRequestTable medDeliveryTable;
  private NodesTable nodeTable;
  private UsersTable userTable;
  private SanitationServRequestTable sanitationServiceTable;
  private SecurityRequestTable securityRequestTable;

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
    this.sanitationServiceTable = new SanitationServRequestTable();
    this.securityRequestTable = new SecurityRequestTable();
  }

  public AudVisRequestTable getAudVisTable() {
    return audVisTable;
  }

  public ComputerRequestTable getCompRequestTable() {
    return compRequestTable;
  }

  public NodesTable getNodeTable() {
    return nodeTable;
  }

  public EdgesTable getEdgeTable() {
    return edgeTable;
  }

  public ExtTransRequestTable getExternalTransportTable() {
    return externalTransportTable;
  }

  public FacilitiesRequestTable getFacilitiesTable() {
    return facilitiesTable;
  }

  public FloralDeliveryRequestTable getFloralDeliveryTable() {
    return floralDeliveryTable;
  }

  public FoodDeliveryRequestTable getFoodDeliveryTable() {
    return foodDeliveryTable;
  }

  public InternalTransRequestTable getInternalDeliveryTable() {
    return internalDeliveryTable;
  }

  public LangInterpreterRequestTable getLangInterpreterTable() {
    return langInterpreterTable;
  }

  public LaundryRequestTable getLaundryRequestTable() {
    return laundryRequestTable;
  }

  public MedDeliveryRequestTable getMedDeliveryTable() {
    return medDeliveryTable;
  }

  public UsersTable getUserTable() {
    return userTable;
  }

  public SanitationServRequestTable getSanitationServiceTable() {
    return sanitationServiceTable;
  }

  public SecurityRequestTable getSecurityRequestTable() {
    return securityRequestTable;
  }

  // src/main/resources/csv/MapDAllEdges.csv
  // src/main/resources/csv/MapDAllNodes.csv
  public void createAllTables() {
    nodeTable.createTable(GlobalDb.getConnection());
    nodeTable.populateTable(GlobalDb.getConnection(), "");
    nodeTable.createFavoriteNodeTable(GlobalDb.getConnection());
    nodeTable.populateFavoriteNodeTable(GlobalDb.getConnection(), "");
    edgeTable.createTable(GlobalDb.getConnection());
    edgeTable.populateTable(GlobalDb.getConnection(), "");
    userTable.createTable(GlobalDb.getConnection());
    userTable.populateTable(GlobalDb.getConnection(), "");
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
    securityRequestTable.createTable(GlobalDb.getConnection());
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
}
