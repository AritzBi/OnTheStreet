package es.deusto.onthestreet;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BootReceiver","intent received");

		boolean notifications=PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_nofitication", false);
		if(notifications){
			 Intent myIntent = new Intent(context, NearPlaceService.class);
		        PendingIntent pendingIntent = PendingIntent.getService(context,  0, myIntent, 0);
		        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTimeInMillis(System.currentTimeMillis());
		        calendar.add(Calendar.SECOND, 60); 
		        long frequency= 60 * 1000; 
		        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent);      
		}
    }
}
