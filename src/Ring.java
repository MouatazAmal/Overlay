import java.util.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class Ring {

	private static int nbNodes;
	private static ArrayList<Node> nodes;
	private static ArrayList<NodeItf> nodesItf;
	private static ArrayList<VNodeItf> vNodes;

	public static void main (String[] args){
		if (args.length < 1) {
			System.out.println("Usage : java Ring <StructureFile>");
			return;
		}
		File input = new File(args[0]);
		try {
			Scanner sc = new Scanner(input);
			nbNodes = sc.nextInt();
			nodesItf = new ArrayList<NodeItf>();
			nodes = new ArrayList<Node>();
			//Creation des nodes
			for (int i = 1 ; i <= nbNodes ; i++) {
				Node n = new Node(i);
				nodes.add(n);

				NodeItf n_stub = (NodeItf) UnicastRemoteObject.exportObject(n, 0);

				nodesItf.add(n_stub);
			}
			//Attribution des neighbours
			for (int i = 1 ; i <= nbNodes ; i++) {
				sc.next();
				for (int j = 0 ; j < nbNodes ; j++) {
					if (sc.nextInt() == 1) {
						nodesItf.get(i-1).addNeighbour(nodesItf.get(j));
					}
				}
			}

			//Affichage voisins
			/*for (int i = 0 ; i < nbNodes ; i++){
				System.out.println("Node : " + (i+1));
				System.out.println("Neighbours : ");
				for (int j = 0 ; j < nodesItf.get(i).getNeighbours().size() ; j++) {
					System.out.println(nodesItf.get(i).getNeighbours().get(j).getId() + " ");
				}
			}*/
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

			VirtualNetwork vn = new VirtualNetwork(nodes);
			vn.getNodes().get(0).firstSend(8, "Bonjour");
			
			/*for (int i = 0 ; i < nbNodes ; i++) {
				System.out.println(" - - - - - - - - - - ");
				for (int j = 0 ; j < nodesItf.get(i).getNodeToTransfer().size() ; j++) {
					System.out.print(" " + nodesItf.get(i).getNodeToTransfer().get(nodesItf.get(j).getId()));
				}
				System.out.print("\n");
			}*/

		} catch (Exception e) {
			System.err.println("Error : " + e);
		}

		for (int i = 0 ; i < nbNodes ; i++){
			/*VNode v = new VNode(i);
			VNodeItf v_stub = (VNodeItf) UnicastRemoteObject.exportObject(v, 0);
			vNodes.add(v_stub);*/
		}

	}
}