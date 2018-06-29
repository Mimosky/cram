package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class GeneratePdfResult implements Result{

	/**
	 * 
	 */
	
	private String result;
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private GeneratePdfResult(){
	}
	
	public GeneratePdfResult(String result){
		this.result = result;
	}
	
	public String getResult() {
		return result;
	}
}
