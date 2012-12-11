package MessageBroker_Server;

import java.util.LinkedList;
import java.util.Queue;

public class TestQueue {
//	private final Mutex mtx = new Mutex();  //차단
//	private final Mutex lock = new Mutex();   //잠금
	private final Queue<Object> queue = new LinkedList<Object>();
	
	public void push(final T aJob) throws InterruptedException {
		mtx.acquire();
		queue.add(aJob);
		if (!lock.isInuse()) {
			synchronized (allBusyTrapLock) {
				allBusyTrap = true;
			}
		}
		lock.release();
		mtx.release();
	}

	public Object pop() throws InterruptedException {
		mtx.acquire();
		T aJob;
		while (queue.size() <= 0) {
			mtx.release();
			lock.acquire();
			mtx.acquire();
		}
		aJob = queue.remove(0);
		mtx.release();
		return aJob;
	}

}
