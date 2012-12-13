package message.broker;

import java.util.LinkedList;
import java.util.Queue;

public class JobQueue {
	private final Mutex key = new Mutex();
	private Queue<Job> jobQue = new LinkedList<Job>();

	public void enQueue(Job job) throws InterruptedException {
		key.acquire(); // keyÈ¹µæ ¼º°ø
		jobQue.add(job);
		key.release(); // key ¹Ý³³
	}

	public Job deQueue() throws InterruptedException {
		key.acquire(); // key È¹µæ ¼º°ø
		while (jobQue.size() <= 0) { // jobQue.size()°¡ 0ÃÊ°ú µÉ¶§±îÁö ¹Ýº¹
			key.release(); // key ¹Ý³³
			// ÀÌ¶§ enQueue¿¡¼­ key È¹µæÇØ jobQue.size()°¡ 0 ÃÊ°ú µÇ¸é Å»Ãâ
			key.acquire();// enQueue¿¡¼­ Å° ¹Ý³³ÇÏ¸é key È¹µæ ¼º°ø
		}
		Job job = jobQue.remove();
		key.release();// key ¹Ý³³
		return job;
	}
}
