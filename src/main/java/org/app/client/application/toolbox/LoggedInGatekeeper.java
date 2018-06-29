package org.app.client.application.toolbox;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.DefaultGatekeeper;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

@DefaultGatekeeper
public class LoggedInGatekeeper implements Gatekeeper {

	private final CurrentUser currentUser;

	@Override
	public boolean canReveal() {
		boolean loggedIn = false;
		if (currentUser != null) {
			loggedIn = currentUser.isLoggedIn();
		}
		return loggedIn;
	}

	@Inject
	public LoggedInGatekeeper(final CurrentUser currentUser) {
		this.currentUser = currentUser;
	}

	public void disonnectUser() {
		currentUser.setLoggedIn(false);
	}
}
