package org.app.shared;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="CONGES")
public class Conges {
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int congesId;
	private Date start;
	private String startD;
	private Date end;
	private String endD;
	private String type;
	private String status;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getCongesId() {
		return congesId;
	}
	public String getStartD() {
		return startD;
	}
	public void setStartD(String startD) {
		this.startD = startD;
	}
	public String getEndD() {
		return endD;
	}
	public void setEndD(String endD) {
		this.endD = endD;
	}
	public String getStart() {
		String day;
		String month;
		if (start.getDate()<10) day="0"+Integer.toString(start.getDate()); else day=Integer.toString(start.getDate());
		if(start.getMonth()<9) month="0"+Integer.toString(start.getMonth()+1); else month=Integer.toString(start.getMonth()+1);
		return day+"/"+month+"/"+Integer.toString(start.getYear()+1900);
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public String getEnd() {
		String day;
		String month;
		if (end.getDate()<10) day="0"+Integer.toString(end.getDate()); else day=Integer.toString(end.getDate());
		if(end.getMonth()<9) month="0"+Integer.toString(end.getMonth()+1); else month=Integer.toString(end.getMonth()+1);
		return day+"/"+month+"/"+Integer.toString(end.getYear()+1900);
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Conges(String type, Date start, Date end, String startD, String endD) {
		this.start = start;
		this.end = end;
		this.type = type;
		//this.status = status;
		this.startD = startD;
		this.endD = endD;
	}
	public Conges(String type, Date start, Date end, String startD, String endD, String status) {
		this.start = start;
		this.end = end;
		this.type = type;
		this.status = status;
		this.startD = startD;
		this.endD = endD;
	}
	public Conges(int congesId,String type, Date start, Date end, String startD, String endD, String status) {
		this.congesId = congesId;
		this.start = start;
		this.end = end;
		this.type = type;
		this.status = status;
		this.startD = startD;
		this.endD = endD;
	}
	
	public Conges(){
		super();
	}
	public Date getStartDate(){
		return this.start;
	}
	public Date getEndDate(){
		return this.end;
	}
	
}
