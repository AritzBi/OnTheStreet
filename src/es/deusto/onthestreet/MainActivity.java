package es.deusto.onthestreet;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.ShareActionProvider;
import android.widget.TextView;


public class MainActivity extends ListActivity {
	static  int SELECT_PLACE=1;
	public static final int ADD_ITEM = 0;
	public static final int EDIT_ITEM = 1;
	public static final int VIEW_ITEM = 2;
	public static final int GEO_FILTER = 0;
	public static final int SEARCH_FILTER=1;
	private ArrayList<Place>arrPlaces;
	private MyCustomAdapter adpPlaces;
	private ActionMode mActionMode=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.createPlaceList();
		adpPlaces = new MyCustomAdapter(this, R.layout.image_list, R.id.place_name,arrPlaces) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text1 = (TextView) view.findViewById(R.id.place_name);
				TextView text2 = (TextView) view.findViewById(R.id.place_location);
				text1.setText(arrPlaces.get(position).getName());
				text2.setText(arrPlaces.get(position).getAddress());
				File imgFile=new File(arrPlaces.get(position).getUri());
				if(imgFile.exists()){
					ImageView myImage = (ImageView) view.findViewById(R.id.icon_image);
					myImage.setImageURI(Uri.fromFile(imgFile));
				}
				return view;
			}
		};
        setListAdapter(adpPlaces);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		    // Called when the user long-clicks an item on the list
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View row, int position, long rowid) {
		        if (mActionMode != null) {
		            return false;
		        }

		        // Important: to marked the editing row as activated
		        getListView().setItemChecked(position, true);
		        SELECT_PLACE=position;
		        // Start the CAB using the ActionMode.Callback defined above
		        mActionMode = MainActivity.this.startActionMode(mActionModeCallback);
		        return true;
		    }
		});
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false); 
    }

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		
	    // Called when the action mode is created; startActionMode() was called
	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        // Inflate a menu resource providing context menu items
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.place_action, menu);
	        MenuItem mnuShare = menu.findItem(R.id.mnu_share); 
	    	ShareActionProvider shareProv = (ShareActionProvider) 
	    	mnuShare.getActionProvider(); 
	    	shareProv.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME); 
	    	Intent intent = new Intent(Intent.ACTION_SEND); 
	    	intent.setType("text/plain"); 
	    	int position=MainActivity.this.getListView().getCheckedItemPosition();
	    	Place tmpPlace=arrPlaces.get(position);
	    	intent.putExtra(Intent.EXTRA_TEXT, tmpPlace.getName() + " " + tmpPlace.getDescription()); 
	    	shareProv.setShareIntent(intent); 
	        return true;
	    }

	    // Called when the user enters the action mode
	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	    	// Disable the list to avoid selecting other elements while editing one
	    	MainActivity.this.getListView().setEnabled(false);
	        return true; // Return false if nothing is done
	    }
	    
	    // Called when the user selects a contextual menu item
	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.mnu_student_edit:
	                mode.finish(); // Action picked, so close the CAB and execute action
	        		Intent itemDetailIntent = new Intent(getApplicationContext(), PlaceCreateActivity.class);
	        		itemDetailIntent.putExtra(Place.PLACE_POSITION, SELECT_PLACE);
	        		itemDetailIntent.putExtra(Place.PLACE, arrPlaces.get(SELECT_PLACE));
	        		startActivityForResult(itemDetailIntent, EDIT_ITEM);
	                return true;
	            case R.id.mnu_student_delete:
	                mode.finish(); // Action picked, so close the CAB and execute action
	                arrPlaces.remove(SELECT_PLACE);
	                adpPlaces.notifyDataSetChanged();
	                return true;
	            default:
	                return false;
	        }
	    }

	    // Called when the user exits the action mode
	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	    	// Re-enable the list after edition
	    	MainActivity.this.getListView().setEnabled(true);
	        mActionMode = null;
	    }
	};
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         SearchView searchView =
                 (SearchView) menu.findItem(R.id.search).getActionView();
         searchView.setSearchableInfo(
                 searchManager.getSearchableInfo(getComponentName()));
         searchView.setOnQueryTextListener(new OnQueryTextListener() { 

             @Override 
             public boolean onQueryTextChange(String query) {
            	 System.out.println("Paso por aquiele");
            	 System.out.println(query+"hola");
            	 adpPlaces.getFilter(SEARCH_FILTER).filter(query);
                 return true; 

             }

			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			} 

         });
        return true;
    }
    private void createPlaceList(){
    	arrPlaces=(new PlaceManager(getApplicationContext())).loadPLaces();
    	if(arrPlaces == null){
    		arrPlaces=new ArrayList<Place>();
    	}
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// If new item, simply call the detail activity with request code ADD_ITEM
		case R.id.mnu_add_place:
			Intent itemCreateIntent = new Intent(this, PlaceCreateActivity.class);
			startActivityForResult(itemCreateIntent, ADD_ITEM);
			break;
		case R.id.action_settings:
			this.showSettings(item);
			break;
		case R.id.action_get_location:
			adpPlaces.getFilter(GEO_FILTER).filter("");
		}

		return super.onOptionsItemSelected(item);
	}

    @Override 
    protected void onListItemClick(ListView l, View v, int position, long id) { 
    	super.onListItemClick(l, v, position, id); 
		Intent itemDetailIntent = new Intent(this, PlaceDetailActivity.class);
		itemDetailIntent.putExtra(Place.PLACE_POSITION, position);
		itemDetailIntent.putExtra(Place.PLACE, arrPlaces.get(position));
		startActivityForResult(itemDetailIntent, VIEW_ITEM);
  
    } 
    @Override
    public void onStop(){
    	super.onStop();
    	Log.i("Stop", "Saving Data");
    	(new PlaceManager(getApplicationContext())).savePlaces(arrPlaces);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == ADD_ITEM){ // If it was an ADD_ITEM, then add the new item and update the list
			if(resultCode == Activity.RESULT_OK){
				Place place=(Place)data.getExtras().getSerializable(Place.PLACE);
				arrPlaces.add(place);
				adpPlaces.notifyDataSetChanged();
			}		
		}else if(requestCode == EDIT_ITEM || requestCode == VIEW_ITEM){ // If it was an EDIT_ITEM, replace the selected item and update the list
			if(resultCode == Activity.RESULT_OK ){
				System.out.println("PASO POR AQUI");
				Place place=(Place)data.getExtras().getSerializable(Place.PLACE);
				int position=(Integer)data.getExtras().getSerializable(Place.PLACE_POSITION);
				arrPlaces.set(position, place);
				adpPlaces.notifyDataSetChanged();
			}		
		}
	}
	 public void showSettings(MenuItem item){
		 Intent intent = new Intent(this, MySettingsActivity.class);
		 startActivity(intent);
		 } 

}
