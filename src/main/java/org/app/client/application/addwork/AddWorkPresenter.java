package org.app.client.application.addwork;

import org.app.client.application.toolbox.CurrentUser;
import org.app.client.events.ClientNameEvent;
import org.app.shared.dispatch.RetrieveUsersAction;
import org.app.shared.dispatch.RetrieveUsersResult;
import org.app.shared.wrapper.ClientWrapper;
import org.app.shared.wrapper.UserWrapper;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.client.RpcDispatchAsync;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class AddWorkPresenter extends PresenterWidget<AddWorkPresenter.MyView> {
	interface MyView extends PopupView {
		public Button getOkButton();
		public ListBox getListBox();
	}

	private CurrentUser currentUser;

	@Inject
	EventBus eventBus;
	@Inject
	RpcDispatchAsync dispatchAsync;

	@Inject
	AddWorkPresenter(EventBus eventBus, MyView view, CurrentUser currentUser) {
		super(eventBus, view);
		this.currentUser = currentUser;

	}

	@Override
	protected void onBind() {
		super.onBind();
		dispatchAsync.execute(new RetrieveUsersAction(), new AsyncCallback<RetrieveUsersResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(RetrieveUsersResult result) {

				for (UserWrapper i : result.getResult()) {
					if (i.getUserEmail().equals(currentUser.getColleageEmail())) {
						for (ClientWrapper j : i.getClients()) {
							getView().getListBox().addItem(j.getName());
						}
					}
				}
			}
		});
		getView().getOkButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientNameEvent clientEvent = new ClientNameEvent(getView().getListBox().getValue());
				eventBus.fireEvent(clientEvent);
				getView().hide();
			}
		});
	}
	
	@Override
	protected void onReveal() {
		// TODO Auto-generated method stub
		super.onReset();
		getView().getListBox().clear();
		dispatchAsync.execute(new RetrieveUsersAction(), new AsyncCallback<RetrieveUsersResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(RetrieveUsersResult result) {
				for (UserWrapper i : result.getResult()) {
					if (i.getUserEmail().equals(currentUser.getColleageEmail())) {
						for (ClientWrapper j : i.getClients()) {
							getView().getListBox().addItem(j.getName());
						}
					}
				}
			}
		});
	}

	@Override
	protected void onHide() {
		super.onHide();
	}

}