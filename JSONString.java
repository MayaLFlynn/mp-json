import java.io.PrintWriter;

/**
 * JSON strings.
 * 
 * @author Maya Flynn
 * @author Tim Yu
 * @author Amelia Vrieze
 */
public class JSONString implements JSONValue {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The underlying string.
   */
  String value;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Build a new JSON string for a particular string.
   */
  public JSONString(String value) {
    this.value = value;
  } // JSONString(String)

  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    return this.value.toString();
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    if (other instanceof JSONString) {
      return this.value.equals(((JSONString) other).getValue());
    }
    return false;
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    return this.value.hashCode();
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
    String rawString = this.value.toString();

    // change escape characters to text file representation
    rawString = rawString.replace("\"", "\\\"");
    rawString = rawString.replace("\\", "\\\\");
    rawString = rawString.replace("\b", "\\b");
    rawString = rawString.replace("\f", "\\f");
    rawString = rawString.replace("\n", "\\n");
    rawString = rawString.replace("\r", "\\r");
    rawString = rawString.replace("\t", "\\t");

    pen.print("\"" + rawString + "\"");
    pen.flush();
  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public String getValue() {
    return this.value;
  } // getValue()

} // class JSONString
