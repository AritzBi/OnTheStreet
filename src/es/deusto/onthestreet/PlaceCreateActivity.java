package es.deusto.onthestreet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class PlaceCreateActivity extends Activity {
	private File file;
	private Place place;
	private Integer editPosition;
	private ProgressBar barConnectivity;
	private EditText txtLat;
	private EditText txtLon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtLat=(EditText) findViewById(R.id.txtLat);
		txtLon=(EditText) findViewById(R.id.txtLon);
		//barConnectivity = (ProgressBar) findViewById(R.id.barConnection);
		//barConnectivity.setMax(100);
		//barConnectivity.setVisibility(View.GONE);
		// If item description provided, fill the EditText widget

		place= (Place) getIntent().getSerializableExtra(Place.PLACE);
		if(place!=null){
			EditText edtDescription = ((EditText) findViewById(R.id.edtItemDescription));
			EditText edtName = ((EditText) findViewById(R.id.edtItemName));
			edtDescription.setText(place.getDescription());
			edtName.setText(place.getName());
			txtLat.setText(place.getLat()+"");
			txtLon.setText(place.getLon()+"");
		}else{
			place=new Place();
		}
		editPosition=(Integer) getIntent().getSerializableExtra(Place.PLACE_POSITION);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// If changes OK, return new value in the result Intent back to the
		// calling activity
		case R.id.action_item_accept:
			String description = ((EditText) findViewById(R.id.edtItemDescription))
					.getText().toString();
			String name = ((EditText) findViewById(R.id.edtItemName))
					.getText().toString();

			place.setDescription(description);
			place.setLat(Double.parseDouble(txtLat.getText().toString()));
			place.setLon(Double.parseDouble(txtLon.getText().toString()));
			place.setName(name);
			if(file !=null){
				place.setUri(getRealPathFromURI(Uri.fromFile(file)));;
			}
			Intent intentResult = new Intent();
			intentResult.putExtra(Place.PLACE, place);
			if(editPosition!=null)
				intentResult.putExtra(Place.PLACE_POSITION, editPosition);
			setResult(Activity.RESULT_OK, intentResult);
			finish();
			break;

		// If cancel, simply return back
		case R.id.action_item_cancel:
			setResult(Activity.RESULT_CANCELED);
			finish();
			break;
		case R.id.action_new_picture:
			intentTakePicture();
			break;
		case R.id.action_get_location:
			this.getLocation();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	private String getRealPathFromURI(Uri contentURI) {
	    String result;
	    Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
	    if (cursor == null) { // Source is Dropbox or other similar local file path
	        result = contentURI.getPath();
	    } else { 
	        cursor.moveToFirst(); 
	        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	        result = cursor.getString(idx);
	        cursor.close();
	    }
	    return result;
	}
	private void intentTakePicture() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				new Date().getTime()+
				".jpg");
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(i, 0);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == Activity.RESULT_OK) {
				System.out.println(file);
			}
			if (resultCode == Activity.RESULT_CANCELED) {
				// Do nothing
			}
		}
	}
	private void getLocation(){
		// First check if there is connectivity
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

	    if (networkInfo != null && networkInfo.isConnected()) {
			// OK -> Access the Internet
	    	txtLat.setVisibility(View.GONE);
	    	txtLon.setVisibility(View.GONE);
	    	//barConnectivity.setProgress(30);
	    	//barConnectivity.setVisibility(View.VISIBLE);
	    	new GetLocation().execute();
	    } else {
			// No -> Display error message
	        Toast.makeText(this, R.string.msg_error_no_connection, Toast.LENGTH_SHORT).show();
	    }		
	}
	// Convenience class to access the Internet and update UI elements
	private class GetLocation extends AsyncTask<String, Void, double[]> implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener{
	
		private LocationClient mLocationClient;
		
		@Override
		protected double[] doInBackground(String... params) {
			if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) != ConnectionResult.SUCCESS){
				return null;
			}
			
	        mLocationClient = new LocationClient(getApplicationContext(), this, this);
	        mLocationClient.connect(); // Emulators with no Google Play support will fail at this point
	        
	        // Wait until connection
	        while(!mLocationClient.isConnected());
	        
			Location loc = mLocationClient.getLastLocation();
			
			double [] coordinates={loc.getLatitude(),loc.getLongitude()};
	        return coordinates;
		}
	
		@Override
		protected void onPostExecute(double [] coordinates) {
			txtLat.setVisibility(View.VISIBLE);
			txtLon.setVisibility(View.VISIBLE);
			if(coordinates == null)
				Toast.makeText(getApplicationContext(), R.string.msg_error_server, Toast.LENGTH_SHORT).show();
			else{
				txtLat.setText(coordinates[0]+"");
				txtLon.setText(coordinates[1]+"");
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
}
