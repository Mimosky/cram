package org.app.client.application.conges;

import java.util.Date;
import java.util.logging.Logger;

import org.app.client.application.confirmconges.ConfirmCongesPresenter;
import org.app.client.application.notify.NotifyPresenter;
import org.app.client.application.toolbox.CurrentUser;
import org.app.client.events.CongesConfirmEvent;
import org.app.client.events.CongesConfirmEvent.CongesConfirmHandler;
import org.app.client.events.PopupMessageEvent;
import org.app.shared.Conges;
import org.app.shared.dispatch.ModifyCongesAction;
import org.app.shared.dispatch.ModifyCongesResult;
import org.app.shared.dispatch.RetrieveCongesAction;
import org.app.shared.dispatch.RetrieveCongesResult;
import org.app.shared.dispatch.SaveCongesAction;
import org.app.shared.dispatch.SaveCongesResult;
import org.app.shared.dispatch.SendEmailAction;
import org.app.shared.dispatch.SendEmailResult;
import org.app.shared.wrapper.CongesWrapper;

import com.github.gwtbootstrap.client.ui.ButtonCell;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.RadioButton;
import com.github.gwtbootstrap.client.ui.SubmitButton;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.datepicker.client.ui.DateBox;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.client.RpcDispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class CongesPresenter extends Presenter<CongesPresenter.MyView, CongesPresenter.MyProxy> {
	interface MyView extends View {
		public CellTable<Conges> getCongesTable();
		public RadioButton getStartMRadio();
		public RadioButton getStartARadio();
		public RadioButton getEndMRadio();
		public RadioButton getEndARadio();
		public ListBox getListBox();
		public ControlGroup getCongesStartControlGroup();
		public DateBox getCongesStart();
		public HelpInline getCongesStartHelpInline();
		public ControlGroup getCongesEndControlGroup();
		public DateBox getCongesEnd();
		public HelpInline getCongesEndHelpInline();
		public SubmitButton getSaveButton();
		public ControlGroup getRadioEndControlGroup();
		public HelpInline getRadioEndHelpInline();
		public Label getCpLabel() ;
		public Label getSoldeLabel() ;
		public Label getpCpLabel() ;
		public Label getPSoldeLabel() ;
		public Label getRttPris() ;
		public Label getRttSolde() ;
		public void setRttPris(Label rttPris) ;
		public void setRttSolde(Label rttSolde) ;
		public Label getConges() ;
		public void setConges(Label conges) ;
		public Label getCongesPris() ;
		public void setCongesPris(Label congesPris);
		public Label getN_1Label() ;
		public Label getnLabel() ;
		
	}

	@ProxyStandard
	interface MyProxy extends Proxy<CongesPresenter> {
	}

	@Inject
	CongesPresenter(EventBus eventBus, MyView view, MyProxy proxy, CurrentUser currentUser) {
		super(eventBus, view, proxy);
		this.currentUser = currentUser;

	}

	private CurrentUser currentUser;
	private ListDataProvider<Conges> dataProvider;
	private Conges conges = null;
	private Logger logger;
	@Inject
	RpcDispatchAsync dispatchAsync;
	@Inject
	EventBus eventBus;
	@Inject
	ConfirmCongesPresenter confirmCongesPresenter;
	@Inject
	NotifyPresenter notifyPresenter;

	@Override
	protected void onBind() {
		super.onBind();
		logger = Logger.getLogger("");
		
		registerHandler(getEventBus().addHandler(CongesConfirmEvent.getType(), congeConfirmHandler));
		prepareConges();
		precheckConges();
		initLabels();
		addConges();
		updateDate();
	}
	
	protected void initLabels(){
		Date nDate = new Date();
		Date refDate = new Date();
		refDate.setMonth(4);
		refDate.setDate(31);
		if(nDate.after(refDate)){
			getView().getN_1Label().setText((nDate.getYear()-102)+"/"+(nDate.getYear()-101));
			getView().getnLabel().setText((nDate.getYear()-101)+"/"+(nDate.getYear()-100));
		}
	}

	@Override
	protected void onReset() {
		// TODO Auto-generated method stub
		super.onReset();
		addCongesToTable();
	}

	protected void prepareConges() {
		getView().getCongesTable().setPageSize(10);
		// Create name column.
		TextColumn<Conges> typeColumn = new TextColumn<Conges>() {
			@Override
			public String getValue(Conges conges) {
				return conges.getType();
			}
		};

		// Make the name column sortable.
		typeColumn.setSortable(true);

		// Create Start Date column.
		TextColumn<Conges> startColumn = new TextColumn<Conges>() {
			@Override
			public String getValue(Conges conges) {
				return conges.getStart();
			}
		};

		TextColumn<Conges> startDayColumn = new TextColumn<Conges>() {
			@Override
			public String getValue(Conges conges) {
				return conges.getStartD();
			}
		};
		// Create the end Date column
		TextColumn<Conges> endColumn = new TextColumn<Conges>() {
			@Override
			public String getValue(Conges conges) {
				return conges.getEnd();
			}
		};

		TextColumn<Conges> endDayColumn = new TextColumn<Conges>() {
			@Override
			public String getValue(Conges conges) {
				return conges.getEndD();
			}
		};
		// Create the status column
		TextColumn<Conges> statusColumn = new TextColumn<Conges>() {
			@Override
			public String getValue(Conges conges) {
				return conges.getStatus();
			}
		};

		ButtonCell buttonCell = new ButtonCell(IconType.REMOVE, ButtonType.DANGER);
		Column<Conges, String> buttonCol = new Column<Conges, String>(buttonCell) {
			@Override
			public String getValue(Conges object) {
				return "supprimer";
			}
		};
		buttonCol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		// Add the columns.
		getView().getCongesTable().addColumn(typeColumn, "Type");
		getView().getCongesTable().addColumn(startColumn, "Date de debut");
		getView().getCongesTable().addColumn(startDayColumn, "Debut");
		getView().getCongesTable().addColumn(endColumn, "Date de fin");
		getView().getCongesTable().addColumn(endDayColumn, "Fin");
		getView().getCongesTable().addColumn(statusColumn, "Statut");
		getView().getCongesTable().addColumn(buttonCol);

		dataProvider = new ListDataProvider<Conges>();
		dataProvider.addDataDisplay(getView().getCongesTable());

		buttonCol.setFieldUpdater(new FieldUpdater<Conges, String>() {

			@Override
			public void update(int index, Conges object, String value) {
				conges = dataProvider.getList().get(dataProvider.getList().indexOf(object));
				PopupMessageEvent messageEvent = new PopupMessageEvent("Voulez vous supprimer le congé ?");
				eventBus.fireEvent(messageEvent);
				addToPopupSlot(confirmCongesPresenter);
			}
		});
	}

	public void addCongesToTable() {
		dispatchAsync.execute(new RetrieveCongesAction(currentUser.getLogin()),
				new AsyncCallback<RetrieveCongesResult>() {

					@Override
					public void onFailure(Throwable arg0) {
						Window.alert("Error");

					}

					@Override
					public void onSuccess(RetrieveCongesResult result) {
						getView().getCpLabel().setText("\t"+result.getCpPris()+"");
						getView().getSoldeLabel().setText("-");//result.getCpSolde()+"");
						getView().getpCpLabel().setText(result.getPreviousCpPris()+"");
						getView().getPSoldeLabel().setText("-");//result.getPreviousCpSolde()+"");
						getView().getRttPris().setText(result.getRttPris()+"");
						getView().getRttSolde().setText("-");//result.getRttSolde()+"");
						getView().getCongesPris().setText(result.getCongesPris()+"");
						getView().getConges().setText("-");//result.getCongesSolde()+"");
						
//						getView().getSoldeLabel().setText(result.getRtt()+"");
//						getView().getpCpLabel().setText(result.getPreviousCp()+"");
//						getView().getPSoldeLabel().setText(result.getPreviousRtt()+"");
						dataProvider.getList().clear();
						for (CongesWrapper c : result.getResult()) {
							dataProvider.getList().add(new Conges(c.getCongesId(), c.getType(), c.getStartDate(),
									c.getEndDate(), c.getStartD(), c.getEndD(), c.getStatus()));
						}
					}
				});
	}

	
	
	
	
	protected void precheckConges() {
		getView().getStartMRadio().setValue(true);
		getView().getEndARadio().setValue(true);
	}

	protected boolean checkDateConges() {
		if (getView().getCongesEnd().getValue().compareTo(getView().getCongesStart().getValue()) < 0) {
			getView().getCongesEndControlGroup().setType(ControlGroupType.ERROR);
			getView().getCongesEndHelpInline()
					.setText("Veuillez sélectionner une date de fin supèrieur à la date de début");
			return false;
		} else {
			getView().getCongesEndControlGroup().setType(ControlGroupType.NONE);
			getView().getCongesEndHelpInline().setText("");
			return true;
		}
	}

	private String startDay = "";
	private String endDay = "";

	protected void addConges() {
		getView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// String startDay = "";
				if (getView().getStartMRadio().getValue())
					startDay = "Matin";
				else
					startDay = "Après-midi";
				// String endDay = "";
				if (getView().getEndMRadio().getValue())
					endDay = "Matin";
				else
					endDay = "Après-midi";
				if (checkDateConges() && CheckCongesInList(getView().getCongesStart().getValue(),
						getView().getCongesEnd().getValue(), startDay, endDay)) {
					dispatchAsync.execute(new SaveCongesAction(currentUser.getLogin(),
							new CongesWrapper(getView().getListBox().getValue(), getView().getCongesStart().getValue(),
									getView().getCongesEnd().getValue(), startDay, endDay)),
							new AsyncCallback<SaveCongesResult>() {

								@Override
								public void onFailure(Throwable arg0) {

								}

								@SuppressWarnings("deprecation")
								@Override
								public void onSuccess(SaveCongesResult arg0) {
									addCongesToTable();

									String message = "<html>\n" + "<body>\n" + "<br />"
											+ "Bonjour,<br />Vous avez posé un nouveau congé: <br /> \t" + "Type   : "
											+ getView().getListBox().getValue() + "<br />\t" + "Début  : "
											+ (getView().getCongesStart().getValue().getYear() + 1900) + "/"
											+ (getView().getCongesStart().getValue().getMonth() + 1) + "/"
											+ getView().getCongesStart().getValue().getDate() + " " + startDay
											+ "<br /> \t" + "Fin    : "
											+ (getView().getCongesEnd().getValue().getYear() + 1900) + "/"
											+ (getView().getCongesEnd().getValue().getMonth() + 1) + "/"
											+ getView().getCongesEnd().getValue().getDate() + " " + endDay + "<br /> \t"
											+ "Statut : En cours de validation" + "\n" + "</body>\n" + "</html>";
									dispatchAsync.execute(new SendEmailAction(currentUser.getLogin(),
											"Demande d'abscence", message, 1), new AsyncCallback<SendEmailResult>() {

												@Override
												public void onFailure(Throwable arg0) {
													// TODO Auto-generated
												}

												@Override
												public void onSuccess(SendEmailResult arg0) {
													// TODO
												}
											});

									message = "<html>\n" + "<body>\n" + "<br />" + "Bonjour,<br />"
											+ currentUser.getName() + " a posé un nouveau congé: <br /> \t"
											+ "Type   : " + getView().getListBox().getValue() + "<br />\t" + "Début  : "
											+ (getView().getCongesStart().getValue().getYear() + 1900) + "/"
											+ (getView().getCongesStart().getValue().getMonth() + 1) + "/"
											+ getView().getCongesStart().getValue().getDate() + " " + startDay
											+ "<br /> \t" + "Fin    : "
											+ (getView().getCongesEnd().getValue().getYear() + 1900) + "/"
											+ (getView().getCongesEnd().getValue().getMonth() + 1) + "/"
											+ getView().getCongesEnd().getValue().getDate() + " " + endDay + "<br /> \t"
											+ "Statut : En cours de validation" + "\n" + "</body>\n" + "</html>";
									dispatchAsync
											.execute(
													new SendEmailAction("admin",
															"Demande d'abscence " + currentUser.getName(), message, 1),
													new AsyncCallback<SendEmailResult>() {

														@Override
														public void onFailure(Throwable arg0) {
															// TODO
															// Auto-generated
														}

														@Override
														public void onSuccess(SendEmailResult arg0) {
															// TODO
														}
													});

								}
							});
				}

			}
		});
	}

	protected boolean CheckCongesInList(Date start, Date end, String startD, String endD) {
		for (Conges c : dataProvider.getList()) {
			if (((start.compareTo(c.getStartDate()) > 0) && (start.compareTo(c.getEndDate()) < 0))
					|| ((end.compareTo(c.getStartDate()) > 0) && (end.compareTo(c.getEndDate()) < 0))) {
				getView().getCongesEndControlGroup().setType(ControlGroupType.ERROR);
				getView().getCongesEndHelpInline().setText("Veuillez vérifier vos congés");
				return false;
			} else if (((start.compareTo(c.getStartDate()) < 0) && (end.compareTo(c.getStartDate()) > 0))
					|| ((start.compareTo(c.getEndDate()) < 0) && (end.compareTo(c.getEndDate()) > 0))) {
				getView().getCongesEndControlGroup().setType(ControlGroupType.ERROR);
				getView().getCongesEndHelpInline().setText("Veuillez vérifier vos congés");
				return false;
			} else if ((start.compareTo(c.getStartDate()) == 0) && (c.getStartD().equals(startD))) {
				getView().getRadioEndControlGroup().setType(ControlGroupType.ERROR);
				getView().getRadioEndHelpInline().setText("Veuillez vérifier vos congés");
				return false;
			} else if ((end.compareTo(c.getEndDate()) == 0) && (c.getEndD().equals(endD))) {
				getView().getRadioEndControlGroup().setType(ControlGroupType.ERROR);
				getView().getRadioEndHelpInline().setText("Veuillez vérifier vos congés");
				return false;
			} else if ((start.compareTo(c.getEndDate()) == 0) && (c.getEndD().equals(startD))) {
				getView().getRadioEndControlGroup().setType(ControlGroupType.ERROR);
				getView().getRadioEndHelpInline().setText("Veuillez vérifier vos congés");
				return false;
			}
		}
		getView().getRadioEndControlGroup().setType(ControlGroupType.NONE);
		getView().getRadioEndHelpInline().setText("");
		getView().getCongesEndControlGroup().setType(ControlGroupType.NONE);
		getView().getCongesEndHelpInline().setText("");
		return true;
	}

	private CongesConfirmHandler congeConfirmHandler = new CongesConfirmHandler() {

		@Override
		public void onCongesConfirm(CongesConfirmEvent event) {

			if (event.getMessage().equals("ok")) {
				// if (conges.getEndDate().compareTo(new Date()) > 0) {
				dispatchAsync.execute(new ModifyCongesAction(conges.getCongesId(), "deleted"),
						new AsyncCallback<ModifyCongesResult>() {

							@Override
							public void onFailure(Throwable arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(ModifyCongesResult result) {
								addCongesToTable();
								String message = "<html>\n" + "<body>\n" + "<br />"
										+ "Bonjour,<br />Le statut de votre congé a été modifié: <br /> \t"
										+ "Type   : " + conges.getType() + "<br />\t" + "Début  : " + conges.getStart()
										+ " " + conges.getStartD() + "<br /> \t" + "Fin    : " + conges.getEnd() + " "
										+ conges.getEndD() + "<br /> \t" + "Statut : " + "Supprimé" + "\n" + "</body>\n"
										+ "</html>";

								dispatchAsync.execute(
										new SendEmailAction(currentUser.getLogin(), "Demande d'abscence", message, 1),
										new AsyncCallback<SendEmailResult>() {

											@Override
											public void onFailure(Throwable arg0) {
												// TODO Auto-generated
												// method stub

											}

											@Override
											public void onSuccess(SendEmailResult arg0) {
												// TODO

											}
										});
								
								
								
								
								
								message = "<html>\n" + "<body>\n" + "<br />"
										+ "Bonjour,<br />Le statut de congé de "+currentUser.getName()+" a été modifié: <br /> \t"
										+ "Type   : " + conges.getType() + "<br />\t" + "Début  : " + conges.getStart()
										+ " " + conges.getStartD() + "<br /> \t" + "Fin    : " + conges.getEnd() + " "
										+ conges.getEndD() + "<br /> \t" + "Statut : " + "Supprimé" + "\n" + "</body>\n"
										+ "</html>";

								dispatchAsync.execute(
										new SendEmailAction("admin", "Demande d'abscence", message, 1),
										new AsyncCallback<SendEmailResult>() {

											@Override
											public void onFailure(Throwable arg0) {
												// TODO Auto-generated
												// method stub

											}

											@Override
											public void onSuccess(SendEmailResult arg0) {
												// TODO

											}
										});

							}
						});
			}

		}
	};

	protected void updateDate() {
		getView().getCongesStart().addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> arg0) {
				if (getView().getCongesStart().getValue() != null) {
					getView().getCongesEnd().setStartDate_(getView().getCongesStart().getValue());
					getView().getCongesEnd().reconfigure();
				}

			}

		});
	}
	


}