package MessageBroker_Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MB_Client {
	
	static class ClientSender extends Thread {
    	private Socket socket = null;
		private String targetIp = "";
		private DataOutputStream out = null;

		public ClientSender(Socket socket, String targetIp) {
			try {
				this.socket=socket;
				this.targetIp = targetIp;
				this.out = new DataOutputStream(this.socket.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			Scanner s = new Scanner(System.in);
			try {
				out.writeUTF(targetIp);
				
				while (out != null) {  
					try{
					ClientSender.sleep(200);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					
					System.out.print("전송할 Message 입력 (종료 : exit) : ");
					String str = s.nextLine().trim();
					
					if (str.equalsIgnoreCase("exit")){
						System.exit(0);						
					}
						
					String zero = "";
					int totalLength = 0;
					
					if (str != null && !str.equals("")) {
						
						if (str.length() == str.getBytes().length) { // 한글 비포함
							totalLength = str.getBytes().length + 10;
						} else {             // 한글 포함될때는 정규 표현식으로 구분
							char[] strCharArr = str.toCharArray();
							totalLength = 0;
							
							for (int i = 0; i < strCharArr.length; i++) {
								String oneStr = String.valueOf(strCharArr[i]);
								
								if (oneStr.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))
									totalLength += 3;
								else
									totalLength += 1;
							}
							
							totalLength += 10;
						}

						for (int i = 0; i < 10 - Integer.toString(totalLength)	.length(); i++) {
							zero += "0";
						}
						
						zero = zero + totalLength;
						out.writeUTF(zero + "/" + str);   // 출력 : 길이필드 + / + 메세지필드
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    static	class ClientReceiver extends Thread {
    	private Socket socket = null;
		private DataInputStream in = null;
		
		public ClientReceiver(Socket socket) {
			try {
				this.socket=socket;
				this.in = new DataInputStream(this.socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			while (in != null) {
				try {
					System.out.println(in.readUTF());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(	System.in));
			boolean loop = true;
			boolean loop2 = true;
			String serverIp = "";
			String targetIp = "";
			String port = "";
			int portINT = 0;
			
			while (loop) {
				System.out.print("연결할 Server의 IP주소 입력 :");
				serverIp = br.readLine();
				System.out.print("Message를 보낼 Target IP주소 입력 :");
				targetIp = br.readLine();

				Pattern pattern = Pattern	.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
				Matcher matcher = pattern.matcher(serverIp);
				Matcher matcher2 = pattern.matcher(targetIp);

				if (matcher.find() && matcher2.find()) { // 패턴이 일치확인
					loop = false;
				} else {
					System.out.println("Server IP주소 또는 Target IP주소를 잘 못 입력 하셨습니다.");
				}
			}
			
			while (loop2) {
				System.out.print("연결할 서버의 port(1023~65535) 입력 :");
				port = br.readLine();
				Pattern pattern = Pattern.compile("\\d{4,5}");
				Matcher matcher = pattern.matcher(port);

				if (matcher.find()) {
					portINT = Integer.parseInt(port);
					
					if (portINT < 1023 || portINT > 65535) {
						System.out.println("1023~65535 번호로 입력하세요.");
					} else {
						System.out.println();
						loop2 = false;
					}
					
				} else {
					System.out.println("port 번호를 잘 못 입력 하셨습니다.");
				}
			}
			
			Socket socket = new Socket(serverIp, portINT);
			System.out.println("서버에 연결 되었습니다.");
			new ClientSender(socket, targetIp).start();
			new ClientReceiver(socket).start();
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
