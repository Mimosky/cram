package org.app.client.application.confirmconges;

import org.app.client.events.CongesConfirmEvent;
import org.app.client.events.PopupMessageEvent;
import org.app.client.events.PopupMessageEvent.PopupMessageHandler;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
public class ConfirmCongesPresenter extends PresenterWidget<ConfirmCongesPresenter.MyView>  {
    interface MyView extends PopupView  {
		public Heading getLabel();

		public Button getOkButton();

		public Button getCancelButton();
    }

	@Inject
	EventBus eventBus;
    @Inject
    ConfirmCongesPresenter(
            EventBus eventBus,
            MyView view) {
        super(eventBus, view);
        
    }
    
    protected void onBind() {
        super.onBind();
        registerHandler(getEventBus().addHandler(PopupMessageEvent.getType(), popupMessageHandler));
		getView().getOkButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				CongesConfirmEvent messageEvent;
				messageEvent = new CongesConfirmEvent("ok");
				eventBus.fireEvent(messageEvent);
				getView().hide();
			}
		});

		getView().getCancelButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				CongesConfirmEvent messageEvent;
				messageEvent = new CongesConfirmEvent("Nok");
				eventBus.fireEvent(messageEvent);
				getView().hide();

			}
		});
    }
    private PopupMessageHandler popupMessageHandler = new PopupMessageHandler() {

		@Override
		public void onPopupMessage(PopupMessageEvent event) {
			getView().getLabel().setText(event.getMessage());
		}
	};

	@Override
	protected void onHide() {
		super.onHide();
	}
}