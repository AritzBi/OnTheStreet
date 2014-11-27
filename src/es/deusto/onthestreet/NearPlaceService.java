package es.deusto.onthestreet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NearPlaceService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("Service", "Start");
		// Display the notification in the notification area
		System.out.println("empieza");
		showNotification(getApplicationContext(), "Activated");	
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.i("Service", "Stop");
		// Remove the notification from the notification area
		removeNotification(getApplicationContext());
		super.onDestroy();
	}
	
	/**
	 * Displays a notification with id 0 in the notification area
	 * @param context the current context
	 * @param message the small text to display
	 */
	private void showNotification(Context context, String message){
		// First, create the notification
		NotificationCompat.Builder nBuilder =
				new NotificationCompat.Builder(context)
				.setContentTitle("OnTheStreet")
				.setContentText(message);
		Notification noti = nBuilder.build();

		// Second, display the notification
		NotificationManager mNotificationManager =
			    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, noti);		
	}
	
	/**
	 * Removes the notification with id 0 from the notification area
	 * @param context the current context
	 */
	private void removeNotification(Context context){
		NotificationManager mNotificationManager =
			    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(0);		
	}

}
