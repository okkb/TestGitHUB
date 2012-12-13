package message.broker;

import java.util.LinkedList;
import java.util.Queue;

public class JobQueue {
	private final Mutex key = new Mutex();
	private Queue<Job> jobQue = new LinkedList<Job>();

	public void enQueue(Job job) throws InterruptedException {
		key.acquire(); // keyȹ�� ����
		jobQue.add(job);
		key.release(); // key �ݳ�
	}

	public Job deQueue() throws InterruptedException {
		key.acquire(); // key ȹ�� ����
		while (jobQue.size() <= 0) { // jobQue.size()�� 0�ʰ� �ɶ����� �ݺ�
			key.release(); // key �ݳ�
			// �̶� enQueue���� key ȹ���� jobQue.size()�� 0 �ʰ� �Ǹ� Ż��
			key.acquire();// enQueue���� Ű �ݳ��ϸ� key ȹ�� ����
		}
		Job job = jobQue.remove();
		key.release();// key �ݳ�
		return job;
	}
}
