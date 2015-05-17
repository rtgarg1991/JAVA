package in.rohit.custom.looper;

/**
 * @author Rohit
 * @version 1.0
 * Contact Email rt.garg1991@gmail.com
 * Custom Message class
 */
class CustomMessage implements Cloneable {
	CustomHandler target;
	Runnable runnable;
	boolean userDefined = false;
	int what = -1;

	// this constructor will be available to api package only
	// will create defaul message object which will reside in queue forever
	// until client requests looper to die
	CustomMessage() {
	}

	// Custom Message with target and runnable, one can be null, both should not be
	// should be used for processing runnable only
	public CustomMessage(CustomHandler target, Runnable runnable) {
		this(target, runnable, 0);
	}

	// Custom Message with target, runnable and what
	// should be used mainly for Custom Handler processing
	public CustomMessage(CustomHandler target, Runnable runnable, int what) {
		this(target, runnable, what, true);
	}

	// private custom message constructor for cloning the message
	// and privately initializing all the member variables
	private CustomMessage(CustomHandler target, Runnable runnable, int what, boolean userDefined) {
		this.target = target;
		this.runnable = runnable;
		this.userDefined = userDefined;
		this.what = what;
	}

	// clone function, used while adding message to looper queue
	// both of target and runnable should not be null
	// if target is there and runnable is not there, then what should not be -1
	@Override
	protected synchronized Object clone() throws CloneNotSupportedException {
		if(this.target == null && this.runnable == null) {
			throw new CloneNotSupportedException("There is no target and runnable present");
		}
		if(this.what == -1 && (this.target != null && this.runnable == null)) {
			throw new CloneNotSupportedException("Please define some meaningful message");
		}
		return new CustomMessage(this.target, this.runnable, this.what, true);
	}
}
