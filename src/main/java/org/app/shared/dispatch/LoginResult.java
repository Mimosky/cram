package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class LoginResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String result;
	private String profile;
	private String name;

	@SuppressWarnings("unused")
	private LoginResult() {
		// For serialization only
	}

	public LoginResult(String result, String profile, String name) {
		this.result = result;
		this.profile = profile;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public String getResult() {
		return result;
	}
	public String getProfile() {
		return profile;
	}
}
