package org.app.client.application.user;

import javax.inject.Inject;

import org.app.shared.User;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.incubator.PickList;
import com.github.gwtbootstrap.datepicker.client.ui.DateBox;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class UserView extends ViewImpl implements UserPresenter.MyView {
	interface Binder extends UiBinder<Widget, UserView> {
	}

	@UiField
	Modal modifyModal;

	@UiField
	TextBox userNameModify;
	@UiField
	TextBox userSurnameModify;
	@UiField
	TextBox emailModify;
	@UiField
	DateBox dateBoxModify;
	@UiField
	PickList pickListModify;
	@UiField
	Button modifyButton;
	@UiField
	ListBox contratListBox;
	@UiField
	ListBox profileListBox;
	@UiField
	PasswordTextBox passwordModify;
	@UiField
	ListBox profileListBoxModify;
	@UiField
	ListBox contratListBoxModify;
	@UiField
	ListBox statusListBoxModify;

	public PasswordTextBox getPasswordModify() {
		return passwordModify;
	}

	public ListBox getProfileListBoxModify() {
		return profileListBoxModify;
	}

	public ListBox getContratListBoxModify() {
		return contratListBoxModify;
	}

	public ListBox getStatusListBoxModify() {
		return statusListBoxModify;
	}

	public ListBox getContratListBox() {
		return contratListBox;
	}

	public ListBox getProfileListBox() {
		return profileListBox;
	}

	public Button getModifyButton() {
		return modifyButton;
	}

	public TextBox getUserNameModify() {
		return userNameModify;
	}

	public TextBox getUserSurnameModify() {
		return userSurnameModify;
	}

	public TextBox getEmailModify() {
		return emailModify;
	}

	public DateBox getDateBoxModify() {
		return dateBoxModify;
	}

	public PickList getPickListModify() {
		return pickListModify;
	}

	public Modal getModifyModal() {
		return modifyModal;
	}

	@UiField
	HTMLPanel main;
	@UiField
	Button showButton;
	@UiField
	Modal modal;
	@UiField
	TextBox userName;
	@UiField
	HelpInline userNameHelpInline;
	@UiField
	TextBox userSurname;
	@UiField
	HelpInline userSurnameHelpInline;
	@UiField
	TextBox email;
	@UiField
	HelpInline emailHelpInline;
	@UiField
	PasswordTextBox password;
	@UiField
	HelpInline passwordHelpInline;
	@UiField
	DateBox dateBox;
	@UiField
	Button saveButton;
	@UiField
	ControlGroup userNameControlGroup;
	@UiField
	ControlGroup userSurNameControlGroup;
	@UiField
	ControlGroup emailControlGroup;
	@UiField
	ControlGroup passwordGroup;
	@UiField
	CellTable<User> table;
	@UiField
	CellTable<User> tableDeleted;
	@UiField
	PickList pickList;

	public CellTable<User> getTableDeleted() {
		return tableDeleted;
	}
	public Button getShowButton() {
		return showButton;
	}

	public PickList getPickList() {
		return pickList;
	}

	public CellTable<User> getTable() {
		return table;
	}

	public ControlGroup getPasswordGroup() {
		return passwordGroup;
	}

	public ControlGroup getEmailControlGroup() {
		return emailControlGroup;
	}

	public ControlGroup getUserSurNameControlGroup() {
		return userSurNameControlGroup;
	}

	public ControlGroup getUserNameControlGroup() {
		return userNameControlGroup;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public TextBox getUserName() {
		return userName;
	}

	public HelpInline getUserNameHelpInline() {
		return userNameHelpInline;
	}

	public TextBox getUserSurname() {
		return userSurname;
	}

	public HelpInline getUserSurnameHelpInline() {
		return userSurnameHelpInline;
	}

	public TextBox getEmail() {
		return email;
	}

	public HelpInline getEmailHelpInline() {
		return emailHelpInline;
	}

	public PasswordTextBox getPassword() {
		return password;
	}

	public HelpInline getPasswordHelpInline() {
		return passwordHelpInline;
	}

	public DateBox getDateBox() {
		return dateBox;
	}

	public void setDateBox(DateBox dateBox) {
		this.dateBox = dateBox;
	}

	public Modal getModal() {
		return modal;
	}

	// @UiHandler("showButton")
	// void onShow(ClickEvent event) {
	// modal.show();
	// dateBox.setValue(new Date());
	// email.setText("");
	// userName.setText("");
	// userSurname.setText("");
	// password.setText("");
	//
	// }

	@Inject
	UserView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

}