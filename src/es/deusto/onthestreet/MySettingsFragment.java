package es.deusto.onthestreet;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class MySettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	private boolean notifications;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        // Register for changes in preferences
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
        
		
        
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
		// In the case of change in pref_username, replace the default summary with the new value
		if(key.equals("pref_username"))
				this.findPreference(key).setSummary(sharedPreferences.getString(key, ""));
		else if(key.equals("pref_key_place_limit")){
			String meters=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(key, "");
			this.findPreference(key).setSummary("Number of meters: "+meters);
		}
		else if(key.equals("pref_notifacation")){
			notifications=PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("pref_notifacation", false);
			if(notifications){
				 Intent myIntent = new Intent(getActivity(), NearPlaceService.class);
			        PendingIntent pendingIntent = PendingIntent.getService(getActivity(),  0, myIntent, 0);
			        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
			        Calendar calendar = Calendar.getInstance();
			        calendar.setTimeInMillis(System.currentTimeMillis());
			        calendar.add(Calendar.SECOND, 10); // first time
			        long frequency= 10 * 1000; // in ms 
			        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent);      
			}else{
				 Intent myIntent = new Intent(getActivity(), NearPlaceService.class);
				  PendingIntent pendingIntent = PendingIntent.getService(getActivity(),  0, myIntent, 0);
			      AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
			      alarmManager.cancel(pendingIntent);
			}
		}
			
		
	}	
	
}
