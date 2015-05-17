/**
 * 
 */
package in.rohit.custom.looper;


/**
 * @author Rohit
 * @version 1.0
 * Contact Email rt.garg1991@gmail.com
 * Custom Activity class which Client will extend and invoke its functions from one thread
 * and will wait for the callbacks in Activity's own thread
 */
public class CustomActivity extends CustomHandler {

	private static final int MESSAGE_ACTIVITY_ONCREATE = 1;
	private static final int MESSAGE_ACTIVITY_ONRESUME = 2;
	private static final int MESSAGE_ACTIVITY_ONPAUSE = 3;
	private static final int MESSAGE_ACTIVITY_ONDESTROY = 4;

	CustomActivity() {
		super(null);
	}

	CustomActivity(CustomLooper looper) {
		super(looper);
	}

	// handle message will be executed in Activity's own thread using looper
	@Override
	protected void handleMessage(CustomMessage message) {
		switch(message.what) {
		case MESSAGE_ACTIVITY_ONCREATE:
			System.out.println("onCreate Message received in " + Thread.currentThread().getName());
			onCreate();
			break;
		case MESSAGE_ACTIVITY_ONRESUME:
			System.out.println("onResume Message received in " + Thread.currentThread().getName());
			onResume();
			break;
		case MESSAGE_ACTIVITY_ONPAUSE:
			System.out.println("onPause Message received in " + Thread.currentThread().getName());
			onPause();
			break;
		case MESSAGE_ACTIVITY_ONDESTROY:
			System.out.println("onDestroy Message received in " + Thread.currentThread().getName());
			onDestroy();
			break;
		}
	}

	// START - Non Parameterized Callbacks which will be executed in Activity's own thread using looper and handler
	protected void onCreate() {
		System.out.println("onCreate called from " + Thread.currentThread().getName());
	}

	protected void onResume() {
		System.out.println("onResume called from " + Thread.currentThread().getName());
	}

	protected void onPause() {
		System.out.println("onPause called from " + Thread.currentThread().getName());
	}

	protected void onDestroy() {
		System.out.println("onDestroy called from " + Thread.currentThread().getName());
		getLooper().die();
	}
	// END

	// START - Parameterized functions which will be called from some other thread
	// and actual implementation will be done in Activity's own thread using Looper

	public void onCreate(int data) throws CloneNotSupportedException {
		CustomMessage message = new CustomMessage(this, null, MESSAGE_ACTIVITY_ONCREATE);
		sendMessage(message);
	}

	public void onResume(int data) throws CloneNotSupportedException {
		CustomMessage message = new CustomMessage(this, null, MESSAGE_ACTIVITY_ONRESUME);
		sendMessage(message);
	}

	public void onPause(int data) throws CloneNotSupportedException {
		CustomMessage message = new CustomMessage(this, null, MESSAGE_ACTIVITY_ONPAUSE);
		sendMessage(message);
	}

	public void onDestroy(int data) throws CloneNotSupportedException {
		CustomMessage message = new CustomMessage(this, null, MESSAGE_ACTIVITY_ONDESTROY);
		sendMessage(message);
	}

	public void runOnMainThread(Runnable runnable) throws CloneNotSupportedException {
		CustomMessage message = new CustomMessage(null, runnable, 0);
		sendMessage(message);
	}
	// END
}
