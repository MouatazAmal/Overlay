import java.rmi.RemoteException;
import java.util.ArrayList;

public class VirtualNetwork {

    private ArrayList<VNodeItf> nodes;
    private int networkSize;

    public VirtualNetwork(ArrayList<Node> physicalNodes) {
        this.nodes = new ArrayList<>();
        this.setup(physicalNodes);
        this.networkSize = this.nodes.size();
    }


    public void setup(ArrayList<Node> physicalNodes){
        // creating VirtualNodes and assign them to PhysicalNodes
        System.out.println(" - - - - - - - - - - ");
        System.out.println("Creating VirtualNodes and associate them to PhysicalNodes");
        int nodesID = 1;
        for (Node n : physicalNodes){
            this.nodes.add(new VNode(nodesID,n));
            try {
                System.out.println("VNode" + nodesID + " associated with PNode" + n.getId());
            } catch (Exception e) {
                System.err.println(e);
            }
            nodesID++;
        }

        // Setting up Neighbours
        System.out.println("Setting up Neighbours");
        try {
            for (int i = 1; i < this.nodes.size() - 1 ; i++ ){
                    this.nodes.get(i).setupNeighbours(this.nodes.get((i+1)%physicalNodes.size()),this.nodes.get((i-1)%physicalNodes.size()));
            }
            this.nodes.get(0).setupNeighbours(this.nodes.get(1), this.nodes.get(this.nodes.size()-1));
            this.nodes.get(this.nodes.size() - 1).setupNeighbours(this.nodes.get(0), this.nodes.get(this.nodes.size()-2));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println(" - - - - - - - - - - ");
    }

    public ArrayList<VNodeItf> getNodes() {
        return nodes;
    }

    public int getNetworkSize() {
        return networkSize;
    }
}
