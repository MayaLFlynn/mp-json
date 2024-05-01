import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;

public class Experiments {
  static PrintWriter pen = new PrintWriter(System.out, true);

  /**
   * Test parsing JSON
   */
  public static void parseExpt() throws Exception {
    Reader fr = new FileReader(new File("test.json"));
    //Reader fr = new FileReader(new File("stringTest.txt"));
    JSONValue value = JSON.parse(fr);
    value.writeJSON(pen);
  }

  public static void main(String[] args) throws Exception {
    // iteratorExpt();
    parseExpt();
    pen.println("");
  }
}
