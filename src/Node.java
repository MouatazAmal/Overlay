import java.util.ArrayList;
import java.util.Hashtable;
import java.rmi.*;
import java.lang.Math;

public class Node implements NodeItf {
	private int id;
	private ArrayList<NodeItf> neighbours;
	private int fatherId;
	private ArrayList<Integer> sons;
	private boolean end;
	private boolean firstNew;
	private int messagesSent;
	private int messagesReceived;
	private int ackReceived;
	private int endReceived;

	public Node (int id){
		this.id = id;
		neighbours = new ArrayList<NodeItf>();
		fatherId = -1;
		sons = new ArrayList<Integer>();
		end = false;
		firstNew = true;
		messagesSent = 0;
		messagesReceived = 0;
		ackReceived = 0;
		endReceived = 0;
	}

	public void setup() throws RemoteException {
		int d = 0;
		while (!end) {
			if (d == 0) {
				sendStart(id, d);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					System.err.println(e);
				}
			} else {
				for (int i = 0 ; i < neighbours.size() ; i++) {
					neighbours.get(i).sendStart(id, d-1);
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					System.err.println(e);
				}
			}
			d++;
		}
	}

	public void sendStart(int idSender, int depth) throws RemoteException {
		System.out.println("START received by " + id + " from " + idSender);
		if (depth == 0 && !end) {
			for (int i = 0 ; i < neighbours.size() ; i++) {
				if (neighbours.get(i).getId() != fatherId) {
					neighbours.get(i).sendNew(id);
					messagesSent++;
				}
			}
			receive();
		} else if (!end) {
			for (int i = 0 ; i < neighbours.size() ; i++) {
				if (neighbours.get(i).getId() != fatherId && !neighbours.get(i).getEnd()) {
					neighbours.get(i).sendStart(id, depth-1);
					messagesSent++;
				}
			}
			receiveDoneEnd();
		}
	}

	public void sendNew(int idSender) throws RemoteException {
		System.out.println("NEW received by " + id + " from " + idSender);
		if (firstNew) {
			fatherId = idSender;
			for (int i = 0 ; i < neighbours.size() ; i++) {
				if (neighbours.get(i).getId() == fatherId){
					neighbours.get(i).sendSon(id);
				}
			}
			firstNew = false;
		} else {
			for (int i = 0 ; i < neighbours.size() ; i++) {
				if (neighbours.get(i).getId() == idSender){
					neighbours.get(i).sendAck(id);
				}
			}

		}
	}

	public void sendSon(int idSender) throws RemoteException {
		System.out.println("SON received by " + id + " from " + idSender);
		messagesReceived++;
		sons.add(idSender);
	}

	public void sendAck(int idSender) throws RemoteException {
		System.out.println("ACK received by " + id + " from " + idSender);
		messagesReceived++;
		ackReceived++;
	}

	public void sendDone(int idSender) throws RemoteException {
		System.out.println("DONE received by " + id + " from " + idSender);
		messagesReceived++;
	}

	public void sendEnd(int idSender) throws RemoteException {
		System.out.println("END received by " + id + " from " + idSender);
		endReceived++;
		messagesReceived++;
	}

	public void receive() throws RemoteException {
		while (messagesSent != messagesReceived){
				System.out.println("Receive waiting..." + id + " MS : " + messagesSent + " MR : " + messagesReceived);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					System.err.println(e);
				}
			}
			if (messagesReceived == ackReceived){
				for (int i = 0 ; i < neighbours.size() ; i++) {
					if (neighbours.get(i).getId() == fatherId){
						neighbours.get(i).sendEnd(id);
						end = true;
					}
				}
			} else {
				for (int i = 0 ; i < neighbours.size() ; i++) {
					if (neighbours.get(i).getId() == fatherId){
						neighbours.get(i).sendDone(id);
					}
				}
			}
			messagesSent = 0;
			messagesReceived = 0;
			ackReceived = 0;
	}

	public void receiveDoneEnd() throws RemoteException {
		while (messagesSent != messagesReceived) {
			System.out.println("ReceiveEnd waiting..." + id + " MS : " + messagesSent + " MR : " + messagesReceived);
			try {
					Thread.sleep(1000);
				} catch (Exception e) {
					System.err.println(e);
				}
		}
		if (messagesReceived == endReceived){
			for (int i = 0 ; i < neighbours.size() ; i++) {
				if (neighbours.get(i).getId() == fatherId){
					neighbours.get(i).sendEnd(id);
					end = true;
				}
			}
		} else {
			for (int i = 0 ; i < neighbours.size() ; i++) {
				if (neighbours.get(i).getId() == fatherId){
					neighbours.get(i).sendDone(id);
				}
			}
		}
		messagesSent = 0;
		messagesReceived = 0;
		endReceived = 0;
	}

	public boolean getEnd() throws RemoteException {
		return end;
	}

	public void send(int idTarget, String message) throws RemoteException {
		/*if (idTarget == id) {
			System.out.println("Node" + id + " received : " + message);
		} else {
			int i = 0;
			boolean notFound = true;
			while (i < neighbours.size() && notFound) {
				if (neighbours.get(i).getId() == nodeToTransfer.get(idTarget)) {
					System.out.println("Node" + id + " transfered to Node" + neighbours.get(i).getId() + " for Node" + idTarget);
					neighbours.get(i).send(idTarget, message);
					notFound = false;
				}
				i++;
			}
		}*/
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
		return null;//nodeToTransfer;
	}
}