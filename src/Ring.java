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
			String cmd = "";
			String msg = "";
			try {
				cmd = stringSc.next();
				if (cmd.equals("quit")){
					System.exit(0);
				}
				sender = stringSc.nextInt();
				target = stringSc.nextInt();
				msg = stringSc.nextLine();
			} catch (Exception e) {
				System.err.println("Input format error : send <IdSender> <IdTarget> <Message>");
				continue;
			}
			
			if (cmd.equals("send") &&  vn.containsVNode(sender)) {
				try {
					vn.getNodes().get(sender-1).firstSend(target, msg);
				} catch (Exception e) {
					System.err.println(e);
				}
			} else if (cmd.equals("send")) {
				System.out.println("Sender not found.");
			} else {
				System.out.println("Command not found. Available command : send <IdSender> <IdTarget> <Message>");
			}
		}
	}
}