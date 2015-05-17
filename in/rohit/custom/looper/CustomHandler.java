/**
 * 
 */
package in.rohit.custom.looper;


/**
 * @author Rohit
 * @version 1.0
 * Contact Email rt.garg1991@gmail.com
 * Custom Handler class for sending the messages
 */

public abstract class CustomHandler {

	// its own looper
	CustomLooper looper;

	CustomHandler(CustomLooper looper) {
		this.looper = looper;
		// if looper is not provided, then create new looper
		if(looper == null) {
			this.looper = new CustomLooper();
		}
	}

	// request associated looper
	public final CustomLooper getLooper() {
		return looper;
	}

	// send message to looper queue for processing
	public final void sendMessage(CustomMessage message) throws CloneNotSupportedException {
		getLooper().addMessage(message);
	}

	// abstract handle message, should be handled by client
	protected abstract void handleMessage(CustomMessage message);
}
