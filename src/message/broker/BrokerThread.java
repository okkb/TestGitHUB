package message.broker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BrokerThread extends Thread { // �̰� ���Ʈ�ǰ� new�Ǵ°� ����
	private String serverName = "";
	private int serverPort = -1;
	private int clientPort = -1;
	private final Queue que;

	public BrokerThread(String serverName, int serverPort, int clientPort,
			Queue que) {
		this.serverName = serverName;
		this.serverPort = serverPort;
		this.clientPort = clientPort;
		this.que = que;
	}

	public void run() {
		ServerSocket css = null; // ClientServerSocket
		Socket cs = null; // ClientSocket
		try {
			css = new ServerSocket(clientPort);
			System.out.println("[Worker]Broker�� ���۵Ǿ����ϴ�.");
			while (true) {
				cs = css.accept(); 									
				Job job = new Job(cs, serverName, serverPort);
				que.putJob(job);
			}
		} catch (IOException e) {
			System.err.println("run() in-2 : " + e.getCause());
			e.printStackTrace();
		} finally {
			try {
				css.close();
			} catch (IOException e) {
				System.err.println("run() in-3 : " + e.getCause());
				e.printStackTrace();
			} finally {
				css = null;
			}
		}
	}
}
