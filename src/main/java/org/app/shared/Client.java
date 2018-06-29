package org.app.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table (name="CLIENT")
public class Client{
	@Id
	private String name;
	private String address;
	private String email;
	private Date date;
	@ManyToMany
	private Collection<User> userList = new ArrayList<User>();
	

	public Client(){
		super();
	}
	public Client(String name, String address, String email, Date date){
		this.name = name;
		this.address = address;
		this.email = email;
		this.date = date;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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



	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getFomatedDate() {
		String day;
		String month;
		if (date.getDate()<10) day="0"+Integer.toString(date.getDate()); else day=Integer.toString(date.getDate());
		if(date.getMonth()<9) month="0"+Integer.toString(date.getMonth()+1); else month=Integer.toString(date.getMonth()+1);
		return day+"/"+month+"/"+Integer.toString(date.getYear()+1900);
	}
	

}
