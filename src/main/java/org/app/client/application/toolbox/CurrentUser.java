package org.app.client.application.toolbox;

import com.google.inject.Singleton;

@Singleton
public class CurrentUser {

	private String name;
	private String login;
	private String colleageEmail;
	private boolean loggedIn = false;
	private boolean administrator = false;

	public CurrentUser() {
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setLogin(String login) {
		this.login = login;
	}

	public CurrentUser(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}
	public String getColleageEmail() {
		return colleageEmail;
	}
	public void setColleageEmail(String colleageEmail) {
		this.colleageEmail = colleageEmail;
	}
}