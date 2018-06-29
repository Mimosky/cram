package org.app.client.application.header;

import org.app.client.application.toolbox.CurrentUser;
import org.app.client.place.NameTokens;
import org.app.shared.dispatch.ModifyUserAction;
import org.app.shared.dispatch.ModifyUserResult;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.client.RpcDispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class HeaderPresenter extends Presenter<HeaderPresenter.MyView, HeaderPresenter.MyProxy> {
	interface MyView extends View {
		public NavLink getHome();

		public NavLink getAdmin();

		public NavLink getDeconnexion();
		public Button getModifyButton();
		public HTMLPanel getMain();
		public PasswordTextBox getPasswordModify() ;
		public NavLink getResetPassword() ;
		public Modal getModifyModal() ;
		public ControlGroup getPasswordGroup() ;
		public HelpInline getPasswordHelpInline();
	}

	public static final NestedSlot SLOT_Header = new NestedSlot();

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<HeaderPresenter> {

	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	private CurrentUser currentUser;

	@Inject
	HeaderPresenter(EventBus eventBus, MyView view, MyProxy proxy, CurrentUser currentUser) {
		super(eventBus, view, proxy, RevealType.Root);
		this.currentUser = currentUser;

	}

	protected void onBind() {
		super.onBind();

	}

	@Inject
	PlaceManager placeManager;
	@Inject RpcDispatchAsync dispatchAsync;

	@Override
	protected void onReset() {
		super.onReset();
		if (currentUser.isAdministrator())
			getView().getAdmin().setVisible(true);
		else
			getView().getAdmin().setVisible(false);
		getView().getAdmin().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PlaceRequest request = new PlaceRequest.Builder().nameToken(NameTokens.admin).build();
				placeManager.revealPlace(request);

			}
		});
		getView().getHome().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentUser.setColleageEmail(currentUser.getLogin());
				PlaceRequest request = new PlaceRequest.Builder().nameToken(NameTokens.home).build();
				placeManager.revealPlace(request);

			}
		});

		getView().getDeconnexion().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentUser.setLoggedIn(false);
				currentUser.setLogin("");
				currentUser.setAdministrator(false);
				placeManager.revealDefaultPlace();
				forceReload();

			}
		});
		
		getView().getResetPassword().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				getView().getModifyModal().show();
				
			}
		});
		
		getView().getModifyButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub
				if(!checkEmptyFields()){
					ModifyUserAction action = new ModifyUserAction(currentUser.getLogin(), getView().getPasswordModify().getText());
					dispatchAsync.execute(action, new AsyncCallback<ModifyUserResult>() {

						@Override
						public void onFailure(Throwable arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(ModifyUserResult result) {
							getView().getModifyModal().hide();
							
						}
					});
					
				}
			}
		});
	}
	
	public boolean checkEmptyFields() {
		boolean hasError = false;

		if (getView().getPasswordModify() == null || getView().getPasswordModify().getText().length() < 8) {
			getView().getPasswordGroup().setType(ControlGroupType.ERROR);
			getView().getPasswordHelpInline().setText("Le mot de passe trop court, il doit contenir min 8 caractères");
			hasError = true;
		} else {
			getView().getPasswordGroup().setType(ControlGroupType.NONE);
			getView().getPasswordHelpInline().setText("");
		}

		return hasError;
	}

	public static native void forceReload() /*-{
		$wnd.location.reload(true);
	}-*/;
}