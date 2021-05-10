package edu.wpi.cs3733.d21.teamD.Testing;

import edu.wpi.cs3733.d21.teamD.Astar.*;
import edu.wpi.cs3733.d21.teamD.Ddb.GlobalDb;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class PathfindingTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void algosMainTest() throws IOException {
    RoomGraph data = new RoomGraph("L1Nodes.csv", "L1Edges.csv");
    RoomGraph data2 = new RoomGraph("MapDnodes.csv", "MapDedges.csv");
    PathAlgoPicker pathfinder = new PathAlgoPicker();

    Node start = data.getNodeByID("CCONF002L1");
    Node target = data.getNodeByID("CHALL002L1"); // more than 2 paths available
    // Node target = data.getNodeByID("GEXIT001L1"); // no path exists
    // Node target = data.getNodeByID("CRETL001L1");

    StopWatch timer = new StopWatch();

    System.out.println("-----------------Starting AllPaths* print--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new AllPathsSearch());
    pathfinder.search(data, start, target);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();
    // pathfinder.getShortestPath().printPath();

    System.out.println("-----------------Starting DFS print--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new singleDFS());
    pathfinder.search(data, start, target);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();

    System.out.println("-----------------Starting BFS print--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new singleBFS());
    pathfinder.search(data, start, target);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();

    System.out.println("-----------------Starting Dijkstra print--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new Dijkstras());
    pathfinder.search(data, start, target);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();

    System.out.println("-----------------Starting A* print--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(
        new aStar()); // note that aStar sorts the edges for the children it traverses through, so
    // don't test DFS if you want different results
    pathfinder.search(data, start, target);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();

    System.out.println();

    Node start2 = data2.getNodeByID("dWALK00101");
    // Node target2 = data2.getNodeByID("dWALK01501");
    Node target2 = data2.getNodeByID("dWALK01201");

    System.out.println(
        "-----------------Starting AllPaths* print2--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new AllPathsSearch());
    pathfinder.search(data2, start2, target2);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();

    System.out.println("-----------------Starting DFS print2--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new singleDFS());
    pathfinder.search(data2, start2, target2);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();

    System.out.println("-----------------Starting BFS print2--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new singleBFS());
    pathfinder.search(data2, start2, target2);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();

    System.out.println(
        "-----------------Starting Dijisktra print2--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new Dijkstras());
    pathfinder.search(data2, start2, target2);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();

    System.out.println("-----------------Starting A* print2--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new aStar());
    pathfinder.search(data2, start2, target2);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();
  }

  @Test
  public void multiFloorTest() throws IOException {
    RoomGraph data = new RoomGraph("MapDAllNodes.csv", "MapDAllEdges.csv");
    PathAlgoPicker pathfinder = new PathAlgoPicker();
    Node start = data.getNodeByID("FHALL02901");
    Node target = data.getNodeByID("FRETL00201");
    //    Node start = data.getNodeByID("FRETL00201");
    //    Node target = data.getNodeByID("FHALL02901");
    // Node target = data.getNodeByID("BHALL00502");

    StopWatch timer = new StopWatch();

    /*System.out.println("-----------------Starting AllPaths* print--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new AllPathsSearch(data, start, target));
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();
    // pathfinder.getShortestPath().printPath();*/

    System.out.println("-----------------Starting DFS print--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new singleDFS());
    pathfinder.search(data, start, target);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();

    System.out.println("-----------------Starting BFS print--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new singleBFS());
    pathfinder.search(data, start, target);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();

    System.out.println("-----------------Starting Dijkstras print--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new Dijkstras());
    pathfinder.search(data, start, target);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();

    System.out.println();

    System.out.println("-----------------Starting A* print--------------------------------");

    timer.reset();
    pathfinder.setAlgorithm(new aStar());
    pathfinder.search(data, start, target);
    timer.elapsedTime();
    timer.printTime();
    pathfinder.printPathTo();

    System.out.println();
  }

  @Test
  public void databaseConversionTest() {
    GlobalDb.establishCon();
    RoomGraph graph = new RoomGraph(GlobalDb.getConnection());
    graph.printGraph();
  }

  @Test
  public void blockedTest() {
    GlobalDb.establishCon();
    RoomGraph data = new RoomGraph(GlobalDb.getConnection());
    PathAlgoPicker pathfinder = new PathAlgoPicker();
    Node start = data.getNodeByID("dRETL00101");
    Node target = data.getNodeByID("WELEV00M01");

    data.getNodeByID("FEXIT00201").setBlocked(true);

    System.out.println("-----------------Starting aStar print--------------------------------");

    pathfinder.setAlgorithm(new aStar());
    pathfinder.search(data, start, target);
    pathfinder.printPathTo();

    System.out.println("-----------------Starting BFS print--------------------------------");

    pathfinder.setAlgorithm(new singleBFS());
    pathfinder.search(data, start, target);
    pathfinder.printPathTo();

    System.out.println("-----------------Starting DFS print--------------------------------");

    pathfinder.setAlgorithm(new singleDFS());
    pathfinder.search(data, start, target);
    pathfinder.printPathTo();

    System.out.println("-----------------Starting Dijkstras print--------------------------------");

    pathfinder.setAlgorithm(new Dijkstras());
    pathfinder.search(data, start, target);
    pathfinder.printPathTo();
  }
}
