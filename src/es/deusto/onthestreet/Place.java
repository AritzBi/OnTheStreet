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
	private double lat;
	private double lon;
	private String address;
	private ArrayList<Contact> relatedContacts;
	private String uri;
	
	public Place(){
		super();
		this.name = "";
		this.description = "";
		this.lat=0;
		this.lon=0;
		this.relatedContacts=new ArrayList<Contact>();
		this.address="";
	}
	public Place(String name, String description, double lat,double lon,ArrayList<Contact>relatedContacts, String address){
		super();
		this.name = name;
		this.description = description;
		this.lat=lat;
		this.lon=lon;
		this.relatedContacts=relatedContacts;
		this.address=address;
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
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString(){
		return this.getName()+" "+getDescription();
	}
	
}
