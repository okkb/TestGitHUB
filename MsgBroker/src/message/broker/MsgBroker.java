package message.broker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MsgBroker { 
	public static void main(String args[]) {
		Matcher matcher=null;
		Pattern pattern=null;
		String serverName="";
		int serverPort = -1;
		int clientPort = -1;
		int workerCount = 0;
			
		if (args.length != 4) {
			System.err.println("java MsgBroker <server name> <server port> <clientPort> <workerCount>");
			System.exit(1);
		}
		
		pattern = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
		matcher = pattern.matcher(args[0]);
		if(!matcher.find()){
			System.err.println("java MsgBroker <server name!> <server port> <client port> <worker count>");
			System.exit(2);
		}
		serverName=args[0];

		pattern = Pattern.compile("\\d{4,5}");
		matcher = pattern.matcher(args[1]);
		try{
		serverPort = Integer.parseInt(args[1]);
		}catch(NumberFormatException e){
			System.err.println("java MsgBroker <server name> <server port(1023~65535)> <client port> <worker count>");
			System.exit(3);
		}
		if(!matcher.find() || !(serverPort>=1023 && serverPort < 65536)){	
			System.err.println("java MsgBroker <server name> <server port(1023~65535)> <client port> <worker count>");
			System.exit(4);
			}
		
		matcher = pattern.matcher(args[2]);
		try{
			clientPort = Integer.parseInt(args[2]);
			}catch(NumberFormatException e){
				System.err.println("java MsgBroker <server name> <server port> <client port(1023~65535)> <worker count>");
				System.exit(5);
			}
		if(!matcher.find() || !(clientPort>=1023 && clientPort < 65536)){	
			System.err.println("java MsgBroker <server name> <server port> <client port(1023~65535)> <worker count>");
			System.exit(6);
			}			
		
		try {
			workerCount = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			System.err.println("java MsgBroker <server name> <server port> <client port> <worker count!>");
			System.exit(7);
		}
	
		try {
			System.out.println("MsgBroker Start");
			new RequestAcceptor(serverName, serverPort, clientPort, workerCount).start();
		} catch (Exception e) {
			System.out.println("new RequestAcceptor(serverName, serverPort, clientPort, workerCount).start() :"+e.getCause());
			e.printStackTrace();
		}
	}
	
}