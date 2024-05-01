import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;

/**
 * Utilities for our simple implementation of JSON.
 */
public class JSON {
  // +---------------+-----------------------------------------------
  // | Static fields |
  // +---------------+

  /**
   * The current position in the input.
   */
  static int pos;

  // +----------------+----------------------------------------------
  // | Static methods |
  // +----------------+

  /**
   * Parse a string into JSON.
   */
  public static JSONValue parse(String source) throws ParseException, IOException {
    return parse(new StringReader(source));
  } // parse(String)

  /**
   * Parse a file into JSON.
   */
  public static JSONValue parseFile(String filename) throws ParseException, IOException {
    FileReader reader = new FileReader(filename);
    JSONValue result = parse(reader);
    reader.close();
    return result;
  } // parseFile(String)

  /**
   * Parse JSON from a reader.
   */
  public static JSONValue parse(Reader source) throws ParseException, IOException {
    pos = 0;
    JSONValue result = parseKernel(source);
    if (-1 != skipWhitespace(source)) {
      throw new ParseException("Characters remain at end", pos);
    }
    return result;
  } // parse(Reader)

  // +---------------+-----------------------------------------------
  // | Local helpers |
  // +---------------+

  /**
   * Parse JSON from a reader, keeping track of the current position
   */
  static JSONValue parseKernel(Reader source) throws ParseException, IOException {
    int ch;
    ch = skipWhitespace(source);
    if (-1 == ch) {
      throw new ParseException("Unexpected end of file", pos);
    }
    // STUB
    
    // "
    // {
    // [
    // -
    // : -> but only after a "" I think
    // , -> but only after other things
    // \ -> but only in a string
    // 0123456789
    // . -> but only after an integer

    if (ch == '{') {
      JSONHash hash = new JSONHash();
      String str = "";
      ch = source.read();
      pos++;
      while (ch != '}') {
        str += ch;
        ch = source.read();
        pos++;
      }
      String[] arr = str.split(","); //this doesn't work because of arrays but idk what else to do
      for (String value : arr) {
        String k = value.substring(0, value.indexOf(':'));
        String v = value.substring(value.indexOf(':'));
        hash.set((JSONString) parse(k), parse(v));
      }
    } else if(ch == '-' || ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9') {
      String str = "";
      boolean real = false;
      while (ch != -1) {
        if (ch == '.') {
          real = true;
        }
        str+= ch;
        ch = source.read();
        pos++;
      }
      if (real == true) {
        return new JSONReal(str);
        //can BigDecimal parse strings where the number is in scientific notation?
      }
      return new JSONInteger(str);
    
    } else if (ch == '\"') {
      String str = "";
      while (ch != '\"') {
        str+= ch;
        ch = source.read();
        pos++;
      }
      return new JSONString(str);
    } 



    throw new ParseException("Unimplemented", pos);
  } // parseKernel

  /**
   * Get the next character from source, skipping over whitespace.
   */
  static int skipWhitespace(Reader source) throws IOException {
    int ch;
    do {
      ch = source.read();
      ++pos;
    } while (isWhitespace(ch));
    return ch;
  } // skipWhitespace(Reader)

  /**
   * Determine if a character is JSON whitespace (newline, carriage return,
   * space, or tab).
   */
  static boolean isWhitespace(int ch) {
    return (' ' == ch) || ('\n' == ch) || ('\r' == ch) || ('\t' == ch);
  } // isWhiteSpace(int)

} // class JSON
