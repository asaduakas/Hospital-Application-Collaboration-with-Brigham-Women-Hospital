package edu.wpi.teamname.views;

import java.io.*;

public class WriteFile {

  private String path;
  private boolean append_to_file =
      false; // whether when adding line, you replace the entire file(false), or add to the
  // file(true)

  public WriteFile(String file_path) {
    this.path = file_path;
  }

  public WriteFile(String file_path, boolean append_value) {
    this.path = file_path;
    this.append_to_file = append_value;
  }

  public void writeToFile(String textLine) throws IOException {
    FileWriter write = new FileWriter(path, append_to_file);
    PrintWriter print_line = new PrintWriter(write);
    print_line.printf("%s\n", textLine);
    print_line.close();
  }

  public void deleteLineInFile(String textLine) throws IOException {
    try {

      File inFile = new File(path);

      if (!inFile.isFile()) { // if file doesn't exist
        System.out.println("Parameter is not an existing file");
        return;
      }

      File tempFile = new File(inFile.getAbsolutePath() + ".tmp"); // make a temporary file

      BufferedReader br = new BufferedReader(new FileReader(path)); // for reading the original file
      PrintWriter pw =
          new PrintWriter(new FileWriter(tempFile)); // for writing to the temporary file

      String line = null; // holds the lines

      // transfer lines to temporary file
      while ((line = br.readLine()) != null) { // if there is a next line
        if (!line.trim().equals(textLine)) { // if line doesn't match given line
          pw.println(line);
          pw.flush(); // wait for println to finish
        }
      }
      pw.close();
      br.close();

      if (!inFile.delete()) { // delete file; returns boolean: false if unable to delete file
        System.out.println("Could not delete file");
        return;
      }

      if (!tempFile.renameTo(inFile))
        System.out.println(
            "Could not rename file"); // rename temporary file to original file; returns boolean:
      // false if unable to delete file

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public boolean isAppend_to_file() {
    return append_to_file;
  }

  public void setAppend_to_file(boolean append_to_file) {
    this.append_to_file = append_to_file;
  }
}
