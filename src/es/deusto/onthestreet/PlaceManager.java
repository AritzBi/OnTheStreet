package es.deusto.onthestreet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;

public class PlaceManager {

	private static final String FILENAME="Places";
	private Context mContext;
	
	public PlaceManager(Context c){
		mContext = c;
	}
	public ArrayList<Place>loadPLaces(){
		try {
			FileInputStream fis = mContext.openFileInput(FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			@SuppressWarnings("unchecked")
			ArrayList<Place> arr = (ArrayList<Place>) ois.readObject();
			ois.close();
			fis.close();
			return arr;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	public void savePlaces(ArrayList<Place>arr){
		try {
			FileOutputStream fos = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(arr);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
