package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class AddUserResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String result;

	@SuppressWarnings("unused")
	private AddUserResult() {
		// For serialization only
	}

	public AddUserResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}
}
