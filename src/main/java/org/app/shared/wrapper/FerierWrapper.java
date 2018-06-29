package org.app.shared.wrapper;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FerierWrapper implements IsSerializable{
	private Date date;
	private String type;
	private String status;
	
	public FerierWrapper(){
		super();
	}
	public FerierWrapper(Date date, String type, String status){
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
