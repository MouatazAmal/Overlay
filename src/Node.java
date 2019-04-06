import java.util.ArrayList;
import java.util.Hashtable;
import java.rmi.*;
import java.lang.Math;

public class Node implements NodeItf {
	private int id;
	private ArrayList<NodeItf> neighbours;
	private Messages messages;
	private int fatherId;
	private ArrayList<Integer> sons;
	private ArrayList<Integer> idsReachable;
	private Hashtable<Integer, Integer> nodeToTransfer;
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
		idsReachable = new ArrayList<Integer>();
		nodeToTransfer = new Hashtable<Integer, Integer>();
		end = false;
		firstNew = true;
		messagesSent = 0;
		messagesReceived = 0;
		ackReceived = 0;
		endReceived = 0;
		messages = new Messages();
	}

	public void setup() throws RemoteException {
		int d = 0;
		fatherId = id;
		nodeToTransfer.put(id, id);
		while (!end) {
			if (d == 0) {
				sendStart(id, d);
			} else {
				for (int i = 0 ; i < neighbours.size() ; i++) {
					if (!neighbours.get(i).getEnd()){
						neighbours.get(i).sendStart(id, d-1);
						messagesSent++;
					}
				}
				receive();
			}
			d++;
		}
		System.out.println("Setup Node" + id + " done");
		System.out.print("Node to reach   : ");
		System.out.println(nodeToTransfer.keySet());
		
		System.out.print("Node to send to : ");
		System.out.println(nodeToTransfer.values());
	}

	public void sendStart(int idSender, int depth) throws RemoteException {
		//System.out.println("START received by " + id + " from " + idSender);
		if (depth == 0 && !end) {
			for (int i = 0 ; i < neighbours.size() ; i++) {
				if (neighbours.get(i).getId() != fatherId && !neighbours.get(i).getEnd()) {
					neighbours.get(i).sendNew(id);
					messagesSent++;
				}
			}
			if (messagesSent != 0) {
				receive();
			} else {
				for (int i = 0 ; i < neighbours.size() ; i++) {
					if (neighbours.get(i).getId() == fatherId) {
						neighbours.get(i).sendEnd(id);
						end = true;
					}
				}
			}
		} else if (!end) {
			for (int h = 0 ; h < sons.size() ; h++) {
				for (int i = 0 ; i < neighbours.size() ; i++) {
					if (neighbours.get(i).getId() != fatherId && !neighbours.get(i).getEnd() && neighbours.get(i).getId() == sons.get(h)) {
						neighbours.get(i).sendStart(id, depth-1);
						messagesSent++;
					}
				}
			}
			receive();
		}
	}

	public void sendNew(int idSender) throws RemoteException {
		//System.out.println("NEW received by " + id + " from " + idSender);
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
		//System.out.println("SON received by " + id + " from " + idSender);
		messagesReceived++;
		sons.add(idSender);
		nodeToTransfer.put(idSender, idSender);
		idsReachable.add(idSender);
	}

	public void sendAck(int idSender) throws RemoteException {
		//System.out.println("ACK received by " + id + " from " + idSender);
		messagesReceived++;
		ackReceived++;
	}

	public void sendDone(int idSender, ArrayList<Integer> idsReachable) throws RemoteException {
		//System.out.println("DONE received by " + id + " from " + idSender);
		messagesReceived++;
		for (int i = 0 ; i < idsReachable.size() ; i ++) {
			if (!this.idsReachable.contains(idsReachable.get(i))) {
				this.idsReachable.add(idsReachable.get(i));
			}
			nodeToTransfer.put(idsReachable.get(i), idSender);
		}
	}

	public void sendEnd(int idSender) throws RemoteException {
		//System.out.println("END received by " + id + " from " + idSender);
		endReceived++;
		messagesReceived++;
	}

	public void receive() throws RemoteException {
		while (messagesSent != messagesReceived || messagesReceived == 0){
				System.out.println("Receive waiting..." + id + " MS : " + messagesSent + " MR : " + messagesReceived);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					System.err.println(e);
				}
			}
			if (messagesReceived == ackReceived || messagesReceived == endReceived){
				if (id != fatherId) {
					for (int i = 0 ; i < neighbours.size() ; i++) {
						if (neighbours.get(i).getId() == fatherId){
							neighbours.get(i).sendEnd(id);
						}
					}
				}				
				end = true;
			} else {
				for (int i = 0 ; i < neighbours.size() ; i++) {
					if (neighbours.get(i).getId() == fatherId){
						neighbours.get(i).sendDone(id, idsReachable);
					}
				}
			}
			messagesSent = 0;
			messagesReceived = 0;
			endReceived = 0;
			ackReceived = 0;
	}

	public boolean getEnd() throws RemoteException {
		return end;
	}

	public void reset() throws RemoteException {
		this.end = false;
		this.firstNew = true;
		this.messagesSent = 0;
		this.messagesReceived = 0;
		this.ackReceived = 0;
		this.endReceived = 0;
		this.fatherId = -1;
		this.sons.clear();
		this.idsReachable.clear();
	}

	public void send(int pIdTarget, String message) throws RemoteException {
		if (pIdTarget == id) {
			messages.addMessage(message);
		} else {
			int i = 0;
			boolean notFound = true;
			while (i < neighbours.size() && notFound) {
				if (neighbours.get(i).getId() == nodeToTransfer.get(pIdTarget)) {
					System.out.println("Node" + id + " transferred to Node" + neighbours.get(i).getId() + " for Node" + pIdTarget);
					neighbours.get(i).send(pIdTarget, message);
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

	public Messages getMessages() {
		return messages;
	}
}