import java.util.ArrayList;
import java.util.Observable;

public class Messages extends Observable {
    private ArrayList<String> archives;

    public Messages() {
        this.archives = new ArrayList<>();
    }

    public ArrayList<String> getArchives() {


        return archives;
    }


    public void addMessage(String msg){
        this.archives.add(msg);
        setChanged();
        notifyObservers(msg);
    }

    public String getRecentMessage(){
        return this.archives.get(this.archives.size() - 1);
    }
}
