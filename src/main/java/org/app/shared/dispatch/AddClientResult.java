package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class AddClientResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String result;

	@SuppressWarnings("unused")
	private AddClientResult() {
		// For serialization only
	}

	public AddClientResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

}
