package es.deusto.onthestreet;

import java.util.ArrayList;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NearPlaceService extends Service implements LocationCallback{
	private Place lastPlace;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("Service", "Start");
		// Display the notification in the notification area
		System.out.println("empieza");
        GeoLocation geo=new GeoLocation(NearPlaceService.this,getApplicationContext());
        geo.execute();
		//showNotification(getApplicationContext(), "Activated");	
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
	private void showNotification(Context context, String message, double minDinstace){
		// First, create the notification
		NotificationCompat.Builder nBuilder =
				new NotificationCompat.Builder(context)
				.setContentTitle(message)
				.setSmallIcon(R.drawable.ic_action_place)
				.setContentText("Distance: "+minDinstace);
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
	@Override
	public void getCurrentLocation(Location location) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String mynumberPref = sharedPref.getString("pref_key_place_limit", ""); 
		ArrayList<Place>allPlaces=(new PlaceManager(getApplicationContext())).loadPLaces();
		Place selected=null;
		double distance=0;
		double minDistance=Integer.MAX_VALUE;
		for(Place p: allPlaces){		
			Location l=new Location("");
			l.setLatitude(p.getLat());
			l.setLongitude(p.getLon());
			distance=location.distanceTo(l);
			if((distance<=Integer.parseInt(mynumberPref)&& distance<minDistance)){
				selected=p;
				minDistance=distance;
			}
		}
		if(selected !=null){
			if(lastPlace == null){
				lastPlace=selected;
				this.showNotification(getApplicationContext(), selected.getName(),minDistance);
			}else{
				System.out.println("Paso por el segundo");
				if(!(lastPlace.equals(selected))){
					lastPlace=selected;
					this.showNotification(getApplicationContext(), selected.getName(),minDistance);
				}
			}
		}
	}

}
