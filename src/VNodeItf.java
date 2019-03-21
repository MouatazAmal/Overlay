import java.rmi.*;

public interface VNodeItf extends Remote {
	public void sendRight(int idTarget, String message) throws RemoteException;
	public void sendLeft(int idTarget, String message) throws RemoteException;
	public void vFollowRight(int idSender, int idTarget, String message) throws RemoteException;
	public void vFollowLeft(int idSender, int idTarget, String message) throws RemoteException;
	public void setRight (VNodeItf right) throws RemoteException;
	public void setLeft (VNodeItf left) throws RemoteException;
	public VNodeItf getRight()throws RemoteException ;
	public VNodeItf getLeft()throws RemoteException ;
	public NodeItf getPhysNode()throws RemoteException ;
}