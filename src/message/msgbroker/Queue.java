package message.msgbroker;

public class Queue { 
	private static final int MAX_JOB = 2000;
	private final Job[] jobQue;
	private final WorkerThread[] threadPool;
	private int tail;
	private int head;
	private int count;


	public Queue(int threads)   // Queue������
	{
		this.jobQue = new Job[MAX_JOB];  // ť ���� 100��  ��û(�����Ҳ�)100�� ���� 
		this.head = 0;
		this.tail = 0;
		this.count = 0;
		threadPool = new WorkerThread[threads];  // �Է��� ������ŭ ������ ����
		
		for (int i = 0; i < threadPool.length; i++) 
		{
			threadPool[i] = new WorkerThread(this);
		}
	}

	public void startWorkers()    
	{
		for (int i = 0; i < threadPool.length; i++) {
			threadPool[i].start();
		}
	}

	public synchronized void putJob(Job job) 
	{
		///if(count >= jobQue.length)  Mutex�� ����			
		while (count >= jobQue.length) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("putJob in-1 : " + e.getCause());
				e.printStackTrace();
			}
		}
		jobQue[tail] = job;
		tail = (tail + 1) % jobQue.length;
		count++;
		notifyAll();
	}

	public synchronized Job takeJob() 
	{
		while (count <= 0) 
		{
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("takeJob in-1 : " + e.getCause());
				e.printStackTrace();
			}
		}
		Job job = jobQue[head];
		head = (head + 1) % jobQue.length;
		count--;
		notifyAll();
		return job;
	}
}
