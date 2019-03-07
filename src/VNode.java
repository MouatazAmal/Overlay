import java.rmi.*;

public class VNode implements VNodeItf {
	
	private NodeItf physNode;
	private int id;
	private VNodeItf right;
	private VNodeItf left;

	public VNode (int id, Node physNode) {
		this.id = id;
		this.physNode = physNode;
	}

	public void sendRight(int idTarget, String message) throws RemoteException {
		if (idTarget == id) {
			System.out.println("VNode" + id + " received : " + message);
		} else {
			physNode.send(idTarget, message);
		}
	}

	public void sendLeft(int idTarget, String message) throws RemoteException {
		if (idTarget == id) {
			System.out.println("VNode" + id + " received : " + message);
		} else {
			physNode.send(idTarget, message);
		}
	}

	public void vFollowRight(int idSender, int idTarget, String message) throws RemoteException {
		if (idTarget == id) {
			System.out.println("VNode" + id + " received from VNode" + idSender + " : " + message);
		} else {
			//physNode.follow()
			right.vFollowRight(id, idTarget, message);
		}
	}

	public void vFollowLeft(int idSender, int idTarget, String message) throws RemoteException {
		if (idTarget == id) {
			System.out.println("VNode" + id + " received from VNode" + idSender + " : " + message);
		} else {
			left.vFollowLeft(id, idTarget, message);
		}
	}

	public void setRight (VNodeItf right) throws RemoteException {
		this.right = right;
	}

	public void setLeft (VNodeItf left) throws RemoteException {
		this.left = left;
	}
}