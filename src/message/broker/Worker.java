package message.broker;

public class Worker extends Thread 
{
    private final JobQueue que;

    public Worker (JobQueue que)   //������
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
