package edu.wpi.cs3733.d21.teamD.Astar;

import java.util.*;

public class AllPathsSearch extends IntermediaryAlgo
    implements IPathFinding { // DFS but for every possibility

  private LinkedList<Path> nodesTo; // visited nodes (in order) that are part of the final path
  private boolean hasPath; // to stop search if path is found

  public AllPathsSearch() {
    this.nodesTo = new LinkedList<Path>();
    this.hasPath = false;
  }

  public Path search(RoomGraph data, Node start, Node target) {
    // reinitialize
    this.nodesTo = new LinkedList<Path>();
    this.hasPath = false;
    search(data, start, target, new Path());
    return nodesTo.getFirst(); // idk just kinda needed something here
  }

  private void search(RoomGraph data, Node start, Node target, Path path) {
    if (start != null && target != null) {
      path.getPath().add(start);

      if (start == target) { // if we reached target
        hasPath = true;
        nodesTo.add(path.clone()); // add that path to the list of paths
        path.getPath().remove(start);
        return;
      }

      List<Edge> edges = data.getNode(start).getEdges(); // get list of edges from parent

      if (edges != null) { // if there are children
        for (int i = 0; i < edges.size(); i++) { // iterates through each edge
          Node child = data.getNodeByID(edges.get(i).getEndNodeID());
          if (!path.getPath()
              .contains(
                  child)) { // add sorted children to open list if we haven't already queued it in
            // open list
            double tempCost = path.getCost();
            path.getPathEdges().add(edges.get(i));
            path.setCost(tempCost + edges.get(i).getCost()); // accumulate cost
            search(data, child, target, path);
            path.setCost(tempCost); // reset cost before going to next edge
            path.getPathEdges().remove(edges.get(i)); // undo addition of edge
          }
        }
      }

      path.getPath().remove(start); // remove current node from path
    }
  }

  public boolean pathExists() {
    return this.hasPath;
  }

  public void printPathTo() {
    int i = 1; // really just for labeling
    if (!nodesTo.isEmpty()) {
      for (Path path : this.nodesTo) {
        System.out.print("Path " + i + " ");
        path.printPath();
        System.out.println(); // extra line for looks
        i++;
      }
    } else {
      System.out.println("Path doesn't exist");
    }
  }

  public void printEdgesTo() {
    int i = 1; // really just for labeling
    if (!nodesTo.isEmpty()) {
      for (Path path : this.nodesTo) {
        System.out.print("Path " + i + " Edges:");
        for (Edge edge : path.getPathEdges()) {
          edge.printEdge();
          System.out.print("\t");
        }
        System.out.println("Cost: " + path.getCost());
        System.out.println(); // extra line for looks
        i++;
      }
    } else {
      System.out.println("Path doesn't exist");
    }
  }

  public Path shortestPath() {
    Path min = this.nodesTo.getFirst();
    for (Path path : nodesTo) {
      if (min.getCost() > path.getCost()) min = path;
    }
    return min;
  }
}
