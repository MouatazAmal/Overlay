import java.rmi.*;
import java.util.ArrayList;
import java.util.Hashtable;

public interface NodeItf extends Remote {
	public void send(int idTarget, String message) throws RemoteException;
	public void addNeighbour(NodeItf neighbour) throws RemoteException;
	public int getId() throws RemoteException;
	public ArrayList<NodeItf> getNeighbours() throws RemoteException;
	public void setup() throws RemoteException;
	public void sendStart(int idSender, int depth) throws RemoteException;
	public void sendNew(int idSender) throws RemoteException;
	public void sendSon(int idSender) throws RemoteException;
	public void sendAck(int idSender) throws RemoteException;
	public void sendDone(int idSender) throws RemoteException;
	public void sendEnd(int idSender) throws RemoteException;
	public boolean getEnd() throws RemoteException;
	public Hashtable<Integer, Integer> getNodeToTransfer() throws RemoteException;
}