package message.broker;
 
import java.util.LinkedList;
import java.util.Queue;
 
public class JobQueue { 
	private Queue<Job> Queue = new LinkedList<Job>();
	private Mutex KEY = new Mutex();
	private Mutex STOP_KEY = new Mutex();
	
	public void enQueue(Job job) throws InterruptedException {
		KEY.acquire(); // KEY ȹ�� ����
		Queue.add(job);
		STOP_KEY.release(); // STOP_KEY �ݳ�( deQueue()�� while{}�� Ż�� )
		KEY.release(); // KEY �ݳ�
	}

	public Job deQueue() throws InterruptedException {
		KEY.acquire(); // KEY ȹ�� ����
		while (Queue.size() <= 0) {
			KEY.release(); // KEY �ݳ�
			STOP_KEY.acquire(); //ù ȸ���� STOP_KEY �� ����, �� �ѹ��� ���� �ι�° ȸ������ enQueue()�� ��ٸ���.
			KEY.acquire();// KEY ȹ�� ����
		}
		Job job = Queue.remove();
		STOP_KEY.release();// STOP_KEY �ݳ�
		KEY.release();// KEY �ݳ�
		return job;
	}
}
