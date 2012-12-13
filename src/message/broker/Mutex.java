package message.broker;

public class Mutex { // Mutual Exclusion
	private transient boolean inuse = false;

	public void acquire() throws InterruptedException {
		if (Thread.interrupted())
			throw new InterruptedException();
		synchronized (this) {
			try {
				while (inuse)
					wait();
				inuse = true;
			} catch (InterruptedException ex) {
				notify();
				throw ex;
			}
		}
	}

	public void release() {
		synchronized (this) {
			inuse = false;
			notify();
		}
	}
}
