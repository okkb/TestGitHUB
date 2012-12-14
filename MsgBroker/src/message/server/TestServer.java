package message.server;
   
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
 
public class TestServer {
	public static void main(String[] args) throws IOException {
	int port = -1; 
	try {
		port = Integer.parseInt(args[0]);
	} catch (NumberFormatException e) {
		System.err.println("java TestServer <port>");
		System.exit(0);
	}
		ServerSocket sc = new ServerSocket(port);
		new Connection(sc);
		System.out.println("서버 오픈");
	}
}
 
class Connection extends Thread {
	private Socket socket;
	private ServerSocket sc;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	public Connection(ServerSocket sc) {
		this.sc = sc; 
		start();
	}
	
	public void run() {
		try {
			while (true) {
				socket = sc.accept();
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