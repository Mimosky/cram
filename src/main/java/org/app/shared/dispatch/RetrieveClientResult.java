package org.app.shared.dispatch;

import java.util.List;

import org.app.shared.wrapper.ClientWrapper;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class RetrieveClientResult implements Result{


	private static final long serialVersionUID = 1L;
	private List<ClientWrapper> clientList;
	
	@SuppressWarnings("unused")
	private RetrieveClientResult() {
		// For serialization only
	}

	public RetrieveClientResult(List<ClientWrapper> clientList) {
		this.clientList = clientList;
	}

	public List<ClientWrapper> getResult() {
		return clientList;
	}

}
