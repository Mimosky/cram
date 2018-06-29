package org.app.shared.dispatch;

import java.util.Date;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class AddFerierAction extends ActionImpl<AddFerierResult>{
	private String type;
	private Date date;
	
	@Override
	public boolean isSecured() {
		return false;
	}
	@SuppressWarnings("unused")
	private AddFerierAction() {
		// For serialization only
	}
	public AddFerierAction(Date date, String type){
		this.date = date;
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	public Date getDate() {
		return date;
	}
}
