package message.broker;

import java.io.IOException;

import main.java.lempel.blueprint.base.concurrent.JobQueue;
import main.java.lempel.blueprint.base.concurrent.Worker;

public class JobWorker extends Worker<Job> {
	public JobWorker(final JobQueue<Job> jobQueue) {
		super(jobQueue);
	}

	protected void process(final Job job) {
		try{
			job.execute();
		}catch (IOException e) {
			System.err.println("job.execute() : " + e.getCause());
			e.printStackTrace();
		}	
	}

}