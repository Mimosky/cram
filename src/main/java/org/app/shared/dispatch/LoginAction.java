package org.app.shared.dispatch;

import java.util.Date;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

public class LoginAction extends UnsecuredActionImpl<LoginResult> {

	private String password;
	private String name;
	private Date date;


	
	public LoginAction(){
		
	}

	public LoginAction(String name,String password, Date date) {
		this.password = password;
		this.name = name;
		this.date = date;

	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}
public Date getDate() {
	return date;
}

}
