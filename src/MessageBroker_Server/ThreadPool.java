package MessageBroker_Server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ThreadPool {
//	final static int THREADCOUNT = 100;
//	
//	public static class TestThread implements Runnable 
//	{
//		public void run()
//		{
//			for (int i = 1; i <= 3; i++) 
//			{
//				System.out.println(Thread.currentThread().getName() + "="+ i);
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e)
//				{
//					e.printStackTrace();
//				}
//			}
//		}
//
//	}
//	
//	public static void main(String[] args) {
//		ExecutorService exec = Executors.newFixedThreadPool(THREADCOUNT);// 쓰레드 풀 생성. THREADCOUNT 개수 만큼
//		
//		for (int i = 0; i < 101 ; i++)   // 100개만 동시에 되고   1개는 다 끝나고 한다.
//		{
//			exec.execute(new TestThread());
//		}
//		
//		exec.shutdown();// 쓰레드풀 생성하고 전부다 사용후에는 반드시 셧다운 해야함~
//	}
//	
	
public static void main(String[] args){
       Queue q =  new LinkedList();
 for(int i=0; i<100; i++){
       q.offer(i);
     
 }
      
       System.out.println("=queue");
       while(!q.isEmpty()){
              System.out.println(q.poll());
       }
}
}
