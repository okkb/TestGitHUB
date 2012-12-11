package MessageBroker_Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MsgBroker {
	private final static int THREADCOUNT = 1000;
	private int port = -1;
	private Queue<Socket> que = new LinkedList<Socket>();

	public MsgBroker(int port) {
		this.port = port;
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
			new MsgBroker(port).start();
		} catch (InterruptedException e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}

	public void start() throws InterruptedException
	{
		ServerSocket serverSocket = null;
		Socket socket = null;
		ExecutorService exec = Executors.newFixedThreadPool(THREADCOUNT);
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("서버가 시작되었습니다.");
			// 쓰레드풀 생성 THREADCOUNT 개수 만큼
		
			while (true) {
				System.out.println("<Client 접속을 기다립니다.>");
				socket = serverSocket.accept();
				if (que.offer(socket)) {
					try {						
						exec.execute(new ServerThread(que.poll()));
					} catch (IOException e) {
						System.err.println(e.getCause());
						e.printStackTrace();
					}
				}else{
					System.err.println("Quere에 socket 추가 실패");
				}
				
			}
		} catch (IOException e) {
			System.err.println("통신 오류");
			e.printStackTrace();
		}finally{
			try{
			serverSocket.close();
			}catch(IOException e){
				System.err.println(e.getCause());
				e.printStackTrace();
			}
			exec.shutdown();
		}
	}

	class ServerThread implements Runnable
	{
		private DataInputStream dis;
		private DataOutputStream dos;
		private byte[] input = new byte[300];
		private byte[] msgSize = new byte[10];

		public ServerThread(Socket socket) throws IOException { // 생성자
			super();
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
		}

		public void run()
		{
			try {
				dis.readFully(input); // 수신
				for (int i = 0; i < 10; i++) {
					msgSize[i] = input[i];
				}
				
				
				/////////
				if (msgSize[7] == '3' && msgSize[8] == '0' && msgSize[9] == '0')
					dos.write(input);// 송신
				else
					dos.writeBytes("[from Server] 전송 실패");
				////////////

			} catch (IOException e) {
				System.err.println(e.getCause());
				e.printStackTrace();
			}
		}
		
	}
}
