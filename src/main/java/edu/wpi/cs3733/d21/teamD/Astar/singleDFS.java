package edu.wpi.cs3733.d21.teamD.Astar;
// initially made in Algos repo

import java.util.LinkedList;
import java.util.List;

public class singleDFS extends IntermediaryAlgo
    implements IPathFinding { // based on aStar (technically aStar is based on this, but aStar was
  // written first)

  private Path nodeTo; // visited nodes (in order) that are part of the final path
  private boolean hasPath; // to stop search if path is found
  private LinkedList<Node> closed;

  public singleDFS() {
    this.nodeTo = new Path();
    this.hasPath = false;
  }

  public Path search(RoomGraph data, Node start, Node target) {
    if (start != null && target != null) {
      // reinitialize
      this.nodeTo = new Path();
      this.closed = new LinkedList<Node>();
      this.hasPath = false;
      searchS(data, start, target);
    }
    return nodeTo;
  }

  private void searchS(RoomGraph data, Node start, Node target) {
    nodeTo.getPath().add(start);

    if (start == target) { // if we reached target
      hasPath = true;
      return;
    }

    List<Edge> edges = data.getNode(start).getEdges(); // get list of edges from parent

    if (edges == null) { // if we reached dead end and we haven't reached target
      nodeTo.getPath().remove(start);
      closed.add(start);
    } else {

      for (int i = 0;
          i < edges.size() && !hasPath;
          i++) { // iterates through each edge. since sorted, it will go through smallest cost
        // first (and it's children)
        Node child = data.getNodeByID(edges.get(i).getEndNodeID());
        if (!nodeTo.getPath().contains(child)
            && !closed.contains(child)) { // analyze if node not already in path
          // open list or determined it's a dead end
          nodeTo.getPathEdges().add(edges.get(i));
          double tempCost = nodeTo.getCost();
          nodeTo.setCost(tempCost + edges.get(i).getCost()); // accumulate cost

          searchS(data, child, target);

          if (!hasPath) {
            nodeTo.setCost(
                tempCost); // undo cost addition and edge addition if this edge/node doesn't lead
            // to target
            nodeTo.getPathEdges().remove(edges.get(i));
          }
        }
      }
      if (!hasPath) {
        closed.add(start);
        nodeTo.getPath().remove(start);
      } // remove current node from edgeTo if after going through children there
      // is no path to target
    }
  }

  public Path shortestPath() {
    return this.nodeTo;
  }

  public boolean pathExists() {
    return this.hasPath;
  }

  public void printPathTo() {
    if (this.hasPath) {
      for (Node node : nodeTo.getPath()) {
        System.out.println("NodeID: " + node.getNodeID());
      }
      System.out.println("Cost: " + nodeTo.getCost());
    } else {
      System.out.println("Path doesn't exist");
    }
  }

  public void printEdgesTo() {
    if (this.hasPath) {
      for (Edge edge : nodeTo.getPathEdges()) {
        edge.printEdge();
        System.out.println();
      }
      System.out.println("Cost: " + nodeTo.getCost());
    } else {
      System.out.println("Path doesn't exist");
    }
  }
}
