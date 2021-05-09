package edu.wpi.cs3733.d21.teamD.Testing;

import edu.wpi.cs3733.d21.teamD.Astar.IntermediaryAlgo;
import edu.wpi.cs3733.d21.teamD.Astar.Node;
import edu.wpi.cs3733.d21.teamD.Astar.RoomGraph;
import edu.wpi.cs3733.d21.teamD.Astar.aStar;
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

    intermediaryAlgo.multiSearch(data, start, target1).printPath();
    intermediaryAlgo.multiSearch(data, start, target1, target2).printPath();
    intermediaryAlgo.multiSearch(data, start, target1, target2, target3).printPath();
  }
}
