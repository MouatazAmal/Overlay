CC=javac
TARGET=Ring
SRC=$(wildcard *.java)
OBJ=$(SRC:.java=.class)


all: $(TARGET)

Ring: $(OBJ)

Ring.class : Node.class

Node.class : NodeItf.class

%.class: %.java
	$(CC) $< $(CFLAGS)

clean:
	rm -rf *.class $(EXEC)