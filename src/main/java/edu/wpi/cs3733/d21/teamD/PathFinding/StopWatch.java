package edu.wpi.cs3733.d21.teamD.PathFinding;

public class StopWatch {

  private long start;
  private long elapsedTime;

  public StopWatch() {
    start = System.currentTimeMillis();
    elapsedTime = 0;
  }

  public void elapsedTime() {
    long now = System.currentTimeMillis();
    elapsedTime = (now - start);
  }

  public void reset() {
    start = System.currentTimeMillis();
    elapsedTime = 0;
  }

  public void printTime() {
    System.out.printf("Elapsed time (ms): %f \n", (float) (elapsedTime));
  }
}
