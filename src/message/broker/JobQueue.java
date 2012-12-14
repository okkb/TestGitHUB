package message.broker;

import java.util.LinkedList;
import java.util.Queue;

public class JobQueue {
	private Queue<Job> jobQueue = new LinkedList<Job>();
	private final Mutex KEY = new Mutex();
	private final Mutex STOP_KEY = new Mutex();
	
	public void enQueue(Job job) throws InterruptedException {
		KEY.acquire(); // key È¹µæ ¼º°ø
		jobQueue.add(job);
		STOP_KEY.release(); // stopkey ¹Ý³³( deQueue()ÀÇ while{}À» Å»Ãâ )
		KEY.release(); // key ¹Ý³³
	}

	public Job deQueue() throws InterruptedException {
		KEY.acquire(); // key È¹µæ ¼º°ø
		while (jobQueue.size() <= 0) {
			KEY.release(); // key ¹Ý³³
			STOP_KEY.acquire(); //Ã¹ È¸Àü¿¡ stopÅ° ¸¦ ¾òÀ½, Áï ÇÑ¹ø¸¸ µ¹°í µÎ¹øÂ° È¸Àü¿¡¼­ enQueue()¸¦ ±â´Ù¸°´Ù.
			KEY.acquire();// key È¹µæ ¼º°ø
		}
		Job job = jobQueue.remove();
		STOP_KEY.release();// stopÅ° ¹Ý³³
		KEY.release();// key ¹Ý³³
		return job;
	}
}
