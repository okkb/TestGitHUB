package message.broker;

public class WorkerThread extends Thread 
{
    private final Queue que;

    public WorkerThread (Queue que)   //»ý¼ºÀÚ
    {
        this.que = que;
    }
    
    public void run ()
    {
        while ( true ) 
        {
//        	try{
            Job job = que.deQueue();
            job.execute();
//        	}
//        	catch(InterruptedException e){
//        		System.err.println("WorkerThread in-1 : " + e.getCause());
//				e.printStackTrace();
//        	}           
        }
    }
}
