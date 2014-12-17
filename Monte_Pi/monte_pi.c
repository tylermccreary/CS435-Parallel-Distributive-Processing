/*	A program to determine the value of Pi.
		Author: Tyler McCreary*/
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <math.h>
#include <time.h>

#define PRINT_VALUE 1000000
#define RANDOM_NUMBER 567342

//global variables
int done;
pthread_cond_t *multiple;
pthread_mutex_t *print_lock;

int print;
int simulations;
int inside_circ;
pthread_mutex_t *inside_circ_lock;

int total_sims_done;
pthread_mutex_t *total_sims_done_lock;

pthread_mutex_t *rand_lock;

//print function
void *print_pi(void *arg)
{
	while(!done) {
		pthread_mutex_lock (print_lock);
		while(!print) {
			pthread_cond_wait (multiple, print_lock);
		}

		//calculates approx of pi
		pthread_mutex_lock (total_sims_done_lock);
		double pi = 4 * ((double)inside_circ / total_sims_done);
		//prints approx
		printf ("Pi = %f after %i simulations with %i hits\n", pi, total_sims_done, inside_circ);
		print = 0;
		pthread_mutex_unlock (total_sims_done_lock);
		pthread_mutex_unlock (print_lock);
	}

	pthread_exit(NULL);
}

//similation threads function
void *simulator (void * arg)
{
	while(!done) {
		//generate a random point on the unit square (two random doubles in the range [0, 1.0])
		double x = ((double) rand() / (double) RAND_MAX);
		double y = ((double) rand() / (double) RAND_MAX);

		//determine if the point is within the circle
		double magnitude = sqrt(pow (x - 0.5, 2) + pow (y - 0.5, 2));
		pthread_mutex_lock (total_sims_done_lock);
		if (magnitude <= 0.5) {
			pthread_mutex_lock (inside_circ_lock);
			inside_circ++;
			pthread_mutex_unlock (inside_circ_lock);
		}
	
		total_sims_done++;

		//signal printing thread
		if (total_sims_done % PRINT_VALUE == 0) {
			pthread_mutex_lock(print_lock);
			print = 1;
			pthread_cond_broadcast(multiple);
			pthread_mutex_unlock(print_lock);
		}
		//set done to true if it gets to the number it needs
		if (total_sims_done == simulations) {
			done = 1;
			print = 1;
			pthread_cond_broadcast(multiple);
		}

		pthread_mutex_unlock (total_sims_done_lock);
	}

	pthread_exit(NULL);
}

int main(int argc, char *argv[])
{
	int n_threads;
	sscanf (argv[1], "%d", &n_threads);
	sscanf (argv[2], "%i", &simulations);
	done = 0;
	inside_circ = 0;
	total_sims_done = 0;
	print = 0;

	//initialize locks
	multiple = (pthread_cond_t *) malloc (sizeof (pthread_cond_t));
	pthread_cond_init (multiple, NULL);
	
	print_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
	pthread_mutex_init (print_lock, NULL);

	inside_circ_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
	pthread_mutex_init (inside_circ_lock, NULL);

	total_sims_done_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
	pthread_mutex_init (total_sims_done_lock, NULL);

	rand_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
	pthread_mutex_init (rand_lock, NULL);

	
	//initialize random number generator
	srand((RANDOM_NUMBER));

	//create print thread
	pthread_t *print_thread = (pthread_t *) malloc (sizeof(pthread_t *));
	if(pthread_create (print_thread, NULL, print_pi, NULL)) {
		fprintf(stderr, "Error creating print thread");
		exit(-1);
	}

	pthread_t **tid = (pthread_t **) malloc (sizeof(pthread_t *) * n_threads);

	/* Create the threads. */
	int i;
	for(i = 0; i < n_threads; i++)
	{
		tid[i] = (pthread_t *) malloc(sizeof(pthread_t *));
		if(pthread_create (tid[i], NULL, simulator, NULL))
		{
			fprintf(stderr, "Error creating thread %d.\n", i);
			exit(-1);
		}
	}

	/*Join the threads.*/
	if(pthread_join(*print_thread, NULL))
	{
		fprintf(stderr, "Error joining with print thread");
		exit(-1);
	}

	for(i = 0; i < n_threads; i ++)
	{
		if(pthread_join(*tid[i], NULL))
		{
			fprintf(stderr, "Error joining with thread %d.", i);
			exit(-1);
		}
	}

	//free everything
	free (print_thread);

	for (i = 0; i < n_threads; i++) {
		free (tid[i]);
	}

	free (tid);

	exit (0);
}


