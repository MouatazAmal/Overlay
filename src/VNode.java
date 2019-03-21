import java.rmi.*;

public class VNode implements VNodeItf {

    private int id;
	private NodeItf physNode;
	private VNodeItf right;
	private VNodeItf left;

	public VNode (int id, Node physNode, VNodeItf right, VNodeItf left) {
		this.id = id;
		this.physNode = physNode;
        this.right = right;
        this.left = left;
	}

	public void sendRight(int idTarget, String message) throws RemoteException {
		if (idTarget == id) {
			System.out.println("VNode" + id + " received : " + message);
		} else {
			physNode.send(this.right.getPhysNode().getId(),message);
		}
	}

	public void sendLeft(int idTarget, String message) throws RemoteException {
		if (idTarget == id) {
			System.out.println("VNode" + id + " received : " + message);
		} else {
			physNode.send(this.left.getPhysNode().getId(),message);
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

	public VNodeItf getRight() throws RemoteException {
		return right;
	}

	public VNodeItf getLeft()throws RemoteException  {
		return left;
	}

	public NodeItf getPhysNode()throws RemoteException {
		return physNode;
	}
}