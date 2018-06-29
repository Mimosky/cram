package org.app.client.application.groups;

import javax.inject.Inject;

import org.app.shared.wrapper.GroupWrapper;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.incubator.PickList;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class GroupsView extends ViewImpl implements GroupsPresenter.MyView {
	interface Binder extends UiBinder<Widget, GroupsView> {
	}

	@UiField
	HTMLPanel main;
	@UiField
	Button showButton;
	@UiField
	Modal modal;
	@UiField
	TextBox groupName;
	@UiField
	HelpInline groupNameHelpInline;
	@UiField
	Button saveButton;
	@UiField
	ListBox managerListBox;
	@UiField
	ControlGroup groupNameControlGroup;
	@UiField
	CellTable<GroupWrapper> table;
	@UiField
	PickList pickList;
	@UiField
	Modal modifyModal;
	@UiField
	TextBox modifyGroupName;
	@UiField
	ListBox modifyManagerListBox;
	@UiField
	PickList modifyPickList;
	@UiField
	Button modifyButton;

	public ListBox getManagerListBox() {
		return managerListBox;
	}
	public ListBox getModifyManagerListBox() {
		return modifyManagerListBox;
	}
	
	public Button getShowButton() {
		return showButton;
	}
	public Modal getModal() {
		return modal;
	}


	public TextBox getGroupName() {
		return groupName;
	}

	public HelpInline getGroupNameHelpInline() {
		return groupNameHelpInline;
	}


	public Button getSaveButton() {
		return saveButton;
	}

	public ControlGroup getGroupNameControlGroup() {
		return groupNameControlGroup;
	}

	public CellTable<GroupWrapper> getTable() {
		return table;
	}


	public PickList getPickList() {
		return pickList;
	}


	public Modal getModifyModal() {
		return modifyModal;
	}

	public TextBox getModifyGroupName() {
		return modifyGroupName;
	}


	public PickList getModifyPickList() {
		return modifyPickList;
	}

	public Button getModifyButton() {
		return modifyButton;
	}


	@Inject
	GroupsView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
}