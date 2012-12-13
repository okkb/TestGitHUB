package message.broker;
public class Mutex {  // Mutual Exclusion
	private transient boolean inuse = false;

	public void acquire() throws InterruptedException 
	{
		if (Thread.interrupted())
			throw new InterruptedException();
		synchronized(this) 
		{
			try {
				while (inuse)
					wait();
				inuse = true;
			} catch(InterruptedException ex){
				notify();
				throw ex;
			}
		}
	}

	public void release() 
	{
		synchronized(this)
		{
		inuse = false;
		notify();
		}
	}

//	public boolean attempt(long msecs) throws InterruptedException 
//	{
//		if (Thread.interrupted())
//			throw new InterruptedException();
//		synchronized (this) 
//		{
//			if (!inuse) 
//			{				
//				inuse = true;
//				return true;
//			} else if (msecs <= 0){
//				return false;
//			}
//			else 
//			{
//				long waitTime = msecs;
//				long start = System.currentTimeMillis();
//				try 
//				{
//					for (;;) 
//					{
//						wait(waitTime);
//						if (!inuse) {
//							inuse = true;
//							return true;
//						} else {
//							waitTime = msecs - (System.currentTimeMillis() - start);
//							if (waitTime <= 0)
//								return false;
//						}
//					}
//				} catch (InterruptedException ex) {
//					notify();
//					throw ex;
//				}
//			}
//		}
//	}
	
}
