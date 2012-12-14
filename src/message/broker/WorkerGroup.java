package message.broker;

import java.io.IOException; 
import java.net.ServerSocket;
import java.net.Socket;
 
public class WorkerGroup extends Thread {
	private static final int THREAD_COUNT = 50;
	private String serverName = "";
	private int serverPort = -1;
	private int clientPort = -1;
	private JobQueue que;	
	private Worker[] workers;
	
	public WorkerGroup(String serverName, int serverPort, int clientPort) {
		this.serverName = serverName;
		this.serverPort = serverPort;
		this.clientPort = clientPort;
		this.que = new JobQueue();
		this.workers = new Worker[THREAD_COUNT];
	}
 
	public void run() {	
		workersStart();
		ServerSocket css = null; // ClientServerSocket
		Socket cs = null; // ClientSocket

		try {
			css = new ServerSocket(clientPort);
			System.out.println("MessageBroker Start");
		} catch (IOException e) {
			System.err.println("css = new ServerSocket(clientPort) : "+ e.getCause());
			e.printStackTrace();
		}
		try {
			while (true) {
				try {
					cs = css.accept();
				} catch (IOException e) {
					System.err.println("cs = css.accept() : " + e.getCause());
					e.printStackTrace();
				}
				try {
					Job job = new Job(cs, serverName, serverPort);
					que.enQueue(job);
				} catch (IOException e) {
					System.err.println("Job job = new Job(cs, serverName, serverPort) : "+ e.getCause());
					e.printStackTrace();
				} catch (InterruptedException e) {
					System.err.println("run() in-2 : " + e.getCause());
					e.printStackTrace();
				}				
			}
		} finally {
			try {
				css.close();
			} catch (IOException e) {
				System.err.println("run() in-4 : " + e.getCause());
				e.printStackTrace();
			} finally {
				css = null;
			}
		}
	}
	
	public void workersStart(){
		for (int i = 0; i < THREAD_COUNT; i++) {
			workers[i] = new Worker(que);
			workers[i].setDaemon(true);
			workers[i].start();
		}
	}
}
