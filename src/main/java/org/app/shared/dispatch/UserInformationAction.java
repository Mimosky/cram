package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class UserInformationAction extends ActionImpl<UserInformationResult>{
	private String email;
	private int year;
	private int month;
	
	public UserInformationAction(String email){
		this.email = email;
		year = -1;
		month = -1;
	}
	
	public UserInformationAction(String email, int year, int month){
		this.email = email;
		this.year = year;
		this.month = month;
	}
	@Override
	public boolean isSecured() {
		return false;
	}
	@SuppressWarnings("unused")
	private UserInformationAction() {
		// For serialization only
	}
	public String getEmail(){
		return email;
	}
	public int getYear() {
		return year;
	}
	public int getMonth() {
		return month;
	}
}
