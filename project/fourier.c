#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <float.h>
#include <limits.h>

// - Multiply every t_rj to same determinator instead?
bool fm_elim(size_t rows, size_t cols, double** a, double* c)
{
   size_t i, j;    

   // Copying a and c
   // - Use reallocate with static memory better?
   // - Fastest with rows or column first?
   // - Use memcpy instead of copying one by one?
   double **t = malloc(rows*sizeof(double*));
   double *q = malloc(rows*sizeof(double));
   for (i = 0; i < rows; i += 1){
       t[i] = malloc(cols*sizeof(double));
       q[i] = c[i];
       for(j = 0; j < cols; j += 1)
           t[i][j] = a[i][j];
   }

   size_t r = cols;
   size_t s = rows;
   size_t n1;
   size_t n2;
   while(1){

       // Determine n1 and n2.
       for(i = 0; i < r; i += 1){
           if(t[i][r-1] > 0)
               n1 += 1;
           else if(t[i][r-1] < 0)
               n2 += 1;
       }
       n2 += n1;

       // Sort positive first, then negative, last 0.
       // - t_temp can be reused?
       double **t_temp = malloc(r*sizeof(double*));
       size_t n1_i = 0, n2_i = n1, n3_i = n2;
       size_t cur = 0;
       while(n1_i != n1 || n2_i != n2 || n3_i != r){
           if(t[cur][r-1] > 0)
               t_temp[n1_i++] = t[cur];
           else if(t[cur][r-1] < 0)
               t_temp[n2_i++] = t[cur];
           else
               t_temp[n3_i++] = t[cur];
           cur++;
       }
       free(t);
       t = t_temp;
       
       // - Can be done with one for-loop?
       for(i = 0; i < r; i += 1)
           for(j = 0; j < n2; j += 1)
               t[j][i] /= t[j][r];
       for(j = 0; j < n2; j += 1)
           q[j] /= t[j][r];
       if(r == 0){
        double tmp = DBL_MAX;
        int min = 0;

	int max = 0;
        for(i = 0; i < n1; i +=1)
            if(q[i] < tmp){
                tmp = q[i];
                min = i;
            }

	tmp = 0;

	for(i = n1; i < n2; i +=1)
            if(q[i] > tmp){
                tmp = q[i];
                max = i;
            }
       
          if (q[max] > q[min]) //b1 > B1
            return false;
        for(; n2 < s; n2 += 1)
            if(q[n2] < 0)
                return false;
        return true;
	}
	size_t s_pr = s - n2 + n1 * (n2 - n1);
	if (s_pr == 0)
	return true;
	
	double **t2 = malloc((r-1)*sizeof(double*));
       	double *q2 = malloc(s_pr*sizeof(double));
	for (i = 0; i < r-1; i += 1)
          t2[i] = malloc(s_pr*sizeof(double));
    int k;
    for(i = 0; i < n1; i += 1)
        for(j = n1; j < n2; j += 1){
            q2[i*(n2-n1)+j-n1] = q[i] - q[j];
    for(k = 0; k < r-1; k += 1)
                t2[i*(n2-n1)+j-n1][k] = t[i][k] - t[j][k];       
}
for(i = n2; i < s; i += 1){
    q2[s_pr-s+i] = q[i];
    for(k = 0; k < r-1; k += 1)
        t2[s_pr-s+i][k] = t[i][k];       
}
free(t);
free(q);
q = q2;
t = t2;
   }    
}
