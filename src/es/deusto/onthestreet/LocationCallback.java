package es.deusto.onthestreet;

import android.location.Location;

public interface LocationCallback {
	public void getCurrentLocation(Location location,String address);
}
