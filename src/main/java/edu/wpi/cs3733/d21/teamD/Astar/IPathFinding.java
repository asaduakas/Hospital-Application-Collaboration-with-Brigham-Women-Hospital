package edu.wpi.cs3733.d21.teamD.Astar;
// initially made in Algos repo

public interface IPathFinding {
  // common methods of algorithms with a start and target
  Path search(RoomGraph data, Node start, Node target);

  void printPathTo();

  void printEdgesTo();

  Path shortestPath();

  boolean pathExists();
}
