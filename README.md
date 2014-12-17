CS435-Parallel-Distributive-Processing
======================================
This repository consists of programs I wrote for a Parallel Distributive Processing Class.

To compile and run the C version of the common meeting times program, go to CommonMeetingTimes/Program_C/ and run: 

gcc -o name meeting_times.c -lpthread

./name (filename)

For the java version, go to CommonMeetingTimes/Program_Java/ and run:

javac McCrearyCMT.java

java McCrearyCMT (filename)

To compile and run the Monte Pi program, go to Monte_Pi/ and run:

gcc -o name monte_pi.c -lpthread -lm

./name (# of threads) (# of simulations)

To compile and run the Regioin Labeling program, go to RegionLabeling/ and run:

javac McCrearyRL.java

Java McCrearyRL (filename)
