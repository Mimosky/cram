package org.app.client.application.client;

import javax.inject.Inject;

import org.app.shared.Client;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.datepicker.client.ui.DateBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class ClientView extends ViewImpl implements ClientPresenter.MyView {
	interface Binder extends UiBinder<Widget, ClientView> {
	}

	@UiField
	HTMLPanel main;
	@UiField
	Button showButton;
	@UiField
	Modal modal;
	@UiField
	Button saveButton;
	@UiField
	ControlGroup clientNameControlGroup;
	@UiField
	TextBox clientName;
	@UiField
	HelpInline clientNameHelpInline;
	@UiField
	ControlGroup emailControlGroup;
	@UiField
	TextBox email;
	@UiField
	HelpInline emailHelpInline;
	@UiField
	DateBox dateBox;
	@UiField
	CellTable<Client> table;
	@UiField 
	ControlGroup addressControlGroup;
	@UiField
	TextBox address;
	@UiField
	HelpInline addressHelpInline;
	
	
	public ControlGroup getAddressControlGroup() {
		return addressControlGroup;
	}

	public TextBox getAddress() {
		return address;
	}

	public HelpInline getAddressHelpInline() {
		return addressHelpInline;
	}

	public CellTable<Client> getTable() {
		return table;
	}

	public HTMLPanel getMain() {
		return main;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public Button getShowButton() {
		return showButton;
	}

	public Modal getModal() {
		return modal;
	}

	public ControlGroup getClientNameControlGroup() {
		return clientNameControlGroup;
	}

	public TextBox getClientName() {
		return clientName;
	}

	public HelpInline getClientNameHelpInline() {
		return clientNameHelpInline;
	}

	public ControlGroup getEmailControlGroup() {
		return emailControlGroup;
	}

	public TextBox getEmail() {
		return email;
	}

	public HelpInline getEmailHelpInline() {
		return emailHelpInline;
	}

	public DateBox getDateBox() {
		return dateBox;
	}

	@UiHandler("showButton")
	void onShow(ClickEvent event) {
		modal.show();
		clientName.setText("");
		email.setText("");
		address.setText("");
	}

	@Inject
	ClientView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

}