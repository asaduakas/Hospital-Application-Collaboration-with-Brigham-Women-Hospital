package edu.wpi.teamname.Testing;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.teamname.Astar.IntermediaryAlgo;
import edu.wpi.teamname.Astar.Node;
import edu.wpi.teamname.Astar.RoomGraph;
import edu.wpi.teamname.Astar.aStar;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class IntermediaryAlgoTest {
  @Before
  public void setUp() throws Exception {}

  @Test
  public void templateTest() throws IOException {
    RoomGraph data = new RoomGraph("MapDnodes.csv", "MapDedges.csv");
    IntermediaryAlgo intermediaryAlgo = new aStar();

    Node start = data.getNodeByID("dWALK00101");
    Node target1 = data.getNodeByID("dWALK01401");
    Node target2 = data.getNodeByID("dWALK01201");
    Node target3 = data.getNodeByID("dWALK01801");

    // example of part having no path
    //    Node target2 = data.getNodeByID("dWALK01201");
    //    Node target3 = data.getNodeByID("dWALK01901");

    intermediaryAlgo.multiSearch(data, start, target1, target2, target3).printPath();
    assertTrue(true);
  }
}
