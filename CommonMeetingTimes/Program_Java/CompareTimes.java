/** A class to instantiate a thread that checks for common meeting times
 *  between two arrays.
 */
public class CompareTimes implements Runnable {

    int time;
    int [] person2;
    int [] person3;

    /** Construct and initialize a frame.
     *  @param time The time to be compared for each person
     *  @param person2 The array of times available for the second person
     *  @param person3 The array of times available for the third person
     */
    public CompareTimes (int time, int [] person2, int [] person3) {
        this.time = time;
        this.person2 = person2;
        this.person3 = person3;
    }

    /** Begin the execution of this object as a thread.
     */
    public void run() {
        int i;
        for (i = 0; i < person2.length; i++) {
            if (person2[i] == time) {
                int j;
                for (j = 0; j < person3.length; j++) {
                    if (person3[j] == time) {
                        System.out.println (time + " is a common meeting time.");
                    }
                }
            }
        }
    }  
}
         
