package org.app.client.application.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.app.client.application.confirm.ConfirmPresenter;
import org.app.client.events.PopupMessageEvent;
import org.app.shared.Client;
import org.app.shared.User;
import org.app.shared.dispatch.AddUser;
import org.app.shared.dispatch.AddUserResult;
import org.app.shared.dispatch.ModifyUserAction;
import org.app.shared.dispatch.ModifyUserResult;
import org.app.shared.dispatch.RetrieveClientAction;
import org.app.shared.dispatch.RetrieveClientResult;
import org.app.shared.dispatch.RetrieveUsersAction;
import org.app.shared.dispatch.RetrieveUsersResult;
import org.app.shared.wrapper.ClientWrapper;
import org.app.shared.wrapper.UserWrapper;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonCell;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.incubator.NameValuePair;
import com.github.gwtbootstrap.client.ui.incubator.NameValuePairImpl;
import com.github.gwtbootstrap.client.ui.incubator.PickList;
import com.github.gwtbootstrap.datepicker.client.ui.DateBox;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.client.RpcDispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class UserPresenter extends Presenter<UserPresenter.MyView, UserPresenter.MyProxy> {
	interface MyView extends View {
		public TextBox getUserName();
		public Button getModifyButton() ;
		public Modal getModal();
		public ListBox getContratListBox() ;
		public ListBox getProfileListBox();
		public HelpInline getUserNameHelpInline();
		public PasswordTextBox getPasswordModify() ;
		public ListBox getProfileListBoxModify() ;
		public ListBox getContratListBoxModify() ;
		public ListBox getStatusListBoxModify();
		public CellTable<User> getTable();
		public TextBox getUserSurname();
		public HelpInline getUserSurnameHelpInline();
		public Button getShowButton();
		public TextBox getEmail();
		public PickList getPickList();
		public HelpInline getEmailHelpInline();
		public PasswordTextBox getPassword();
		public HelpInline getPasswordHelpInline();
		public DateBox getDateBox();
		public Button getSaveButton();
		public ControlGroup getUserNameControlGroup();
		public ControlGroup getUserSurNameControlGroup();
		public ControlGroup getEmailControlGroup();
		public Modal getModifyModal() ;
		public ControlGroup getPasswordGroup();
		public TextBox getUserNameModify() ;
		public TextBox getUserSurnameModify() ;
		public TextBox getEmailModify() ;
		public DateBox getDateBoxModify() ;
		public PickList getPickListModify() ;
		public CellTable<User> getTableDeleted() ;

	}

	private ListDataProvider<User> dataProvider;
	private ListDataProvider<User> deletedUser;

	@Inject
	UserPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);

	}

	@ProxyStandard
	interface MyProxy extends Proxy<UserPresenter> {
	}
	
	@Inject
	RpcDispatchAsync dispatchAsync;
	@Inject
	ConfirmPresenter confirmPresenter;
	private PopupMessageEvent messageEvent;
	@Inject EventBus eventBus;
	
	@Override
	protected void onBind() {
		super.onBind();
		prepareTable();
		prepareTableDeletedUser();
		addRowToUserTable();

	}

	@Override
	protected void onReset() {
		super.onReset();

		getView().getModifyButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				List<NameValuePair> list = getView().getPickListModify().getRightListElements();
				String str ="";
				for (int i = 0 ; i< list.size(); i++){
					str += list.get(i).value()+ "/" ;
				}
				ModifyUserAction action = new ModifyUserAction (getView().getUserNameModify().getText(),
						getView().getUserSurnameModify().getText(), getView().getEmailModify().getText(),
						getView().getPasswordModify().getValue(), getView().getDateBoxModify().getValue().toString(),str,getView().getStatusListBoxModify().getValue(),
						getView().getProfileListBoxModify().getValue(),getView().getContratListBoxModify().getValue());
//				Logger logger = Logger.getLogger("MS");
//				logger.log(Level.SEVERE, "Modify Button Click : " +str);
				dispatchAsync.execute(action, modifyUserCallback);				
			}
		});
		getView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!checkEmptyFields()) {
					List<NameValuePair> list = getView().getPickList().getRightListElements();
					String str ="";
					for (int i = 0 ; i< list.size(); i++){
						str += list.get(i).value()+ "/" ;
					}
					AddUser action = new AddUser(getView().getUserName().getText(),
							getView().getUserSurname().getText(), getView().getEmail().getText(),
							getView().getPassword().getText(), getView().getDateBox().getValue().toString(),str,getView().getProfileListBox().getValue(),getView().getContratListBox().getValue());
		
					dispatchAsync.execute(action, addUserCallback);
				}
			}
		});

		getView().getShowButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				getView().getModal().show();
				getView().getDateBox().setValue(new Date());
				getView().getEmail().setText("");
				getView().getUserName().setText("");
				getView().getUserSurname().setText("");
				getView().getPassword().setText("");
				dispatchAsync.execute(new RetrieveClientAction(), new AsyncCallback<RetrieveClientResult>() {

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(RetrieveClientResult arg0) {
						
//						Logger logger = Logger.getLogger("MS");
//						logger.log(Level.SEVERE, "Retrive clients on User Modal : " + arg0.getResult().size());
						getView().getPickList().clearLeftList();
						getView().getPickList().clearRightList();
						for (ClientWrapper i : arg0.getResult()) {
							getView().getPickList()
									.addElementToLeftList((new NameValuePairImpl(i.getName(), i.getName())));
						}
					}
				});
			}
		});
	}

	protected void addRowToUserTable() {

		dispatchAsync.execute(new RetrieveUsersAction(), new AsyncCallback<RetrieveUsersResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(RetrieveUsersResult arg0) {
//				Logger logger = Logger.getLogger("MS");
//				logger.log(Level.SEVERE, "Retrieve User On success : " + arg0.getResult().size());
				List<User> list = dataProvider.getList();
				List<User> deletedUserList = deletedUser.getList();
				deletedUserList.clear();
				list.clear();
				for (UserWrapper i : arg0.getResult()) {
					User user = new User();
					user.setName(i.getUserName());
					user.setSurname(i.getUserSurname());
					user.setEmail(i.getUserEmail());
					user.setEntryDate(i.getUserEntryDate());
					user.setProfile(i.getProfile());
					user.setStatus(i.getStatus());
					//logger.log(Level.SEVERE, "On Retrieve after client creation: Status = " + user.getStatus());
					user.setTypeDeContrat(i.getTypeDeContrat());
					List<Client> clients = new ArrayList<Client>();
					for (ClientWrapper j : i.getClients()){
						clients.add(new Client(j.getName(), j.getAddress(), j.getEmail(), j.getDate()));
					}
					user.setClient(clients);
					if(user.getStatus().equals("enabled")){
						list.add(user);
					}else{
						deletedUserList.add(user);
					}
				}
				dataProvider.flush();
				dataProvider.refresh();
				deletedUser.flush();
				deletedUser.refresh();
				
			}
		});
	}

	
	
	protected void prepareTable() {
		getView().getTable().setPageSize(10);
		// Create Name column
		TextColumn<User> nameColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getName();
			}
		};

		// Make the name column sortable.
		// typeColumn.setSortable(true);

		// Creathttp://127.0.0.1:8888/index.html#e the surname column
		TextColumn<User> surNameColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getSurname();
			}
		};

		// Create the email column
		TextColumn<User> emailColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getEmail();
			}
		};
		// Create the start Date column
		TextColumn<User> entreeColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getFomatedDate();
			}
		};

		ButtonCell buttonCell = new ButtonCell(IconType.EDIT, ButtonType.WARNING);
		Column<User, String> buttonCol = new Column<User, String>(buttonCell) {
			@Override
			public String getValue(User object) {
				return "modifier";
			}
		};
		// Add     the columns.
		getView().getTable().addColumn(nameColumn, "Nom");
		getView().getTable().addColumn(surNameColumn, "Prénom");
		getView().getTable().addColumn(emailColumn, "Email");
		getView().getTable().addColumn(entreeColumn, "Date d'entrée");
		getView().getTable().addColumn(buttonCol);

		dataProvider = new ListDataProvider<User>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(getView().getTable());
		
		
		
		
		buttonCol.setFieldUpdater(new FieldUpdater<User, String>() {

			@Override
			public void update(int index, User object, String value) {
				User user = dataProvider.getList().get(dataProvider.getList().indexOf(object));
				getView().getModifyModal().show();
				getView().getDateBoxModify().setValue(user.getEntryDate());
				getView().getEmailModify().setText(user.getEmail());
				getView().getEmailModify().setReadOnly(true);
				getView().getUserNameModify().setText(user.getName());
				getView().getUserSurnameModify().setText(user.getSurname());
				getView().getPasswordModify().setText("");
//				Logger logger = Logger.getLogger("MS");
//				logger.log(Level.SEVERE, "On Modify Modal : Name = " + user.getName());
//				logger.log(Level.SEVERE, "On Modify Modal : Profile = " + user.getProfile());
//				logger.log(Level.SEVERE, "On Modify Modal : Status = " + user.getStatus());
//				logger.log(Level.SEVERE, "On Modify Modal : TypeDeContrat = " + user.getTypeDeContrat());
				getView().getProfileListBoxModify().setSelectedValue(user.getProfile());
				getView().getContratListBoxModify().setSelectedValue(user.getTypeDeContrat());
				getView().getStatusListBoxModify().setSelectedValue(user.getStatus());
				dispatchAsync.execute(new RetrieveClientAction(), new AsyncCallback<RetrieveClientResult>() {

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(RetrieveClientResult arg0) {
						getView().getPickListModify().clearLeftList();
						getView().getPickListModify().clearRightList();
						List<ClientWrapper> list = arg0.getResult();
						String str = "";
						
						for(Client i : user.getClient()){
							getView().getPickListModify().addElementToRightList(new NameValuePairImpl(i.getName(),i.getName()));
							str += i.getName();
						}
						
						for (ClientWrapper i :  list) {
							if(! str.contains(i.getName()))
							getView().getPickListModify()
									.addElementToLeftList((new NameValuePairImpl(i.getName(), i.getName())));
						}
					}
				});

			}
		});
	}
	
	
	
	protected void prepareTableDeletedUser() {
		getView().getTableDeleted().setPageSize(10);
		// Create Name column
		TextColumn<User> nameColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getName();
			}
		};

		// Make the name column sortable.
		// typeColumn.setSortable(true);

		// Creathttp://127.0.0.1:8888/index.html#e the surname column
		TextColumn<User> surNameColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getSurname();
			}
		};

		// Create the email column
		TextColumn<User> emailColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getEmail();
			}
		};
		// Create the start Date column
		TextColumn<User> entreeColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getFomatedDate();
			}
		};

		ButtonCell buttonCell = new ButtonCell(IconType.EDIT, ButtonType.WARNING);
		Column<User, String> buttonCol = new Column<User, String>(buttonCell) {
			@Override
			public String getValue(User object) {
				return "modifier";
			}
		};
		// Add     the columns.
		getView().getTableDeleted().addColumn(nameColumn, "Nom");
		getView().getTableDeleted().addColumn(surNameColumn, "Prénom");
		getView().getTableDeleted().addColumn(emailColumn, "Email");
		getView().getTableDeleted().addColumn(entreeColumn, "Date d'entrée");
		getView().getTableDeleted().addColumn(buttonCol);

		deletedUser = new ListDataProvider<User>();

		// Connect the table to the data provider.
		deletedUser.addDataDisplay(getView().getTableDeleted());
		
		
		
		
		buttonCol.setFieldUpdater(new FieldUpdater<User, String>() {

			@Override
			public void update(int index, User object, String value) {
				User user = deletedUser.getList().get(deletedUser.getList().indexOf(object));
				getView().getModifyModal().show();
				getView().getDateBoxModify().setValue(user.getEntryDate());
				getView().getEmailModify().setText(user.getEmail());
				getView().getEmailModify().setReadOnly(true);
				getView().getUserNameModify().setText(user.getName());
				getView().getUserSurnameModify().setText(user.getSurname());
				getView().getPasswordModify().setText("");
//				Logger logger = Logger.getLogger("MS");
//				logger.log(Level.SEVERE, "On Modify Modal : Name = " + user.getName());
//				logger.log(Level.SEVERE, "On Modify Modal : Profile = " + user.getProfile());
//				logger.log(Level.SEVERE, "On Modify Modal : Status = " + user.getStatus());
//				logger.log(Level.SEVERE, "On Modify Modal : TypeDeContrat = " + user.getTypeDeContrat());
				getView().getProfileListBoxModify().setSelectedValue(user.getProfile());
				getView().getContratListBoxModify().setSelectedValue(user.getTypeDeContrat());
				getView().getStatusListBoxModify().setSelectedValue(user.getStatus());
				dispatchAsync.execute(new RetrieveClientAction(), new AsyncCallback<RetrieveClientResult>() {

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(RetrieveClientResult arg0) {
						getView().getPickListModify().clearLeftList();
						getView().getPickListModify().clearRightList();
						List<ClientWrapper> list = arg0.getResult();
						String str = "";
						
						for(Client i : user.getClient()){
							getView().getPickListModify().addElementToRightList(new NameValuePairImpl(i.getName(),i.getName()));
							str += i.getName();
						}
						
						for (ClientWrapper i :  list) {
							if(! str.contains(i.getName()))
							getView().getPickListModify()
									.addElementToLeftList((new NameValuePairImpl(i.getName(), i.getName())));
						}
					}
				});

			}
		});
	}
	
	
	
	

	public boolean checkEmptyFields() {
		boolean hasError = false;

		if (getView().getUserName() == null || getView().getUserName().getText().isEmpty()) {
			getView().getUserNameControlGroup().setType(ControlGroupType.ERROR);
			getView().getUserNameHelpInline().setText("Le nom est obligatoire");
			hasError = true;
		} else {
			getView().getUserNameControlGroup().setType(ControlGroupType.NONE);
			getView().getUserNameHelpInline().setText("");
		}
		if (getView().getUserSurname() == null || getView().getUserSurname().getText().isEmpty()) {
			getView().getUserSurNameControlGroup().setType(ControlGroupType.ERROR);
			getView().getUserSurnameHelpInline().setText("Le prénom est obligatoire");
			hasError = true;
		} else {
			getView().getUserSurNameControlGroup().setType(ControlGroupType.NONE);
			getView().getUserSurnameHelpInline().setText("");
		}
		if (getView().getPassword() == null || getView().getPassword().getText().length() < 8) {
			getView().getPasswordGroup().setType(ControlGroupType.ERROR);
			getView().getPasswordHelpInline().setText("Le mot de passe trop court, il doit contenir min 8 caractères");
			hasError = true;
		} else {
			getView().getPasswordGroup().setType(ControlGroupType.NONE);
			getView().getPasswordHelpInline().setText("");
		}
		if (getView().getEmail() == null || getView().getEmail().getText().isEmpty()) {
			getView().getEmailControlGroup().setType(ControlGroupType.ERROR);
			getView().getEmailHelpInline().setText("L'email est obligatoire");
			hasError = true;
		} else {
			getView().getEmailControlGroup().setType(ControlGroupType.NONE);
			getView().getEmailHelpInline().setText("");
		}
		return hasError;
	}

	AsyncCallback<AddUserResult> addUserCallback = new AsyncCallback<AddUserResult>() {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Error while creating user");

		}

		@Override
		public void onSuccess(AddUserResult result) {
			getView().getModal().hide();
			addRowToUserTable();
		}
	};
	
	AsyncCallback<ModifyUserResult> modifyUserCallback = new AsyncCallback<ModifyUserResult>() {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Error while Modifing user");

		}

		@Override
		public void onSuccess(ModifyUserResult result) {
			getView().getModifyModal().hide();
			addRowToUserTable();
		}
	};

}