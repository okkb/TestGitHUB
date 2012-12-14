package message.broker;

import java.util.LinkedList;
import java.util.Queue;

public class JobQueue {
	private Queue<Job> jobQueue = new LinkedList<Job>();
	private final Mutex KEY = new Mutex();
	private final Mutex STOP_KEY = new Mutex();
	
	public void enQueue(Job job) throws InterruptedException {
		KEY.acquire(); // key ȹ�� ����
		jobQueue.add(job);
		STOP_KEY.release(); // stopkey �ݳ�( deQueue()�� while{}�� Ż�� )
		KEY.release(); // key �ݳ�
	}

	public Job deQueue() throws InterruptedException {
		KEY.acquire(); // key ȹ�� ����
		while (jobQueue.size() <= 0) {
			KEY.release(); // key �ݳ�
			STOP_KEY.acquire(); //ù ȸ���� stopŰ �� ����, �� �ѹ��� ���� �ι�° ȸ������ enQueue()�� ��ٸ���.
			KEY.acquire();// key ȹ�� ����
		}
		Job job = jobQueue.remove();
		STOP_KEY.release();// stopŰ �ݳ�
		KEY.release();// key �ݳ�
		return job;
	}
}
