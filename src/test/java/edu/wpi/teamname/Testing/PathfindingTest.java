package edu.wpi.teamname.Testing;

import edu.wpi.teamname.Astar.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    // Node target = data.getNode("GEXIT001L1"); // no path exists
    // Node target = data.getNode("CRETL001L1");

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
    // Node target2 = data2.getNode("dWALK01501");
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
    //    Node start = data.getNode("FRETL00201");
    //    Node target = data.getNode("FHALL02901");
    // Node target = data.getNode("BHALL00502");

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
    Connection connection = null;
    try {
      connection =
          DriverManager.getConnection("jdbc:derby:myDB;create=true;username=Admin;password=Admin");
      RoomGraph graph = new RoomGraph(connection);
      graph.printGraph();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
