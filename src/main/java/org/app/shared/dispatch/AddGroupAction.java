package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class AddGroupAction extends ActionImpl<AddGroupResult>{

	public AddGroupAction() {
		// For serialization only
	}
	
	@Override
	public boolean isSecured() {
		return false;
	}
	
	private String users;
	private String name;
	private String manager;
	public AddGroupAction(String name, String manager, String users){
		this.name = name;
		this.manager = manager;
		this.users = users;
	}
	
	public String getUsers() {
		return users;
	}
	public String getName() {
		return name;
	}
	public String getManager() {
		return manager;
	}
}
