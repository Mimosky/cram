package org.app.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="CRAM")
public class Cram  {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int myKey;
	private Date date;
	@ElementCollection
	private Collection<WorkLoad> workList;
	
	public Cram(){
		super();
		workList = new ArrayList<WorkLoad>();
		date = new Date();
	}
	public Cram(Date date){
		this.date = date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
	public Collection<WorkLoad> getWorkList() {
		return workList;
	}
	public void setWorkList(Collection<WorkLoad> workList) {
		this.workList = workList;
	}
	
	
}