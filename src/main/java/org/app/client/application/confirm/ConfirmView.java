package org.app.client.application.confirm;

import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewImpl;

class ConfirmView extends PopupViewImpl implements ConfirmPresenter.MyView {
    interface Binder extends UiBinder<Widget, ConfirmView> {
    }

    @UiField
    HTMLPanel main;
    @UiField Heading label;
    @UiField Button okButton;
    @UiField Button cancelButton;
    
    

    public Heading getLabel() {
		return label;
	}
	public Button getOkButton() {
		return okButton;
	}
	public Button getCancelButton() {
		return cancelButton;
	}

	@Inject
    ConfirmView(EventBus eventBus, Binder uiBinder) {
        super(eventBus);
    
        initWidget(uiBinder.createAndBindUi(this));
    }
    
}