package MessageBroker_Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MB_Server {
	private HashMap<String, DataOutputStream> hashMap;
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public MB_Server() {
		this.hashMap = new HashMap<String, DataOutputStream>();
		//Collections.synchronizedMap(hashMap);// 사용법 x 무의미하다 
	}

	public void start() {
		ServerSocket serverSocket = null;
		Socket sc = null;
		String port = "";
		int portInt = 0;
		boolean loop = true;
		try {

			while (loop) {
				System.out.print("연결요청을 기다릴 서버의 port(1023~65535) 입력 :");
				port = br.readLine();
				Pattern pattern = Pattern.compile("\\d{4,5}");
				Matcher matcher = pattern.matcher(port);
				
				if (matcher.find()) {
					portInt = Integer.parseInt(port);
					if (portInt < 1023 || portInt > 65535) {
						System.out.println("1023~65535 번호로 입력하세요.");
					} else {
						System.out.println();
						loop = false;
					}
				}else{
					System.out.println("잘못 입력하셨습니다.");
				}
			}

			serverSocket = new ServerSocket(portInt);
			System.out.println("서버가 시작되었습니다.");
			while (true) {
				System.out.println("<Client 접속을 기다립니다.>");
				sc = serverSocket.accept();
				String sender = (sc.getInetAddress() + "").substring(1);
				try{
					serverThread th = new serverThread(sc, sender);
					th.start();
				}catch(IOException e){
					System.out.println(e.getCause());
				}				
				System.out.println(sender + "에서 접속했습니다.");
				//System.out.println("현재 동작하는 Thread 개수 "+S_Thread.activeCount()); 사용자의 Thread만 출력되지x
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// byte 크기 만큼 문자열 짜르는 메소드
	public String cutStrByte(String str, int msgByte) {
		StringBuilder sbStr = new StringBuilder(msgByte);
		int total = 0;
		for (char c : str.toCharArray()) {
			total += String.valueOf(c).getBytes().length;
			if (total > msgByte) {
				break;
			}
			sbStr.append(c);
		}
		return sbStr.toString();
	}

	class serverThread extends Thread {
		private Socket sc;
		private DataInputStream in;
		private DataInputStream in2;
		private DataOutputStream out;
		private String target = "";
		private String sender = "";
		private String msgCheck = "";
		private String msgContent = "";
		private int msgByte=0;
		public serverThread(Socket sc, String sender) throws IOException { // 생성자
			this.sc = sc;
			this.sender = sender;
			try {
				in = new DataInputStream(sc.getInputStream());
				in2 = new DataInputStream(sc.getInputStream());
				out = new DataOutputStream(sc.getOutputStream());
			} catch (IOException e) {
				throw new IOException(e.getMessage(), e.getCause());
				//e.printStackTrace();
			}
		}

		public void run() {
			try {
				target = in.readUTF();
				//targer= in.readFully();
				hashMap.put(sender, out);
				while (in2 != null) {
					String input = in2.readUTF();
					String[] strArr = input.split("/");
					String lengthField = strArr[0];
					char[] strCharArr = lengthField.toCharArray();
					StringBuffer strBuf = new StringBuffer(strCharArr.length);
					
					for (int i = 0; i < strCharArr.length; i++) {
						String oneStr = String.valueOf(strCharArr[i]);
						if (!oneStr.equals("0")) {
							strBuf.append(oneStr);
						}
					}
					msgByte = Integer.parseInt(strBuf + "") - 10;
					msgContent = strArr[1];
					msgCheck = cutStrByte(msgContent, msgByte);

					if (msgContent.equals(msgCheck)) {// 수신된 메시지와 길이필드의 byte만큼 짜른 메시지가 같은지 확인
						if (hashMap.containsKey(target)) {// target이 Server에 접속했는지 확인
							DataOutputStream out = (DataOutputStream) hashMap.get(target);
							DataOutputStream out2 = (DataOutputStream) hashMap.get(sender);
							out.writeUTF("[from " + sender + "] : " + msgCheck);  // target으로 전송
							out2.writeUTF("[from Server] Target으로 Message 전송완료");
							System.out.println(sender+"로 부터 Message Spec을 준수하는 메시지 ["+input+"]을(를) 받아 "+target+"으로 전송 하였습니다.");
							System.out.println("현재 동작하는 Thread 개수 "+Thread.activeCount());
						} else {
							DataOutputStream out = (DataOutputStream) hashMap.get(sender);
							out.writeUTF("[from Server] 해당 Target이 없습니다.");
						}
					} else {
						DataOutputStream out = (DataOutputStream) hashMap.get(sender);
						out.writeUTF("[from Server] Message Spec이 준수 되지 않아 "+target+"으로 전송 되지 않았습니다.");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				hashMap.remove(sender);
				System.out.println("[" + (sc.getInetAddress()+"").substring(1)+"]" + "에서 접속을 종료 하였습니다.");
				System.out.println("현재 접속 Client 수는" + hashMap.size() + "대 입니다.");
			}
		}
	}
	public static void main(String args[]) {
		new MB_Server().start();
	}
}
