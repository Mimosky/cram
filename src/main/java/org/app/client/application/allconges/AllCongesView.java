package org.app.client.application.allconges;

import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class AllCongesView extends ViewImpl implements AllCongesPresenter.MyView {
    interface Binder extends UiBinder<Widget, AllCongesView> {
    }

    @UiField
    HTMLPanel main;
    @UiField
    HTMLPanel calendarPanel;
    @UiField
    Button previousButton;
    @UiField
    Button nextButton;
    @UiField
    ListBox listBox;
    @UiField
    Button display;
    
    
    public Button getDisplay() {
		return display;
	}
    public ListBox getListBox() {
		return listBox;
	}
    public Button getPreviousButton() {
		return previousButton;
	}
    public Button getNextButton() {
		return nextButton;
	}
    public HTMLPanel getCalendarPanel() {
		return calendarPanel;
	}
    
    
    @Inject
    AllCongesView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

}