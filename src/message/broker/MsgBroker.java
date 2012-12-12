package message.broker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MsgBroker {
	private static final int THREAD_COUNT =2000;
	private String serverName="";
	private int serverPort=-1;
	private int clientPort = -1;
	
	private Queue<Socket> que = new LinkedList<Socket>();


	public MsgBroker(String serverName, int serverPort, int clientPort) {
		this.serverName=serverName;
		this.serverPort=serverPort;
		this.clientPort = clientPort;
	}

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
			new MsgBroker(serverName, serverPort, clientPort).start();
		} catch (InterruptedException e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}

	public void start() throws InterruptedException {
		ServerSocket css = null;  //ClientServerSocket
		Socket cs = null;  //ClientSocket
		ExecutorService exec = Executors.newFixedThreadPool(THREAD_COUNT);	// ������Ǯ ���� THREADCOUNT ���� ��ŭ
		
		try {
			css = new ServerSocket(clientPort);
			System.out.println("[Api ���]Broker�� ���۵Ǿ����ϴ�.");
			while (true) {	
				cs = css.accept(); //broker�� ������ Ŭ���̾�Ʈ socket  //css.   (#����)Ŭ���̾�Ʈ�� ���Ʈ ��ٸ��� Ÿ�Ӿƿ� �ɸ�
				if (que.offer(cs)) {
					try {
						exec.execute(new MsgThread(que.poll(), serverName, serverPort));
					} catch (IOException e) {
						System.err.println("start()_while() in-1 : "+e.getCause());
						e.printStackTrace();
					}
				} else {
					System.err.println("Quere�� socket �߰� ����");
				}
			}
		} catch (IOException e) {
			System.err.println("start() in-2 : "+e.getCause());
			e.printStackTrace();
		} finally {
			try {
				css.close();
			} catch (IOException e) {
				System.err.println("start() in-3 : "+e.getCause());
				e.printStackTrace();
			}finally{
				css = null;
			}
			exec.shutdown();
		}
	}
}
