package org.app.client.application.groups;

import java.util.ArrayList;
import java.util.List;

import org.app.shared.dispatch.AddGroupAction;
import org.app.shared.dispatch.AddGroupResult;
import org.app.shared.dispatch.ModifyGroupAction;
import org.app.shared.dispatch.ModifyGroupResult;
import org.app.shared.dispatch.RetrieveGroupAction;
import org.app.shared.dispatch.RetrieveGroupResult;
import org.app.shared.dispatch.RetrieveUsersAction;
import org.app.shared.dispatch.RetrieveUsersResult;
import org.app.shared.wrapper.GroupWrapper;
import org.app.shared.wrapper.UserWrapper;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonCell;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.incubator.NameValuePair;
import com.github.gwtbootstrap.client.ui.incubator.NameValuePairImpl;
import com.github.gwtbootstrap.client.ui.incubator.PickList;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.client.RpcDispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class GroupsPresenter extends Presenter<GroupsPresenter.MyView, GroupsPresenter.MyProxy> {
	interface MyView extends View {
		public ListBox getManagerListBox();

		public ListBox getModifyManagerListBox();

		public Button getShowButton();

		public Modal getModal();

		public TextBox getGroupName();

		public HelpInline getGroupNameHelpInline();

		public Button getSaveButton();

		public ControlGroup getGroupNameControlGroup();

		public CellTable<GroupWrapper> getTable();

		public PickList getPickList();

		public Modal getModifyModal();

		public TextBox getModifyGroupName();

		public PickList getModifyPickList();

		public Button getModifyButton();
	}

	// Variables
	private ListDataProvider<GroupWrapper> dataProvider;
	private ArrayList<String> emails;

	@Inject
	RpcDispatchAsync dispatchAsync;

	@ProxyStandard
	interface MyProxy extends Proxy<GroupsPresenter> {
	}

	@Inject
	GroupsPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, RevealType.Root);

	}

	@Override
	protected void onBind() {
		// TODO Auto-generated method stub
		super.onBind();
		prepareTable();
		addRowToTable();
		getView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String str = "";
				for (NameValuePair nV : getView().getPickList().getRightListElements()) {
					str += nV.value() + "--";
				}
				dispatchAsync.execute(new AddGroupAction(getView().getGroupName().getText(),
						getView().getManagerListBox().getValue(), str), new AsyncCallback<AddGroupResult>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(AddGroupResult result) {
								addRowToTable();
								getView().getModal().hide();

							}
						});
			}
		});
		getView().getShowButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				getView().getGroupName().setText("");
				getView().getPickList().clearLeftList();
				getView().getManagerListBox().clear();
				dispatchAsync.execute(new RetrieveUsersAction(), new AsyncCallback<RetrieveUsersResult>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(RetrieveUsersResult result) {
						for (UserWrapper u : result.getResult()) {
							getView().getManagerListBox().addItem(u.getUserName() + " " + u.getUserSurname());
							if (!emails.contains(u.getUserEmail())) {
								
								getView().getPickList().addElementToLeftList(
										new NameValuePairImpl(u.getUserName() + " " + u.getUserSurname(),
												u.getUserName() + " " + u.getUserSurname()));
							}
						}
						getView().getModal().show();

					}
				});

			}
		});

		getView().getModifyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String str = "";
				for (NameValuePair nV : getView().getModifyPickList().getRightListElements()) {
					str += nV.value() + "--";
				}
				dispatchAsync.execute(
						new ModifyGroupAction(getView().getModifyGroupName().getText(),
								getView().getModifyManagerListBox().getValue(), str),
						new AsyncCallback<ModifyGroupResult>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(ModifyGroupResult response) {
								addRowToTable();
								getView().getModifyModal().hide();

							}
						});
			}
		});
	}

	protected void addRowToTable() {
		emails = new ArrayList<String>();
		dispatchAsync.execute(new RetrieveGroupAction("All"), new AsyncCallback<RetrieveGroupResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(RetrieveGroupResult result) {
				List<GroupWrapper> list = dataProvider.getList();
				list.clear();
				for (GroupWrapper g : result.getGroup()) {
					list.add(g);
					for (UserWrapper u : g.getCollaborateurs()) {
						emails.add(u.getUserEmail());
					}
				}
				dataProvider.flush();
				dataProvider.refresh();
			}
		});
	}

	protected void prepareTable() {
		getView().getTable().setPageSize(10);
		// Create Name column
		TextColumn<GroupWrapper> nameColumn = new TextColumn<GroupWrapper>() {
			@Override
			public String getValue(GroupWrapper group) {
				return group.getName();
			}
		};
		nameColumn.setSortable(true);

		TextColumn<GroupWrapper> managerColumn = new TextColumn<GroupWrapper>() {
			@Override
			public String getValue(GroupWrapper group) {
				return group.getManager().getUserName() + " " + group.getManager().getUserSurname();
			}
		};

		// Create the email column
		TextColumn<GroupWrapper> workersColumn = new TextColumn<GroupWrapper>() {
			@Override
			public String getValue(GroupWrapper group) {
				String collaborateurs = "";
				for (UserWrapper u : group.getCollaborateurs()) {
					collaborateurs += u.getUserName() + "  ";
				}
				return collaborateurs;
			}
		};

		ButtonCell buttonCell = new ButtonCell(IconType.EDIT, ButtonType.WARNING);
		Column<GroupWrapper, String> buttonCol = new Column<GroupWrapper, String>(buttonCell) {
			@Override
			public String getValue(GroupWrapper object) {
				return "modifier";
			}
		};
		// Add the columns.
		getView().getTable().addColumn(nameColumn, "Groupe");
		getView().getTable().addColumn(managerColumn, "Manager");
		getView().getTable().addColumn(workersColumn, "Collaborateurs");
		getView().getTable().addColumn(buttonCol);

		dataProvider = new ListDataProvider<GroupWrapper>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(getView().getTable());

		buttonCol.setFieldUpdater(new FieldUpdater<GroupWrapper, String>() {

			@Override
			public void update(int index, GroupWrapper object, String value) {
				GroupWrapper group = dataProvider.getList().get(dataProvider.getList().indexOf(object));
				getView().getModifyModal().show();
				getView().getModifyGroupName().setText(group.getName());
				getView().getModifyGroupName().setReadOnly(true);
				getView().getModifyManagerListBox().clear();

				dispatchAsync.execute(new RetrieveUsersAction(), new AsyncCallback<RetrieveUsersResult>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(RetrieveUsersResult result) {
						getView().getModifyPickList().clearLeftList();
						getView().getModifyPickList().clearRightList();
						ArrayList<String> users = new ArrayList<String>();
						for (UserWrapper u : group.getCollaborateurs()) {
							getView().getModifyPickList().addElementToRightList(
									new NameValuePairImpl(u.getUserName() + " " + u.getUserSurname(),
											u.getUserName() + " " + u.getUserSurname()));
							users.add(u.getUserEmail());
						}
						for (UserWrapper u : result.getResult()) {
							getView().getModifyManagerListBox().addItem(u.getUserName() + " " + u.getUserSurname());
							if (!users.contains(u.getUserEmail())) {
								if (!emails.contains(u.getUserEmail())) {
									getView().getModifyPickList().addElementToLeftList(
											new NameValuePairImpl(u.getUserName() + " " + u.getUserSurname(),
													u.getUserName() + " " + u.getUserSurname()));
								}
							}
						}
						getView().getModifyManagerListBox().setSelectedValue(
								group.getManager().getUserName() + " " + group.getManager().getUserSurname());
					}
				});

			}
		});
	}

}