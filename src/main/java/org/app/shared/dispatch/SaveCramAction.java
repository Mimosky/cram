package org.app.shared.dispatch;

import java.util.Date;

import org.app.shared.wrapper.CramWrapper;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class SaveCramAction extends ActionImpl<SaveCramResult>{
	
	private String email;
	private CramWrapper cram;
	private Date date;
	
	public SaveCramAction(String email, CramWrapper cram, Date date){
		this.email = email;
		this.cram = cram;
		this.date = date;
	}
	
	@SuppressWarnings("unused")
	private SaveCramAction(){
		// For serialization only
	}
	
	@Override
	public boolean isSecured() {
		return false;
	}
	
	public String getEmail() {
		return email;
	}
	public CramWrapper getCram() {
		return cram;
	}
	public Date getDate() {
		return date;
	}

}
