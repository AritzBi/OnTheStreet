package es.deusto.onthestreet;






import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PlaceDetailActivity extends Activity{
	private Place place;
	private int placePosition;
	private ArrayList<Place> arrPlaces;
	private ArrayAdapter<Contact> adpContacts;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		TextView edtDescription = ((TextView) findViewById(R.id.txtDescription));
		ListView listContact=((ListView)findViewById(R.id.listContacts));
		placePosition= (Integer) getIntent().getSerializableExtra(Place.PLACE_POSITION);
		place=(Place)getIntent().getSerializableExtra(Place.PLACE);
		if(place != null){
			edtDescription.setText(place.getDescription());
			adpContacts = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1,place.getRelatedContacts());
			listContact.setAdapter(adpContacts);
			listContact.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_show, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnu_add_contact:
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
			startActivityForResult(intent, 1);
			break;
		case R.id.mnu_save_changes:
			Intent intentResult = new Intent();
			intentResult.putExtra(Place.PLACE, place);
			intentResult.putExtra(Place.PLACE_POSITION, placePosition);
			setResult(Activity.RESULT_OK, intentResult);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Cursor cursor = getContentResolver().query(data.getData(), new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
				if(cursor.moveToNext()){
					Log.i("Intent:",data.getDataString());
					place.getRelatedContacts().add(new Contact(cursor.getString(0), cursor.getString(1)));
					adpContacts.notifyDataSetChanged();
					return;
				}
			}
		}
				// Recommended
		super.onActivityResult(requestCode, resultCode, data);
	}
    @Override
    public void onStop(){
    	super.onStop();

    }
}
