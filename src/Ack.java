import java.io.Serializable;

public class Ack implements Serializable {
	public boolean acknowledged;
	public long idMessage;

	public Ack(boolean a, long id){
		this.acknowledged = a;
		this.idMessage = id;
	}
}