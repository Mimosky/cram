package org.app.shared.wrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CramWrapper implements IsSerializable{
	private Date date;
	private Collection<WorkLoadWrapper> workList;
	
	public CramWrapper(){
		super();
		workList = new ArrayList<WorkLoadWrapper>();
		date = new Date();
	}
	public CramWrapper(Date date){
		this.date = date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
	public Collection<WorkLoadWrapper> getWorkList() {
		return workList;
	}
	public void setWorkList(Collection<WorkLoadWrapper> workList) {
		this.workList = workList;
	}
}
