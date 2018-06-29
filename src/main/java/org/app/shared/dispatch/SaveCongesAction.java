package org.app.shared.dispatch;

import org.app.shared.wrapper.CongesWrapper;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class SaveCongesAction extends ActionImpl<SaveCongesResult>{
	private String email;
	private CongesWrapper conges;

	
	public SaveCongesAction(String email, CongesWrapper conges){
		this.email = email;
		this.conges = conges;
	}
	
	@SuppressWarnings("unused")
	private SaveCongesAction(){
		// For serialization only
	}
	
	@Override
	public boolean isSecured() {
		return false;
	}
	
	public String getEmail() {
		return email;
	}
	public CongesWrapper getConges() {
		return conges;
	}
}
