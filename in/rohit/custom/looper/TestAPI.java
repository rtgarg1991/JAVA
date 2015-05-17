package in.rohit.custom.looper;

/**
 * @author Rohit
 * @version 1.0
 * Sample class using Sample Activity, Sample Handler, Sample Looper and Sample Message API
 */

class MyActivity extends CustomActivity {

	@Override
	protected void onCreate() {
		// just call super function
		super.onCreate();
	}

	@Override
	protected void onResume() {
		// just call super function
		super.onResume();
	}

	@Override
	protected void onPause() {
		// just call super function
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// just call super function
		super.onDestroy();
	}
	
}
public class TestAPI {

	// create a runnable
	// this will run in its own thread and will invoke Activity functions with delay
	static class ActivitySampleRunnable implements Runnable {

		CustomActivity activity;
		public ActivitySampleRunnable(MyActivity activity) {
			this.activity = activity;
		}

		@Override
		public void run() {
			System.out.println("Sample Runnable running in " + Thread.currentThread().getName());
			try {
				System.out.println("Calling onCreate from Sample Runnable" + Thread.currentThread().getName());
				activity.onCreate(0);

				System.out.println("Waiting and then calling onResume from Sample Runnable " + Thread.currentThread().getName());
				Thread.sleep(4000);
				activity.onResume(0);

				System.out.println("Waiting and then calling onPause from Sample Runnable" + Thread.currentThread().getName());
				Thread.sleep(6000);
				activity.onPause(0);

				System.out.println("Waiting and then calling onDestroy from Sample Runnable" + Thread.currentThread().getName());
				Thread.sleep(2000);
				activity.onDestroy(0);
			} catch (CloneNotSupportedException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	// create a runnable
	// this will run in activity's main thread
	static class ActivityRunnable implements Runnable {

		CustomActivity activity;
		public ActivityRunnable(MyActivity activity) {
			this.activity = activity;
		}

		@Override
		public void run() {
			System.out.println("Activity Runnable running in " + Thread.currentThread().getName());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws CloneNotSupportedException, InterruptedException {
		System.out.println("MAIN running in " + Thread.currentThread().getName());
		MyActivity activity = new MyActivity();

		// lets start temporary runnable which will invoke activity functions
		Thread thread = new Thread(new ActivitySampleRunnable(activity));
		thread.start();

		// wait for 5 seconds
		Thread.sleep(5000);

		// lets try to start one runnable in activity's own thread and looper
		System.out.println("Starting runnable from main thread, but will execute in activity's thread " + Thread.currentThread().getName());
		activity.runOnMainThread(new ActivityRunnable(activity));

		// wait for 8 seconds and then finish main thread
		Thread.sleep(8000);
	}
	
}
