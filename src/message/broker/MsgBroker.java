package message.broker;


public class MsgBroker {
	public static void main(String args[]) {
		if (args.length != 3) {
			System.err.println("java MsgBroker <server name> <serverPort> <clientPort>");
			System.exit(1);
		}
		String serverName = args[0];
		int serverPort = -1;
		try {
			serverPort = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.err.println("java MsgBroker <server name> <serverPort> <clientPort>");
			System.exit(2);
		}
		int clientPort = -1;
		try {
			clientPort = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			System.err.println("java MsgBroker <server name> <serverPort> <clientPort>");
			System.exit(3);
		}
		try {
			Queue que = new Queue(2000);  // 쓰레드풀 쓰레드 100개
			que.startWorkers();// Queue startWorkers()시작 100개의 쓰레드 계속 Que에서 request요청 하고 실행!  요청중 없음 wait
			new BrokerThread(serverName, serverPort, clientPort, que).start();
		} catch (Exception e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}
	
}