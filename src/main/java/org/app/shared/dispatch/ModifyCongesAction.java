package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class ModifyCongesAction extends ActionImpl<ModifyCongesResult>  {
	private int congesId;
	private String value;
	
	public ModifyCongesAction(int congesId, String value){
		this.congesId = congesId;
		this.value = value;
	}
	
	@Override
	public boolean isSecured() {
		return false;
	}
	@SuppressWarnings("unused")
	private ModifyCongesAction() {
		// For serialization only
	}
	public String getValue() {
		return value;
	}
	public int getCongesId() {
		return congesId;
	}
	
}
