import java.util.ArrayList;
import java.util.Hashtable;
import java.rmi.*;
import java.lang.Math;

public class Node implements NodeItf {
	private int id;
	private ArrayList<NodeItf> neighbours;
	private Hashtable<Integer, Integer> nodeToTransfer;
	private Hashtable<Integer, Integer> sizeToTransfer;
	private ArrayList<Ack> ackList;

	public Node (int id, int nbNodes){
		this.id = id;
		neighbours = new ArrayList<NodeItf>();
		nodeToTransfer = new Hashtable<Integer, Integer>();
		sizeToTransfer = new Hashtable<Integer, Integer>();
		ackList = new ArrayList<Ack>();
	}

	public void setup() throws RemoteException {
		nodeToTransfer.put(id, id);
		sizeToTransfer.put(id, 0);
		ArrayList<Integer> nodesFrom = new ArrayList<Integer>();
		nodesFrom.add(id);
		for (int i = 0 ; i < neighbours.size() ; i++) {
			neighbours.get(i).setupSend(id, nodesFrom);
		}
	}

	public void setupSend(int idSender, ArrayList<Integer> idsFrom) throws RemoteException {
		if (!nodeToTransfer.containsKey(idSender)){
			nodeToTransfer.put(idSender, idsFrom.get(idsFrom.size()-1));
			sizeToTransfer.put(idSender, idsFrom.size());
			idsFrom.add(id);
			for (int i = 0 ; i < neighbours.size() ; i++) {
				neighbours.get(i).setupSend(idSender, idsFrom);
			}
		} else {
			if (sizeToTransfer.get(idSender) > idsFrom.size()){
				sizeToTransfer.replace(idSender, idsFrom.size());
				nodeToTransfer.replace(idSender, idsFrom.get(idsFrom.size()-1));
			}
		}
	}

	public void send(int idTarget, String message) throws RemoteException {
		if (idTarget == id) {
			System.out.println("Node" + id + " received : " + message);
		} else {
			int i = 0;
			boolean notFound = true;
			while (i < neighbours.size() && notFound) {
				if (neighbours.get(i).getId() == nodeToTransfer.get(idTarget)) {
					neighbours.get(i).send(idTarget, message);
					notFound = false;
				}
				i++;
			}
		}
	}

	public void addNeighbour(NodeItf neighbour) throws RemoteException {
		neighbours.add(neighbour);
	}

	public int getId() throws RemoteException {
		return id;
	}

	public ArrayList<NodeItf> getNeighbours() throws RemoteException {
		return neighbours;
	}

	public Hashtable<Integer, Integer> getNodeToTransfer() throws RemoteException {
		return nodeToTransfer;
	}
}