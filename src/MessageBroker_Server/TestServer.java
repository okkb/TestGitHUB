package MessageBroker_Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {

	public static void main(String[] args) {
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
		byte[] input = new byte[300];
		try {
			// 연결
		/////while{
				ServerSocket ssocket = new ServerSocket(port);
				System.out.println("접속을 기다림");
				Socket socket = ssocket.accept();
				DataInputStream dis = new DataInputStream(
						socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(
						socket.getOutputStream());

				// 수신
				dis.readFully(input);
				// 결과 확인 - 일치해야 정상

				// 송신
				dos.write(input);

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
			//}  while  end
		} catch (IOException e) {
			System.err.println("통신 오류");
			e.printStackTrace();
		}
	}

}
