import java.util.ArrayList;
import java.rmi.*;
import java.lang.Math;

public class Node implements NodeItf {
	private int id;
	private ArrayList<NodeItf> neighbours;
	private ArrayList<Ack> ackList;

	public Node (int id){
		this.id = id;
		neighbours = new ArrayList<NodeItf>();
		ackList = new ArrayList<Ack>();
	}

	public void send(int idTarget, String message) throws RemoteException {
		if (idTarget == id) {
			System.out.println("Node" + id + " received : " + message);
			/*for (int i = 0 ; i < idsFrom.size() ; i++){
				System.out.println(idsFrom.get(i));
			}*/
		} else {
			ArrayList<Integer> idsFrom = new ArrayList<Integer>();
			idsFrom.add(id);
			long idMessage = (long) (Math.random() * Long.MAX_VALUE);
			ackList.add(new Ack (false, idMessage));
			for (int i = 0 ; i < neighbours.size() ; i++) {
				System.out.println("Followed by Node" + id + " to Node" + neighbours.get(i).getId());
				neighbours.get(i).follow(id, idTarget, idsFrom, message, idMessage);	
			}
		}
	}

	public void follow(int idSender, int idTarget, ArrayList<Integer> idsFrom, String message, long idMessage) throws RemoteException {
		if (idTarget == id) {
			boolean alreadyReceived = false;
			for (int k = 0 ; k < ackList.size() ; k++) {
				if (ackList.get(k).idMessage == idMessage) {
					alreadyReceived = true;
				}
			}
			if (!alreadyReceived) {
				System.out.println("Node" + id + " received from Node" + idSender + " : " + message);
				Ack newAck = new Ack(true, idMessage);
				ackList.add(newAck);
				ArrayList<Integer> idsAckFrom = new ArrayList<Integer>();
				idsAckFrom.add(id);
				for (int i = 0 ; i < neighbours.size() ; i++){
					neighbours.get(i).acknowledge(newAck, idsAckFrom);
				}
			}
			/*for (int i = 0 ; i < idsFrom.size() ; i++){
				System.out.println(idsFrom.get(i));
			}*/
		} else {
			idsFrom.add(id);
			ackList.add(new Ack(false, idMessage));
			int j;
			boolean dontFollow;
			for (int i = 0 ; i < neighbours.size() ; i++) {
				j = 0;
				dontFollow = false;
				while (j < idsFrom.size() && !dontFollow) {
					if (neighbours.get(i).getId() == idsFrom.get(j)){
						dontFollow = true;
					}
					j++;
				}
				for (int k = 0 ; k < ackList.size() ; k++) {
					if (ackList.get(k).idMessage == idMessage) {
						dontFollow = ackList.get(k).acknowledged || dontFollow;
					}
				}
				if(!dontFollow){
					System.out.println("Followed by Node" + id + " to Node" + neighbours.get(i).getId());
					neighbours.get(i).follow(idSender, idTarget, idsFrom, message, idMessage);
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

	public void acknowledge(Ack ack, ArrayList<Integer> idsFrom) throws RemoteException {
		for (int i = 0 ; i < ackList.size() ; i++) {
			if (ackList.get(i).idMessage == ack.idMessage) {
				ackList.get(i).acknowledged = true;
			}
		}
		System.out.println("Ack from Node" + idsFrom.get(0) + " received by Node" + id);
		idsFrom.add(id);
		int j;
		boolean dontFollow;
		for (int i = 0 ; i < neighbours.size() ; i++) {
			j = 0;
			dontFollow = false;
			while (j < idsFrom.size() && !dontFollow) {
				if (neighbours.get(i).getId() == idsFrom.get(j)){
					dontFollow = true;
				}
				j++;
			}
			if(!dontFollow){
				neighbours.get(i).acknowledge(ack, idsFrom);
			}
		}
	}
}