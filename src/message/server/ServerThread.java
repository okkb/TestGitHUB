package message.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread implements Runnable {
	private Socket socket = null;
	private DataInputStream cdis;
	private DataOutputStream cdos;
	private byte[] clientInput = new byte[300];
	private byte[] msgSize = new byte[10];

	public ServerThread(Socket socket) throws IOException {
		this.socket = socket;
		this.cdis = new DataInputStream(this.socket.getInputStream());
		this.cdos = new DataOutputStream(this.socket.getOutputStream());
	}

	public void run() {
		try {
			cdis.readFully(clientInput); // ����
			for (int i = 0; i < 10; i++) {
				msgSize[i] = clientInput[i];
			}

			// ///////// Spec �˻� ���� �ʿ�
			if (msgSize[7] == '3' && msgSize[8] == '0' && msgSize[9] == '0') {
				cdos.write(clientInput);// �۽�
			} else {
				cdos.writeBytes("[from Server] Message Spec ���� ���� ����");
			}
			// //////////
			try {
				cdis.close();
			} catch (IOException e) {
				System.err.println("MsgThread in-1 : " + e.getCause());
				e.printStackTrace();
			} finally {
				cdis = null;
			}
			try {
				cdos.close();
			} catch (IOException e) {
				System.err.println("MsgThread in-2 : " + e.getCause());
				e.printStackTrace();
			} finally {
				cdos = null;
			}
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println("MsgThread in-3 : " + e.getCause());
				e.printStackTrace();
			} finally {
				socket = null;
			}
			
		} catch (IOException e) {
			System.err.println("MsgThread in-7 : " + e.getCause());
			e.printStackTrace();
		}		
	}

}