import java.util.*;
import java.io.*;

public class Ring {

	public static void main (String[] args){
		if (args.length < 1) {
			System.out.println("Usage : java Ring <StructureFile>");
			return;
		}
		File fileInput = new File(args[0]);
		
		PhysicalNetwork pn = new PhysicalNetwork(fileInput);
		VirtualNetwork vn = new VirtualNetwork(pn.getNodes());
		
		Scanner inputSc = new Scanner(System.in);
		String input = "";
		int sender, target;
		while (true) {
			input = inputSc.nextLine();
			Scanner stringSc = new Scanner(input);
			String cmd = stringSc.next();
			sender = stringSc.nextInt();
			target = stringSc.nextInt();
			String msg = stringSc.nextLine();
			if (input.startsWith("send ")) {
				try {
					vn.getNodes().get(sender-1).firstSend(target, msg);
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		}
	}
}