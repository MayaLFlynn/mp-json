import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;

/**
 * Some experiments for testing our JSON library
 * 
 * @author Tim Yu
 * @author Maya Flynn
 * @author Amelia Vrieze
 */
public class Experiments {
  static PrintWriter pen = new PrintWriter(System.out, true);

  /**
   * Test parsing JSON
   */
  public static void parseExpt() throws Exception {
    pen.println("parseExpt()");
    Reader fr = new FileReader(new File("test.json"));
    //Reader fr = new FileReader(new File("stringTest.txt"));
    JSONValue value = JSON.parse(fr);

    pen.println("test writeJSON:");
    value.writeJSON(pen);
    pen.println();

    pen.println("test toString:");
    pen.println(value.toString());
    pen.println();
    pen.println();
  }

  public static void equalsExpt() throws Exception {
    pen.println("equalsExpt()");
    JSONArray arr1 = new JSONArray();
    JSONArray arr2 = new JSONArray();
    JSONArray arr3 = new JSONArray();
    arr1.add(new JSONString("a"));
    arr1.add(new JSONString("a"));
    arr2.add(new JSONString("a"));
    arr2.add(new JSONString("a"));
    arr3.add(new JSONString("a"));
    // arr1 = arr2 = ["a","a"], arr3 = ["a"]
    pen.println("arr1.equals(arr2): " + arr1.equals(arr2));
    pen.println("arr1.equals(arr3): " + arr1.equals(arr3));

    pen.println("123 equals 123: " + new JSONInteger(123).equals(new JSONInteger(123)));
    pen.println("123 equals 456: " + new JSONInteger(123).equals(new JSONInteger(456)));

    pen.println("123.456 equals 123.456: " + new JSONReal(123.456).equals(new JSONReal(123.456)));
    pen.println("123.456 equals 123.789: " + new JSONReal(123.456).equals(new JSONReal(123.789)));

    pen.println("\"abc\" equals \"abc\": " + new JSONString("abc").equals(new JSONString("abc")));
    pen.println("\"abc\" equals \"xyz\": " + new JSONString("abc").equals(new JSONString("xyz")));

    JSONHash h1 = new JSONHash();
    JSONHash h2 = new JSONHash();
    JSONHash h3 = new JSONHash();

    JSONHash h4 = new JSONHash();
    JSONHash h5 = new JSONHash();
    h4.set(new JSONString("nested"), new JSONString("value"));
    h5.set(new JSONString("nested"), new JSONString("value"));

    h1.set(new JSONString("k1"), new JSONString("1"));
    h1.set(new JSONString("k2"), h4);
    h2.set(new JSONString("k2"), h5);
    h2.set(new JSONString("k1"), new JSONString("1"));
    h3.set(new JSONString("k1"), new JSONString("v1"));


    pen.println("h1.equals(h2): " + h1.equals(h2));
    pen.println("h1.equals(h3): " + h1.equals(h3));

    pen.println();
  }

  public static void escapeExpt() throws Exception {
    pen.println("escapeExpt()");

    JSONValue parsed = JSON.parse("\"hello \\n\\tworld\""); // "hello \n\tworld"
    pen.println(parsed.toString());
    parsed.writeJSON(pen);

    pen.println();
  }


  public static void main(String[] args) throws Exception {
    parseExpt();
    equalsExpt();
    escapeExpt();

    // String test = "\n";
    // pen.println(test.replace("\n", "\\\\"));
  }
}
