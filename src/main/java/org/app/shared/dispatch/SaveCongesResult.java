package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class SaveCongesResult  implements Result{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String result;

	@SuppressWarnings("unused")
	private SaveCongesResult() {
		// For serialization only
	}

	public SaveCongesResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

}
