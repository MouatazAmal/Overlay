
import java.rmi.*;
import java.util.Observable;
import java.util.Observer;

public class VNode implements VNodeItf, Observer {

    private int id;
	private Node physNode;
	private VNodeItf right;
	private VNodeItf left;
	private Messages messages;


	public VNode (int id, Node physNode) {
		this.id = id;
		this.physNode = physNode;
        this.right = null;
        this.left = null;
        this.messages = physNode.getMessages();
        this.messages.addObserver(this);
	}

	public void setupNeighbours(VNodeItf right, VNodeItf left){
		this.right = right;
		this.left = left;
	}

	public void firstSend(int vIdTarget, String message) throws RemoteException {
		if (vIdTarget == id) {
			System.out.println("VNode" + id + " received : " + message);
		} else {
			try {
				sendRight(id + "/" + vIdTarget + "/" + message);
			} catch (Exception e) {
				System.err.print(e);
			}
		}
	}

	public void sendRight(String message) throws RemoteException {
			physNode.send(right.getPhysNode().getId(), message);
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

	public Messages getMessages()throws RemoteException {
		return messages;
	}

	@Override
	public void update(Observable o, Object arg) {
		/*
		Un message est constitué de trois partie séparé d'un | :
			+ Virtual ID de l'émetteur
			+ Physical ID du recepteur
			+ Le contenu du message
			*/

		String[] messageFromPhysicalNode = this.messages.getRecentMessage().split("/");
		int sourceVirtualID = Integer.parseInt(messageFromPhysicalNode[0]);
		int destinationVirtualID = Integer.parseInt(messageFromPhysicalNode[1]);
		String msg = messageFromPhysicalNode[2];

		try {
			if (this.id == sourceVirtualID )
				throw new Exception("Virtual Node not found");
			else if (this.id != destinationVirtualID)
			{
				System.out.println("VNode " + id + " sends right.");
				sendRight(messages.getRecentMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.id == destinationVirtualID) {
			System.out.println("Message received by " + id + " : " + msg);
		}
	}


	public int getId() {
		return id;
	}
}