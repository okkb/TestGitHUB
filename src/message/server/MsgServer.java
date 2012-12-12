package message.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MsgServer {
	private static final int THREADCOUNT =1000;
	private int serverPort=-1;	
	private Queue<Socket> que = new LinkedList<Socket>();

	public MsgServer(int serverPort) {
		this.serverPort=serverPort;
	}
	public static void main(String args[]) {
		if (args.length != 1) {
			System.err.println("java TestServer <port>");
			System.exit(1);
		}
		int port = -1;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.err.println("java TestServer <port>");
			System.exit(2);
		}
		try {
			new MsgServer(port).start();
		} catch (InterruptedException e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}

	public void start() throws InterruptedException {
		ServerSocket css = null;  //ClientServerSocket
		Socket cs = null;  //ClientSocket
		ExecutorService exec = Executors.newFixedThreadPool(THREADCOUNT);	// 쓰레드풀 생성 THREADCOUNT 개수 만큼
		
		try {
			css = new ServerSocket(serverPort);
			System.out.println("[kim]Server 시작되었습니다.");
			while (true) {	
				cs = css.accept(); //broker에 접속한 클라이언트 socket  //css.   (#가정)클라이언트가 어셉트 기다리다 타임아웃 걸림
				if (que.offer(cs)) {
					try {
						exec.execute(new ServerThread(que.poll()));
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
