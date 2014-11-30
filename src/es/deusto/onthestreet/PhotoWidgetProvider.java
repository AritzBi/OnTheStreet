package es.deusto.onthestreet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;

public class PhotoWidgetProvider extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.i("Widget","Widget Update");
		GeoLocation locationTask = new GeoLocation(context);
		locationTask.execute();
		String[] locationData;
		try {
			locationData = locationTask.get();
			System.out.println("Paso por aqui");
			ArrayList<Place>allPlaces=(new PlaceManager(context)).loadPLaces();
			double latitude=Double.parseDouble(locationData[0]);
			double longitude=Double.parseDouble(locationData[1]);		
			Location location=new Location("");
			location.setLatitude(latitude);
			location.setLongitude(longitude);
			Place selected=null;
			double distance=0;
			double minDistance=Integer.MAX_VALUE;
			for(Place p: allPlaces){		
				Location l=new Location("");
				l.setLatitude(p.getLat());
				l.setLongitude(p.getLon());
				distance=location.distanceTo(l);
				if( distance<minDistance){
					selected=p;
					minDistance=distance;
				}
			}
			// We must iterate all the widget instances
			for(int i = 0; i < appWidgetIds.length; i++){
				int widgetId = appWidgetIds[i];
				File imgFile=new File(selected.getUri());
				Bitmap bitmap=null;
				if(imgFile.exists()){
			        try {
						bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(imgFile));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					 bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_picture);
				}
				RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_photo_widget);				
				views.setImageViewBitmap(R.id.imgPhoto, bitmap);
				appWidgetManager.updateAppWidget(widgetId, views);		
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
}
