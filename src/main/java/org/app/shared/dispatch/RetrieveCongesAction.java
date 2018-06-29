package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class RetrieveCongesAction extends ActionImpl<RetrieveCongesResult>{

	private String email;
	public RetrieveCongesAction(){
	}
	
	@Override
	public boolean isSecured() {
		return false;
	}
	public RetrieveCongesAction(String email){
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
}
