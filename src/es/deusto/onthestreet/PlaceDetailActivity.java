package es.deusto.onthestreet;






import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PlaceDetailActivity extends Activity{
	private Place place;
	private Integer placePosition;
	private ActionMode mActionMode = null;
	private ArrayAdapter<Contact> adpContacts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		TextView edtName=((TextView)findViewById(R.id.txtName));
		TextView edtDescription = ((TextView) findViewById(R.id.txtDescription));
		TextView edtAddress= ((TextView) findViewById(R.id.txtAddress));
		final ListView listContact=((ListView)findViewById(R.id.listContacts));
		ImageView image=(ImageView)findViewById(R.id.image_icon);
		if(getIntent().hasExtra(Place.PLACE_POSITION))
			placePosition= (Integer) getIntent().getSerializableExtra(Place.PLACE_POSITION);
		place=(Place)getIntent().getSerializableExtra(Place.PLACE);
		if(place != null){
			edtName.setText(place.getName());
			edtDescription.setText(place.getDescription());
			edtAddress.setText(place.getAddress());
			File imgFile=new File(place.getUri());
			if(imgFile.exists()){
				image.setImageURI(Uri.fromFile(imgFile));
			}
			adpContacts = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1,place.getRelatedContacts());
			listContact.setAdapter(adpContacts);
			listContact.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			    // Called when the user long-clicks an item on the list
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View row, int position, long rowid) {
			        if (mActionMode != null) {
			            return false;
			        }

			        // Important: to marked the editing row as activated
			        listContact.setItemChecked(position, true);

			        // Start the CAB using the ActionMode.Callback defined above
			        mActionMode = PlaceDetailActivity.this.startActionMode(mActionModeCallback);
			        return true;
			    }
			});
		}
	}
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		
	    // Called when the action mode is created; startActionMode() was called
	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        // Inflate a menu resource providing context menu items
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.delete_contact, menu);
	        return true;
	    }

	    // Called when the user enters the action mode
	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	    	ListView listContact=((ListView)findViewById(R.id.listContacts));
	    	// Disable the list to avoid selecting other elements while editing one
	    	listContact.setEnabled(false);
	        return true; // Return false if nothing is done
	    }
	    
	    // Called when the user selects a contextual menu item
	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.mnu_contact_delete:
	                mode.finish(); // Action picked, so close the CAB and execute action
	                ListView listContact=((ListView)findViewById(R.id.listContacts));
	                place.getRelatedContacts().remove(listContact.getCheckedItemPosition());
	                adpContacts.notifyDataSetChanged();
	                return true;
	            default:
	                return false;
	        }
	    }

	    // Called when the user exits the action mode
	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	    	// Re-enable the list after edition
	    	ListView listContact=((ListView)findViewById(R.id.listContacts));
	    	listContact.setEnabled(true);
	        mActionMode = null;
	    }
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (placePosition!=null)
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
