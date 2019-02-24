import java.rmi.*;
import java.util.ArrayList;
import java.rmi.*;

public interface NodeItf extends Remote {
	public void send(int idTarget, String message) throws RemoteException;
	public void follow(int idSender, int idTarget, ArrayList<Integer> idsFrom, String message, long idMessage) throws RemoteException;
	public void addNeighbour(NodeItf neighbour) throws RemoteException;
	public int getId() throws RemoteException;
	public ArrayList<NodeItf> getNeighbours() throws RemoteException;
	public void acknowledge(Ack ack, ArrayList<Integer> idsFrom) throws RemoteException;
}