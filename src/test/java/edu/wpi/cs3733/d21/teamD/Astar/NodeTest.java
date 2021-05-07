package edu.wpi.cs3733.d21.teamD.Astar;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeTest {

  @Test
  public void compareFloor() {
    Node n = new Node();
    n.setFloor("1");
    Node n1 = new Node();
    n1.setFloor("L1");

    assertEquals(n.compareFloor(n1), 1);
    assertEquals(n1.compareFloor(n), -1);

    n1.setFloor("1");
    assertEquals(n1.compareFloor(n), 0);

    n.setFloor("3");
    n1.setFloor("2");
    assertEquals(n.compareFloor(n1), 1);

    n.setFloor("1");
    n1.setFloor("3");
    assertEquals(n.compareFloor(n1), 1);
  }
}
