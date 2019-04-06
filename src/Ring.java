import java.util.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class Ring {

	private static int nbNodes;
	private static ArrayList<Node> nodes;
	private static Hashtable<Integer, Node> nodesH;
	private static ArrayList<NodeItf> nodesItf;
	private static Hashtable<Integer, NodeItf> nodesItfH;
	private static ArrayList<VNodeItf> vNodes;

	public static void main (String[] args){
		if (args.length < 1) {
			System.out.println("Usage : java Ring <StructureFile>");
			return;
		}
		File fileInput = new File(args[0]);
		try {
			Scanner sc = new Scanner(fileInput);
			nbNodes = sc.nextInt();
			nodesItf = new ArrayList<NodeItf>();
			nodes = new ArrayList<Node>();
			nodesH = new Hashtable<Integer, Node>();
			nodesItfH = new Hashtable<Integer, NodeItf>();
			int id;
			//Creation des nodes
			for (int i = 1 ; i <= nbNodes ; i++) {
				id = sc.nextInt();
				Node n = new Node(id);
				nodes.add(n);

				NodeItf n_stub = (NodeItf) UnicastRemoteObject.exportObject(n, 0);

				nodesItf.add(n_stub);
			}
			sc.nextLine();
			//Attribution des neighbours
			for (int i = 0 ; i < nbNodes ; i++) {
				sc.nextInt();
				for (int j = 0 ; j < nbNodes ; j++) {
					if (sc.nextInt() == 1) {
						nodesItf.get(i).addNeighbour(nodesItf.get(j));
					}
				}
			}

			//Affichage voisins
			for (int i = 0 ; i < nbNodes ; i++){
				System.out.println("Node : " + nodes.get(i).getId());
				System.out.println("Neighbours : ");
				for (int j = 0 ; j < nodesItf.get(i).getNeighbours().size() ; j++) {
					System.out.println(nodesItf.get(i).getNeighbours().get(j).getId() + " ");
				}
			}
			sc.close();
		} catch (Exception e) {
			System.err.println("Error : " + e);
		}
		try {
			System.out.println(" - - - - - - - - - - ");
			for (int i = 0 ; i < nbNodes ; i++) {
				nodesItf.get(i).setup();
				for (int j = 0 ; j < nbNodes ; j++) {
					nodesItf.get(j).reset();
				}	
			}
		} catch (Exception e) {
			System.err.println("Error : " + e);
		}
		VirtualNetwork vn = new VirtualNetwork(nodes);
		Scanner inputSc = new Scanner(System.in);
		String input = "";
		String[] split;
		while (true) {
			input = inputSc.nextLine();
			split = input.split("\\s+");
			if (input.startsWith("send")) {
				try {
					vn.getNodes().get(Integer.parseInt(split[1])-1).firstSend(Integer.parseInt(split[2]), split[3]);
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		}
	}
}