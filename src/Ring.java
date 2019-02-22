import java.util.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class Ring {

	private static int nbNodes;
	private static ArrayList<NodeItf> nodes;

	public static void main (String[] args){
		if (args.length < 1) {
			System.out.println("Usage : java Ring <StructureFile>");
			return;
		}
		File input = new File(args[0]);
		try {
			Scanner sc = new Scanner(input);
			nbNodes = sc.nextInt();
			nodes = new ArrayList<NodeItf>();
			//Creation des nodes
			for (int i = 1 ; i <= nbNodes ; i++) {
				Node n = new Node(i);
				NodeItf n_stub = (NodeItf) UnicastRemoteObject.exportObject(n, 0);

				nodes.add(n_stub);
			}
			//Attribution des neighbours
			for (int i = 1 ; i <= nbNodes ; i++) {
				sc.next();
				for (int j = 0 ; j < nbNodes ; j++) {
					if (sc.nextInt() == 1) {
						nodes.get(i-1).addNeighbour(nodes.get(j));
					}
				}
			}

			//Affichage voisins
			for (int i = 0 ; i < nbNodes ; i++){
				System.out.println("Node : " + (i+1));
				System.out.println("Neighbours : ");
				for (int j = 0 ; j < nodes.get(i).getNeighbours().size() ; j++) {
					System.out.println(nodes.get(i).getNeighbours().get(j).getId() + " ");
				}
			}
			sc.close();
		} catch (Exception e) {
			System.err.println("Error : " + e);
		}
		try {
			nodes.get(2).send(nodes.get(2).getId(), 4, new ArrayList<Integer>(), "Hello");
		} catch (Exception e) {
			System.err.println("Error : " + e);
		}

	}
}