package es.deusto.onthestreet;

import java.io.Serializable;
import java.util.ArrayList;

public class Place implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private String location;
	private ArrayList<Contact> relatedContacts;
	
	
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
	@Override
	public String toString(){
		return this.getName()+" "+getDescription();
	}
	
}
