package es.deusto.onthestreet;

import java.io.Serializable;

public class Contact implements Serializable {
	private static final long serialVersionUID = 1L;
	private String mName;
	private String mPhoneNumber;
	
	public Contact(String mName, String mPhoneNumber) {
		super();
		this.mName = mName;
		this.mPhoneNumber = mPhoneNumber;
	}
	public String getName() {
		return mName;
	}
	public void setName(String mName) {
		this.mName = mName;
	}
	public String getPhoneNumber() {
		return mPhoneNumber;
	}
	public void setPhoneNumber(String mPhoneNumber) {
		this.mPhoneNumber = mPhoneNumber;
	}
	@Override
	public String toString(){
		return this.getName()+" ("+this.getPhoneNumber()+")";
	}
	
}
