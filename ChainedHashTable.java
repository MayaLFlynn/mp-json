import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.BiConsumer;

/**
 * A simple implementation of hash tables.
 *
 * @author Samuel A. Rebelsky
 * @author Maya Flynn
 * @author Tim Yu
 * @author Amelia Vrieze
 * @author David Rhoades (from in-class lab work)
 */
public class ChainedHashTable<K,V> implements Iterable<KVPair<K, V>> {
  // +-----------+-------------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The load factor for expanding the table.
   */
  static final double LOAD_FACTOR = 0.5;

  // +--------+----------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The number of values currently stored in the hash table. We use this to
   * determine when to expand the hash table.
   */
  int size = 0;

  /**
   * The array that we use to store the ArrayList of key/value pairs. (We use an
   * array, rather than an ArrayList, because we want to control expansion and
   * ArrayLists of ArrayLists are just weird.)
   */
  Object[] buckets;

  /**
   * Our helpful random number generator, used primarily when expanding the size
   * of the table..
   */
  Random rand;

  // +--------------+----------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new hash table.
   */
  public ChainedHashTable() {
    this.rand = new Random();
    this.clear();
  } // ChainedHashTable


  // +---------+----------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Determine if the hash table contains a particular key.
   */
  public boolean containsKey(K key) {
    try {
      get(key);
      return true;
    } catch (Exception e) {
      return false;
    } // try/catch
  } // containsKey(K)

  /**
   * Apply a function to each key/value pair.
   */
  public void forEach(BiConsumer<? super K, ? super V> action) {
    for (KVPair<K,V> pair : this) {
      action.accept(pair.key(), pair.value());
    } // for
  } // forEach(BiConsumer)

  /**
   * Get the value for a particular key.
   */
  public V get(K key) {
    int index = find(key);
    @SuppressWarnings("unchecked")
    ArrayList<KVPair<K,V>> alist = (ArrayList<KVPair<K,V>>) buckets[index];
    if (alist == null) {
      throw new IndexOutOfBoundsException("Invalid key: " + key);
    } else {
      for (KVPair<K,V> p : alist) {
        if (p.key().equals(key)) {
          return p.value();
        }
      }
      throw new IndexOutOfBoundsException("Invalid key: " + key);
    } // get
  } // get(K)

  /**
   * Iterate the keys in some order.
   */
  public Iterator<K> keys() {
    return MiscUtils.transform(this.iterator(), (pair) -> pair.key());
  } // keys()

  /**
   * Set a value.
   */
  @SuppressWarnings("unchecked")
  public V set(K key, V value) {
    V result = null;
    // If there are too many entries, expand the table.
    if (this.size > (this.buckets.length * LOAD_FACTOR)) {
      expand();
    } // if there are too many entries

    // Find out where the key belongs and put the pair there.
    int index = find(key);
    ArrayList<KVPair<K,V>> alist = (ArrayList<KVPair<K,V>>) this.buckets[index];
    // Special case: Nothing there yet
    if (alist == null) {
      alist = new ArrayList<KVPair<K,V>>();
      this.buckets[index] = alist;
    }
    for (int i = 0; i < alist.size(); i++) {
      if(alist.get(i).key().equals(key)) {
        alist.set(i, new KVPair<K,V>(key, value));
        return value;
      } // if
    } // for
    alist.add(new KVPair<K,V>(key, value));
    ++this.size;

    // And we're done
    return result;
  } // set(K,V)

  /**
   * Get the size of the dictionary - the number of values stored.
   */
  public int size() {
    return this.size;
  } // size()

  /**
   * Iterate the values in some order.
   */
  public Iterator<V> values() {
    return MiscUtils.transform(this.iterator(), (pair) -> pair.value());
  } // values()

  /**
   * Returns the hash code of this hash table.
   */
  public int hashCode() {
    long result = 6491;
    for (KVPair<K,V> pair : this) {
      result += pair.hashCode();
      result = result % Integer.MAX_VALUE;
    } // for
    return (int) result;
  } // hashCode()

  // +------------------+--------------------------------------------
  // | Iterator methods |
  // +------------------+

  /**
   * Iterate the key/value pairs in some order.
   */
  public Iterator<KVPair<K,V>> iterator() {
    // KVPair<K,V> next;
    
    return new Iterator<KVPair<K,V>>() {
      int bucket = 0;
      int bucketAlistIndex = 0;

      @SuppressWarnings("unchecked")
      public boolean hasNext() {
        if (ChainedHashTable.this.buckets[bucket] != null
            && bucketAlistIndex < ((ArrayList<KVPair<K,V>>) ChainedHashTable.this.buckets[bucket]).size()) {
          return true;
        } // if
        // Otherwise, we try to find the next bucket
        try {
          findNextBucketIndex();
          return true;
        } catch (Exception e) {
          return false;
        } // try catch
      } // hasNext()

      @SuppressWarnings("unchecked")
      public KVPair<K,V> next() {
        if (ChainedHashTable.this.buckets[bucket] != null
            && bucketAlistIndex < ((ArrayList<KVPair<K,V>>) ChainedHashTable.this.buckets[bucket]).size()) {
          KVPair<K,V> toReturn = ((ArrayList<KVPair<K,V>>) ChainedHashTable.this.buckets[bucket]).get(bucketAlistIndex);
          bucketAlistIndex++;
          return toReturn;
        } // try catch
        try {
          bucket = findNextBucketIndex();
          bucketAlistIndex = 0;
          KVPair<K,V> toReturn = ((ArrayList<KVPair<K,V>>) ChainedHashTable.this.buckets[bucket]).get(bucketAlistIndex);
          bucketAlistIndex++;
          return toReturn;
        } catch (Exception e) {
          throw new NoSuchElementException();
        } // try catch
      } // next()

      @SuppressWarnings("unchecked")
      int findNextBucketIndex() throws Exception {
        int candidate;
        for (candidate = bucket + 1; candidate < ChainedHashTable.this.buckets.length; candidate++) {
          Object candidateBucket = ChainedHashTable.this.buckets[candidate];
          if (candidateBucket != null
              && !((ArrayList<KVPair<K,V>>) candidateBucket).isEmpty()) {
            return candidate;
          } // if
        } // for
        throw new Exception("no more buckets");
      } // findNextBucketIndex()
    }; // new Iterator
  } // iterator()

  // +-------------------+-------------------------------------------
  // | HashTable methods |
  // +-------------------+

  /**
   * Clear the whole table.
   */
  public void clear() {
    this.buckets = new Object[41];
    this.size = 0;
  } // clear()

  /**
   * Dump the hash table.
   */
  public void dump(PrintWriter pen) {
    pen.println("Capacity: " + this.buckets.length + ", Size: " + this.size);
    for (int i = 0; i < this.buckets.length; i++) {
      @SuppressWarnings("unchecked")
      ArrayList<KVPair<K,V>> alist = (ArrayList<KVPair<K,V>>) this.buckets[i];
      if (alist != null) {
        for (KVPair<K,V> pair : alist) {
          pen.println("  " + i + ": <" + pair.key() + "(" + pair.key().hashCode()
              + "):" + pair.value() + ">");
        } // for each pair in the bucket
      } // if the current bucket is not null
    } // for each bucket
  } // dump(PrintWriter)

  // +---------+---------------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Expand the size of the table.
   */
  void expand() {
    // Figure out the size of the new table
    int newSize = 2 * this.buckets.length + rand.nextInt(10);
    // Remember the old table
    Object[] oldBuckets = this.buckets;
    // Create a new table of that size.
    this.buckets = new Object[newSize];
    // Move all buckets from the old table to their appropriate
    // location in the new table.
    // Temporarily set size to 0, since set handles size
    this.size = 0;
    for (Object obj : oldBuckets) {
      if (obj != null) {
        // type cast
        @SuppressWarnings("unchecked")
        ArrayList<KVPair<K,V>> alist = (ArrayList<KVPair<K,V>>) obj;
        // rehash each element and set to right place
        for (KVPair<K,V> p : alist) {
          this.set(p.key(), p.value());
        } // for
      } // if
    } // for
  } // expand()

  /**
   * Find the index of the entry with a given key. If there is no such entry,
   * return the index of an entry we can use to store that key.
   */
  int find(K key) {
    return Math.abs(key.hashCode()) % this.buckets.length;
  } // find(K)

} // class ChainedHashTable<K,V>