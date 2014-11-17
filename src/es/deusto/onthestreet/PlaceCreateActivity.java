package es.deusto.onthestreet;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class PlaceCreateActivity extends Activity {
	private File file;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// If item description provided, fill the EditText widget
		EditText edtDescription = ((EditText) findViewById(R.id.edtItemDescription));
		EditText edtName = ((EditText) findViewById(R.id.edtItemName));
		EditText edtLocation = ((EditText) findViewById(R.id.edtItemLocation));
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
			String name = ((EditText) findViewById(R.id.edtItemDescription))
					.getText().toString();
			String location = ((EditText) findViewById(R.id.edtItemDescription))
					.getText().toString();

			Place place = new Place(name, description, location,
					new ArrayList<Contact>());
			if(file !=null){
				place.setUri(getRealPathFromURI(Uri.fromFile(file)));;
			}
			Intent intentResult = new Intent();
			intentResult.putExtra(Place.PLACE, place);
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

}
