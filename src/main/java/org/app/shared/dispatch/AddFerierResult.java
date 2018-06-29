package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class AddFerierResult implements Result{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String result;

	@SuppressWarnings("unused")
	private AddFerierResult() {
		// For serialization only
	}

	public AddFerierResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

}
