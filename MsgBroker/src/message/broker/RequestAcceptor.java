package message.broker;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import main.java.lempel.blueprint.base.concurrent.WorkerGroup;

public class RequestAcceptor extends Thread {
	private final int WORKER_COUNT;
	private String serverName = "";
	private int serverPort = -1;
	private int clientPort = -1;
	private WorkerGroup wGroup;

	public RequestAcceptor(String serverName, int serverPort, int clientPort, int workerCount) throws IllegalArgumentException,	SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		super();
		this.serverName = serverName;
		this.serverPort = serverPort;
		this.clientPort = clientPort;
		this.WORKER_COUNT = workerCount;
		this.wGroup = new WorkerGroup(JobWorker.class, WORKER_COUNT);
	}

	public void run() {
		ServerSocket css = null;
		Socket cs = null;
		try {
			css = new ServerSocket();
			css.setReuseAddress(true);
			css.bind(new InetSocketAddress(InetAddress.getByName(serverName), clientPort), 15);
			System.out.println("MessageBroker Start");
		} catch (IOException e) {
			System.err.println("Cause : " + e.getCause());
			e.printStackTrace();
		}

		while (true) {
			try {
				cs = css.accept(); // ClientSocket
			} catch (IOException e) {
				System.err.println("cs = css.accept() : " + e.getCause());
				e.printStackTrace();
			}
			try {
				Job job = new Job(cs, serverName, serverPort);
				wGroup.addJob(job);
			} catch (IOException e) {
				System.err.println("Job job = new Job(cs, serverName, serverPort) : "+ e.getCause());
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.err.println("wGroup.addJob(job) : " + e.getCause());
				e.printStackTrace();
			}
		}
	}

}
