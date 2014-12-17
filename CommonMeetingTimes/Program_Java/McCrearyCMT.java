import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/** A program to determine common meeting times between three people
 */
public class McCrearyCMT {
    
    public static void main (String [] args) {
        String fileName = args[0];

        try {
            Scanner inFile = new Scanner(new FileReader(fileName));
            
            int i;
            //The number of threads to be created
            int nThreads = inFile.nextInt();
            //Possible times for the first person
            int [] person1 = new int[nThreads];
            for(i = 0; i < nThreads; i++) {
                person1[i] = inFile.nextInt();
            }

            int times = inFile.nextInt();
            //Possible times for the second person
            int [] person2 = new int[times];
            for(i = 0; i < times; i++) {
                person2[i] = inFile.nextInt();
            }

            times = inFile.nextInt();
            //Possible times for the third person
            int [] person3 = new int[times];
            for(i = 0; i < times; i++) {
                person3[i] = inFile.nextInt();
            }
            
            //Create arrays to hold frames and threads
            CompareTimes [] frame = new CompareTimes[nThreads];
            Thread [] t = new Thread[nThreads];
            
            //Create the objects that will run as threads
            for (i = 0; i < nThreads; i ++) {
                frame[i] = new CompareTimes(person1[i], person2, person3);
            }
            
            //Create the threads
            for (i = 0; i < nThreads; i++) {
                t[i] = new Thread(frame[i]);
            }
            
            //Start the threads
            for (i = 0; i < nThreads; i++) {
                t[i].start();
            }
            
            //Join the threads
            for (i = 0; i < nThreads; i++) {
                try {
            	    t[i].join();
                } catch (InterruptedException e) {
            	    System.out.println("Thread was interrupted.");
                }
            }
            
            inFile.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 
    }
}
            
