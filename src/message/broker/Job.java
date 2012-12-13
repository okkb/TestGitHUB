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
	private byte[] msgSize = new byte[10];

	public Job(Socket socket, String serverName, int serverPort) throws IOException // ���� , ����������, ������Ʈ
	{
		this.socket = socket;
		this.serverSocket = new Socket(serverName, serverPort); // ���� ������ socket
		this.cdis = new DataInputStream(this.socket.getInputStream());
		this.cdos = new DataOutputStream(this.socket.getOutputStream());
		this.sdis = new DataInputStream(this.serverSocket.getInputStream());
		this.sdos = new DataOutputStream(this.serverSocket.getOutputStream());
	}

	public void execute() {
		try {
			cdis.readFully(clientInput); // ����
			for (int i = 0; i < 10; i++) {
				msgSize[i] = clientInput[i];
			}

			// ///////// Spec �˻� ���� �ʿ�
			if (msgSize[7] == '3' && msgSize[8] == '0' && msgSize[9] == '0') {
				sdos.write(clientInput);// �۽�
				sdis.readFully(serverInput);
				/*
				 * �������� �� ���̴� spec�ؼ� �˻� �ʿ�
				 */
				cdos.write(serverInput);
			} else {
				cdos.writeBytes("[from Broker] Message Spec ���� ���� ����");
			}
			try {
				cdis.close();
			} catch (IOException e) {
				System.err.println("cdis.close() : " + e.getCause());
				e.printStackTrace();
			} finally {
				cdis = null;
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
		} catch (IOException e) {
			System.err.println("Job �ۼ��� �� : " + e.getCause());
			e.printStackTrace();
		}
	}

}
