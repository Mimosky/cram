package org.app.shared;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="FERIER")
public class Ferier {
	
	@Id
	@Column (name ="email")
	private Date date;
	@Column (name="type")
	private String type;
	@Column (name="status")
	private String status;
	
	public Ferier(){
		super();
	}
	
	public Ferier(Date date, String type, String status){
		this.date = date;
		this.type = type;
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDate() {
		return date;
	}
	public String getType() {
		return type;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFomatedDate() {
		String day;
		String month;
		if (date.getDate()<10) day="0"+Integer.toString(date.getDate()); else day=Integer.toString(date.getDate());
		if(date.getMonth()<9) month="0"+Integer.toString(date.getMonth()+1); else month=Integer.toString(date.getMonth()+1);
		return day+"/"+month+"/"+Integer.toString(date.getYear()+1900);
	}
}
