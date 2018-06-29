package org.app.shared.dispatch;

import java.util.ArrayList;

import org.app.shared.wrapper.GroupWrapper;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class RetrieveGroupResult implements Result{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String response;
	private ArrayList<GroupWrapper> group;
	
	public RetrieveGroupResult(){
		//Used for serialization only
	}
	public RetrieveGroupResult(String response, ArrayList<GroupWrapper> group){
		this.response = response;
		this.group = group;
	}
	
	public String getResponse() {
		return response;
	}
	public ArrayList<GroupWrapper> getGroup() {
		return group;
	}

}
