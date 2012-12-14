package message.broker;

public class Mutex { // Mutual Exclusion
	private boolean inuse = false;
 
	public void acquire() throws InterruptedException {
		synchronized (this) {
			try {
				while (inuse) {
					wait();
				}
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
