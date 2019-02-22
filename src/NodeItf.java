import java.rmi.*;
import java.util.ArrayList;
import java.rmi.*;

public interface NodeItf extends Remote {
	public void send(int idSender, int idTarget, ArrayList<Integer> idsFrom, String message) throws RemoteException;
	public void addNeighbour(NodeItf neighbour) throws RemoteException;
	public int getId() throws RemoteException;
	public ArrayList<NodeItf> getNeighbours() throws RemoteException;
}