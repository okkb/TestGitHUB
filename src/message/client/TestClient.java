package message.client;
 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
 
/**
 * 테스트용 client
 *  
 * @author Simon Lee
 */
public class TestClient {
	/**
	 * Entry Point
	 * 
	 * @param args
	 *            port
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("java TestClient <host name> <port>");
			System.exit(1);
		}

		String host = args[0];
		int port = -1;

		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.err.println("java TestClient <host name> <port>");
			System.exit(2);
		}
		
		// 랜덤 데이터 생성
		byte[] data = new byte[300];
		byte[] result = new byte[300];
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) (Math.random() * 100);
		}

		// 길이 필드에 0000000300 set
		for (int i = 0; i < 10; i++) {
			data[i] = '0';
		}
		data[7] = '3';

		try {
			long count =0;
			while (true) {
				// 연결
				Socket sock = new Socket(host, port);
				DataInputStream dis = new DataInputStream(sock.getInputStream());
				DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
				// 송수신
				dos.write(data);
				dis.readFully(result);

				// 결과 확인 - 일치해야 정상
				if (compare(data, result)) {					
					System.out.println("데이터 일치"+(count++));
					System.out.println(result);
				} else {
					System.out.println("데이터 불일치");
				}
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
					sock.close();
				} catch (Exception ignored) {
				} finally {
					sock = null;
				}
			}
		} catch (UnknownHostException e) {
			System.err.println("알 수 없는 host");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("통신 오류");
			e.printStackTrace();
		}
	} 

	/** 
	 * Source와 destination을 비교
	 * 
	 * @param src
	 * @param dst
	 * @return 같으면 true
	 */
	private static boolean compare(byte[] src, byte[] dst) {
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
}
