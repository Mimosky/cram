package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class ModifyCongesResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String result;

	@SuppressWarnings("unused")
	private ModifyCongesResult() {
		// For serialization only
	}

	public ModifyCongesResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}
}
