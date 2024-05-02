import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Stack;

/**
 * Utilities for our simple implementation of JSON.
 * 
 * @author Maya Flynn
 * @author Tim Yu
 * @author Amelia Vrieze
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
   * Saving the braces that have not yet closed
   */
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
    if (ch == '{') { // If a new JSON Hash is starting
      stack.push(ch);
      JSONHash hash = new JSONHash();
      while (stack.peek() != '}') {
        JSONString key = (JSONString) parseKernel(source);
        JSONValue value = parseKernel(source);
        hash.set(key, value);
      } // while
      stack.pop();
      stack.pop();
      ch = skipWhitespace(source);
      pushBrace(ch);
      return hash;
    } else if (ch == '-' || (ch >= '0' && ch <= '9')) { // If a new number (JSONInteger or JSONReal)
                                                        // is starting
      StringBuilder str = new StringBuilder();
      boolean real = false;
      while (ch != ',' && ch != '}' && ch != ']') {
        if (ch == '.') {
          real = true;
        } // if
        str.append((char) ch);
        ch = skipWhitespace(source);
      } // while
      pushBrace(ch);
      if (real == true) {
        return new JSONReal(str.toString());
      } // if
      return new JSONInteger(str.toString());
    } else if (ch == '\"') { // If a new JSONString is starting
      StringBuilder str = new StringBuilder();
      ch = skipWhitespace(source);
      while (ch != '\"') {
        // Code to read escape sequences
        if (ch == '\\') {
          ch = readEscapeSequence(source);
        }
        // End of code to read escape sequences
        str.append((char) ch);
        ch = source.read();
        pos++;
      } // while
      ch = skipWhitespace(source);
      pushBrace(ch);
      return new JSONString(str.toString());
    } else if (ch == '[') { // If a new JSONArray is starting
      stack.push(ch);
      JSONArray array = new JSONArray();
      while (stack.peek() != ']') {
        JSONValue value = parseKernel(source);
        array.add(value);
      } // while
      stack.pop();
      stack.pop();
      ch = skipWhitespace(source);
      pushBrace(ch);
      return array;
    } else if (ch == 't') { // if the JSONConstant 'true' is starting
      isConstant(ch, 'r', source);
      isConstant(ch, 'u', source);
      isConstant(ch, 'e', source);
      ch = skipWhitespace(source);
      pushBrace(ch);

      return JSONConstant.TRUE;
    } else if (ch == 'f') { // if the JSONConstant 'false' is starting
      isConstant(ch, 'a', source);
      isConstant(ch, 'l', source);
      isConstant(ch, 's', source);
      isConstant(ch, 'e', source);
      ch = skipWhitespace(source);
      pushBrace(ch);

      return JSONConstant.FALSE;
    } else if (ch == 'n') { // if the JSONConstant 'null' is starting
      isConstant(ch, 'u', source);
      isConstant(ch, 'l', source);
      isConstant(ch, 'l', source);
      ch = skipWhitespace(source);
      pushBrace(ch);

      return JSONConstant.NULL;
    } else if (ch == ',') { // if there is a ',' comma seperating objects
      skipWhitespace(source);
      return parseKernel(source);
    }
    throw new ParseException("Unexpected character", pos);
  } // parseKernel


  private static void isConstant(int ch, int expected, Reader source)
      throws ParseException, IOException {
    ch = skipWhitespace(source);
    if (ch != expected)
      throw new ParseException("Unexpected value", ch);
  } // isConstant


  /**
   * Determines if the character is a closed brace, and if it is, it pushes it to the stack
   */
  private static void pushBrace(int ch) throws ParseException {
    if (ch == '}') {
      if (stack.peek() == '{') {
        stack.push(ch);
      } else {
        throw new ParseException("Unexpected closed brace", pos);
      } // if
    } // if {}

    if (ch == ']') {
      if (stack.peek() == '[') {
        stack.push(ch);
      } else {
        throw new ParseException("Unexpected closed brace", pos);
      } // if
    } // if []
  } // pushBrace(int)

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

  /**
   * Returns escape sequence character by reading next character from source after having already
   * read backslash from source
   */
  static int readEscapeSequence(Reader source) throws ParseException, IOException {
    int escNext = source.read();
    pos++;
    switch (escNext) {
      case '\"':
        return '\"';
      case '\\':
        return '\\';
      case '/':
        return '/';
      case 'b':
        return '\b';
      case 'f':
        return '\f';
      case 'n':
        return '\n';
      case 't':
        return '\t';
      default:
        throw new ParseException("Invalid escape sequence", pos);
    }
  }

} // class JSON
