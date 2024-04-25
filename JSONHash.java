import java.io.PrintWriter;
import java.util.Iterator;
import static java.lang.reflect.Array.newInstance;

/**
 * JSON hashes/objects.
 */
public class JSONHash implements JSONValue {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+
  private final int INITIAL_CAPACITY = 16;
  KVPair<JSONString, JSONValue>[] contents;
  private final double LOAD_FACTOR = 0.5;
  int size;
  int capacity;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+
  @SuppressWarnings({"unchecked"})
  public JSONHash() {
    this.contents = (KVPair<JSONString, JSONValue> []) newInstance((new KVPair<JSONString, JSONValue>()).getClass(), INITIAL_CAPACITY);
    this.size = 0;
    this.capacity = INITIAL_CAPACITY;
  }
  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    return ""; //STUB
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    return true; // STUB
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    int hashNum = 0;
    for (KVPair<JSONString, JSONValue> pair : this.contents) {
      if(pair.key() != null) {

      }
    }
    return hashNum;
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
    // STUB
  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public Iterator<KVPair<JSONString, JSONValue>> getValue() {
    return this.iterator();
  } // getValue()

  // +-------------------+-------------------------------------------
  // | Hashtable methods |
  // +-------------------+

  /**
   * Get the value associated with a key.
   */
  public JSONValue get(JSONString key) {
    return null; // STUB
  } // get(JSONString)

  /**
   * Get all of the key/value pairs.
   */
  public Iterator<KVPair<JSONString, JSONValue>> iterator() {
    return null; // STUB
  } // iterator()

  /**
   * Set the value associated with a key.
   */
  public void set(JSONString key, JSONValue value) {
    // STUB
  } // set(JSONString, JSONValue)

  /**
   * Find out how many key/value pairs are in the hash table.
   */
  public int size() {
    return 0; // STUB
  } // size()

} // class JSONHash
