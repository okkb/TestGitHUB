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
		ExecutorService exec = Executors.newFixedThreadPool(THREAD_COUNT);	// 쓰레드풀 생성 THREADCOUNT 개수 만큼
		
		try {
			css = new ServerSocket(clientPort);
			System.out.println("[Api 사용]Broker가 시작되었습니다.");
			while (true) {	
				cs = css.accept(); //broker에 접속한 클라이언트 socket  //css.   (#가정)클라이언트가 어셉트 기다리다 타임아웃 걸림
				if (que.offer(cs)) {
					try {
						exec.execute(new MsgThread(que.poll(), serverName, serverPort));
					} catch (IOException e) {
						System.err.println("start()_while() in-1 : "+e.getCause());
						e.printStackTrace();
					}
				} else {
					System.err.println("Quere에 socket 추가 실패");
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
