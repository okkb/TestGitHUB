package message.broker;

public class Queue {
	private final Mutex key = new Mutex();
	private final Mutex lock = new Mutex();
	private static final int MAX_JOB = 2000;
	private final Job[] jobQue;   // 배열로 큐 구현 
	private final WorkerThread[] workerThread;
	private int tail;
	private int head;
	private int count;

	public Queue(int threads) // Queue생성자
	{
		this.jobQue = new Job[MAX_JOB]; // 큐 공간 100개 
		this.head = 0;
		this.tail = 0;
		this.count = 0;
		this.workerThread = new WorkerThread[threads]; // 입력한 갯수만큼 쓰레드 생성

		for (int i = 0; i < workerThread.length; i++) {
			workerThread[i] = new WorkerThread(this);
		}
	}

	public void startWorkers() {
		for (int i = 0; i < workerThread.length; i++) {
			workerThread[i].start();
		}
	}

	public synchronized void enQueue(Job job) /*throws InterruptedException*/ // in
	{
		//key.acquire(); // 키 획득 성공
		while (count >= jobQue.length) {
//			key.release(); // 키반납
//			lock.acquire(); // deQueue될때까지 wait
//			key.acquire();// 키 획득 성공
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
//		key.release(); // 키 반납
	}

	public synchronized Job deQueue() /*throws InterruptedException*/ // out
	{
		//key.acquire(); // 키 획득 성공
		while (count <= 0) {
//			key.release(); // 키반납
//			lock.acquire(); // enQueue될때까지 wait
//			key.acquire();// 키 획득 성공
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
