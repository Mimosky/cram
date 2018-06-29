package org.app.shared.dispatch;

import java.util.List;

import org.app.shared.wrapper.FerierWrapper;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class RetrieveFerierResult implements Result{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<FerierWrapper> ferierList; 
	
	@SuppressWarnings("unused")
	private RetrieveFerierResult(){
		//Used only for serialization
	}
	public RetrieveFerierResult(List<FerierWrapper> ferierList){
		this.ferierList = ferierList;
	}

	public List<FerierWrapper> getResult() {
		return ferierList;
	}
}
