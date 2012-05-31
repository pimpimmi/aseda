# for power.ludat.lth.se, comment out on x86
CFLAGS	= #-m64 -O6 -g -Wall -Wextra -Werror -std=c99 -mcpu=970 -mtune=970


CC	= gcc
OUT	= fm
OBJS	= main.o name_fm.o fourier.o

all: $(OBJS)
	$(CC) $(CFLAGS) $(OBJS) -o $(OUT)
	./fm
	size name_fm.o

clean:
	rm -f $(OUT) $(OBJS)
