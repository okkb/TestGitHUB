package message.broker;

import java.util.LinkedList;
import java.util.Queue;

public class JobQueue {
	private Queue<Job> jobQue = new LinkedList<Job>();
	private final Mutex key = new Mutex();
	private final Mutex stopkey = new Mutex();
	
	public void enQueue(Job job) throws InterruptedException {
		key.acquire(); // key È¹µæ ¼º°ø
		jobQue.add(job);
		stopkey.release(); // stopÅ° ¹Ý³³( deQueue()ÀÇ while{}À» Å»Ãâ )
		key.release(); // key ¹Ý³³
	}

	public Job deQueue() throws InterruptedException {
		key.acquire(); // key È¹µæ ¼º°ø
		while (jobQue.size() <= 0) {
			key.release(); // key ¹Ý³³
			stopkey.acquire(); //Ã¹ È¸Àü¿¡ stopÅ° ¸¦ ¾òÀ½, Áï ÇÑ¹ø¸¸ µ¹°í µÎ¹øÂ° È¸Àü¿¡¼­ enQueue()¸¦ ±â´Ù¸°´Ù.
			key.acquire();// key È¹µæ ¼º°ø
		}
		Job job = jobQue.remove();
		stopkey.release();// stopÅ° ¹Ý³³
		key.release();// key ¹Ý³³
		return job;
	}
}
