package edu.wpi.cs3733.d21.teamD.Astar;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CSVRead_Edges {
  private List<String[]> edges;
  private String fileName;

  public CSVRead_Edges(String fileName) {
    this.edges = new LinkedList<String[]>();
    this.fileName = fileName;
  }

  public List<Edge> getEdges(HashMap<String, Node> nodes) throws IOException {
    List<Edge> edges = new LinkedList<Edge>();
    Scanner sc;
    try {
      sc = new Scanner(new File("src/main/resources/csv/" + fileName));
    } catch (IOException e) {
      String path =
          new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath())
              .getParent();
      sc = new Scanner(new File(path + "/" + fileName));
    }
    sc.useDelimiter(",|\\r\\n|\\n");

    sc.nextLine(); // skip first line
    while (sc.hasNext()) {
      String[] element = {"0", "0", "0"};
      for (int i = 0; i < 3; i++) {
        element[i] = sc.next();
        // System.out.print(entries[i] + "\t");
      }
      if (nodes.get(element[1]) != null && nodes.get(element[2]) != null) {
        String edgeID = element[0];
        Node startNode = nodes.get(element[1]);
        Node endNode = nodes.get(element[2]);
        double cost = startNode.getMeasuredDistance(endNode);
        Edge edge = new Edge(startNode, endNode, cost);
        if (!edges.contains(edge)) edges.add(edge);
        // bidirectional
        Edge edge1 = new Edge(endNode, startNode, cost);
        if (!edges.contains((edge1))) edges.add(edge1);
      }
    }
    return edges;
  }
}
