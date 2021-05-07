package edu.wpi.cs3733.d21.teamD.Astar;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CSVRead_nodes {

  private HashMap<String, Node> nodes;
  private String fileName;

  public CSVRead_nodes(String fileName) throws IOException {
    this.fileName = fileName;
    this.nodes = new HashMap<String, Node>();
    fetchNodes();
  }

  private void fetchNodes() throws IOException {
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

    sc.nextLine();
    while (sc.hasNext()) {
      String[] element = {"0", "0", "0", "0", "0", "0", "0", "0"};
      for (int i = 0; i < 8; i++) {
        element[i] = sc.next();
        // System.out.print(entries[i] + "\t");
      }
      String nodeID = element[0];
      int xcoord = Integer.parseInt(element[1]);
      int ycoord = Integer.parseInt(element[2]);
      String floor = element[3];
      String building = element[4];
      String nodeType = element[5];
      String longName = element[6];
      String shortName = element[7];
      Node node = new Node(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName);
      this.nodes.put(nodeID, node);
    }

    sc.close();
  }

  public HashMap<String, Node> getNodes() {
    return this.nodes;
  }

  public void writeNodes() throws IOException {
    ArrayList<String> ids = new ArrayList<>(nodes.keySet());
    // Sort nodes in alphabetical order before saving, since ordering was NOT preserved when loading
    ids.sort(String::compareToIgnoreCase);

    PrintWriter pw = new PrintWriter("./Data/" + fileName);
    pw.println("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName");
    for (String nodeID : ids) {
      Node node = nodes.get(nodeID);
      pw.println(
          node.getNodeID()
              + ","
              + node.getXCoord()
              + ","
              + node.getYCoord()
              + ","
              + node.getFloor()
              + ","
              + node.getBuilding()
              + ","
              + node.getNodeType()
              + ","
              + node.getLongName()
              + ","
              + node.getShortName());
    }
    pw.close();
  }
}
