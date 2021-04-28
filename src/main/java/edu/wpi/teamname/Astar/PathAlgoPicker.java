package edu.wpi.teamname.Astar;
// initially made in Algos repo

public class PathAlgoPicker {
  private IPathFinding algorithm;

  public PathAlgoPicker() {
    this.algorithm = null;
  }

  public PathAlgoPicker(IPathFinding algorithm) {
    this.algorithm = algorithm;
  }

  public void search(RoomGraph data, Node start, Node target) {
    algorithm.search(data, start, target);
  }

  public void printPathTo() {
    algorithm.printPathTo();
  }

  public void printEdgeTo() {
    algorithm.printEdgesTo();
  }

  public Path getShortestPath() {
    return algorithm.shortestPath();
  }

  public IPathFinding getAlgorithm() {
    return algorithm;
  }

  public boolean hasPath() {
    return algorithm.pathExists();
  }

  public void setAlgorithm(IPathFinding algorithm) {
    this.algorithm = algorithm;
  }
}
