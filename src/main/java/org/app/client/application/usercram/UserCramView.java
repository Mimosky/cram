package org.app.client.application.usercram;

import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class UserCramView extends ViewImpl implements UserCramPresenter.MyView {

	interface Binder extends UiBinder<Widget, UserCramView> {
	}

	@UiField HTMLPanel calendarPanel;
	@UiField Button saveCram;
	@UiField ListBox usersBox;
	@UiField ListBox clientBox;
	@UiField ListBox monthBox;
	@UiField ListBox yearBox;
	@UiField Button display;
	
	public Button getDisplay() {
		return display;
	}
	public Button getSaveCram() {
		return saveCram;
	}
	
	public ListBox getUsersBox() {
		return usersBox;
	}
	public ListBox getClientBox() {
		return clientBox;
	}
	public ListBox getMonthBox() {
		return monthBox;
	}
	public ListBox getYearBox() {
		return yearBox;
	}
	public HTMLPanel getCalendarPanel() {
		return calendarPanel;
	}

	@Inject
	UserCramView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}



}