package es.deusto.onthestreet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;

public class PhotoWidgetProvider extends AppWidgetProvider implements
		LocationCallback {
	private Context context;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		this.context = context;
		Log.i("Widget", "Widget Update");
		GeoLocation locationTask = new GeoLocation(PhotoWidgetProvider.this,
				context);
		locationTask.execute();
	}

	@Override
	public void getCurrentLocation(Location location, String address) {
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName thisWidget = new ComponentName(context,
				PhotoWidgetProvider.class);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		ArrayList<Place> allPlaces = (new PlaceManager(context)).loadPLaces();
		Place selected = null;
		double distance = 0;
		double minDistance = Integer.MAX_VALUE;
		for (Place p : allPlaces) {
			Location l = new Location("");
			l.setLatitude(p.getLat());
			l.setLongitude(p.getLon());
			distance = location.distanceTo(l);
			if (distance < minDistance) {
				selected = p;
				minDistance = distance;
			}
		}
		// We must iterate all the widget instances
		for (int i = 0; i < appWidgetIds.length; i++) {
			int widgetId = appWidgetIds[i];
			if (selected != null) {
				Intent itemDetailIntent = new Intent(context, PlaceDetailActivity.class);
				itemDetailIntent.putExtra(Place.PLACE, selected);
				PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, itemDetailIntent, 0);
				
				File imgFile = new File(selected.getUri());
				Bitmap bitmap = null;
				if (imgFile.exists()) {
					try {
						bitmap = MediaStore.Images.Media.getBitmap(
								context.getContentResolver(),
								Uri.fromFile(imgFile));
						bitmap=scaleDownBitmap(bitmap, 200, context);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					bitmap = BitmapFactory.decodeResource(
							context.getResources(),
							R.drawable.ic_action_picture);
				}
				if (bitmap != null) {
					RemoteViews views = new RemoteViews(
							context.getPackageName(),
							R.layout.layout_photo_widget);
					views.setImageViewBitmap(R.id.imgPhoto, bitmap);
					views.setOnClickPendingIntent(R.id.imgPhoto, pendingIntent);
					appWidgetManager.updateAppWidget(widgetId, views);
				}
			}

		}

	}
	 public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

		 final float densityMultiplier = context.getResources().getDisplayMetrics().density;        

		 int h= (int) (newHeight*densityMultiplier);
		 int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

		 photo=Bitmap.createScaledBitmap(photo, w, h, true);

		 return photo;
	 }
}
