package message.msgbroker;

public class WorkerThread extends Thread 
{
    private final Queue que;

    public WorkerThread (Queue que)   //������
    {
        this.que = que;
    }
    
    public void run ()
    {
        while ( true ) 
        {
            Job job = que.takeJob();
            job.execute();
        }
    }
}
