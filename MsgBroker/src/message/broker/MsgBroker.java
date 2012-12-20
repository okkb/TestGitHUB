package message.broker;

public class MsgBroker { 
	public static void main(String args[]) {
		if (args.length != 4) { 
			System.err.println("java MsgBroker <server name> <server port> <clientPort> <workerCount>");
			System.exit(1);
		}
		String serverName = args[0];
		int serverPort = -1;
		try {
			serverPort = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.err.println("java MsgBroker <server name> <server port> <client port> <worker count>");
			System.exit(2);
		}
		int clientPort = -1;
		try {
			clientPort = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			System.err.println("java MsgBroker <server name> <server port> <client port> <worker count>");
			System.exit(3);
		}	
		int workerCount = 0;
		try {
			workerCount = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			System.err.println("java MsgBroker <server name> <server port> <client port> <worker count>");
			System.exit(4);
		}
	
		try {
			System.out.println("MsgBroker Start");
			new RequestAcceptor(serverName, serverPort, clientPort, workerCount).start();
		} catch (Exception e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}
	
}