package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class ModifyGroupResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String response;
	
	public ModifyGroupResult(){
		//Used for serialization
	}
	public ModifyGroupResult(String response){
		this.response = response;
	}
	public String getResponse() {
		return response;
	}

}
