/* Driver program for the Fourier-Motzkin project in the course
 * EDAF15 Algorithm at Lund University.
 * 
 * To use it, write a function that takes two file names and a time
 * as input parameters. Since all project's function's will be put
 * in the same program, try to find a unique name, based e.g. on your
 * name or account. In the example below, the function is called name_fm.
 * So, change that to something different!
 * 
 * In a separate file, called "name_fm.c", make your function complete.
 * That file should have one externally visible symbol, namely your 
 * Fourier-Motzkin function.
 *
 * Edit the makefile to refer to the file name with your function.
 * 
 * By adding multiple functions in different files and the array fm 
 * declared in this file, you can benchmark different versions of your
 * project.
 * 
 * When your function is called with seconds = 0, your function should
 * return 1 if there is a solution and 0 if none exists. Otherwise, your
 * function should return the number of times it solved the system.
 *
 * The array correct contains the correct answers, i.e. the systems in
 * input/0 and input/2 have solutions while the other have no solutions.
 *
 * Your program should work on both the 64-bit Power machine and any x86
 * machine such as the login machines, e.g. login-11.student.lth.se.
 *
 */

#include <stdio.h>
#include <stdlib.h>

unsigned long long name_fm(char* aname, char* cname, int seconds);

#define ENTRY(id)	{ .name = #id, .func = id, }
#define NAME_WIDTH	(20)
#define COUNT_WIDTH	(20)
#define	SPACE		(4)
#define WIDTH		(SPACE + NAME_WIDTH + COUNT_WIDTH)

static struct fm {
	char*			name;
	unsigned long long	(*func)(char*, char*, int);
	unsigned long long	count;
} fm[] = { 
	ENTRY(name_fm),
};

static unsigned int correct[] = { 1, 0, 1, 0, 0, 0 };

static int compare_count(const void* ap, const void* bp)
{
	const struct fm*	a = ap;
	const struct fm*	b = bp;

	if (a->count > b->count)
		return -1;
	else if (a->count < b->count)
		return +1;
	else
		return 0;
}

int main(int argc, char** argv)
{
	size_t			nfunc;
	size_t			i;
	size_t			j;
	unsigned long long	result;
	char			a[BUFSIZ];
	char			c[BUFSIZ];
	size_t			pass;
	size_t			ntest;
	int			seconds = 4;

	if (argc > 1 
		&& sscanf(argv[1], "%d", &seconds) == 1
		&& seconds < 0) {
		fprintf(stderr, "invalid parameter for seconds: %d\n", seconds);
		exit(1);
	}

	ntest = sizeof correct/sizeof correct[0];
	nfunc = sizeof fm/sizeof fm[0];

	for (i = 0; i < nfunc; ++i) {

		pass = 0;

		for (j = 0; j < ntest; ++j) {
			snprintf(a, sizeof a, "input/%zu/A", j);
			snprintf(c, sizeof c, "input/%zu/c", j);

			result = (*fm[i].func)(a, c, 0);

			if (result == correct[j])
				pass += 1;
			else {
				printf("\"%s\" failed test %zu: "
					"return value %llu but "
					"the system has %s solution.\n", 
					fm[i].name, j, result,
					correct[j] ? "a" : "no");
			}
		}

		if (pass < ntest)
			printf("\"%s\" FAILED %zu/%zu TESTS.\n", fm[i].name,
				ntest - pass, ntest);

		else {
			printf("\"%s\" PASSED ALL %zu TESTS.\n", 
				fm[i].name, ntest);

			for (j = 0; j < ntest; ++j) {
				printf("Counting test %zu: ", j);
				fflush(stdout);
				snprintf(a, sizeof a, "input/%zu/A", j);
				snprintf(c, sizeof c, "input/%zu/c", j);

				result = (*fm[i].func)(a, c, seconds);
				printf("%*llu\n", COUNT_WIDTH, result);
				fm[i].count += result;
			}
		}
	}

	/* Sort in descending order of number of solved systems. */
	qsort(fm, nfunc, sizeof fm[0], compare_count);

	for (j = 0; j < WIDTH; ++j)
		putchar('=');
	putchar('\n');

	/* Print out function with most solved systems first... */
	for (j = 0; j < nfunc; ++j)
		printf("%2zu %-*s %*llu\n", j+1, 
			NAME_WIDTH ,fm[j].name, 
			COUNT_WIDTH ,fm[j].count);

	for (j = 0; j < WIDTH; ++j)
		putchar('=');
	putchar('\n');
	return 0;
}
