package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class ModifyUserResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String result;

	@SuppressWarnings("unused")
	private ModifyUserResult() {
		// For serialization only
	}

	public ModifyUserResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}
}
