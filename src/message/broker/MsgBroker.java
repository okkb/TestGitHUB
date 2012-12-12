package message.broker;

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
	private static final int THREADCOUNT = 1000;
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
		Socket ss = null; //ServerSocket 
		ServerSocket css = null;  //ClientServerSocket
		Socket cs = null;  //ClientSocket
		ExecutorService exec = Executors.newFixedThreadPool(THREADCOUNT);	// ������Ǯ ���� THREADCOUNT ���� ��ŭ
		
		try {
			css = new ServerSocket(clientPort);
			System.out.println("������ ���۵Ǿ����ϴ�.");
			while (true) {				
				ss = new Socket(serverName, serverPort);
				System.out.println("<Client ������ ��ٸ��ϴ�.>");
				cs = css.accept();   // broker�� ������ Ŭ���̾�Ʈ socket
				if (que.offer(cs)) {
					try {
						exec.execute(new MsgThread(que.poll(), ss));
					} catch (IOException e) {
						System.err.println(e.getCause());
						e.printStackTrace();
					}
				} else {
					System.err.println("Quere�� socket �߰� ����");
				}
			}
		} catch (IOException e) {
			System.err.println("��� ����");
			e.printStackTrace();
		} finally {
			try {
				css.close();
			} catch (IOException e) {
				System.err.println(e.getCause());
				e.printStackTrace();
			}finally{
				css = null;
			}
			try {
				ss.close();
			} catch (IOException e) {
				System.err.println(e.getCause());
				e.printStackTrace();
			}finally{
				ss = null;
			}
			exec.shutdown();
		}
	}
	
	class MsgThread implements Runnable {
		private Socket socket = null;
		private Socket serverSocket = null;
		private DataInputStream cdis;
		private DataOutputStream cdos;
		private DataInputStream sdis;
		private DataOutputStream sdos;
		private byte[] clientInput = new byte[300];
		private byte[] serverInput = new byte[300];
		private byte[] msgSize = new byte[10];

		public MsgThread(Socket socket, Socket serverSocket) throws IOException { // ������
			super();
			this.socket = socket;
			this.serverSocket = serverSocket;
			this.cdis = new DataInputStream(this.socket.getInputStream());
			this.cdos = new DataOutputStream(this.socket.getOutputStream());
			//this.serverSocket = new Socket(serverName, serverPort); //���� ������ socket
			this.sdis = new DataInputStream(this.serverSocket.getInputStream());
			this.sdos = new DataOutputStream(this.serverSocket.getOutputStream());
		}

		public void run() {
			try {
				
				
				cdis.readFully(clientInput); // ����
				for (int i = 0; i < 10; i++) {
					msgSize[i] = clientInput[i];
				}

				///////////  Spec �˻� ���� �ʿ�
				if (msgSize[7] == '3' && msgSize[8] == '0' && msgSize[9] == '0'){
					sdos.write(clientInput);// �۽�
					sdis.readFully(serverInput);
					/*
					�������� �� ���̴� spec�ؼ� �˻� �ʿ�
					*/
					cdos.write(serverInput);
				}else{
					cdos.writeBytes("[from Server] Message Spec ���� ���� ����");
				}
				////////////
				try {
					cdis.close();
				} catch (IOException e) {
					System.err.println("MsgThread ��-1 : "+e.getCause());
					e.printStackTrace();
				} finally {
					cdis = null;
				}
				try {
					cdos.close();
				} catch (IOException e) {
					System.err.println(e.getCause());
					e.printStackTrace();
				} finally {
					cdos = null;
				}
				try {
					socket.close();
				} catch (IOException e) {
					System.err.println(e.getCause());
					e.printStackTrace();
				} finally {
					socket = null;
				}
				try {
					sdis.close();
				} catch (IOException e) {
					System.err.println(e.getCause());
					e.printStackTrace();
				} finally {
					sdis = null;
				}
				try {
					sdos.close();
				} catch (IOException e) {
					System.err.println(e.getCause());
					e.printStackTrace();
				} finally {
					sdos = null;
				}
				try {
					serverSocket.close();
				} catch (IOException e) {
					System.err.println(e.getCause());
					e.printStackTrace();
				} finally {
					serverSocket = null;
				}
			} catch (IOException e) {
				System.err.println(e.getCause());
				e.printStackTrace();
			}
		}

	}
}
