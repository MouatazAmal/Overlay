import java.rmi.*;
import java.util.ArrayList;
import java.util.Hashtable;

public interface NodeItf extends Remote {
	public void send(int idTarget, String message) throws RemoteException;
	public void addNeighbour(NodeItf neighbour) throws RemoteException;
	public int getId() throws RemoteException;
	public ArrayList<NodeItf> getNeighbours() throws RemoteException;
	public void setup() throws RemoteException;
	public void setupSend(int idSender, ArrayList<Integer> idsFrom) throws RemoteException;
	public Hashtable<Integer, Integer> getNodeToTransfer() throws RemoteException;
}