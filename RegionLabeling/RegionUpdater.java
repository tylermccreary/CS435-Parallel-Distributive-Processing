import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

/** A class instantiated in multiple threads to compare and update
 * positions in a two-dimensional array based on the neighbors of each 
 * position. 
 * 
 * @author Tyler McCreary
 *
 */
public class RegionUpdater implements Runnable {
	
	private int row;
	private ExplicitlyLockingInt [][] board;
	private ExplicitlyLockingInt [][] origBoard;
	private ExplicitlyLockingInt done;
	private ExplicitlyLockingInt allDone;
	private int numRows;
	private int numCols;
	final CyclicBarrier barrier;
	final CyclicBarrier barrier2;
	
	/** Constructs a RegionUpdater
	 * 
	 * @param row the row in the two-dimensional array
	 * @param origBoard the two-dimensional array entered by the user
	 * @param board the two-dimensional array to be labeled based on 
	 * the regions in origBoard
	 * @param numRows the number of rows in the two-dimensional arrays
	 * @param numCols the number of rows in the two-dimensional arrays
	 * @param barrier the first CyclicBarrier
	 * @param barrier2 the second CyclicBarrier
	 * @param done the condition for threads to stop comparing labels
	 * @param allDone the condition that keeps track of when label 
	 * changing stops
	 */
	RegionUpdater (int row, ExplicitlyLockingInt [][] origBoard, ExplicitlyLockingInt [][] board,
							int numRows, int numCols, CyclicBarrier barrier,
							CyclicBarrier barrier2, ExplicitlyLockingInt done, ExplicitlyLockingInt allDone) {
		this.row = row;
		this.board = board;
        this.origBoard = origBoard;
		this.numRows = numRows;
		this.numCols = numCols;
		this.barrier = barrier;
		this.barrier2 = barrier2;
		this.done = done;
		this.allDone = allDone;
	}
	
	public void run() {
		int max;
		int i;
		
		while (done.get() == 0) {
            for (i = 0; i < numCols; i++) {
			    max = board[row][i].get();
			    if (row - 1 >= 0) {
				    if (board[row - 1][i].get() > max && origBoard[row - 1][i].get() == origBoard[row][i].get()) {
			    		max = board[row - 1][i].get();
			    		allDone.set(0);
			    	}
			    }
			    if (row + 1 < numRows) {
			    	if (board[row + 1][i].get() > max && origBoard[row + 1][i].get() == origBoard[row][i].get()) {
			    		max = board[row + 1][i].get();
			    		allDone.set(0);
			    	}
			    }
			    if (i - 1 >= 0) {
			    	if (board[row][i - 1].get() > max && origBoard[row][i - 1].get() == origBoard[row][i].get()) {
			    		max = board[row][i - 1].get();
			    		allDone.set(0);
			    	}
			    }
			    if (i + 1 < numCols) {
			    	if (board[row][i + 1].get() > max && origBoard[row][i + 1].get() == origBoard[row][i].get()) {
			    		max = board[row][i + 1].get();
			    		allDone.set(0);
			    	}
			    }
			    board[row][i].set(max);
            }

			try {
				barrier.await();
			} catch (InterruptedException e) {
				System.out.println("The thread was interrupted.");
			} catch (BrokenBarrierException e) {
				System.out.println("Broken Barrier Exception.");
			}
			try {
				if (row == 0) {
					done.set(1);
					if (allDone.get() == 0) {
						done.set(0);
					}
					allDone.set(1);
				}
				barrier2.await();
			} catch (InterruptedException e) {
				System.out.println("The thread was interrupted.");
			} catch (BrokenBarrierException e) {
				System.out.println("Broken Barrier Exception.");
			}
		}
	}
}

