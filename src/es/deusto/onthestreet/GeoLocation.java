package es.deusto.onthestreet;

import java.io.IOException;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class GeoLocation extends AsyncTask<String, Void, String[]> implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{

	private LocationClient mLocationClient;
	private Context context;
	private LocationCallback cb;
	public GeoLocation(LocationCallback cb,Context context) {
		this.context=context;
		this.cb=cb;
	}
	public GeoLocation(Context context) {
		this.context=context;
	}
	@Override
	protected String[] doInBackground(String... params) {
		if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) != ConnectionResult.SUCCESS){
			return null;
		}
		
        mLocationClient = new LocationClient(context, this, this);
        mLocationClient.connect(); // Emulators with no Google Play support will fail at this point
        System.out.println("1");
        // Wait until connection
        while(!mLocationClient.isConnected());
        System.out.println("1.5");
		Location loc = mLocationClient.getLastLocation();
		if(cb!=null){
			double [] coordinates={loc.getLatitude(),loc.getLongitude()};
			String address=this.getLocationAddress(coordinates);
			String []data={coordinates[0]+"",coordinates[1]+"",address};
			System.out.println("2");
			return data;
		}else{
			double [] coordinates={loc.getLatitude(),loc.getLongitude()};
			String []data={coordinates[0]+"",coordinates[1]+""};
			System.out.println("2");
			return data;
		}

		
        
	}
	private String getLocationAddress(double[] coords){ 
		 String result = ""; 
		 try { 
		 Geocoder geo = new Geocoder(context); 
		 Address address = geo.getFromLocation(coords[0], coords[1], 1).get(0); 
		 for(int i=0;i<address.getMaxAddressLineIndex();i++) 
		 result += address.getAddressLine(i) + " "; 
		 } catch (IOException e) { 
		 e.printStackTrace(); 
		 } 
		 return result; 
		} 

	@Override
	protected void onPostExecute(String [] data) {
		if(data == null)
			Toast.makeText(context, R.string.msg_error_server, Toast.LENGTH_SHORT).show();
		else{
			if(cb !=null){
				double latitude=Double.parseDouble(data[0]);
				double longitude=Double.parseDouble(data[1]);		
				Location l=new Location("");
				l.setLatitude(latitude);
				l.setLongitude(longitude);
				cb.getCurrentLocation(l);
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.i("Location client", "Connection failed");		
	}

	@Override
	public void onConnected(Bundle arg0) {
		Log.i("Location client", "Connected");
		// Normally, we will perform the actions here, but from this place we cannot access UI elements
	}

	@Override
	public void onDisconnected() {
		Log.i("Location client", "Disconnected");		
	}

	
}