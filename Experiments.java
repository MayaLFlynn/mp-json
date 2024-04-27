import java.io.PrintWriter;
import java.util.Iterator;

public class Experiments {
  static PrintWriter pen = new PrintWriter(System.out, true);

  /**
   * A word list stolen from some tests that SamR wrote in the distant past.
   * It has 37 words.
   */
  static String[] words = {"aardvark", "anteater", "antelope", "bear", "bison", "buffalo",
      "chinchilla", "cat", "dingo", "elephant", "eel", "flying squirrel", "fox", "goat", "gnu",
      "goose", "hippo", "horse", "iguana", "jackalope", "kestrel", "llama", "moose", "mongoose",
      "nilgai", "orangutan", "opossum", "red fox", "snake", "tarantula", "tiger", "vicuna",
      "vulture", "wombat", "yak", "zebra", "zorilla"};
  
  public static void iteratorExpt() {
    JSONHash table = new JSONHash();
    pen.println("Adding items to hash table...");
    for (String word : words) {
      table.set(new JSONString(word), new JSONString(word));
    }
    pen.println("Printing hash table...");
    table.contents.dump(pen);
    pen.println("Testing iterator...");
    Iterator<KVPair<JSONString,JSONValue>> iter = table.iterator();
    while (iter.hasNext()) {
      KVPair<JSONString,JSONValue> pair = iter.next();
      pen.println(pair);
    }
    pen.println();
  }

  public static void main(String[] args) {
    iteratorExpt();
  }
}
