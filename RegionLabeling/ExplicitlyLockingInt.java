import java.util.concurrent.locks.ReentrantLock;

/** An atomic integer that achieves atomicity through explicit 
 * locking and unlocking. 
 * 
 * @author matthews
 *
 */
public class ExplicitlyLockingInt implements AtomicInt {

    private int i;
    private ReentrantLock theLock;
	
    /** Instantiate a new ExplicitlyLockingInt that is assigned
     *  a value of 0
     */
    public ExplicitlyLockingInt() {
    	i = 0;
    	theLock = new ReentrantLock();
    }
	
    /** Instantiate a new ExplicitlyLockingInt
     * 
     * @param initialVal the integer value
     */
    public ExplicitlyLockingInt (int initialVal) {
    	i = initialVal;
    	theLock = new ReentrantLock();
    }
	
    public void add(int value) {
		theLock.lock();
		i += value;
		theLock.unlock();
    }

    public int get() {
    	// Technically, we don't need to take the lock here.
    	theLock.lock();
    	int val = i;
    	theLock.unlock();
    	return val;
    }

    public void set(int value) {
	theLock.lock();
	i = value;
	theLock.unlock();
    }

}
