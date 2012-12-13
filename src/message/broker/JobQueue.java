package message.broker;

import java.util.LinkedList;
import java.util.Queue;

public class JobQueue {
	private Queue<Job> jobQue = new LinkedList<Job>();
	private final Mutex key = new Mutex();
	private final Mutex stopkey = new Mutex();
	
	public void enQueue(Job job) throws InterruptedException {
		key.acquire(); // key ȹ�� ����
		jobQue.add(job);
		stopkey.release(); // stopŰ �ݳ�( deQueue()�� while{}�� Ż�� )
		key.release(); // key �ݳ�
	}

	public Job deQueue() throws InterruptedException {
		key.acquire(); // key ȹ�� ����
		while (jobQue.size() <= 0) {
			key.release(); // key �ݳ�
			stopkey.acquire(); //ù ȸ���� stopŰ �� ����, �� �ѹ��� ���� �ι�° ȸ������ enQueue()�� ��ٸ���.
			key.acquire();// key ȹ�� ����
		}
		Job job = jobQue.remove();
		stopkey.release();// stopŰ �ݳ�
		key.release();// key �ݳ�
		return job;
	}
}
