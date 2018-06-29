package org.app.shared.wrapper;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientWrapper implements IsSerializable{

	private String name;
	private String address;
	private String email;
	private Date date;
	
	public ClientWrapper(){
		super();
	}
	
	public ClientWrapper(String name, String address, String email, Date date){
		this.name = name;
		this.address = address;
		this.email = email;
		this.date = date;
	
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	

}
