package MessageBroker_Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MsgBroker {
	private int port = -1;

	public MsgBroker(int port) 
	{
		this.port = port;
	}

	public static void main(String args[]) 
	{
		if (args.length != 1) {
			System.err.println("java TestServer <port>");
			System.exit(1);
		}
		int port = -1;
		try {
			port = Integer.parseInt(args[0]); // port

		} catch (NumberFormatException e) {
			System.err.println("java TestServer <port>");
			System.exit(2);
		}
		new MsgBroker(port).start();
	}

	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("서버가 시작되었습니다.");
			while (true) {
				System.out.println("<Client 접속을 기다립니다.>");
				socket = serverSocket.accept();
				try {
					ServerThread th = new ServerThread(socket);
					th.start();
				} catch (IOException e) {
					System.out.println(e.getCause());
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			System.err.println("통신 오류");
			e.printStackTrace();
		}
	}

	class ServerThread extends Thread {
		private DataInputStream dis;
		private DataOutputStream dos;
		private byte[] input = new byte[300];
		private byte[] msgSize = new byte[10];

		public ServerThread(Socket socket) throws IOException { // 생성자
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
				if (msgSize[7] == '3' && msgSize[8] == '0' && msgSize[9] == '0')
					dos.write(input);// 송신
				else
					dos.writeBytes("[from Server] 전송 실패");

			} catch (IOException e) {
				System.out.println("run중 : " + e.getCause());
				e.printStackTrace();
			}
			// try {
			// target = in.readUTF();
			// //targer= in.readFully();
			// hashMap.put(sender, out);
			// while (in2 != null) {
			// String input = in2.readUTF();
			// String[] strArr = input.split("/");
			// String lengthField = strArr[0];
			// char[] strCharArr = lengthField.toCharArray();
			// StringBuffer strBuf = new StringBuffer(strCharArr.length);
			//
			// for (int i = 0; i < strCharArr.length; i++) {
			// String oneStr = String.valueOf(strCharArr[i]);
			// if (!oneStr.equals("0")) {
			// strBuf.append(oneStr);
			// }
			// }
			// msgByte = Integer.parseInt(strBuf + "") - 10;
			// msgContent = strArr[1];
			// msgCheck = cutStrByte(msgContent, msgByte);
			//
			// if (msgContent.equals(msgCheck)) {// 수신된 메시지와 길이필드의 byte만큼 짜른
			// 메시지가 같은지 확인
			// if (hashMap.containsKey(target)) {// target이 Server에 접속했는지 확인
			// DataOutputStream out = (DataOutputStream) hashMap.get(target);
			// DataOutputStream out2 = (DataOutputStream) hashMap.get(sender);
			// out.writeUTF("[from " + sender + "] : " + msgCheck); // target으로
			// 전송
			// out2.writeUTF("[from Server] Target으로 Message 전송완료");
			// System.out.println(sender+"로 부터 Message Spec을 준수하는 메시지 ["+input+"]을(를) 받아 "+target+"으로 전송 하였습니다.");
			// System.out.println("현재 동작하는 Thread 개수 "+Thread.activeCount());
			// } else {
			// DataOutputStream out = (DataOutputStream) hashMap.get(sender);
			// out.writeUTF("[from Server] 해당 Target이 없습니다.");
			// }
			// } else {
			// DataOutputStream out = (DataOutputStream) hashMap.get(sender);
			// out.writeUTF("[from Server] Message Spec이 준수 되지 않아 "+target+"으로 전송 되지 않았습니다.");
			// }
			// }
			// } catch (IOException e) {
			// e.printStackTrace();
			// } finally {
			// hashMap.remove(sender);
			// System.out.println("[" +
			// (sc.getInetAddress()+"").substring(1)+"]" + "에서 접속을 종료 하였습니다.");
			// System.out.println("현재 접속 Client 수는" + hashMap.size() +
			// "대 입니다.");
			// }
		}
	}

	// byte 크기 만큼 문자열 짜르는 메소드

}
