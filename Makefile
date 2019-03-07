CC=javac
TARGET=Ring
BUILDPATH=classes
SRC=$(wildcard src/*.java)
OBJ=$(patsubst src/%,$(BUILDPATH)/%,$(SRC:.java=.class))


all: $(TARGET)

Ring: $(OBJ)

$(BUILDPATH)/Ring.class : $(BUILDPATH)/Node.class

$(BUILDPATH)/Node.class : $(BUILDPATH)/NodeItf.class $(BUILDPATH)/Ack.class

$(BUILDPATH)/VNode.class : $(BUILDPATH)/VNodeItf.class

$(BUILDPATH)/%.class: src/%.java
	$(CC) -d $(BUILDPATH) -cp $(BUILDPATH) $<

clean:
	rm -rf classes/*.class