package edu.wpi.cs3733.d21.teamD.Testing;

import static org.junit.Assert.*;

import edu.wpi.cs3733.d21.teamD.Astar.Edge;
import edu.wpi.cs3733.d21.teamD.Astar.Node;
import edu.wpi.cs3733.d21.teamD.Astar.RoomGraph;
import edu.wpi.cs3733.d21.teamD.Astar.aStar;
import java.util.LinkedList;
import org.junit.Before;
import org.junit.Test;

public class aStarTest {

  RoomGraph graph = new RoomGraph();

  // nodes in graph
  Node node1 = new Node("ID1", 1, 1, "1", "building", "test", "long1", "short1");
  Node node2 = new Node("ID2", 1, 2, "1", "building", "test", "long2", "short2");
  Node node3 = new Node("ID3", 1, 3, "1", "building", "test", "long3", "short3");
  Node node4 = new Node("ID4", 2, 1, "1", "building", "test", "long4", "short4");
  Node node5 = new Node("ID5", -3, 3, "1", "building", "test", "long5", "short5");
  Node node6 = new Node("ID6", 3, 4, "1", "building", "test", "long6", "short6");
  Node node7 = new Node("ID7", 3, 1, "1", "building", "test", "long7", "LONELY");

  @Before
  public void setUp() throws Exception {

    // add edges to graph
    LinkedList<Edge> edges1 = new LinkedList<Edge>();
    LinkedList<Edge> edges2 = new LinkedList<Edge>();
    LinkedList<Edge> edges3 = new LinkedList<Edge>();
    LinkedList<Edge> edges4 = new LinkedList<Edge>();
    LinkedList<Edge> edges5 = new LinkedList<Edge>();
    LinkedList<Edge> edges6 = new LinkedList<Edge>();
    LinkedList<Edge> edges7 = new LinkedList<Edge>();

    // 1-2
    Edge edge1_2 = new Edge(node1, node2, node1.getMeasuredDistance(node2));
    edges1.add(edge1_2);
    Edge edge2_1 = new Edge(node2, node1, node1.getMeasuredDistance(node2));
    edges2.add(edge2_1);

    // 2-3
    Edge edge2_3 = new Edge(node2, node3, node3.getMeasuredDistance(node2));
    edges2.add(edge2_3);
    Edge edge3_2 = new Edge(node3, node2, node3.getMeasuredDistance(node2));
    edges3.add(edge3_2);

    // 1-4
    Edge edge1_4 = new Edge(node1, node4, node4.getMeasuredDistance(node1));
    edges1.add(edge1_4);
    Edge edge4_1 = new Edge(node4, node1, node4.getMeasuredDistance(node1));
    edges4.add(edge4_1);

    // 1-5
    Edge edge1_5 = new Edge(node1, node5, node5.getMeasuredDistance(node1));
    edges1.add(edge1_5);
    Edge edge5_1 = new Edge(node5, node1, node5.getMeasuredDistance(node1));
    edges5.add(edge5_1);

    // 3-6
    Edge edge3_6 = new Edge(node3, node6, node6.getMeasuredDistance(node3));
    edges3.add(edge3_6);
    Edge edge6_3 = new Edge(node6, node3, node6.getMeasuredDistance(node3));
    edges6.add(edge6_3);

    // 4-5
    Edge edge5_4 = new Edge(node5, node4, node4.getMeasuredDistance(node5));
    edges5.add(edge5_4);
    Edge edge4_5 = new Edge(node4, node5, node4.getMeasuredDistance(node5));
    edges4.add(edge4_5);

    // 5-6
    Edge edge6_5 = new Edge(node6, node5, node5.getMeasuredDistance(node6));
    edges6.add(edge6_5);
    Edge edge5_6 = new Edge(node5, node6, node5.getMeasuredDistance(node6));
    edges5.add(edge5_6);

    // 2-5
    Edge edge2_5 = new Edge(node2, node5, node5.getMeasuredDistance(node2));
    edges2.add(edge2_5);
    Edge edge5_2 = new Edge(node5, node2, node5.getMeasuredDistance(node2));
    edges5.add(edge2_5);

    node1.setEdges(edges1);
    node2.setEdges(edges2);
    node3.setEdges(edges3);
    node4.setEdges(edges4);
    node5.setEdges(edges5);
    node6.setEdges(edges6);
    node7.setEdges(edges7);

    graph.getGraphInfo().add(node1);
    graph.getGraphInfo().add(node2);
    graph.getGraphInfo().add(node3);
    graph.getGraphInfo().add(node4);
    graph.getGraphInfo().add(node5);
    graph.getGraphInfo().add(node6);
    graph.getGraphInfo().add(node7);

    /* TestGraph
           1 - 4   7
         /  |/
        / //2
       / // |
        5   3
          \  \
            \  \
               6
    */
  }

  @Test
  public void pathExists() {
    aStar star = new aStar();
    star.search(graph, node1, node7);
    assertFalse(star.pathExists());

    star = new aStar();
    star.search(graph, node1, node6);
    assertTrue(star.pathExists());
  }

  @Test
  public void getPathTo() {
    aStar star = new aStar();
    star.search(graph, node1, node5);
    LinkedList<Node> path = new LinkedList<Node>();
    path.add(node1);
    path.add(node5);
    assertTrue(path.equals(star.shortestPath().getPath()));

    star.search(graph, node1, node6);
    path = new LinkedList<Node>();
    path.add(node1);
    path.add(node2);
    path.add(node3);
    path.add(node6);
    star.printPathTo();
    assertTrue(path.equals(star.shortestPath().getPath()));

    star.search(graph, node2, node4);
    path = new LinkedList<Node>();
    path.add(node2);
    path.add(node1);
    path.add(node4);
    assertTrue(path.equals(star.shortestPath().getPath()));
  }

  @Test
  public void bidirectionalPath() {
    aStar star = new aStar();
    star.search(graph, node1, node6);
    star.printPathTo();
    aStar star2 = new aStar();
    star2.search(graph, node6, node1);
    star2.printPathTo();
    assertTrue(star.shortestPath().equals(star2.shortestPath()));
  }
}
