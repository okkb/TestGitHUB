package message.broker;

public class Queue {
	private final Mutex key = new Mutex();
	private final Mutex lock = new Mutex();
	private static final int MAX_JOB = 2000;
	private final Job[] jobQue;   // �迭�� ť ���� 

	private int tail;
	private int head;
	private int count;

	public Queue(int threads) // Queue������
	{
		this.jobQue = new Job[MAX_JOB]; // ť ���� 100�� 
		this.head = 0;
		this.tail = 0;
		this.count = 0;
	
	}

	

	public synchronized void enQueue(Job job) /*throws InterruptedException*/ // in
	{
		//key.acquire(); // Ű ȹ�� ����
		while (count >= jobQue.length) {
//			key.release(); // Ű�ݳ�
//			lock.acquire(); // deQueue�ɶ����� wait
//			key.acquire();// Ű ȹ�� ����
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("enQueue in-1 : " + e.getCause());
				e.printStackTrace();
			}
		}
		jobQue[tail] = job;
		tail = (tail + 1) % jobQue.length;
		count++;
		notifyAll();   ///
//		lock.release();
//		key.release(); // Ű �ݳ�
	}

	public synchronized Job deQueue() /*throws InterruptedException*/ // out
	{
		//key.acquire(); // Ű ȹ�� ����
		while (count <= 0) {
//			key.release(); // Ű�ݳ�
//			lock.acquire(); // enQueue�ɶ����� wait
//			key.acquire();// Ű ȹ�� ����
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("putJob in-1 : " + e.getCause());
				e.printStackTrace();
			}
		}
		
		Job job = jobQue[head];
		head = (head + 1) % jobQue.length;
		count--;
		notifyAll();
//		lock.release();
//		key.release();
		return job;
	}
}
