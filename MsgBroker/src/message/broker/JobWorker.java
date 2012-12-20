package message.broker;

import main.java.lempel.blueprint.base.concurrent.JobQueue;
import main.java.lempel.blueprint.base.concurrent.Worker;

public class JobWorker extends Worker<Job> {
	public JobWorker(final JobQueue<Job> jobQueue) {
		super(jobQueue);
	}

	protected void process(final Job job) {
		job.execute();
	}

}