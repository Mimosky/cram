package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class AddClientAction extends ActionImpl<AddClientResult> {

	private String name;
	private String address;
	private String email;
	private String date;

	public AddClientAction(String name, String address, String email, String date) {
		super();
		this.name = name;
		this.address = address;
		this.email = email;
		this.date = date;
	}
	
	@Override
	public boolean isSecured() {
		return false;
	}
	@SuppressWarnings("unused")
	private AddClientAction() {
		// For serialization only
	}
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	public String getEmail() {
		return email;
	}
	public String getDate() {
		return date;
	}
}
