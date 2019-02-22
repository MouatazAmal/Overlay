import java.util.ArrayList;
import java.rmi.*;

public class Node implements NodeItf {
	private int id;
	private ArrayList<NodeItf> neighbours;

	public Node (int id){
		this.id = id;
		neighbours = new ArrayList<NodeItf>();
	}

	public void send(int idSender, int idTarget, ArrayList<Integer> idsFrom, String message) throws RemoteException {
		if (idTarget == id) {
			System.out.println("Node" + id + " received from Node" + idSender + " : " + message);
			for (int i = 0 ; i < idsFrom.size() ; i++){
				System.out.println(idsFrom.get(i));
			}
		} else {
			idsFrom.add(id);
			int j;
			boolean antiLoop;
			System.out.println("Passage par Node" + id);
			for (int i = 0 ; i < neighbours.size() ; i++) {
				j = 0;
				antiLoop = false;
				while (j < idsFrom.size() && !antiLoop) {
					if (neighbours.get(i).getId() == idsFrom.get(j)){
						antiLoop = true;
					}
					j++;
								
				}
				if(!antiLoop){
						neighbours.get(i).send(idSender, idTarget, idsFrom, message);
				}
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
}