package es.deusto.onthestreet;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings({ "rawtypes" })
public class MyCustomAdapter extends BaseAdapter implements Filterable,LocationCallback{
	private ArrayList<Place>arrPlaces;
	private ArrayList<Place>allPlaces;
	private Context context;
	private int resource;
	@SuppressWarnings("unchecked")
	public MyCustomAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<Place> places) {
		super();
		/*allPlaces=places;
		arrPlaces=cloneList(allPlaces);
		arrPlaces.clear();
		System.out.println(allPlaces);
		System.out.println(arrPlaces);*/
		allPlaces=cloneList(places);
		arrPlaces=places;
		this.context=context;
		this.resource=resource;
	}
	public static ArrayList<Place> cloneList(ArrayList<Place> list) {
	    ArrayList<Place> clone = new ArrayList<Place>(list.size());
	    for(Place item: list) clone.add(item);
	    return clone;
	}
	@Override
	public void getCurrentLocation(Location location) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String mynumberPref = sharedPref.getString("pref_key_place_limit", ""); 
		arrPlaces.clear();
		for(Place p: allPlaces){		
			Location l=new Location("");
			l.setLatitude(p.getLat());
			l.setLongitude(p.getLon());
			if(location.distanceTo(l)<=Integer.parseInt(mynumberPref)){
				arrPlaces.add(p);
			}
		}
		this.notifyDataSetChanged();
	}
	@Override
	public Filter getFilter() {
		 return new Filter() {
	            @SuppressWarnings("unchecked")
	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	            	System.out.println("Arriba values");
	            	System.out.println(results.values);
	            	arrPlaces = (ArrayList<Place>) results.values;
	                MyCustomAdapter.this.notifyDataSetChanged();
	            }

	            @Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	            	
	                FilterResults results = new FilterResults();
	                /*if(mode == MainActivity.GEO_FILTER){
	                    results.values = cloneList(allPlaces);
	                    GeoLocation geo=new GeoLocation(MyCustomAdapter.this,context);
	                    geo.execute();
	                }else{*/
	                	if(constraint.length()==0){
	                		System.out.println("vacio");
	                		//results.values=cloneList(allPlaces);
	                		results.values=allPlaces;
	                		System.out.println(allPlaces);
	                	}else{
	                		/*System.out.println("Llamo al segundo");
	                		System.out.println(allPlaces);
	                    	System.out.println(constraint);
	                		arrPlaces.clear();
	                		System.out.println(allPlaces);
	                    	ArrayList<Place>filteredPlaces=new ArrayList<Place>();
	                		for(Place p: allPlaces){		
	                			if(p.getName().contains(constraint)){
	                				System.out.println("true");
	                				filteredPlaces.add(new Place(p));
	                			}
	                				
	                		}
	                		results.values = filteredPlaces;
	                		System.out.println("Values");
	                		System.out.println(results.values);*/
	                		ArrayList<Place>filteredPlaces=new ArrayList<Place>();
	                		for(Place p: allPlaces){		
	                			if(p.getName().contains(constraint)){
	                				System.out.println("true");
	                				filteredPlaces.add(p);
	                			}
	                				
	                		}
	                		results.values = filteredPlaces;
	                	}
	                //}

	                return results;
	            }
	        };
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrPlaces.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrPlaces.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View view = convertView;
	    if (view == null) {
	        LayoutInflater mInflater = (LayoutInflater) context
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        view = mInflater.inflate(resource, null);
	    }
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

}
