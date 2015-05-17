package com.rohit.custom.threadpool;

/**
 * @author Rohit Garg
 * Contact : rt.garg1991@gmail.com
 */

import java.util.ArrayList;


public class CustomThreadPool {

	int totalThreads;
	CustomThread[] threads;
	ArrayList<Runnable> runnables;

	// default constructor
	public CustomThreadPool() {
		int maxProcessors = Runtime.getRuntime().availableProcessors();
		this.totalThreads = maxProcessors;
		threads = new CustomThread[this.totalThreads];
	}

	// Parameterized constructor
	public CustomThreadPool(int noOfThreads) {
		this.totalThreads = noOfThreads;
		threads = new CustomThread[this.totalThreads];
	}

	public void submit(Runnable runnable) {
		int i = 0;
		// check if we have started all the threads or not
		while(i < totalThreads) {
			// check if ith thread exists or not
			// if not, then we can create new object of ith thread
			// and start it with the runnable sent by client
			if(threads[i] == null) {
				// use synchronization to make sure this thread will be created by only one thread from client
				synchronized(this) {
					if(threads[i] == null) {
						// create new thread and start it with this new runnable and return
						threads[i] = new CustomThread(runnable);
						threads[i].start();
						return;
					}
				}
			}
			i++;
		}
		// if here, it means we have already started all the threads
		// so now we will add this runnable to waiting list
		// from where any idle thread will pick it when its turn will come
		// this waiting list works on first come first serve basis

		// use synchronization to make sure only one thread will do write operation on this list at a time
		synchronized (this) {
			if(this.runnables == null) {
				this.runnables = new ArrayList<Runnable>();
			}
			this.runnables.add(runnable);
		}
	}

	// we need to make sure only one thread enters this function and it can thrown RuntimeException
	public synchronized void shutdown() throws RuntimeException {
		// if we have already shutdown this Thread Pool earlier
		// we will throw an exception indicating client is trying to shutdown more than once
		if(totalThreads == -1) {
			RuntimeException ex = new RuntimeException("Thread Pool has already been shut down and it cannot be shut down again");
			throw ex;
		}

		// now we will check for all the threads and will ask them to complete current job and then finish
		int i = 0;
		while(i < totalThreads) {
			if(threads[i] != null) {
				threads[i].setRunning(false);
			}
			i++;
		}
		// make threads object available for GC to claim
		threads = null;

		// clear the waiting list and make it available for GC to claim
		runnables.clear();
		runnables = null;

		// set total threads to -1, indicating this Thread Pool has already been shut down earlier
		totalThreads = -1;
	}

	private class CustomThread extends Thread {

		private Runnable runnable;
		boolean threadPoolRunning;

		public CustomThread(Runnable runnable) {
			this.runnable = runnable;
			threadPoolRunning = true;
		}

		public void setRunning(boolean running) {
			this.threadPoolRunning = running;
		}

		@Override
		public void run() {
			// if thread pool is running or we have job to do, then keep this function running
			while(threadPoolRunning || this.runnable != null) {
				// check if we have job to work on
				if(this.runnable != null) {
					// if there is job, then start the job
					this.runnable.run();
					// after finish, make this job available for GC to claim its memory
					// and our thread will start new job if there is any 
					// otherwise it will go on waiting indefinitely unless Thread Pool is shut down by client
					this.runnable = null;
				} else if(runnables != null && runnables.size() > 0) {
					// if here, it means there are some jobs waiting to be executed
					// use synchronization to pick one job from the waiting list
					// and in next cycle of this while loop, this job will be processed
					synchronized (CustomThreadPool.this) {
						if(runnables != null && runnables.size() > 0) {
							this.runnable = runnables.remove(0);
						}
					}
				}
			}
		}
	}
}
