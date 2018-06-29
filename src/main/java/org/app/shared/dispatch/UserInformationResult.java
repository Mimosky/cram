package org.app.shared.dispatch;

import org.app.shared.wrapper.UserWrapper;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class UserInformationResult implements Result{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserWrapper user;
	@SuppressWarnings("unused")
	private UserInformationResult() {
		// For serialization only
	}

	public UserInformationResult(UserWrapper user){
		this.user = user;
	}
	
	public UserWrapper getResult(){
		return user;
	}
}
