package org.app.client.application.confirm;

import org.app.client.events.ConfirmEvent;
import org.app.client.events.PopupMessageEvent;
import org.app.client.events.PopupMessageEvent.PopupMessageHandler;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class ConfirmPresenter extends PresenterWidget<ConfirmPresenter.MyView> {
	interface MyView extends PopupView {
		public Heading getLabel();
		public Button getOkButton();
		public Button getCancelButton();
	}

	@Inject
	ConfirmPresenter(EventBus eventBus, MyView view) {
		super(eventBus, view);

	}

	@Inject
	EventBus eventBus;

	private JsDate date;
	private String Id;
	private String sender;

	protected void onBind() {
		super.onBind();
		registerHandler(getEventBus().addHandler(PopupMessageEvent.getType(), popupMessageHandler));
		getView().getOkButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				ConfirmEvent messageEvent;
				messageEvent = new ConfirmEvent("ok", date, Id, sender);
				eventBus.fireEvent(messageEvent);
				getView().hide();
			}
		});

		getView().getCancelButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				ConfirmEvent messageEvent;
				messageEvent = new ConfirmEvent("Nok", date, Id, sender);
				eventBus.fireEvent(messageEvent);
				getView().hide();

			}
		});

	}

	private PopupMessageHandler popupMessageHandler = new PopupMessageHandler() {

		@Override
		public void onPopupMessage(PopupMessageEvent event) {
			getView().getLabel().setText(event.getMessage());
			Id = event.getId();
			date = event.getDate();
			sender = event.getSender();
		}
	};

	@Override
	protected void onHide() {
		super.onHide();
	}
}