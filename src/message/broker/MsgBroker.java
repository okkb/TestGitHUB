package message.broker;


public class MsgBroker {
	public static void main(String args[]) {
//		if (args.length != 3) {
//			System.err.println("java MsgBroker <server name> <serverPort> <clientPort>");
//			System.exit(1);
//		}
//		String serverName = args[0];
//		int serverPort = -1;
//		try {
//			serverPort = Integer.parseInt(args[1]);
//		} catch (NumberFormatException e) {
//			System.err.println("java MsgBroker <server name> <serverPort> <clientPort>");
//			System.exit(2);
//		}
//		int clientPort = -1;
//		try {
//			clientPort = Integer.parseInt(args[2]);
//		} catch (NumberFormatException e) {
//			System.err.println("java MsgBroker <server name> <serverPort> <clientPort>");
//			System.exit(3);
//		}
		String serverName = "127.0.0.1";    // �ϵ� �ڵ� ���� �ʿ�
		int serverPort = 1023;    // �ϵ� �ڵ� ���� �ʿ�
		int clientPort = 6666;  // �ϵ� �ڵ� ���� �ʿ�
		
		try {
			Queue que = new Queue(2000);  // WorkerThread 100��
			que.startWorkers();// Queue startWorkers()���� 100���� ������ ��� Que���� request��û �ϰ� ����!  ��û�� ���� wait
			new BrokerThread(serverName, serverPort, clientPort, que).start();
		} catch (Exception e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}
	
}