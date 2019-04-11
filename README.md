# Overlay_GroupB

# AUTHORS

	AMAL Mouataz : mouataz.amal@gmail.com
	LONGA Benjamin : benjamin.longa@gmail.com

# COMMANDS
	To compile : make
	To run (default Structure) : make run
	To run (specific structure) : make run STRUC=StructureFileName.txt
	To clean object files : make clean

# Implementation 
	1 - Use java rmi for communication
	2 - Class PhysicalNetwork to represent the physical network and setting it up containes physical nodes
	3 - Same for virtual network.


# Information printed after running the application

	1 - Print a network overview
	2 - Print routing tables
	3 - physical and virtual nodes association
	
# How to use the application

	send <SenderID> <TergetID> <Message contents>
	Exemple : send 1 8 Hello you
	Quit : quit
