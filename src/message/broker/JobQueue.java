package message.broker;
 
import java.util.LinkedList;
import java.util.Queue;
 
public class JobQueue { 
	private Queue<Job> Queue = new LinkedList<Job>();
	private Mutex KEY = new Mutex();
	private Mutex STOP_KEY = new Mutex();
	
	public void enQueue(Job job) throws InterruptedException {
		KEY.acquire(); // KEY È¹µæ ¼º°ø
		Queue.add(job);
		STOP_KEY.release(); // STOP_KEY ¹Ý³³( deQueue()ÀÇ while{}À» Å»Ãâ )
		KEY.release(); // KEY ¹Ý³³
	}

	public Job deQueue() throws InterruptedException {
		KEY.acquire(); // KEY È¹µæ ¼º°ø
		while (Queue.size() <= 0) {
			KEY.release(); // KEY ¹Ý³³
			STOP_KEY.acquire(); //Ã¹ È¸Àü¿¡ STOP_KEY ¸¦ ¾òÀ½, Áï ÇÑ¹ø¸¸ µ¹°í µÎ¹øÂ° È¸Àü¿¡¼­ enQueue()¸¦ ±â´Ù¸°´Ù.
			KEY.acquire();// KEY È¹µæ ¼º°ø
		}
		Job job = Queue.remove();
		STOP_KEY.release();// STOP_KEY ¹Ý³³
		KEY.release();// KEY ¹Ý³³
		return job;
	}
}
