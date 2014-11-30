package es.deusto.onthestreet;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceCreateActivity extends Activity implements LocationCallback {
	private File file;
	private Place place;
	private Integer editPosition;
	private EditText txtLat;
	private EditText txtLon;
	private TextView txtAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtLat=(EditText) findViewById(R.id.txtLat);
		txtLon=(EditText) findViewById(R.id.txtLon);
		txtAddress=(TextView)findViewById(R.id.txtAddress);

		place= (Place) getIntent().getSerializableExtra(Place.PLACE);
		if(place!=null){
			EditText edtDescription = ((EditText) findViewById(R.id.edtItemDescription));
			EditText edtName = ((EditText) findViewById(R.id.edtItemName));
			edtDescription.setText(place.getDescription());
			edtName.setText(place.getName());
			txtLat.setText(place.getLat()+"");
			txtLon.setText(place.getLon()+"");
			txtAddress.setText(place.getAddress());
		}else{
			place=new Place();
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			boolean automatic = sharedPref.getBoolean("pref_automatic_location",false); 
			if(automatic){
				GeoLocation geo=new GeoLocation(PlaceCreateActivity.this,getApplicationContext());
				geo.execute();
			}
		}
		editPosition=(Integer) getIntent().getSerializableExtra(Place.PLACE_POSITION);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_create, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// If changes OK, return new value in the result Intent back to the
		// calling activity
		case R.id.action_item_accept:
			EditText txtDescription=((EditText) findViewById(R.id.edtItemDescription));
			EditText txtName=((EditText) findViewById(R.id.edtItemName));
			String description = txtDescription
					.getText().toString();
			String name = txtName
					.getText().toString();
			if( txtLat.getText().toString().trim().equals("") ||txtLon.getText().toString().trim().equals("") || description.trim().equals("")| name.trim().equals(""))
			 {    
				Toast.makeText(getApplicationContext(), "A required field is empty", Toast.LENGTH_SHORT).show();
			 }else{

					place.setDescription(description);
					place.setLat(Double.parseDouble(txtLat.getText().toString()));
					place.setLon(Double.parseDouble(txtLon.getText().toString()));
					place.setAddress(txtAddress.getText().toString());
					place.setName(name);
					if(file !=null){
						place.setUri(getRealPathFromURI(Uri.fromFile(file)));;
					}else{
						place.setUri("unknown.jpg");
					}
					Intent intentResult = new Intent();
					intentResult.putExtra(Place.PLACE, place);
					if(editPosition!=null)
						intentResult.putExtra(Place.PLACE_POSITION, editPosition);
					setResult(Activity.RESULT_OK, intentResult);
					finish();
			 }
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
			GeoLocation geo=new GeoLocation(PlaceCreateActivity.this,getApplicationContext());
			geo.execute();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	private String getRealPathFromURI(Uri contentURI) {
	    String result;
	    Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
	    if (cursor == null) {
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

	@Override
	public void getCurrentLocation(Location location, String address) {
		txtLat.setText(location.getLatitude()+"");
		txtLon.setText(location.getLongitude()+"");
		txtAddress.setText(address);
		
	}

}
