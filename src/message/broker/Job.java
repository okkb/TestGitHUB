package message.broker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Job 
{
	private Socket socket = null;
	private Socket serverSocket = null;
	private DataInputStream cdis;
	private DataOutputStream cdos;
	private DataInputStream sdis;
	private DataOutputStream sdos;
	private byte[] clientInput = new byte[300];
	private byte[] serverInput = new byte[300];
	private byte[] msgSize = new byte[10];

    public Job (Socket socket, String serverName, int serverPort) throws IOException  // 소켓 ,  서버아이피, 서버포트
    {   
    	this.socket = socket;
		this.serverSocket = new Socket(serverName, serverPort); // 서버 접속한 socket
		this.cdis = new DataInputStream(this.socket.getInputStream());
		this.cdos = new DataOutputStream(this.socket.getOutputStream());
		this.sdis = new DataInputStream(this.serverSocket.getInputStream());
		this.sdos = new DataOutputStream(this.serverSocket.getOutputStream());       
    }
    public void execute () {//실행될꺼듯 
    	try {
			cdis.readFully(clientInput); // 수신
			
			for (int i = 0; i < 10; i++) {
				msgSize[i] = clientInput[i];
			}
			if (msgSize[7] == '3' && msgSize[8] == '0' && msgSize[9] == '0') {//////// Spec 검사 수정 필요
				sdos.write(clientInput);// 송신
				sdis.readFully(serverInput);
				/*
				 * 서버에서 온 테이더 spec준수 검사 필요
				 */
				cdos.write(serverInput);
			} else {
				cdos.writeBytes("[from Broker] Message Spec 위반 전송 실패");
			}
			// //////////
			try {
				cdis.close();
			} catch (IOException e) {
				System.err.println("Job in-1 : " + e.getCause());
				e.printStackTrace();
			} finally {
				cdis = null;
			}
			try {
				cdos.close();
			} catch (IOException e) {
				System.err.println("Job in-2 : " + e.getCause());
				e.printStackTrace();
			} finally {
				cdos = null;
			}
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println("Job in-3 : " + e.getCause());
				e.printStackTrace();
			} finally {
				socket = null;
			}
			try {
				sdis.close();
			} catch (IOException e) {
				System.err.println("Job in-4 : " + e.getCause());
				e.printStackTrace();
			} finally {
				sdis = null;
			}
			try {
				sdos.close();
			} catch (IOException e) {
				System.err.println("Job in-5 : " + e.getCause());
				e.printStackTrace();
			} finally {
				sdos = null;
			}
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Job in-6 : " + e.getCause());
				e.printStackTrace();
			} finally {
				serverSocket = null;
			}
		} catch (IOException e) {
			System.err.println("Job in-7 : " + e.getCause());
			e.printStackTrace();
		}       
    }
  
}
