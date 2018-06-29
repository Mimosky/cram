package org.app.client.application.notify;

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
public class NotifyPresenter extends PresenterWidget<NotifyPresenter.MyView>  {
    interface MyView extends PopupView  {
        public Heading getLabel() ;
        public Button getOkButton();
    }

    @Inject
    NotifyPresenter(
            EventBus eventBus,
            MyView view) {
        super(eventBus, view);
        
    }
    
	@Inject
	EventBus eventBus;
    
    protected void onBind() {
        super.onBind();
        registerHandler(getEventBus().addHandler(PopupMessageEvent.getType(),popupMessageHandler ));
        getView().getOkButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				getView().hide();
			}
		});
    }
    
	@Override
	protected void onHide() {
		super.onHide();
	}
	
	private PopupMessageHandler popupMessageHandler = new PopupMessageHandler() {
		
		@Override
		public void onPopupMessage(PopupMessageEvent event) {
			getView().getLabel().setText(event.getMessage());
			
		}
	};
}