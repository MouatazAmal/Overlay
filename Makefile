CC=javac
TARGET=Ring
BUILDPATH=classes
SRC=$(wildcard src/*.java)
OBJ=$(patsubst src/%,$(BUILDPATH)/%,$(SRC:.java=.class))
STRUC=Structure2.txt


all: $(TARGET)

Ring: $(OBJ)

$(BUILDPATH)/Ring.class : $(BUILDPATH)/Node.class $(BUILDPATH)/VirtualNetwork.class $(BUILDPATH)/PhysicalNetwork.class

$(BUILDPATH)/Node.class : $(BUILDPATH)/NodeItf.class

$(BUILDPATH)/VNode.class : $(BUILDPATH)/VNodeItf.class $(BUILDPATH)/Messages.class $(BUILDPATH)/Node.class

$(BUILDPATH)/VirtualNetwork.class : $(BUILDPATH)/VNode.class

$(BUILDPATH)/PhysicalNetwork.class : $(BUILDPATH)/Node.class

$(BUILDPATH)/%.class: src/%.java
	$(CC) -d $(BUILDPATH) -cp $(BUILDPATH) $<

clean:
	rm -rf classes/*.class

run: all
	java -cp $(BUILDPATH) Ring $(STRUC)