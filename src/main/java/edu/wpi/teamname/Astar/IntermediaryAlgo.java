package edu.wpi.teamname.Astar;

public abstract class IntermediaryAlgo implements IPathFinding {

  public abstract Path search(RoomGraph data, Node start, Node target);

  public final Path multiSearch(RoomGraph data, Node start, Node... targets) {
    Path thePath = search(data, start, targets[0]);
    for (int i = 1; i < targets.length; i++) {
      // remove repetition of target and next start
      thePath.getPath().removeLast();
      thePath.getPathEdges().removeLast();

      // search and append path
      Path aPath = search(data, targets[i - 1], targets[i]);
      if (aPath.getPath().isEmpty()) {
        System.out.printf(
            "No path from %s to %s\n", targets[i - 1].getNodeID(), targets[i].getNodeID());
        return new Path();
      }
      aPath.appendTo(thePath);
    }
    return thePath;
  }
}
