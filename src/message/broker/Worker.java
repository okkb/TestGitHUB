package message.broker;
  
public class Worker extends Thread {
	private JobQueue que;

	public Worker(JobQueue que)  
	{
		this.que = que;
	}

	public void run() {
		while (true) {
			try {
				Job job = que.deQueue();
				job.execute();
			} catch (InterruptedException e) {
				System.err.println("Job job = que.deQueue() : " + e.getCause());
				e.printStackTrace();
			}
		}
	}
}
