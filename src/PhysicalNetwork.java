import java.util.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class PhysicalNetwork {

	private static int nbNodes;
	private static File fileInput;
	private static ArrayList<Node> nodes;
	private static ArrayList<NodeItf> nodesItf;

	public PhysicalNetwork (File fileInput) {
		this.fileInput = fileInput;
		nodes = new ArrayList<Node>();
		nodesItf = new ArrayList<NodeItf>();
		setupNodes();
		setupRoutes();
	}

	private void setupNodes() {
		try {
			Scanner sc = new Scanner(fileInput);
			nbNodes = sc.nextInt();
			int id;
			//Creation des nodes
			for (int i = 0 ; i < nbNodes ; i++) {
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
			sc.close();
		} catch (Exception e) {
			System.err.println("Error : " + e);
		}
	}

	private void setupRoutes() {
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
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}
}