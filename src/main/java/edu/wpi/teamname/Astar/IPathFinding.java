package edu.wpi.teamname.Astar;
// initially made in Algos repo

public interface IPathFinding {
  // common methods of algorithms with a start and target
  void search(RoomGraph data, Node start, Node target);

  void printPathTo();

  void printEdgesTo();

  Path shortestPath();

  boolean pathExists();
}
