import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;

/** A program to label regions in a two-dimensional array based 
 * on the neighbors of each position.
 * 
 * @author Tyler McCreary
 *
 */
public class McCrearyRL {

	public static void main(String[] args) {
		String fileName = args[0];
        try {
            Scanner inFile = new Scanner(new FileReader(fileName));
  
            int row = inFile.nextInt();
            int col = inFile.nextInt();
            ExplicitlyLockingInt [][] board = new ExplicitlyLockingInt[row][col];
            ExplicitlyLockingInt [][] origBoard = new ExplicitlyLockingInt[row][col];
            int i;
            int j;
            for(i = 0; i < row; i++) {
                for(j = 0; j < col; j++) {
                	ExplicitlyLockingInt eli = new ExplicitlyLockingInt(inFile.nextInt());
                	origBoard[i][j] = eli;
                }
            }

            for (i = 0; i < row; i++) {
			    for (j = 0; j < col; j++) {
			    	ExplicitlyLockingInt eli = new ExplicitlyLockingInt((i * col) + j);
				    board[i][j] = eli;
			    }
		    }
            
            CyclicBarrier barrier = new CyclicBarrier(row);
            CyclicBarrier barrier2 = new CyclicBarrier(row);
            
            RegionUpdater [] frame = new RegionUpdater[row];
            Thread [] t = new Thread[row];
            
            ExplicitlyLockingInt done = new ExplicitlyLockingInt(0);
            ExplicitlyLockingInt allDone = new ExplicitlyLockingInt(0);
            
            for (i = 0; i < row; i++) {
                frame[i] = new RegionUpdater(i, origBoard, board, row, col, barrier, barrier2, done, allDone);
            }
            
            for (i = 0; i < row; i++) {
            	t[i] = new Thread(frame[i]);
            }
            
            for (i = 0; i < row; i++) {
            	t[i].start();
            }

            for (i = 0; i < row; i++) {
            	try {
            		t[i].join();
            	} catch (InterruptedException e) {
            		System.out.println("Oops, thread " +
            							i + " was interrupted.");
            	}
            }
            
            inFile.close();
            
            for (i = 0; i < row; i++) {
            	for (j = 0; j < col; j++) {
                	if (j == col - 1) {
                		System.out.println(board[i][j].get());
                	} else {
                		System.out.print(board[i][j].get() + "	");
                	}
            	}
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
	}

}

