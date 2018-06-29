package org.app.client.application.addonedaywork;

import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewImpl;

class AddOneDayWorkView extends PopupViewImpl implements AddOneDayWorkPresenter.MyView {
    interface Binder extends UiBinder<Widget, AddOneDayWorkView> {
    }

    
    @UiField HTMLPanel main;
    @UiField Button okButton;
    @UiField ListBox clientListBox;
    @UiField ListBox listBox;
    
    public ListBox getListBox() {
		return listBox;
	}

    
public ListBox getClientListBox() {
	return clientListBox;
}


	public Button getOkButton() {
		return okButton;
	}
    
    
    @Inject
    AddOneDayWorkView(EventBus eventBus, Binder uiBinder) {
        super(eventBus);
    
        initWidget(uiBinder.createAndBindUi(this));
    }
    
}