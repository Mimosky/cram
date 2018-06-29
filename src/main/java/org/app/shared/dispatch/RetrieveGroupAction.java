package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class RetrieveGroupAction extends ActionImpl<RetrieveGroupResult> {

	@Override
	public boolean isSecured() {
		return false;
	}

	private String groupName;

	public RetrieveGroupAction() {
		// Used only for serialization
	}

	public RetrieveGroupAction(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupName() {
		return groupName;
	}
}
