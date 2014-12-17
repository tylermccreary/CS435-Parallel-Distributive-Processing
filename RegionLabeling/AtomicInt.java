/** An interface for an object that holds an integer
 * allows only atomic operations.
 *
 * @author matthews
 *
 */
public interface AtomicInt {

  /** Set the value of the atomic int.
   *  @param value the value
   */
  void set (int value);

  /** Get the calue of the atomic int,
   *  @return the value of the int
   */
  int get();

  /** Increase the value of the atomic int.
   *  @param value the amount to increase
   */
  void add (int value);
}
