package message.broker;

import java.util.LinkedList;
import java.util.Queue;

public class JobQueue {
	private final Mutex key = new Mutex();
	private final Mutex lock = new Mutex();
	//private final Job[] jobQue;   // ¹è¿­·Î Å¥ ±¸Çö 
	private Queue<Job> jobQue = new LinkedList<Job>();
	private int tail;
	private int head;
	private int count;

	public JobQueue(/*int size*/) // Queue»ý¼ºÀÚ
	{
	//	this.jobQue = new Job[size]; 
		
		this.head = 0;
		this.tail = 0;
		this.count = 0;
	
	}

	

	public synchronized void enQueue(Job job) /*throws InterruptedException*/ // in
	{
		//key.acquire(); // Å° È¹µæ ¼º°ø
	////while (count >= jobQue.length) {
	
//			key.release(); // Å°¹Ý³³
//			lock.acquire(); // deQueueµÉ¶§±îÁö wait
//			key.acquire();// Å° È¹µæ ¼º°ø
	////		try {
	////			wait();
		////	} catch (InterruptedException e) {
		////		System.err.println("enQueue in-1 : " + e.getCause());
		////		e.printStackTrace();
		////	}
	//	}
	////	jobQue[tail] = job;
	////	tail = (tail + 1) % jobQue.length;
	////	count++;
		jobQue.add(job);
		notifyAll();   ///
//		lock.release();
//		key.release(); // Å° ¹Ý³³
	}

	public synchronized Job deQueue() /*throws InterruptedException*/ // out
	{
		//key.acquire(); // Å° È¹µæ ¼º°ø
////		while (count <= 0) {
		while(jobQue.size() <=0){
//			key.release(); // Å°¹Ý³³
//			lock.acquire(); // enQueueµÉ¶§±îÁö wait
//			key.acquire();// Å° È¹µæ ¼º°ø
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("putJob in-1 : " + e.getCause());
				e.printStackTrace();
			}
		}
		
////		Job job = jobQue[head];
////		head = (head + 1) % jobQue.length;
////		count--;
		Job job = jobQue.remove();
		notifyAll();
//		lock.release();
//		key.release();
		return job;
	}
}
