package org.app.client.application.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.app.client.application.addonedaywork.AddOneDayWorkPresenter;
import org.app.client.application.addwork.AddWorkPresenter;
import org.app.client.application.confirm.ConfirmPresenter;
import org.app.client.application.conges.CongesPresenter;
import org.app.client.application.header.HeaderPresenter;
import org.app.client.application.notify.NotifyPresenter;
import org.app.client.application.showgroupconge.ShowGroupCongePresenter;
import org.app.client.application.toolbox.CurrentUser;
import org.app.client.events.ClientNameEvent;
import org.app.client.events.ClientNameEvent.ClientNameHandler;
import org.app.client.events.ConfirmEvent;
import org.app.client.events.ConfirmEvent.ConfirmHandler;
import org.app.client.events.PopupMessageEvent;
import org.app.client.place.NameTokens;
import org.app.shared.WorkLoad;
import org.app.shared.dispatch.NotifyCramAction;
import org.app.shared.dispatch.NotifyCramResult;
import org.app.shared.dispatch.RetrieveCongesAction;
import org.app.shared.dispatch.RetrieveCongesResult;
import org.app.shared.dispatch.RetrieveFerierAction;
import org.app.shared.dispatch.RetrieveFerierResult;
import org.app.shared.dispatch.SaveCramAction;
import org.app.shared.dispatch.SaveCramResult;
import org.app.shared.dispatch.UserInformationAction;
import org.app.shared.dispatch.UserInformationResult;
import org.app.shared.wrapper.CongesWrapper;
import org.app.shared.wrapper.CramWrapper;
import org.app.shared.wrapper.FerierWrapper;
import org.app.shared.wrapper.WorkLoadWrapper;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.CalendarConfig;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.ClickAndHoverConfig;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.ClickAndHoverEventCallback;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.Event;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.FullCalendar;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.GeneralDisplay;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.Header;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.Language;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.SelectConfig;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.SelectEventCallback;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.ViewOption;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Tab;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.client.RpcDispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy> {
	interface MyView extends View {
		public HTMLPanel getCalendarPanel();

		public Tab getCongesTab();

		public Button getSaveCram();

		public Tab getCramTab();

		public Button getNotifyCram();

		public HelpInline getNotifyErrors();

		public ControlGroup getNotifyGroup();
	}

	// Private Variables
	private FullCalendar configuringCalendar;
	private JavaScriptObject eventStart;
	private JavaScriptObject eventEnd;
	private List<WorkLoad> days;
	private List<WorkLoad> validation;
	// private String currentUserName;
	private CurrentUser currentUser;
	private Date currentDate;
	private ArrayList<Integer> ferier;
	private Logger logger = Logger.getLogger("");


	@ProxyStandard
	@NameToken(NameTokens.home)
	interface MyProxy extends ProxyPlace<HomePresenter> {
	}

	@Inject
	HomePresenter(EventBus eventBus, MyView view, MyProxy proxy, CurrentUser currentUser) {
		super(eventBus, view, proxy, HeaderPresenter.SLOT_Header);
		this.currentUser = currentUser;
	}

	@Inject
	ConfirmPresenter confirmPresenter;
	@Inject
	NotifyPresenter notifyPresenter;
	@Inject
	AddWorkPresenter addWorkPresenter;
	@Inject
	AddOneDayWorkPresenter addOneDayWorkPresenter;
	@Inject
	RpcDispatchAsync dispatchAsync;
	@Inject
	CongesPresenter congesPresenter;
	@Inject
	EventBus eventBus;
	@Inject ShowGroupCongePresenter showGroupCongePresenter;

	public static final NestedSlot SLOT_Conges = new NestedSlot();
	public static final NestedSlot Slot_ShowConge = new NestedSlot();

	private ClientNameHandler clientEventHandler = new ClientNameHandler() {

		@Override
		public void onClientName(ClientNameEvent event) {
			Event tempEvent = new Event("" + System.currentTimeMillis(), event.getMessage());
			float load;
			if (eventStart != null) {

				tempEvent.setStart(eventStart);
				tempEvent.setEnd(eventEnd);
				tempEvent.setAllDay(true);
				tempEvent.setConstraint("businessHours");
				// Check if we are at the end of the month
				if (tempEvent.getEnd().getDate() == 1) {
					if ((tempEvent.getStart().getDate() + 1 == tempEvent.getEnd().getDate())
							|| tempEvent.getStart().getDate() == monthLength()) {
						String[] tokens = event.getMessage().split("-");
						if (tokens[1].equals("J"))
							load = (float) 1;
						else
							load = (float) 0.5;
						if (addWorkDay(tokens[0], tokens[1], load, monthLength())
								&& tempEvent.getStart().getMonth() == new Date().getMonth()) {
							addEvents(tempEvent.getStart(), tempEvent.getEnd(), event.getMessage(), 5);

						} else {
							messageEvent = new PopupMessageEvent("Erreur de saisie.");
							eventBus.fireEvent(messageEvent);
							addToPopupSlot(notifyPresenter);
						}
					} else {
						if (tempEvent.getStart().getMonth() == new Date().getMonth()) {

							addWorkDays(event.getMessage(), tempEvent.getStart().getDate(), monthLength());
							addEvents(tempEvent.getStart(), tempEvent.getEnd(), event.getMessage(), 1);
						} else {
							messageEvent = new PopupMessageEvent("Erreur de saisie.");
							eventBus.fireEvent(messageEvent);
							addToPopupSlot(notifyPresenter);
						}
					}
				} else {
					if (tempEvent.getStart().getDate() + 1 == tempEvent.getEnd().getDate()) {
						String[] tokens = event.getMessage().split("-");
						if (tokens[1].equals("J"))
							load = (float) 1;
						else
							load = (float) 0.5;
						if (addWorkDay(tokens[0], tokens[1], load, tempEvent.getStart().getDate())
								&& tempEvent.getStart().getMonth() == new Date().getMonth())
							addEvents(tempEvent.getStart(), tempEvent.getEnd(), event.getMessage(), 5);
						else {
							messageEvent = new PopupMessageEvent("Erreur de saisie.");
							eventBus.fireEvent(messageEvent);
							addToPopupSlot(notifyPresenter);
						}

					} else {
						if (tempEvent.getStart().getMonth() == new Date().getMonth()) {
							addWorkDays(event.getMessage(), tempEvent.getStart().getDate(),
									tempEvent.getEnd().getDate() - 1);
							addEvents(tempEvent.getStart(), tempEvent.getEnd(), event.getMessage(), 1);
						} else {
							messageEvent = new PopupMessageEvent("Erreur de saisie.");
							eventBus.fireEvent(messageEvent);
							addToPopupSlot(notifyPresenter);
						}
					}
				}
			}
		}
	};

	private boolean cramVisible = false;
	private ConfirmHandler confirmHandler = new ConfirmHandler() {

		@Override
		public void onConfirm(ConfirmEvent event) {
			if (days != null) {
				if (event.getMessage().equals("ok")) {
					if (event.getSender().equals("Home")) {
						removeEvent(event.getDate());
						configuringCalendar.removeEvent(event.getId());
					}
				}
			}

		}
	};

	@Override
	protected void onBind() {
		super.onBind();

		currentDate = new Date();
		registerHandler(getEventBus().addHandler(ClientNameEvent.getType(), clientEventHandler));
		registerHandler(getEventBus().addHandler(ConfirmEvent.getType(), confirmHandler));

		configureCalendar();

		getView().getCongesTab().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				congesPresenter.addCongesToTable();
			}
		});
		getView().getCramTab().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentUser.setColleageEmail(currentUser.getLogin());
				dispatchAsync.execute(new UserInformationAction(currentUser.getLogin()),
						new AsyncCallback<UserInformationResult>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(UserInformationResult result) {
								configuringCalendar.today();
								configuringCalendar.removeAllEvents();
								initFerier();
								initilizeDays(result.getResult().getCrams().iterator().next().getWorkList());
								initConges();

							}
						});
			}
		});

		getView().getSaveCram().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// Convert days List to CramWrapper
				CramWrapper cram = new CramWrapper();
				cram.setDate(currentDate);

				for (WorkLoad w : days) {
					cram.getWorkList().add(new WorkLoadWrapper(w.getClientName(), w.getDayLoad(), w.getLoad()));
				}
				logger.info("Validation " + validation.toString());
				logger.info("Days " + days.toString());
				dispatchAsync.execute(new SaveCramAction(currentUser.getLogin(), cram, currentDate),
						new AsyncCallback<SaveCramResult>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(SaveCramResult result) {
								PopupMessageEvent messageEvent;
								messageEvent = new PopupMessageEvent("Great, le cram est enregistré.");
								eventBus.fireEvent(messageEvent);
								addToPopupSlot(notifyPresenter);

							}
						});

			}
		});

		getView().getNotifyCram().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				// Convert days List to CramWrapper
				CramWrapper cram = new CramWrapper();
				cram.setDate(currentDate);

				for (WorkLoad w : days) {
					cram.getWorkList().add(new WorkLoadWrapper(w.getClientName(), w.getDayLoad(), w.getLoad()));
				}
				logger.info("Validation " + validation.toString());
				logger.info("Days " + days.toString());
				dispatchAsync.execute(new SaveCramAction(currentUser.getLogin(), cram, currentDate),
						new AsyncCallback<SaveCramResult>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(SaveCramResult result) {

							}
						});

				if (validateCramBeforeSave() != 0) {
					getView().getNotifyGroup().setType(ControlGroupType.ERROR);
					getView().getNotifyErrors().setText(
							"Le Cram n'est pas valide, veuillez corriger le jour " + validateCramBeforeSave() + " !");
				} else {
					getView().getNotifyGroup().setType(ControlGroupType.NONE);
					getView().getNotifyErrors().setText("");
					List<WorkLoadWrapper> work = new ArrayList<WorkLoadWrapper>();

					for (WorkLoad w : validation) {
						work.add(new WorkLoadWrapper(w.getClientName(), w.getDayLoad(), w.getLoad()));
					}
					dispatchAsync.execute(new NotifyCramAction(currentUser.getLogin(), work, currentDate),
							new AsyncCallback<NotifyCramResult>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess(NotifyCramResult result) {
									PopupMessageEvent messageEvent;
									if (result.getResutl().equals("ok")) {
										messageEvent = new PopupMessageEvent("Votre CRAM est envoyé.");
										eventBus.fireEvent(messageEvent);
										addToPopupSlot(notifyPresenter);
									} else {
										messageEvent = new PopupMessageEvent("Erreur.");
										eventBus.fireEvent(messageEvent);
										addToPopupSlot(notifyPresenter);
									}

								}
							});
				}
			}
		});

	}

	protected void configureCalendar() {
		// Configure when I'm over an event
		CalendarConfig config = new CalendarConfig();
		ClickAndHoverConfig clickHover = new ClickAndHoverConfig(new ClickAndHoverEventCallback() {

			@Override
			public void eventMouseover(JavaScriptObject calendarEvent, NativeEvent event, JavaScriptObject viewObject) {
			}

			@Override
			public void eventMouseout(JavaScriptObject calendarEvent, NativeEvent event, JavaScriptObject viewObject) {
			}

			@Override
			public void eventClick(JavaScriptObject calendarEvent, NativeEvent event, JavaScriptObject viewObject) {
				Event calEvent = new Event(calendarEvent);
				// System.out.println("id " + calEvent.getId() + " start: " +
				// calEvent.getStart() + " end: "
				// + calEvent.getEnd() + " all day: " + calEvent.isAllDay());
				if (calEvent.getTitle().equals("Congé payé") || calEvent.getTitle().equals("RTT")
						|| calEvent.getTitle().equals("Mariage") || calEvent.getTitle().equals("Autres")
						|| calEvent.getTitle().equals("Maladie") || calEvent.getTitle().equals("Naissance")
						|| calEvent.getTitle().equals("Congé sans solde")) {
					// messageEvent = new PopupMessageEvent(
					// "Vous ne pouvez pas supprimer un congé depuis cette
					// interface!");
					// eventBus.fireEvent(messageEvent);
					// addToPopupSlot(notifyPresenter);
				} else {
					messageEvent = new PopupMessageEvent("Voulez-vous supprimer '" + calEvent.getTitle() + "'?",
							calEvent.getStart(), calEvent.getId(), "Home");
					eventBus.fireEvent(messageEvent);
					addToPopupSlot(confirmPresenter);
				}

			}

			@Override
			public void dayClick(JavaScriptObject moment, NativeEvent event, JavaScriptObject viewObject) {
				// TODO Auto-generated method stub

			}
		});

		config.setClickHoverConfig(clickHover);

		// Configure when I click on an empty Cell
		SelectConfig selectConfig = new SelectConfig(new SelectEventCallback() {

			@Override
			public void select(JavaScriptObject start, JavaScriptObject end, NativeEvent event,
					JavaScriptObject viewObject) {
				eventStart = start;
				eventEnd = end;
				Event tmpEvent = new Event("" + System.currentTimeMillis(), "");
				tmpEvent.setStart(eventStart);
				tmpEvent.setEnd(eventEnd);

				if ((tmpEvent.getStart().getDate() + 1 == tmpEvent.getEnd().getDate())
						|| tmpEvent.getStart().getDate() == monthLength()) {
					addToPopupSlot(addOneDayWorkPresenter);
				} else {
					if (precheck(tmpEvent.getStart(), tmpEvent.getEnd())) {
						addToPopupSlot(addWorkPresenter);
					} else {
						messageEvent = new PopupMessageEvent("Erreur de saisie.");
						eventBus.fireEvent(messageEvent);
						addToPopupSlot(notifyPresenter);
					}
				}

			}

			@Override
			public void unselect(JavaScriptObject viewObject, NativeEvent event) {
			}
		});
		config.setSelectConfig(selectConfig);
		config.setSelectable(true);
		config.setSelectHelper(true);
		config.setLangauge(Language.French);

		Header header = new Header();
		// header.setRight("prev,next");
		header.setRight("");
		GeneralDisplay gd = new GeneralDisplay();
		gd.setBusinessHours(true);
		gd.setHeader(header);
		config.setGeneralDisplay(gd);
		configuringCalendar = new FullCalendar("" + System.currentTimeMillis(), ViewOption.month, config, true);

		getView().getCalendarPanel().add(configuringCalendar);

	}

	private PopupMessageEvent messageEvent;

	@Override
	protected void onReset() {
		super.onReset();
		if (!cramVisible) {
			dispatchAsync.execute(new UserInformationAction(currentUser.getLogin()),
					new AsyncCallback<UserInformationResult>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(UserInformationResult result) {
							configuringCalendar.today();
							// configuringCalendar.removeAllEvents();
							initFerier();
							initilizeDays(result.getResult().getCrams().iterator().next().getWorkList());
							initConges();
							cramVisible = true;
						}
					});
		}
		setInSlot(SLOT_Conges, congesPresenter);
		setInSlot(Slot_ShowConge, showGroupCongePresenter);

		// Logger logger = Logger.getLogger("MS");
		// logger.log(Level.SEVERE, "The Work List " + days);

	}

	protected void initBlocked() {
		for (int i = 1; i <= monthLength(); i++) {
			Date date = new Date();
			date.setDate(i);
			Event blocked = new Event("some event", "");
			blocked.setStart(date);
			blocked.setAllDay(true);
			blocked.setOverlap(false);
			blocked.setRendering("background");
			blocked.setBackgroundColor("#ffffff");
			configuringCalendar.addEvent(blocked);
		}
	}

	protected void initFerier() {
		Date date = new Date();
		ferier = new ArrayList<Integer>();
		dispatchAsync.execute(new RetrieveFerierAction(), new AsyncCallback<RetrieveFerierResult>() {

			@Override
			public void onFailure(Throwable error) {
				Window.alert(error.toString());
			}

			@Override
			public void onSuccess(RetrieveFerierResult result) {
				for (FerierWrapper Fw : result.getResult()) {
					if (((date.getYear() == Fw.getDate().getYear())) && (date.getMonth() == Fw.getDate().getMonth())) {
						Event blocked = new Event("some_event_id", "");
						blocked.setStart(Fw.getDate());
						blocked.setAllDay(true);
						blocked.setOverlap(false);
						blocked.setRendering("background");
						blocked.setBackgroundColor("#ff9f89");
						configuringCalendar.addEvent(blocked);
						ferier.add(Fw.getDate().getDate());
						addWorkDay("Férié", "Férié", 0, Fw.getDate().getDate());
					}
				}
			}
		});
	}

	protected int monthLength() {
		Date date = new Date();

		int listLength;
		// Année Bissextile
		if (date.getMonth() == 1 && (date.getYear() % 4) == 0) {
			listLength = 29;
		} else if (date.getMonth() == 1 && (date.getYear() % 4) != 0) {
			listLength = 28;
		} else if (date.getMonth() == 0 || (date.getMonth() == 2) || (date.getMonth() == 4) || (date.getMonth() == 6)
				|| (date.getMonth() == 7) || (date.getMonth() == 9) || (date.getMonth() == 11)) {
			listLength = 31;
		} else {
			listLength = 30;
		}
		return listLength;
	}

	// Initialize the WorkDays List
	protected void initilizeDays() {
		days = new ArrayList<WorkLoad>();
		for (int i = 0; i < monthLength(); i++) {
			days.add(new WorkLoad("", "", 0));
		}
		validation = new ArrayList<WorkLoad>();
		for (int i = 0; i < monthLength(); i++) {
			validation.add(new WorkLoad("", "", 0));
		}
	}

	// Initialize the WorkDays List
	protected void initilizeDays(Collection<WorkLoadWrapper> work) {
		int index = 1;
		if (work.size() == 0) {
			initilizeDays();
		} else {
			days = new ArrayList<WorkLoad>();
			validation = new ArrayList<WorkLoad>();
			for (WorkLoadWrapper w : work) {
				if (w.getLoad() != 0) {
					if (w.getClientName().equals("Congé payé") || w.getClientName().equals("Naissance")
							|| w.getClientName().equals("Mariage") || w.getClientName().equals("Congé sans solde")
							|| w.getClientName().equals("Autres") || w.getClientName().equals("RTT")) {
						days.add(new WorkLoad("", "", 0));
						validation.add(new WorkLoad("", "", 0));
					} else {
						Event oldEvent = new Event("" + System.currentTimeMillis(), w.getClientName());
						Date eventDate = new Date();
						eventDate.setDate(index);
						oldEvent.setStart(eventDate);
						oldEvent.setEnd(eventDate);
						oldEvent.setAllDay(true);
						oldEvent.setConstraint("availableForMeeting");
						configuringCalendar.addEvent(oldEvent);
						days.add(new WorkLoad(w.getClientName(), w.getDayLoad(), w.getLoad()));
						validation.add(new WorkLoad(w.getClientName(), w.getDayLoad(), w.getLoad()));
					}
				} else {
					days.add(new WorkLoad("", "", 0));
					validation.add(new WorkLoad("", "", 0));
				}
				index++;
			}
		}
	}

	// Add Ond day work to the working days List
	protected boolean addWorkDay(String clientName, String loadDay, float workLoad, int day) {
		// Check if the day is already selected
		if ((validation.get(day - 1).getLoad() + workLoad) > 1) {
			return false;
		} else {
			if (loadDay.equals(validation.get(day - 1).getDayLoad())) {
				return false;
			}
			if (clientName.equals("Férié")) {
				validation.get(day - 1).addClientName(clientName);
				validation.get(day - 1).addDayLoad(loadDay);
				validation.get(day - 1).addLoad(workLoad);
			} else {
				days.get(day - 1).addClientName(clientName);
				days.get(day - 1).addDayLoad(loadDay);
				days.get(day - 1).addLoad(workLoad);

				validation.get(day - 1).addClientName(clientName);
				validation.get(day - 1).addDayLoad(loadDay);
				validation.get(day - 1).addLoad(workLoad);
			}
			return true;
		}

	}

	// Check that user did'nt select a cell that already been filled
	protected boolean checkWorkDays(JsDate start, JsDate end) {
		int borne;
		if (start.getMonth() + 1 == end.getMonth()) {
			borne = monthLength();
		} else {
			borne = end.getDate() - 1;
		}
		for (int i = start.getDate() - 1; i < borne; i++) {

			if ((validation.get(i).getLoad() + 1) > 1) {
				return false;
			}
		}
		return true;
	}

	// Add the selected days to the workDays list
	protected void addWorkDays(String clientName, int start, int end) {
		Date date = new Date();
		for (int i = start - 1; i < end; i++) {
			date.setDate(i + 1);
			if (date.getDay() == 0 || date.getDay() == 6 || ferier.contains(date.getDate())) {
				// Do nothing
			} else {
				days.get(i).setClientName(clientName);
				days.get(i).setDayLoad("J");
				days.get(i).setLoad((float) 1);

				validation.get(i).setClientName(clientName);
				validation.get(i).setDayLoad("J");
				validation.get(i).setLoad((float) 1);
			}
		}
	}

	protected void initConges() {
		dispatchAsync.execute(new RetrieveCongesAction(currentUser.getLogin()),
				new AsyncCallback<RetrieveCongesResult>() {

					@Override
					public void onFailure(Throwable arg0) {
						Window.alert("Error");

					}

					@Override
					public void onSuccess(RetrieveCongesResult result) {
						for (CongesWrapper c : result.getResult()) {
							if (c.getStatus().equals("Validé")) {
								if ((c.getStartDate().getYear() == new Date().getYear())
										|| ((c.getStartDate().getYear() == new Date().getYear() - 1)
												&& (c.getEndDate().getYear() == new Date().getYear()))) {
									if ((c.getStartDate().getMonth() == new Date().getMonth())
											&& (new Date().getMonth() == c.getEndDate().getMonth())) {
										if (c.getStartDate().compareTo(c.getEndDate()) == 0) {
											if (c.getEndD().equals(c.getStartD())) {
												if (c.getStartDate().getDay() == 0 || c.getStartDate().getDay() == 6
														|| ferier.contains(c.getStartDate().getDate())) {
													// Do Nothing
												} else {
													Event event = new Event("" + System.currentTimeMillis(),
															c.getType());
													event.setStart(c.getStartDate());
													event.setEnd(c.getEndDate());
													event.setAllDay(true);
													event.setColor("#257e4a");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
													validation.get(c.getStartDate().getDate() - 1)
															.addClientName(c.getType());
													validation.get(c.getStartDate().getDate() - 1)
															.addDayLoad(c.getStartD());
													validation.get(c.getStartDate().getDate() - 1).addLoad((float) 0.5);
												}
											} else {
												if (c.getStartDate().getDay() == 0 || c.getStartDate().getDay() == 6
														|| ferier.contains(c.getStartDate().getDate())) {
													// Do Nothing
												} else {
													Event event = new Event("" + System.currentTimeMillis(),
															c.getType());
													event.setStart(c.getStartDate());
													event.setEnd(c.getEndDate());
													event.setAllDay(true);
													event.setColor("#257e4a");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
													validation.get(c.getStartDate().getDate() - 1)
															.addClientName(c.getType());
													validation.get(c.getStartDate().getDate() - 1)
															.addDayLoad("Journée");
													validation.get(c.getStartDate().getDate() - 1).addLoad((float) 1);
												}
											}
										} else {
											int index = c.getStartDate().getDate() - 1;
											if (c.getStartD().equals("Matin")) {
												if (c.getStartDate().getDay() == 0 || c.getStartDate().getDay() == 6
														|| ferier.contains(c.getStartDate().getDate())) {
													// Do Nothing
												} else {
													Event event = new Event("" + System.currentTimeMillis(),
															c.getType());
													event.setStart(c.getStartDate());
													event.setEnd(c.getStartDate());
													event.setAllDay(true);
													event.setColor("#257e4a");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
													validation.get(c.getStartDate().getDate() - 1)
															.addClientName(c.getType());
													validation.get(c.getStartDate().getDate() - 1)
															.addDayLoad("Journée");
													validation.get(c.getStartDate().getDate() - 1).addLoad((float) 1);
												}
											} else {
												if (c.getStartDate().getDay() == 0 || c.getStartDate().getDay() == 6
														|| ferier.contains(c.getStartDate().getDate())) {
													// Do Nothing
												} else {
													Event event = new Event("" + System.currentTimeMillis(),
															c.getType());
													event.setStart(c.getStartDate());
													event.setEnd(c.getStartDate());
													event.setAllDay(true);
													event.setColor("#257e4a");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
													validation.get(c.getStartDate().getDate() - 1)
															.addClientName(c.getType());
													validation.get(c.getStartDate().getDate() - 1)
															.addDayLoad(c.getStartD());
													validation.get(c.getStartDate().getDate() - 1).addLoad((float) 0.5);
												}
											}
											index++;
											while (index < c.getEndDate().getDate() - 1) {
												Event event = new Event("" + System.currentTimeMillis(), c.getType());
												Date date = new Date();
												date.setDate(index + 1);
												if (date.getDay() == 0 || date.getDay() == 6
														|| ferier.contains(date.getDate())) {
													// Do Nothing
												} else {
													event.setStart(date);
													event.setEnd(date);
													event.setAllDay(true);
													event.setColor("#257e4a");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
													validation.get(date.getDate() - 1).addClientName(c.getType());
													validation.get(date.getDate() - 1).addDayLoad("Journée");
													validation.get(date.getDate() - 1).addLoad((float) 1);
												}
												index++;
											}
											if (c.getEndD().equals("Matin")) {
												if (c.getEndDate().getDay() == 0 || c.getEndDate().getDay() == 6
														|| ferier.contains(c.getEndDate().getDate())) {
													// Do Nothing
												} else {
													Event event = new Event("" + System.currentTimeMillis(),
															c.getType());
													event.setStart(c.getEndDate());
													event.setEnd(c.getEndDate());
													event.setAllDay(true);
													event.setColor("#257e4a");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
													validation.get(c.getEndDate().getDate() - 1)
															.addClientName(c.getType());
													validation.get(c.getEndDate().getDate() - 1)
															.addDayLoad(c.getEndD());
													validation.get(c.getEndDate().getDate() - 1).addLoad((float) 0.5);
												}
											} else {
												if (c.getEndDate().getDay() == 0 || c.getEndDate().getDay() == 6
														|| ferier.contains(c.getEndDate().getDate())) {
													// Do Nothing
												} else {
													Event event = new Event("" + System.currentTimeMillis(),
															c.getType());
													event.setStart(c.getEndDate());
													event.setEnd(c.getEndDate());
													event.setAllDay(true);
													event.setColor("#257e4a");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
													validation.get(c.getEndDate().getDate() - 1)
															.addClientName(c.getType());
													validation.get(c.getEndDate().getDate() - 1).addDayLoad("Journée");
													validation.get(c.getEndDate().getDate() - 1).addLoad((float) 1);
												}
											}

										}
									} else if (c.getStartDate().getMonth() == new Date().getMonth()) {
										int index = c.getStartDate().getDate() - 1;
										if (c.getStartD().equals("Matin")) {
											if (c.getStartDate().getDay() == 0 || c.getStartDate().getDay() == 6
													|| ferier.contains(c.getStartDate().getDate())) {
												// Do Nothing
											} else {
												Event event = new Event("" + System.currentTimeMillis(), c.getType());
												event.setStart(c.getStartDate());
												event.setEnd(c.getStartDate());
												event.setAllDay(true);
												event.setColor("#257e4a");
												event.setConstraint("availableForMeeting");
												configuringCalendar.addEvent(event);
												validation.get(c.getStartDate().getDate() - 1)
														.addClientName(c.getType());
												validation.get(c.getStartDate().getDate() - 1).addDayLoad("Journée");
												validation.get(c.getStartDate().getDate() - 1).addLoad((float) 1);
											}
										} else {
											if (c.getStartDate().getDay() == 0 || c.getStartDate().getDay() == 6
													|| ferier.contains(c.getStartDate().getDate())) {
												// Do Nothing
											} else {
												Event event = new Event("" + System.currentTimeMillis(), c.getType());
												event.setStart(c.getStartDate());
												event.setEnd(c.getStartDate());
												event.setAllDay(true);
												event.setColor("#257e4a");
												event.setConstraint("availableForMeeting");
												configuringCalendar.addEvent(event);
												validation.get(c.getStartDate().getDate() - 1)
														.addClientName(c.getType());
												validation.get(c.getStartDate().getDate() - 1)
														.addDayLoad(c.getStartD());
												validation.get(c.getStartDate().getDate() - 1).addLoad((float) 0.5);
											}
										}
										index++;
										while (index < monthLength()) {
											Event event = new Event("" + System.currentTimeMillis(), c.getType());
											Date date = new Date();
											date.setDate(index + 1);
											if (date.getDay() == 0 || date.getDay() == 6
													|| ferier.contains(date.getDate())) {
												// Do Nothing
											} else {
												event.setStart(date);
												event.setEnd(date);
												event.setAllDay(true);
												event.setColor("#257e4a");
												event.setConstraint("availableForMeeting");
												configuringCalendar.addEvent(event);
												validation.get(date.getDate() - 1).addClientName(c.getType());
												validation.get(date.getDate() - 1).addDayLoad("Journée");
												validation.get(date.getDate() - 1).addLoad((float) 1);
											}
											index++;
										}

									} else if (c.getEndDate().getMonth() == new Date().getMonth()) {
										int index = 0;
										while (index < c.getEndDate().getDate() - 1) {
											Event event = new Event("" + System.currentTimeMillis(), c.getType());
											Date date = new Date();
											date.setDate(index + 1);
											if (date.getDay() == 0 || date.getDay() == 6
													|| ferier.contains(date.getDate())) {
												// Do Nothing
											} else {
												event.setStart(date);
												event.setEnd(date);
												event.setAllDay(true);
												event.setColor("#257e4a");
												event.setConstraint("availableForMeeting");
												configuringCalendar.addEvent(event);
												validation.get(date.getDate() - 1).addClientName(c.getType());
												validation.get(date.getDate() - 1).addDayLoad("Journée");
												validation.get(date.getDate() - 1).addLoad((float) 1);
											}
											index++;
										}
										if (c.getEndD().equals("Matin")) {
											Event event = new Event("" + System.currentTimeMillis(), c.getType());
											Date date = new Date();
											date.setDate(index + 1);
											if (date.getDay() == 0 || date.getDay() == 6
													|| ferier.contains(date.getDate())) {
												// Do Nothing
											} else {
												event.setStart(date);
												event.setEnd(date);
												event.setAllDay(true);
												event.setColor("#257e4a");
												event.setConstraint("availableForMeeting");
												configuringCalendar.addEvent(event);
												validation.get(date.getDate() - 1).addClientName(c.getType());
												validation.get(date.getDate() - 1).addDayLoad(c.getEndD());
												validation.get(date.getDate() - 1).addLoad((float) 0.5);
											}
										} else if (c.getEndD().equals("Après-midi")) {
											Event event = new Event("" + System.currentTimeMillis(), c.getType());
											Date date = new Date();
											date.setDate(index + 1);
											if (date.getDay() == 0 || date.getDay() == 6
													|| ferier.contains(date.getDate())) {
												// Do Nothing
											} else {
												event.setStart(date);
												event.setEnd(date);
												event.setAllDay(true);
												event.setColor("#257e4a");
												event.setConstraint("availableForMeeting");
												configuringCalendar.addEvent(event);
												validation.get(date.getDate() - 1).addClientName(c.getType());
												validation.get(date.getDate() - 1).addDayLoad("Journée");
												validation.get(date.getDate() - 1).addLoad((float) 1);
											}
										}

									}
								}
							}
						}
					}

				});
	}

	protected boolean precheck(JsDate start, JsDate end) {
		if (!checkWorkDays(start, end))
			return false;
		if (!checkMonths(start, end))
			return false;
		return true;
	}

	// Check if the user selected the write month
	protected boolean checkMonths(JsDate start, JsDate end) {
		Date date = new Date();
		if (end.getDate() == 1) {
			return true;
		} else if ((start.getMonth() == end.getMonth()) && start.getMonth() == date.getMonth()) {
			return true;
		}
		return false;
	}

	// Add Events to the Calendar; This action is only graphic
	protected void addEvents(JsDate start, JsDate end, String clientName, int workLoad) {
		if (workLoad == 1) {
			int endDate;
			if (end.getDate() == 1) {
				endDate = monthLength();
			} else {
				endDate = end.getDate();
			}
			for (; start.getDate() < endDate; start.setDate(start.getDate() + 1)) {
				Event tempEvent = new Event(Integer.toString(start.getDate()), clientName);
				tempEvent.setAllDay(true);
				tempEvent.setStart(start);
				tempEvent.setEnd(start);
				tempEvent.setConstraint("availableForMeeting");
				if (start.getDay() == 0 || start.getDay() == 6 || ferier.contains(start.getDate())) {
				} else {
					configuringCalendar.addEvent(tempEvent);
				}
			}
			if (end.getDate() == 1) {
				Event tempEvent = new Event(Integer.toString(start.getDate()), clientName);
				tempEvent.setAllDay(true);
				tempEvent.setStart(start);
				tempEvent.setEnd(start);
				tempEvent.setConstraint("availableForMeeting");
				if (start.getDay() == 0 || start.getDay() == 6 || ferier.contains(start.getDate())) {
				} else {
					configuringCalendar.addEvent(tempEvent);
				}
			}

		} else {
			Event tempEvent = new Event(Integer.toString(start.getDate()), clientName);
			tempEvent.setAllDay(true);
			tempEvent.setStart(start);
			tempEvent.setEnd(end);
			tempEvent.setConstraint("availableForMeeting");
			configuringCalendar.addEvent(tempEvent);
		}
	}

	protected void removeEvent(JsDate date) {

		days.get(date.getDate() - 1).removeClientName();
		days.get(date.getDate() - 1).removeDayLoad();
		days.get(date.getDate() - 1).removeLoad();

		if ((validation.get(date.getDate() - 1).getClientName().contains("Congé payé"))
				|| (validation.get(date.getDate() - 1).getClientName().contains("RTT"))
				|| (validation.get(date.getDate() - 1).getClientName().contains("Mariage"))
				|| (validation.get(date.getDate() - 1).getClientName().contains("Naissance"))
				|| (validation.get(date.getDate() - 1).getClientName().contains("Férié"))
				|| (validation.get(date.getDate() - 1).getClientName().contains("Autres"))) {
			String clientsName[] = validation.get(date.getDate() - 1).getClientName().split("-");
			String clientsLoad[] = validation.get(date.getDate() - 1).getDayLoad().split("-");
			int index = 0;
			for (String name : clientsName) {
				if (name.equals("Congé payé") || name.equals("RTT") || name.equals("Mariage")
						|| name.equals("Naissance") || name.equals("Autres")) {
					validation.get(date.getDate() - 1).setClientName(clientsName[index]);
					validation.get(date.getDate() - 1).setDayLoad(clientsLoad[index]);
					if (clientsLoad[index].equals("Journée"))
						validation.get(date.getDate() - 1).setLoad((float) 1);
					else
						validation.get(date.getDate() - 1).setLoad((float) 0.5);
				}
				if (name.equals("Férié")) {
					validation.get(date.getDate() - 1).setClientName("Férié");
					validation.get(date.getDate() - 1).setDayLoad("Férié");
					validation.get(date.getDate() - 1).removeLoad();
				}
				index++;
			}

		} else {
			validation.get(date.getDate() - 1).removeClientName();
			validation.get(date.getDate() - 1).removeDayLoad();
			validation.get(date.getDate() - 1).removeLoad();
		}
	}

	protected int validateCramBeforeSave() {
		Date date = new Date(currentDate.getTime());
		for (int i = 1; i <= monthLength(); i++) {
			date.setDate(i);
			if ((date.getDay() == 0) || (date.getDay() == 6) || ferier.contains(date.getDate())) {
				continue;
			} else {
				if (validation.get(i - 1).getLoad() != 1) {
					return i;
				}
			}
		}
		return 0;
	}

}
