package org.app.client.application.login;

import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

class LoginView extends ViewImpl implements LoginPresenter.MyView {
	interface Binder extends UiBinder<Widget, LoginView> {
	}

	@UiField
	HTMLPanel main;

	@UiField
	TextBox username;
	@UiField
	Button loginButton;
	@UiField
	PasswordTextBox password;
	@UiField
	ControlGroup usernameGroup;
	@UiField
	ControlGroup passwordGroup;
	@UiField
	HelpInline usernameErrors;
	@UiField
	HelpInline passwordErrors;
	
	@Inject
	LoginView(EventBus eventBus,Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	

	public HelpInline getUsernameErrors() {
		return usernameErrors;
	}



	public HelpInline getPasswordErrors() {
		return passwordErrors;
	}



	public ControlGroup getUsernameGroup() {
		return usernameGroup;
	}

	public ControlGroup getPasswordGroup() {
		return passwordGroup;
	}



	public Button getLoginButton() {
		return loginButton;
	}

	public TextBox getUsername() {
		return username;
	}

	public PasswordTextBox getPassword() {
		return password;
	}

	/*
	 * @Override public void setInSlot(Object slot, IsWidget content) { if (slot
	 * == LoginPresenter.SLOT_Login) { main.setWidget(content); } else {
	 * super.setInSlot(slot, content); } }
	 */
}