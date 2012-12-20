package message.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestServer {
	public static void main(String[] args) throws IOException {
		int port = -1;
		Pattern pattern = Pattern.compile("\\d{4,5}");
		Matcher matcher = pattern.matcher(args[0]);
		try{
			port = Integer.parseInt(args[0]);
		}catch(NumberFormatException e){
			System.err.println("java TestServer <port(1023~65535)>");
			System.exit(1);
		}
		if(!matcher.find() || !(port >= 1023 && port < 65536)){	
			System.err.println("java TestServer <port(1023~65535)>");
			System.exit(2);
			}
	
		ServerSocket sSocket = new ServerSocket();
		sSocket.setReuseAddress(true);
		sSocket.bind(new InetSocketAddress(port), 15); //요청 대기 큐크기 15
		new Connection(sSocket);
		System.out.println("서버 오픈");
	}
}

class Connection extends Thread {
	private Socket socket;
	private ServerSocket sSocket;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;

	public Connection(ServerSocket sSocket) {
		this.sSocket = sSocket;
		start();
	}

	public void run() {
		try {
			while (true) {
				socket = sSocket.accept();
				byte[] data = new byte[300];
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				dis.readFully(data);
				dos.write(data);
				dos.flush();

				// 연결 종료
				try {
					dis.close();
				} catch (Exception ignored) {
				} finally {
					dis = null;
				}
				try {
					dos.close();
				} catch (Exception ignored) {
				} finally {
					dos = null;
				}
				try {
					socket.close();
				} catch (Exception ignored) {
				} finally {
					socket = null;
				}
			}
		} catch (IOException e) {
		}
	}
}