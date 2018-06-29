package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class ModifyGroupAction extends ActionImpl<ModifyGroupResult>{
	
	@Override
	public boolean isSecured() {
		// TODO Auto-generated method stub
		return false;
	}
	private String users;
	private String name;
	private String manager;
	
	public ModifyGroupAction(){
		// Used for serialization
	}
	public ModifyGroupAction(String name, String manager, String users){
		this.name = name;
		this.manager = manager;
		this.users = users;
	}
	
	public String getName() {
		return name;
	}
	public String getManager() {
		return manager;
	}
	public String getUsers() {
		return users;
	}
	

}
