/**
 * 
 */
package in.rohit.custom.looper;

import java.util.ArrayList;

/**
 * @author Rohit
 * @version 1.0
 * Contact Email rt.garg1991@gmail.com
 * Custom Looper class
 */

public class CustomLooper {
	// message queue containing messages to be executed
	private ArrayList<CustomMessage> messageQueue;

	// default constructor, creating message queue and adding default message
	public CustomLooper() {
		messageQueue = new ArrayList<CustomMessage>();
		messageQueue.add(new CustomMessage());
		// start the thread for this looper
		prepare();
	}

	private void prepare() {
		// create one thread for this looper and start it
		CustomThread thread = new CustomThread();
		thread.start();
	}

	// synchronized function to add message to Looper's queue
	synchronized void addMessage(CustomMessage message) throws CloneNotSupportedException {
		// if message queue is null, then this looper is invalid and should not be used
		if(messageQueue == null) {
			throw new RuntimeException("Looper Invalid");
		}

		// if message queue size is 0, it means this already has been requested to die
		if(messageQueue.size() == 0) {
			throw new RuntimeException("Looper Dead");
		}

		// add message clone to message queue for processing at the end of queue
		// make sure default message in the message queue is the last message
		// so we will add it to n - 1 location
		messageQueue.add(messageQueue.size() - 1, (CustomMessage)message.clone());
	}

	//  synchronized die function, which will clear the message queue
	// and hence loop function will stop working and custom thread will also finish
	synchronized void die() {
		messageQueue.clear();
		messageQueue = null;
	}

	// loop function which will go on executing the tasks (messages) present in the queue
	private final void loop() {
		// if message queue has some message to process, then keep running
		while(messageQueue != null && messageQueue.size() > 0) {
			CustomMessage message;
			// synchronized block to get message from the message queue
			synchronized (this) {
				if(messageQueue != null && messageQueue.size() > 0)  {
					// if top message is user defined, then remove it from queue and process it
					if (messageQueue.get(0).userDefined) {
						message = messageQueue.remove(0);
					} else {
						// otherwise no need to process
						message = messageQueue.get(0);
					}
				} else {
					// if here, then continue
					continue;
				}
			}
			// if message is user defined, then process it
			if(message.userDefined) {
				// message has runnable, so run it
				if(message.runnable != null) {
					message.runnable.run();
				}
				// message has target, so send this message to the target
				if(message.target != null) {
					message.target.handleMessage(message);
				}
			} else {
				// no user defined message in queue
				// continue checking for messages in the queue
				continue;
			}
		}
	}

	// Custom thread for our looper
	private class CustomThread extends Thread {

		@Override
		public void run() {
			// call loop from here
			// as long as loop is running, this thread is running
			CustomLooper.this.loop();
		}
		
	}
}
