package org.app.client.application.showgroupconge;

import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class ShowGroupCongeView extends ViewImpl implements ShowGroupCongePresenter.MyView {
    interface Binder extends UiBinder<Widget, ShowGroupCongeView> {
    }

    @UiField
    HTMLPanel main;
    @UiField
    HTMLPanel calendarPanel;
    @UiField
    Button previousButton;
    @UiField
    Button nextButton;
    @UiField Label message;
    @UiField
    Button display;
    
    public Label getMessage() {
		return message;
	}
    
    public Button getDisplay() {
		return display;
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
    ShowGroupCongeView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

}