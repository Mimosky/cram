package org.app.client.application.congesvalidator;

import org.app.client.application.confirmconges.ConfirmCongesPresenter;
import org.app.client.application.toolbox.CurrentUser;
import org.app.client.application.validconge.ValidCongePresenter;
import org.app.client.events.PopupMessageEvent;
import org.app.client.events.ValidCongesEvent;
import org.app.client.events.ValidCongesEvent.ValidCongesHandler;
import org.app.shared.dispatch.ModifyCongesAction;
import org.app.shared.dispatch.ModifyCongesResult;
import org.app.shared.dispatch.RetrieveCongesAction;
import org.app.shared.dispatch.RetrieveCongesResult;
import org.app.shared.dispatch.SendEmailAction;
import org.app.shared.dispatch.SendEmailResult;
import org.app.shared.wrapper.CongesWrapper;

import com.github.gwtbootstrap.client.ui.ButtonCell;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.cell.client.FieldUpdater;
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

public class CongesValidatorPresenter
		extends Presenter<CongesValidatorPresenter.MyView, CongesValidatorPresenter.MyProxy> {
	interface MyView extends View {
		public CellTable<CongesWrapper> getCongesTable();
	}

	private ListDataProvider<CongesWrapper> dataProvider;
	private int congesId = 1;
	private String start = "";
	private String end = "";
	//private CurrentUser currentUser;
	@ProxyStandard
	interface MyProxy extends Proxy<CongesValidatorPresenter> {
	}

	@Inject
	RpcDispatchAsync dispatchAsync;
	@Inject
	EventBus eventBus;
	@Inject
	ConfirmCongesPresenter confirmCongesPresenter;
	@Inject 
	ValidCongePresenter validCongesPresenter;

	@Inject CurrentUser currentUser;
	
	@Inject
	CongesValidatorPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);

	}

	private boolean validate = true;

	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getEventBus().addHandler(ValidCongesEvent.getType(), congeConfirmHandler));
		prepareConges();
		addCongesToTable();

	}

	@Override
	protected void onReset() {
		super.onReset();
		addCongesToTable();

	}

	protected void prepareConges() {
		getView().getCongesTable().setPageSize(10);

		// Create name column.
		TextColumn<CongesWrapper> nameColumn = new TextColumn<CongesWrapper>() {
			@Override
			public String getValue(CongesWrapper conges) {
				return conges.getName();
			}
		};
		nameColumn.setSortable(true);
		// Create name column.
		TextColumn<CongesWrapper> typeColumn = new TextColumn<CongesWrapper>() {
			@Override
			public String getValue(CongesWrapper conges) {
				return conges.getType();
			}
		};

		// Make the name column sortable.
		typeColumn.setSortable(true);

		// Create Start Date column.
		TextColumn<CongesWrapper> startColumn = new TextColumn<CongesWrapper>() {
			@Override
			public String getValue(CongesWrapper conges) {
				return conges.getStart();
			}
		};
		

		TextColumn<CongesWrapper> startDayColumn = new TextColumn<CongesWrapper>() {
			@Override
			public String getValue(CongesWrapper conges) {
				return conges.getStartD();
			}
		};

		// Create the end Date column
		TextColumn<CongesWrapper> endColumn = new TextColumn<CongesWrapper>() {
			@Override
			public String getValue(CongesWrapper conges) {
				return conges.getEnd();
			}
		};

		TextColumn<CongesWrapper> endDayColumn = new TextColumn<CongesWrapper>() {
			@Override
			public String getValue(CongesWrapper conges) {
				return conges.getEndD();
			}
		};
		// Create the status column
		TextColumn<CongesWrapper> statusColumn = new TextColumn<CongesWrapper>() {
			@Override
			public String getValue(CongesWrapper conges) {
				return conges.getStatus();
			}
		};

		ButtonCell buttonCell = new ButtonCell(IconType.SAVE, ButtonType.SUCCESS);
		Column<CongesWrapper, String> buttonCol = new Column<CongesWrapper, String>(buttonCell) {
			@Override
			public String getValue(CongesWrapper object) {
				return "Valider";
			}
		};

		ButtonCell buttonRefCell = new ButtonCell(IconType.SAVE, ButtonType.WARNING);
		Column<CongesWrapper, String> buttonRefCol = new Column<CongesWrapper, String>(buttonRefCell) {
			@Override
			public String getValue(CongesWrapper object) {
				return "Refuser";
			}
		};
		// Add the columns.
		getView().getCongesTable().addColumn(nameColumn, "Nom");
		getView().getCongesTable().addColumn(typeColumn, "Type");
		getView().getCongesTable().addColumn(startColumn, "Date de debut");
		getView().getCongesTable().addColumn(startDayColumn, "Debut");
		getView().getCongesTable().addColumn(endColumn, "Date de fin");
		getView().getCongesTable().addColumn(endDayColumn, "Fin");
		getView().getCongesTable().addColumn(statusColumn, "Statut");
		getView().getCongesTable().addColumn(buttonCol);
		getView().getCongesTable().addColumn(buttonRefCol);
		dataProvider = new ListDataProvider<CongesWrapper>();
		dataProvider.addDataDisplay(getView().getCongesTable());

		buttonCol.setFieldUpdater(new FieldUpdater<CongesWrapper, String>() {

			@Override
			public void update(int index, CongesWrapper object, String value) {
				CongesWrapper conges = dataProvider.getList().get(dataProvider.getList().indexOf(object));
				PopupMessageEvent messageEvent = new PopupMessageEvent("Voulez vous valider le congé ?");
				eventBus.fireEvent(messageEvent);
				congesId = conges.getCongesId();
				start = conges.getStart();
				end = conges.getEnd();
				validate = true;
				addToPopupSlot(validCongesPresenter);

			}
		});

		buttonRefCol.setFieldUpdater(new FieldUpdater<CongesWrapper, String>() {

			@Override
			public void update(int index, CongesWrapper object, String value) {
				CongesWrapper conges = dataProvider.getList().get(dataProvider.getList().indexOf(object));
				PopupMessageEvent messageEvent = new PopupMessageEvent("Voulez vous refuser le congé ?");
				eventBus.fireEvent(messageEvent);
				congesId = conges.getCongesId();
				start = conges.getStart();
				end = conges.getEnd();
				validate = false;
				addToPopupSlot(validCongesPresenter);

			}
		});
	}

	protected void addCongesToTable() {
		dispatchAsync.execute(new RetrieveCongesAction("getAll"), new AsyncCallback<RetrieveCongesResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				Window.alert("Error");

			}

			@Override
			public void onSuccess(RetrieveCongesResult result) {
//				Logger logger = Logger.getLogger("MS");
//				logger.log(Level.SEVERE, "The Conges dates" + result.getResult().size());
				dataProvider.getList().clear();
				for (CongesWrapper c : result.getResult()) {
					dataProvider.getList().add(new CongesWrapper(c.getCongesId(), c.getType(), c.getStartDate(),
							c.getEndDate(), c.getStartD(), c.getEndD(), c.getStatus(), c.getName()));
				}
			}
		});
	}

	private ValidCongesHandler congeConfirmHandler = new ValidCongesHandler() {

		@Override
		public void onValidConges(ValidCongesEvent event) {
			if (validate) {
				if (event.getMessage().equals("ok")) {
					dispatchAsync.execute(new ModifyCongesAction(congesId, "Validé"),
							new AsyncCallback<ModifyCongesResult>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess(ModifyCongesResult result) {
									addCongesToTable();
									dispatchAsync.execute(new SendEmailAction("", "", "", congesId,start, end,currentUser.getName()),
											new AsyncCallback<SendEmailResult>() {

												@Override
												public void onFailure(Throwable caught) {
													// TODO Auto-generated
													// method stub

												}

												@Override
												public void onSuccess(SendEmailResult result) {
													// TODO Auto-generated
													// method stub

												}
											});
								}
							});
				}
			} else {
				if (event.getMessage().equals("ok")) {
					dispatchAsync.execute(new ModifyCongesAction(congesId, "Refusé"),
							new AsyncCallback<ModifyCongesResult>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess(ModifyCongesResult result) {
									addCongesToTable();
									dispatchAsync.execute(new SendEmailAction("", "", "", congesId, start,end, currentUser.getName()),
											new AsyncCallback<SendEmailResult>() {

												@Override
												public void onFailure(Throwable caught) {
													// TODO Auto-generated
													// method stub

												}

												@Override
												public void onSuccess(SendEmailResult result) {
													// TODO Auto-generated
													// method stub

												}
											});
								}
							});
				}

			}

		}
	};

}