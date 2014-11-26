package es.deusto.onthestreet;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Filter;

@SuppressWarnings({ "rawtypes" })
public class MyCustomAdapter extends ArrayAdapter implements LocationCallback{
	private ArrayList<Place>arrPlaces;
	private ArrayList<Place>allPlaces;
	private Context context;
	@SuppressWarnings("unchecked")
	public MyCustomAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<Place> places) {
		super(context, resource, textViewResourceId, places);
		arrPlaces=places;
		allPlaces=cloneList(arrPlaces);
		this.context=context;
	}
	public static ArrayList<Place> cloneList(ArrayList<Place> list) {
	    ArrayList<Place> clone = new ArrayList<Place>(list.size());
	    for(Place item: list) clone.add(new Place(item));
	    return clone;
	}
	
    public Filter getFilter(final int mode) {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
            	arrPlaces = (ArrayList<Place>) results.values;
                MyCustomAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
            	
                FilterResults results = new FilterResults();
                if(mode == MainActivity.GEO_FILTER){
                    results.values = allPlaces;
                    GeoLocation geo=new GeoLocation(MyCustomAdapter.this,context);
                    geo.execute();
                }else{
                	if(constraint.length()==0){
                		System.out.println("vacio");
                		results.values=allPlaces;
                		System.out.println(allPlaces);
                	}else{
                		System.out.println(allPlaces);
                    	System.out.println(constraint);
                		arrPlaces.clear();
                		System.out.println(allPlaces);
                    	ArrayList<Place>filteredPlaces=new ArrayList<Place>();
                		for(Place p: allPlaces){		
                			if(p.getName().contains(constraint)){
                				System.out.println("true");
                				filteredPlaces.add(p);
                			}
                				
                		}
                		results.values = filteredPlaces;	
                	}
                }

                return results;
            }
        };
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

}
