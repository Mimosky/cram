package org.app.client.application.login;

import java.util.Date;

import org.app.client.application.toolbox.CurrentUser;
import org.app.client.place.NameTokens;
import org.app.shared.dispatch.LoginAction;
import org.app.shared.dispatch.LoginResult;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class LoginPresenter extends Presenter<LoginPresenter.MyView, LoginPresenter.MyProxy> {
	interface MyView extends View {
		public Button getLoginButton();

		public TextBox getUsername();

		public PasswordTextBox getPassword();

		public ControlGroup getUsernameGroup();

		public ControlGroup getPasswordGroup();

		public HelpInline getUsernameErrors();

		public HelpInline getPasswordErrors();
	}

	@NameToken(NameTokens.login)
	@ProxyCodeSplit
	@NoGatekeeper
	interface MyProxy extends ProxyPlace<LoginPresenter> {
	}

	private final DispatchAsync dispatchAsync;
	@Inject
	LoginPresenter(EventBus eventBus, MyView view, MyProxy proxy,DispatchAsync dispatchAsync, CurrentUser currentUser) {
		super(eventBus, view, proxy, RevealType.Root);
		this.currentUser = currentUser;
		this.dispatchAsync = dispatchAsync ;

	}

	private CurrentUser currentUser;
	@Inject
	PlaceManager placeManager;
	

	protected void onReset() {
		super.onReset();
		getView().getUsername().setText("");
		getView().getPassword().setText("");


	}
	
	@Override
	protected void onBind() {
		// TODO Auto-generated method stub
		super.onBind();
		Date date = new Date();
		getView().getPassword().addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeEvent().getKeyCode()==KeyCodes.KEY_ENTER){
					if (!checkEmptyFields()) {
						LoginAction action = new LoginAction(getView().getUsername().getText(), getView().getPassword().getText(),date);
						dispatchAsync.execute(action, loginCallBack);
					}
				}
				
			}
		});
		
		
		getView().getLoginButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!checkEmptyFields()) {
					LoginAction action = new LoginAction(getView().getUsername().getText(), getView().getPassword().getText(), date);
					dispatchAsync.execute(action, loginCallBack);
				}
			}
		});

	}

	private AsyncCallback<LoginResult> loginCallBack = new AsyncCallback<LoginResult>() {

		@Override
		public void onFailure(Throwable caught) {
			System.out.println("Error while executing action");
		}

		@Override
		public void onSuccess(LoginResult result) {
			if (result.getResult().equals("no")) {
				getView().getPasswordGroup().setType(ControlGroupType.ERROR);
				getView().getPasswordErrors().setText("Identifiant ou mot de passe incorrect");
			} else {
				getView().getPasswordGroup().setType(ControlGroupType.NONE);
				getView().getPasswordErrors().setText("");
				currentUser.setLoggedIn(true);
				currentUser.setLogin(getView().getUsername().getText());
				currentUser.setColleageEmail(getView().getUsername().getText());
				currentUser.setName(result.getName());
				if(result.getProfile().equals("admin"))
					currentUser.setAdministrator(true);
				else
					currentUser.setAdministrator(false);
				PlaceRequest request = new PlaceRequest.Builder().nameToken(NameTokens.home).build();
				placeManager.revealPlace(request);
			}
		}
	};

	public boolean checkEmptyFields() {
		boolean hasError = false;

		if (getView().getUsername() == null || getView().getUsername().getText().isEmpty()) {
			getView().getUsernameGroup().setType(ControlGroupType.ERROR);
			getView().getUsernameErrors().setText("Le nom est obligatoire");
			hasError = true;
		} else {
			getView().getUsernameGroup().setType(ControlGroupType.NONE);
			getView().getUsernameErrors().setText("");
		}
		if (getView().getPassword() == null || getView().getPassword().getText().isEmpty()) {
			getView().getPasswordGroup().setType(ControlGroupType.ERROR);
			getView().getPasswordErrors().setText("Le mot de passe est obligatoire");
			hasError = true;
		} else {
			getView().getPasswordGroup().setType(ControlGroupType.NONE);
			getView().getPasswordErrors().setText("");
		}

		return hasError;
	}

}
