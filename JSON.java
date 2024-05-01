import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Stack;

/* TO DO:
 * -Constants should actually check to see if they are the right words
 * -Error checking: braces and brackets should match each other in the stack (check when pushing)
 * -Literally everything else
 */

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

  /**
   * saving a character that was read but not used
   */
  static int cached;

  static Stack<Integer> stack = new Stack<Integer>();

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
    stack.push(-1);
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
      stack.push(ch);
      JSONHash hash = new JSONHash();
      while (stack.peek() != '}') {
        JSONString key = (JSONString) parseKernel(source);
        JSONValue value = parseKernel(source);
        hash.set(key, value);
      }
      stack.pop();
      stack.pop();
      ch = skipWhitespace(source);
      if (ch == ']' || ch == '}') {
        stack.push(ch);
      }
      return hash;
    } else if (ch == '-' || ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4'
        || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9') {
      StringBuilder str = new StringBuilder();
      boolean real = false;
      while (ch != ',' && ch != '}' && ch != ']') {
        if (ch == '.') {
          real = true;
        }
        str.append((char) ch);
        ch = skipWhitespace(source);
      }
      if (ch == '}' || ch == ']') { 
        stack.push(ch);
      }
      if (real == true) {
        return new JSONReal(str.toString());
      }
      return new JSONInteger(str.toString());

    } else if (ch == '\"') {
      StringBuilder str = new StringBuilder();
      ch = skipWhitespace(source);
      while (ch != '\"') {
        str.append((char) ch);
        ch = source.read();
        pos++;
      }
      ch = skipWhitespace(source);
      if (ch == ']' || ch == '}') {
        stack.push(ch);
      }
      return new JSONString(str.toString());
    } else if (ch == '[') {
      stack.push(ch);
      JSONArray array = new JSONArray();
      while (stack.peek() != ']') {
        JSONValue value = parseKernel(source);
        array.add(value);
      } // while
      stack.pop();
      stack.pop();
      ch = skipWhitespace(source);
      if (ch == ']' || ch == '}') {
        stack.push(ch);
      }
      return array;
    } else if (ch == 't') {
      while (Character.isLetter((char) ch)) {
        ch = skipWhitespace(source);
      }
      if (ch == ']' || ch == '}') {
        stack.push(ch);
      }
      return JSONConstant.TRUE;
    } else if (ch == 'f') {
      while (Character.isLetter((char) ch)) {
        ch = skipWhitespace(source);
      }
      if (ch == ']' || ch == '}') {
        stack.push(ch);
      }
      return JSONConstant.FALSE;
    } else if (ch == 'n') {
      while (Character.isLetter((char) ch)) {
        ch = skipWhitespace(source);
      }
      if (ch == ']' || ch == '}') {
        stack.push(ch);
      }
      return JSONConstant.NULL;
    } else if (ch == ',') {
      skipWhitespace(source);
      return parseKernel(source);
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
   * Determine if a character is JSON whitespace (newline, carriage return, space, or tab).
   */
  static boolean isWhitespace(int ch) {
    return (' ' == ch) || ('\n' == ch) || ('\r' == ch) || ('\t' == ch);
  } // isWhiteSpace(int)

} // class JSON
