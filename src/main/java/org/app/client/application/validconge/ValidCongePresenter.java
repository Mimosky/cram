package org.app.client.application.validconge;

import org.app.client.events.CongesConfirmEvent;
import org.app.client.events.PopupMessageEvent;
import org.app.client.events.PopupMessageEvent.PopupMessageHandler;
import org.app.client.events.ValidCongesEvent;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
public class ValidCongePresenter extends PresenterWidget<ValidCongePresenter.MyView>  {
    interface MyView extends PopupView  {
		public Heading getLabel();

		public Button getOkButton();

		public Button getCancelButton();
    }

    @Inject
    ValidCongePresenter(
            EventBus eventBus,
            MyView view) {
        super(eventBus, view);
        
    }
    
    @Inject EventBus eventBus;
    
    protected void onBind() {
        super.onBind();
        registerHandler(getEventBus().addHandler(PopupMessageEvent.getType(), popupMessageHandler));
		getView().getOkButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				ValidCongesEvent messageEvent;
				messageEvent = new ValidCongesEvent("ok");
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