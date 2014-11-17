package es.deusto.onthestreet;

import java.io.Serializable;
import java.util.ArrayList;

import android.net.Uri;

public class Place implements Serializable {

	private static final long serialVersionUID = 1L;
	protected static String PLACE="place";
	protected static String ARRAY="array";
	protected static String PLACE_POSITION="position";
	private String name;
	private String description;
	private String location;
	private ArrayList<Contact> relatedContacts;
	private String uri;
	
	
	public Place(String name, String description, String location,ArrayList<Contact>relatedContacts){
		super();
		this.name = name;
		this.description = description;
		this.location = location;
		this.relatedContacts=relatedContacts;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public ArrayList<Contact> getRelatedContacts() {
		return relatedContacts;
	}
	public void setRelatedContacts(ArrayList<Contact> relatedContacts) {
		this.relatedContacts = relatedContacts;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	public static String getPLACE() {
		return PLACE;
	}
	public static void setPLACE(String pLACE) {
		PLACE = pLACE;
	}
	public static String getARRAY() {
		return ARRAY;
	}
	public static void setARRAY(String aRRAY) {
		ARRAY = aRRAY;
	}
	public static String getPLACE_POSITION() {
		return PLACE_POSITION;
	}
	public static void setPLACE_POSITION(String pLACE_POSITION) {
		PLACE_POSITION = pLACE_POSITION;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	@Override
	public String toString(){
		return this.getName()+" "+getDescription();
	}
	
}
