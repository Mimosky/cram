package org.app.client.application.addwork;

import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewImpl;

class AddWorkView extends PopupViewImpl implements AddWorkPresenter.MyView {
    interface Binder extends UiBinder<Widget, AddWorkView> {
    }

    @UiField HTMLPanel main;
    @UiField Button okButton;

    @UiField ListBox listBox;
    public ListBox getListBox() {
		return listBox;
	}

	public Button getOkButton() {
		return okButton;
	}
    

    @Inject
    AddWorkView(EventBus eventBus, Binder uiBinder) {
        super(eventBus);
    
        initWidget(uiBinder.createAndBindUi(this));
    }
    
}