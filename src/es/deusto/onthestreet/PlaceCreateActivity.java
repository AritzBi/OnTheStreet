package es.deusto.onthestreet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class PlaceCreateActivity extends Activity{
	public static final String PLACE = "place";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// If item description provided, fill the EditText widget 
		EditText edtDescription = ((EditText) findViewById(R.id.edtItemDescription));
		EditText edtName = ((EditText) findViewById(R.id.edtItemName));
		EditText edtLocation = ((EditText) findViewById(R.id.edtItemLocation));
		//String value = getIntent().getStringExtra(ITEM_DESCRIPTION);
		/*Place place=(Place) getIntent().getExtras().getSerializable("Pito");
		if(place != null){
			edtDescription.setText(place.getDescription());
			edtName.setText(place.getDescription());
			edtLocation.setText(place.getDescription());
		}*/
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
		// If changes OK, return new value in the result Intent back to the calling activity  
		case R.id.action_item_accept:
			String description = ((EditText) findViewById(R.id.edtItemDescription)).getText().toString();
			String name = ((EditText) findViewById(R.id.edtItemDescription)).getText().toString();
			String location = ((EditText) findViewById(R.id.edtItemDescription)).getText().toString();
			Place place=new Place(name, description, location,new ArrayList<Contact>());
			Intent intentResult = new Intent();
			intentResult.putExtra(PLACE, place);
			setResult(Activity.RESULT_OK, intentResult);
			finish();
			break;

		// If cancel, simply return back  
		case R.id.action_item_cancel:
			setResult(Activity.RESULT_CANCELED);
			finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
