package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class NotifyCramResult implements Result{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String result;
	
	@SuppressWarnings("unused")
	private NotifyCramResult(){
	}
	
	public NotifyCramResult(String result){
		this.result = result;
	}
	public String getResutl(){
		return this.result;
	}

}
