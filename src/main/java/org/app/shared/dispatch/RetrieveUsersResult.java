package org.app.shared.dispatch;

import java.util.List;

import org.app.shared.wrapper.UserWrapper;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class RetrieveUsersResult implements Result {
	private static final long serialVersionUID = 1L;

	private List<UserWrapper> usersList;
	
	@SuppressWarnings("unused")
	private RetrieveUsersResult() {
		// For serialization only
	}

	public RetrieveUsersResult(List<UserWrapper> usersList) {
		this.usersList = usersList;
	}

	public List<UserWrapper> getResult() {
		return usersList;
	}
}
