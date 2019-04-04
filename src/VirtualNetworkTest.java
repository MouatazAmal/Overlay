import java.rmi.RemoteException;
import java.util.ArrayList;

public class VirtualNetworkTest {
    public static void main(String[] args){



        VNode vn = new VNode();

        try {
            vn.getMessages().addMessage("Salut c'est mouataz");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
