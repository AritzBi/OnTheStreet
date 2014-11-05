package es.deusto.onthestreet;

import java.util.ArrayList;




import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ListActivity {
	static final int SELECT_PLACE=1;
	public static final int ADD_ITEM = 0;
	public static final int EDIT_ITEM = 1;
	public static final int VIEW_ITEM = 2;
	private ArrayList<Place>arrPlaces;
	private ArrayAdapter<Place> adpPlaces;
	private ActionMode mActionMode=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        this.createPlaceList();
        adpPlaces = new ArrayAdapter<Place>(this, android.R.layout.simple_list_item_1, arrPlaces);
        setListAdapter(adpPlaces);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// If new item, simply call the detail activity with request code ADD_ITEM
		case R.id.mnu_add_place:
			Intent itemCreateIntent = new Intent(this, PlaceCreateActivity.class);
			startActivityForResult(itemCreateIntent, ADD_ITEM);
			break;
		}

		return super.onOptionsItemSelected(item);
	}
    private void createPlaceList(){
    	arrPlaces=(new PlaceManager(getApplicationContext())).loadPLaces();
    	if(arrPlaces == null){
    		arrPlaces=new ArrayList<Place>();
    	}
    }
    @Override 
    protected void onListItemClick(ListView l, View v, int position, long id) { 
    	super.onListItemClick(l, v, position, id); 
		Intent itemDetailIntent = new Intent(this, PlaceDetailActivity.class);
		itemDetailIntent.putExtra(PlaceCreateActivity.PLACE, arrPlaces.get(position));
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
				Place place=(Place)data.getExtras().getSerializable(PlaceCreateActivity.PLACE);
				arrPlaces.add(place);
				adpPlaces.notifyDataSetChanged();
			}		
		}else if(requestCode == EDIT_ITEM){ // If it was an EDIT_ITEM, replace the selected item and update the list
			if(resultCode == Activity.RESULT_OK){

			}		
		}
	}
}
