package es.deusto.onthestreet;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


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
        //adpPlaces = new ArrayAdapter<Place>(this, android.R.layout.simple_list_item_1, arrPlaces);
		adpPlaces = new ArrayAdapter<Place>(this, R.layout.image_list, R.id.place_name,arrPlaces) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text1 = (TextView) view.findViewById(R.id.place_name);
				TextView text2 = (TextView) view.findViewById(R.id.place_location);
				text1.setText(arrPlaces.get(position).getName());
				text2.setText(arrPlaces.get(position).getLocation());
				System.out.println(arrPlaces.get(position).getUri());
				File imgFile=new File(arrPlaces.get(position).getUri());
				System.out.println("Antes de exists");
				System.out.println(imgFile);
				if(imgFile.exists()){
					System.out.println("Paso por aqui");
					//Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
					ImageView myImage = (ImageView) view.findViewById(R.id.icon_image);
					//myImage.setImageBitmap(myBitmap);
					System.out.println(myImage);
					System.out.println(Uri.fromFile(imgFile));
					myImage.setImageURI(Uri.fromFile(imgFile));
				}
				return view;
			}
		};
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
		itemDetailIntent.putExtra(Place.PLACE, position);
		itemDetailIntent.putExtra(Place.ARRAY, arrPlaces);
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
				System.out.println(place);
				arrPlaces.add(place);
				adpPlaces.notifyDataSetChanged();
			}		
		}else if(requestCode == EDIT_ITEM){ // If it was an EDIT_ITEM, replace the selected item and update the list
			if(resultCode == Activity.RESULT_OK){

			}		
		}else if(requestCode == VIEW_ITEM){
			 createPlaceList();
		}
	}
}
