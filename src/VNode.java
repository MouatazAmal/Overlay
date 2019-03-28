
import java.rmi.*;
import java.util.Observable;
import java.util.Observer;

public class VNode implements VNodeItf, Observer {

    private int id;
	private NodeItf physNode;
	private VNodeItf right;
	private VNodeItf left;
	private  Messages messages;


	public VNode (int id, Node physNode) {
		this.id = id;
		this.physNode = physNode;
        this.right = null;
        this.left = null;
        this.messages = new Messages();
        this.messages.addObserver(this);

	}

	public void setup(VNodeItf right,VNodeItf left){
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

	public Messages getMessages() {
		return messages;
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("i get the message " + this.messages.getRecentMessage());
	}
}