
import java.rmi.*;
import java.util.Observable;
import java.util.Observer;

public class VNode implements VNodeItf, Observer {

    private int id;
	private NodeItf physNode;
	private VNodeItf right;
	private VNodeItf left;
	private  Messages messages;


	public VNode (int id, NodeItf physNode) {
		this.id = id;
		this.physNode = physNode;
        this.right = null;
        this.left = null;
        this.messages = new Messages();
        this.messages.addObserver(this);
	}

	public void setupNeighbours(VNodeItf right, VNodeItf left){
		this.right = right;
		this.left = left;
	}



	public void sendRight(int idTarget, String message) throws RemoteException {
		if (idTarget == id) {
			System.out.println("VNode" + id + " received : " + message);
		} else {
			physNode.send(idTarget,message);
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

		String[] messageFromPhysicalNode = this.messages.getRecentMessage().split("|");
		int sourceVirtualID = Integer.parseInt(messageFromPhysicalNode[0]);
		int destinationPhysicalID = Integer.parseInt(messageFromPhysicalNode[1]);
		String msg = messageFromPhysicalNode[0];

		try {
			if (this.id == sourceVirtualID )
				throw new Exception("Virtual Node not found");
			else if (this.physNode.getId() != destinationPhysicalID)
			{
				sendRight(this.right.getId(),this.messages.getRecentMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(this.messages.getRecentMessage());
	}


	public int getId() {
		return id;
	}
}