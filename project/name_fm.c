#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

static unsigned long long	fm_count = 0;
static volatile bool		proceed = false;

static void done(int unused)
{
	proceed = false;
	unused = unused;
}
	
unsigned long long name_fm(char* aname, char* cname, int seconds)
{
	FILE*		afile = fopen(aname, "r");
	FILE*		cfile = fopen(cname, "r");

	if (afile == NULL) {
		fprintf(stderr, "could not open file A\n");
		exit(1);
	}

	if (cfile == NULL) {
		fprintf(stderr, "could not open file c\n");
		exit(1);
	}

	size_t rows, cols;
	fscanf(afile, "%zu %zu\n", &rows, &cols);
	double a[rows][cols];
	fscanf(cfile, "%zu\n", &rows);
	double c[rows];
	size_t i, j;
	for(i = 0; i < rows; i += 1){
		for(j = 0; j < cols; j += 1)
			fscanf(afile, "%lf\t", &a[i][j]);
		fscanf(afile, "\n");
		fscanf(cfile, "%lf\n", &c[i]);
	}


	if (seconds == 0) {
		/* Just run once for validation. */
			
		// Uncomment when your function and variables exist...
		// return fm_elim(rows, cols, a, c);
		return 1; // return one, i.e. has a solution for now...
	}

	/* Tell operating system to call function DONE when an ALARM comes. */
	signal(SIGALRM, done);
	alarm(seconds);

	/* Now loop until the alarm comes... */
	proceed = true;
	while (proceed) {
		// Uncomment when your function and variables exist...
		// fm_elim(rows, cols, a, c);

		fm_count++;
	}

	return fm_count;
}
