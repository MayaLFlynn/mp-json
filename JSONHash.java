import java.io.PrintWriter;
import java.util.Iterator;

/**
 * JSON hashes/objects.
 * 
 * @author Maya Flynn
 * @author Tim Yu
 * @author Amelia Vrieze
 */
public class JSONHash implements JSONValue, Iterable<KVPair<JSONString, JSONValue>> {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+
  /**
   * The underlying Hash Table
   */
  ChainedHashTable<JSONString, JSONValue> contents;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+
  /**
   * Creates a new JSONHash by setting it to an empty hash table
   */
  public JSONHash() {
    this.contents = new ChainedHashTable<JSONString, JSONValue>();
  }
  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    return this.contents.toString();
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    if (other instanceof JSONHash) {
      // check other contains every pair in this
      JSONHash otherHash = (JSONHash) other;
      for (KVPair<JSONString, JSONValue> pair : this) {
        if (!otherHash.contents.containsKey(pair.key())
            || !otherHash.contents.get(pair.key()).equals(pair.value())) {
          return false;
        }
      }
      // check this contains every pair in other
      for (KVPair<JSONString, JSONValue> pair : otherHash) {
        if (!this.contents.containsKey(pair.key())
            || !this.contents.get(pair.key()).equals(pair.value())) {
          return false;
        }
      }
      // we have shown double containment
      return true;
    }
    // wrong type of other
    return false;
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    return this.contents.hashCode();
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
    pen.print("{");
    Iterator<KVPair<JSONString, JSONValue>> contentsIter = this.contents.iterator();
    while (contentsIter.hasNext()) {
      KVPair<JSONString, JSONValue> pair = contentsIter.next();
      pair.key().writeJSON(pen);
      pen.print(":");
      pair.value().writeJSON(pen);
      if (contentsIter.hasNext()) {
        pen.print(", ");
      } // if
    } // while
    pen.print("}");
    pen.flush();
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
    return this.contents.get(key);
  } // get(JSONString)

  /**
   * Get all of the key/value pairs.
   */
  public Iterator<KVPair<JSONString, JSONValue>> iterator() {
    return this.contents.iterator();
  } // iterator()

  /**
   * Set the value associated with a key.
   */
  public void set(JSONString key, JSONValue value) {
    this.contents.set(key, value);
  } // set(JSONString, JSONValue)

  /**
   * Find out how many key/value pairs are in the hash table.
   */
  public int size() {
    return this.contents.size();
  } // size()

} // class JSONHash
