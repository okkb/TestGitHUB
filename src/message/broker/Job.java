package message.broker;
 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
  
public class Job {
	private Socket socket = null;
	private Socket serverSocket = null;
	private DataInputStream cdis;
	private DataOutputStream cdos;
	private DataInputStream sdis;
	private DataOutputStream sdos;
	private byte[] clientInput = new byte[300];
	private byte[] serverInput = new byte[300];


	public Job(Socket socket, String serverName, int serverPort)
			throws IOException // 소켓 , 서버아이피, 서버포트
	{
		this.socket = socket;
		this.serverSocket = new Socket(serverName, serverPort); // 서버 접속한 socket
		this.cdis = new DataInputStream(this.socket.getInputStream());
		this.cdos = new DataOutputStream(this.socket.getOutputStream());
		this.sdis = new DataInputStream(this.serverSocket.getInputStream());
		this.sdos = new DataOutputStream(this.serverSocket.getOutputStream());
	}

	public void execute() {
		try {
			cdis.readFully(clientInput); // from client
			int msgLength = checkMsgLength(clientInput);
			int totalLength = msgLength+10;
			byte[] serverOutput = new byte[totalLength];// 메시지내용만큼 공간
			System.arraycopy(clientInput, 0, serverOutput, 0, 10); //길이필드 copy
			System.arraycopy(clientInput, 10, serverOutput, 10, msgLength);// 메시지필드 copy
			
			sdos.write(serverOutput); // to server
			sdis.readFully(serverInput); // from server
			if (compare(serverOutput, serverInput)) {  // 같으면  클라이언트로 응답
				cdos.write(serverInput);
			} else {
			cdos.writeBytes("[from Server] Message Spec 위반 전송 실패");
			}
		} catch (IOException e) {
			System.err.println("Job 송수신 중 : " + e.getCause());
			e.printStackTrace();
		}

		try {
			cdos.close();
		} catch (IOException e) {
			System.err.println("cdos.close() : " + e.getCause());
			e.printStackTrace();
		} finally {
			cdos = null;
		}
		try { 
			socket.close();
		} catch (IOException e) {
			System.err.println("socket.close() : " + e.getCause());
			e.printStackTrace();
		} finally {
			socket = null;
		}
		try {
			sdis.close();
		} catch (IOException e) {
			System.err.println("sdis.close() : " + e.getCause());
			e.printStackTrace();
		} finally {
			sdis = null;
		}
		try {
			sdos.close();
		} catch (IOException e) {
			System.err.println("	sdos.close() : " + e.getCause());
			e.printStackTrace();
		} finally {
			sdos = null;
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("serverSocket.close() : " + e.getCause());
			e.printStackTrace();
		} finally {
			serverSocket = null;
		}
	}

	private static boolean compare(byte[] src, byte[] dst) { // 같은지 검사
		boolean result = true;

		if (src == null || dst == null || src.length != dst.length) {
			result = false;
		} else {
			for (int i = 0; i < src.length; i++) {
				if (src[i] != dst[i]) {
					result = false;
					break;
				}
			}
		}
		return result;
	}

	private static int checkMsgLength(byte[] data) {// 메시지필드 크기 구함
		byte[] msgSize = new byte[10];
		for (int i = 0; i < 10; i++) {
			msgSize[i] = data[i];
		}
		char[] len = new char[10];
		for (int i = 0; i < 10; i++) {
			if (msgSize[i] != 0) {
				int k = 0;
				for (int j = i; j < 10; j++) {
					len[k] = (char) msgSize[j];
					k++;
				}
				break;
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len.length; i++) {
			sb.append(len[i]);
		}
		return Integer.parseInt(sb.toString()) - 10;
	}
}
