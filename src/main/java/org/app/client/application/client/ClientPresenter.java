package org.app.client.application.client;

import java.util.List;

import org.app.shared.Client;
import org.app.shared.dispatch.AddClientAction;
import org.app.shared.dispatch.AddClientResult;
import org.app.shared.dispatch.RetrieveClientAction;
import org.app.shared.dispatch.RetrieveClientResult;
import org.app.shared.wrapper.ClientWrapper;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.datepicker.client.ui.DateBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.client.RpcDispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class ClientPresenter extends Presenter<ClientPresenter.MyView, ClientPresenter.MyProxy> {

	interface MyView extends View {
		public HTMLPanel getMain();

		public Button getShowButton();

		public Modal getModal();

		public ControlGroup getClientNameControlGroup();

		public TextBox getClientName();

		public HelpInline getClientNameHelpInline();

		public ControlGroup getEmailControlGroup();

		public TextBox getEmail();

		public HelpInline getEmailHelpInline();

		public DateBox getDateBox();

		public Button getSaveButton();

		public CellTable<Client> getTable();

		public ControlGroup getAddressControlGroup();

		public TextBox getAddress();

		public HelpInline getAddressHelpInline();
	}
	@ProxyStandard
	interface MyProxy extends Proxy<ClientPresenter> {
	}
	
	@Inject
	ClientPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);

	}

	@Inject
	RpcDispatchAsync dispatchAsync;

	private ListDataProvider<Client> dataProvider;

	@Override
	protected void onBind() {
		super.onBind();
		prepareTable();
		addRowToClientTable();
	}

	@Override
	protected void onReset() {
		super.onReset();
		getView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
//				Logger logger = Logger.getLogger("MS");
//				logger.log(Level.SEVERE, "Click handled");
				if (!checkBoxes()) {
					dispatchAsync.execute(
							new AddClientAction(getView().getClientName().getText(), getView().getAddress().getText(),
									getView().getEmail().getText(), getView().getDateBox().getValue().toString()),
							new AsyncCallback<AddClientResult>() {

								@Override
								public void onFailure(Throwable arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess(AddClientResult arg0) {
									getView().getModal().hide();
									addRowToClientTable();

								}
							});
				}
			}
		});
	}

	protected void addRowToClientTable() {

		dispatchAsync.execute(new RetrieveClientAction(), new AsyncCallback<RetrieveClientResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(RetrieveClientResult arg0) {
//				Logger logger = Logger.getLogger("MS");
//				logger.log(Level.SEVERE, "Retrieve Client On success" + arg0.getResult().size());
				List<Client> list = dataProvider.getList();
				list.clear();
				for (ClientWrapper i : arg0.getResult()) {
					Client client = new Client();
					client.setName(i.getName());
					client.setAddress(i.getAddress());
					client.setEmail(i.getEmail());
					client.setDate(i.getDate());
					list.add(client);
				}
				dataProvider.flush();
				dataProvider.refresh();
			}
		});
	}

	public boolean checkBoxes() {
		boolean hasError = false;

		if (getView().getClientName() == null || getView().getClientName().getText().isEmpty()) {
			getView().getClientNameControlGroup().setType(ControlGroupType.ERROR);
			getView().getClientNameHelpInline().setText("Le nom est obligatoire");
			hasError = true;
		} else {
			getView().getClientNameControlGroup().setType(ControlGroupType.NONE);
			getView().getClientNameHelpInline().setText("");
		}
		if (getView().getEmail() == null || getView().getEmail().getText().isEmpty()) {
			getView().getEmailControlGroup().setType(ControlGroupType.ERROR);
			getView().getEmailHelpInline().setText("L'email est obligatoire");
			hasError = true;
		} else {
			getView().getEmailControlGroup().setType(ControlGroupType.NONE);
			getView().getEmailHelpInline().setText("");
		}
		if (getView().getAddress() == null || getView().getAddress().getText().isEmpty()) {
			getView().getAddressControlGroup().setType(ControlGroupType.ERROR);
			getView().getAddressHelpInline().setText("L'adresse est obligatoire");
			hasError = true;
		} else {
			getView().getAddressControlGroup().setType(ControlGroupType.NONE);
			getView().getAddressHelpInline().setText("");
		}
		return hasError;
	}

	protected void prepareTable() {
		getView().getTable().setPageSize(10);
		// Create Name column
		TextColumn<Client> nameColumn = new TextColumn<Client>() {
			@Override
			public String getValue(Client client) {
				return client.getName();
			}
		};

		// Make the name column sortable.
		// typeColumn.setSortable(true);

		// Create the surname column
		TextColumn<Client> addressColumn = new TextColumn<Client>() {
			@Override
			public String getValue(Client client) {
				return client.getAddress();
			}
		};

		// Create the email column
		TextColumn<Client> emailColumn = new TextColumn<Client>() {
			@Override
			public String getValue(Client client) {
				return client.getEmail();
			}
		};
		// Create the start Date column
		TextColumn<Client> dateColumn = new TextColumn<Client>() {
			@Override
			public String getValue(Client client) {
				return client.getFomatedDate();
			}
		};

		// Add the columns.
		getView().getTable().addColumn(nameColumn, "Nom");
		getView().getTable().addColumn(addressColumn, "Adresse");
		getView().getTable().addColumn(emailColumn, "Email");
		getView().getTable().addColumn(dateColumn, "Date");

		// getView().getTable().addColumn(buttonCol);
		dataProvider = new ListDataProvider<Client>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(getView().getTable());

	}

}