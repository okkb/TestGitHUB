package message.broker;
 
import java.util.LinkedList;
import java.util.Queue;
     
public class JobQueue { 
	private Queue<Job> que = new LinkedList<Job>();
	private Mutex key = new Mutex();
	private Mutex stopKey = new Mutex();
	
	public void enQueue(Job job) throws InterruptedException {
		key.acquire(); // KEY È¹µæ ¼º°ø
		que.add(job);
		stopKey.release(); // STOP_KEY ¹Ý³³( deQueue()ÀÇ while{}À» Å»Ãâ )
		key.release(); // KEY ¹Ý³³
	}

	public Job deQueue() throws InterruptedException {
		key.acquire(); // KEY È¹µæ ¼º°ø
		while (que.size() <= 0) {
			key.release(); // KEY ¹Ý³³
			stopKey.acquire(); //Ã¹ È¸Àü¿¡ STOP_KEY ¸¦ ¾òÀ½, Áï ÇÑ¹ø¸¸ µ¹°í µÎ¹øÂ° È¸Àü¿¡¼­ enQueue()¸¦ ±â´Ù¸°´Ù.
			key.acquire();// KEY È¹µæ ¼º°ø
		}
		Job job = que.remove();
		stopKey.release();// STOP_KEY ¹Ý³³
		key.release();// KEY ¹Ý³³
		return job;
	}
}
