package org.app.client.application.altercram;

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
import org.app.client.application.notify.NotifyPresenter;
import org.app.client.application.toolbox.CurrentUser;
import org.app.client.events.ClientNameEvent;
import org.app.client.events.ClientNameEvent.ClientNameHandler;
import org.app.client.events.ConfirmEvent;
import org.app.client.events.ConfirmEvent.ConfirmHandler;
import org.app.client.events.PopupMessageEvent;
import org.app.shared.WorkLoad;
import org.app.shared.dispatch.NotifyCramAction;
import org.app.shared.dispatch.NotifyCramResult;
import org.app.shared.dispatch.RetrieveCongesAction;
import org.app.shared.dispatch.RetrieveCongesResult;
import org.app.shared.dispatch.RetrieveFerierAction;
import org.app.shared.dispatch.RetrieveFerierResult;
import org.app.shared.dispatch.RetrieveUsersAction;
import org.app.shared.dispatch.RetrieveUsersResult;
import org.app.shared.dispatch.SaveCramAction;
import org.app.shared.dispatch.SaveCramResult;
import org.app.shared.dispatch.UserInformationAction;
import org.app.shared.dispatch.UserInformationResult;
import org.app.shared.wrapper.ClientWrapper;
import org.app.shared.wrapper.CongesWrapper;
import org.app.shared.wrapper.CramWrapper;
import org.app.shared.wrapper.FerierWrapper;
import org.app.shared.wrapper.UserWrapper;
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
import com.github.gwtbootstrap.client.ui.ListBox;
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
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class AlterCramPresenter extends Presenter<AlterCramPresenter.MyView, AlterCramPresenter.MyProxy> {
	interface MyView extends View {
		public HTMLPanel getCalendarPanel();

		public Button getSaveCram();

		public ListBox getUsersBox();

		public ListBox getClientBox();

		public ListBox getMonthBox();

		public ListBox getYearBox();

		public Button getDisplay();

	}

	// Private Variables
	private FullCalendar configuringCalendar;
	private JavaScriptObject eventStart;
	private JavaScriptObject eventEnd;
	private List<WorkLoadWrapper> daysUC;
	private List<WorkLoad> validationUC;
	private List<UserWrapper> users;
	// private String userWrapperName;
	private UserWrapper userWrapper;
	private Date currentDate;
	private ArrayList<Integer> ferier;
	private Logger logger = Logger.getLogger("MS");

	// private Date date ;
	private CurrentUser currentUser;

	@ProxyStandard
	interface MyProxy extends Proxy<AlterCramPresenter> {
	}

	@Inject
	AlterCramPresenter(EventBus eventBus, MyView view, MyProxy proxy, CurrentUser currentUser) {
		super(eventBus, view, proxy);
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

	@Override
	protected void onBind() {
		getView().getClientBox().setVisible(false);
		super.onBind();
		users = new ArrayList<UserWrapper>();
		currentDate = new Date();
		// updateDate();
		//initilizedaysUC();
		retrieveUsers();
		retriveClients();
		initMonthAndYear();
		//initFerier();

		registerHandler(getEventBus().addHandler(ClientNameEvent.getType(), clientEventHandler));
		registerHandler(getEventBus().addHandler(ConfirmEvent.getType(), confirmHandler));
		configureCalendar();

		afficherCalendar();
		modifierCram();
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
//				System.out.println("id " + calEvent.getId() + " start: " + calEvent.getStart() + " end: "
//						+ calEvent.getEnd() + " all day: " + calEvent.isAllDay());
				if (calEvent.getTitle().equals("Congé payé") || calEvent.getTitle().equals("RTT")
						|| calEvent.getTitle().equals("Mariage") || calEvent.getTitle().equals("Autres")
						|| calEvent.getTitle().equals("Maladie") || calEvent.getTitle().equals("Naissance")
						|| calEvent.getTitle().equals("Congé sans solde")) {
					// Nothing todo
				} else {
					messageEvent = new PopupMessageEvent("Voulez-vous supprimer '" + calEvent.getTitle() + "'?",
							calEvent.getStart(), calEvent.getId(), "AlterCram");
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

	private PopupMessageEvent messageEvent;;

	@Override
	protected void onReset() {
		super.onReset();
	}

	protected void initBlocked() {
		for (int i = 1; i <= monthLength(); i++) {
			Date date = currentDate;
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
		ferier = new ArrayList<Integer>();
		dispatchAsync.execute(new RetrieveFerierAction(), new AsyncCallback<RetrieveFerierResult>() {

			@Override
			public void onFailure(Throwable error) {
				Window.alert(error.toString());
			}

			@Override
			public void onSuccess(RetrieveFerierResult result) {
				for (FerierWrapper Fw : result.getResult()) {
					if (((currentDate.getYear() == Fw.getDate().getYear()))
							&& (currentDate.getMonth() == Fw.getDate().getMonth())) {
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

		int listLength;
		// Année Bissextile
		if (currentDate.getMonth() == 1 && (currentDate.getYear() % 4) == 0) {
			listLength = 29;
		} else if (currentDate.getMonth() == 1 && (currentDate.getYear() % 4) != 0) {
			listLength = 28;
		} else if (currentDate.getMonth() == 0 || (currentDate.getMonth() == 2) || (currentDate.getMonth() == 4)
				|| (currentDate.getMonth() == 6) || (currentDate.getMonth() == 7) || (currentDate.getMonth() == 9)
				|| (currentDate.getMonth() == 11)) {
			listLength = 31;
		} else {
			listLength = 30;
		}
		return listLength;
	}

	// Initialize the WorkdaysUC List
	protected void initilizedaysUC() {
		daysUC = new ArrayList<WorkLoadWrapper>();
		for (int i = 0; i < monthLength(); i++) {
			daysUC.add(new WorkLoadWrapper("", "", 0));
		}
		validationUC = new ArrayList<WorkLoad>();
		for (int i = 0; i < monthLength(); i++) {
			validationUC.add(new WorkLoad("", "", 0));
		}
	}

	// Initialize the WorkdaysUC List
	protected void initilizedaysUC(Collection<WorkLoadWrapper> work) {
		int index = 1;
		if (work.size() == 0) {
			initilizedaysUC();
		} else {
			daysUC = new ArrayList<WorkLoadWrapper>();
			validationUC = new ArrayList<WorkLoad>();
			for (WorkLoadWrapper w : work) {
				if (w.getLoad() != 0) {
					if (w.getClientName().equals("Congé payé") || w.getClientName().equals("Naissance")
							|| w.getClientName().equals("Mariage") || w.getClientName().equals("Congé sans solde")
							|| w.getClientName().equals("Autres") || w.getClientName().equals("RTT")) {
						daysUC.add(new WorkLoadWrapper("", "", 0));
						validationUC.add(new WorkLoad("", "", 0));
					} else {
						Event oldEvent = new Event("" + System.currentTimeMillis(), w.getClientName());
						Date eventDate = currentDate;
						eventDate.setDate(index);
						oldEvent.setStart(eventDate);
						oldEvent.setEnd(eventDate);
						oldEvent.setAllDay(true);
						oldEvent.setConstraint("availableForMeeting");
						configuringCalendar.addEvent(oldEvent);
						daysUC.add(new WorkLoadWrapper(w.getClientName(), w.getDayLoad(), w.getLoad()));
						validationUC.add(new WorkLoad(w.getClientName(), w.getDayLoad(), w.getLoad()));
					}
				} else {
					daysUC.add(new WorkLoadWrapper("", "", 0));
					validationUC.add(new WorkLoad("", "", 0));
				}
				index++;
			}
		}
	}

	// Add Ond day work to the working daysUC List
	protected boolean addWorkDay(String clientName, String loadDay, float workLoad, int day) {
		// Check if the day is already selected
		if ((validationUC.get(day - 1).getLoad() + workLoad) > 1) {
			return false;
		} else {
			if (loadDay.equals(validationUC.get(day - 1).getDayLoad())) {
				return false;
			}
			daysUC.get(day - 1).addClientName(clientName);
			daysUC.get(day - 1).addDayLoad(loadDay);
			daysUC.get(day - 1).addLoad(workLoad);

			validationUC.get(day - 1).addClientName(clientName);
			validationUC.get(day - 1).addDayLoad(loadDay);
			validationUC.get(day - 1).addLoad(workLoad);
			return true;
		}

	}

	// Check that user did'nt select a cell that already been filled
	protected boolean checkWorkdaysUC(JsDate start, JsDate end) {
		int borne;
		if (start.getMonth() + 1 == end.getMonth()) {
			borne = monthLength();
		} else {
			borne = end.getDate() - 1;
		}
		for (int i = start.getDate() - 1; i < borne; i++) {

			if ((validationUC.get(i).getLoad() + 1) > 1) {
				return false;
			}
		}
		return true;
	}

	// Add the selected daysUC to the workdaysUC list
	protected void addWorkdaysUC(String clientName, int start, int end) {
		Date date = new Date(currentDate.getTime());
		// date.setYear(currentDate.getYear());
		// date.setMonth(currentDate.getMonth());
		for (int i = start - 1; i < end; i++) {
			date.setDate(i + 1);
			if (date.getDay() == 0 || date.getDay() == 6 || ferier.contains(date.getDate())) {
				// Do nothing
			} else {
				daysUC.get(i).setClientName(clientName);
				daysUC.get(i).setDayLoad("J");
				daysUC.get(i).setLoad((float) 1);

				validationUC.get(i).setClientName(clientName);
				validationUC.get(i).setDayLoad("J");
				validationUC.get(i).setLoad((float) 1);
			}
		}
	}

	protected boolean precheck(JsDate start, JsDate end) {
		if (!checkWorkdaysUC(start, end))
			return false;
		if (!checkMonths(start, end))
			return false;
		return true;
	}

	// Check if the user selected the write month
	protected boolean checkMonths(JsDate start, JsDate end) {
		if (end.getDate() == 1) {
			return true;
		} else if ((start.getMonth() == end.getMonth()) && start.getMonth() == currentDate.getMonth()) {
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

		daysUC.get(date.getDate() - 1).removeClientName();
		daysUC.get(date.getDate() - 1).removeDayLoad();
		daysUC.get(date.getDate() - 1).removeLoad();

		if ((validationUC.get(date.getDate() - 1).getClientName().contains("Congé payé"))
				|| (validationUC.get(date.getDate() - 1).getClientName().contains("RTT"))
				|| (validationUC.get(date.getDate() - 1).getClientName().contains("Mariage"))
				|| (validationUC.get(date.getDate() - 1).getClientName().contains("Naissance"))
				|| (validationUC.get(date.getDate() - 1).getClientName().contains("Férié"))
				|| (validationUC.get(date.getDate() - 1).getClientName().contains("Autres"))) {
			String clientsName[] = validationUC.get(date.getDate() - 1).getClientName().split("-");
			String clientsLoad[] = validationUC.get(date.getDate() - 1).getDayLoad().split("-");
			int index = 0;
			for (String name : clientsName) {
				if (name.equals("Congé payé") || name.equals("RTT") || name.equals("Mariage")
						|| name.equals("Naissance") || name.equals("Autres")) {
					validationUC.get(date.getDate() - 1).setClientName(clientsName[index]);
					validationUC.get(date.getDate() - 1).setDayLoad(clientsLoad[index]);
					if (clientsLoad[index].equals("Journée"))
						validationUC.get(date.getDate() - 1).setLoad((float) 1);
					else
						validationUC.get(date.getDate() - 1).setLoad((float) 0.5);
				}
				if (name.equals("Férié")) {
					validationUC.get(date.getDate() - 1).setClientName("Férié");
					validationUC.get(date.getDate() - 1).setDayLoad("Férié");
					validationUC.get(date.getDate() - 1).removeLoad();
				}
				index++;
			}

		} else {
			validationUC.get(date.getDate() - 1).removeClientName();
			validationUC.get(date.getDate() - 1).removeDayLoad();
			validationUC.get(date.getDate() - 1).removeLoad();
		}
	}

	protected void retrieveUsers() {

		dispatchAsync.execute(new RetrieveUsersAction(), new AsyncCallback<RetrieveUsersResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(RetrieveUsersResult response) {
				userWrapper = response.getResult().get(0);
				for (UserWrapper i : response.getResult()) {
					if(i.getStatus().equals("enabled")){
					getView().getUsersBox().addItem(i.getUserName() + " " + i.getUserSurname());
					users.add(i);
					}
				}
			}
		});
	}

	protected void retriveClients() {
		getView().getClientBox().clear();
		getView().getClientBox().addItem("All");
		getView().getUsersBox().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for (UserWrapper i : users) {
					String user[] = getView().getUsersBox().getSelectedItemText().split(" ");
					if (user[0].equals(i.getUserName()) && user[1].equals(i.getUserSurname())) {
						userWrapper = i;
						getView().getClientBox().clear();
						getView().getClientBox().addItem("All");
						for (ClientWrapper c : i.getClients()) {
							getView().getClientBox().addItem(c.getName());
						}
					}
				}

			}
		});
	}

	protected void initMonthAndYear() {
		getView().getMonthBox().addItem("Janvier");
		getView().getMonthBox().addItem("Février");
		getView().getMonthBox().addItem("Mars");
		getView().getMonthBox().addItem("Avril");
		getView().getMonthBox().addItem("Mai");
		getView().getMonthBox().addItem("Juin");
		getView().getMonthBox().addItem("Juillet");
		getView().getMonthBox().addItem("Aout");
		getView().getMonthBox().addItem("Septembre");
		getView().getMonthBox().addItem("Octobre");
		getView().getMonthBox().addItem("Novembre");
		getView().getMonthBox().addItem("Décembre");
		// Logger logger = Logger.getLogger("MS");
		// logger.info(currentDate.getMonth()+"");
		int month = currentDate.getMonth();
		if (month == 0)
			getView().getMonthBox().setSelectedValue("Janvier");
		else if (month == 1)
			getView().getMonthBox().setSelectedValue("Février");
		else if (month == 2)
			getView().getMonthBox().setSelectedValue("Mars");
		else if (month == 3)
			getView().getMonthBox().setSelectedValue("Avril");
		else if (month == 4)
			getView().getMonthBox().setSelectedValue("Mai");
		else if (month == 5)
			getView().getMonthBox().setSelectedValue("Juin");
		else if (month == 6)
			getView().getMonthBox().setSelectedValue("Juillet");
		else if (month == 7)
			getView().getMonthBox().setSelectedValue("Aout");
		else if (month == 8)
			getView().getMonthBox().setSelectedValue("Septembre");
		else if (month == 9)
			getView().getMonthBox().setSelectedValue("Octobre");
		else if (month == 10)
			getView().getMonthBox().setSelectedValue("Novembre");
		else if (month == 12)
			getView().getMonthBox().setSelectedValue("Décembre");

		int year = currentDate.getYear() + 1900;
		getView().getYearBox().addItem((year - 1) + "");
		getView().getYearBox().addItem(year + "");
		getView().getYearBox().setSelectedValue(year + "");
	}

	protected void afficherCalendar() {
		// updateDate();
		getView().getDisplay().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentUser.setColleageEmail(userWrapper.getUserEmail());
				currentDate.setYear(Integer.parseInt(getView().getYearBox().getSelectedItemText()) - 1900);
				currentDate.setMonth(getView().getMonthBox().getSelectedIndex());
				configuringCalendar.goToDate(currentDate);
				configuringCalendar.removeAllEvents();
				List<WorkLoadWrapper> theWork = new ArrayList<WorkLoadWrapper>();
				dispatchAsync.execute(
						new UserInformationAction(userWrapper.getUserEmail(),
								Integer.parseInt(getView().getYearBox().getSelectedItemText()) - 1900,
								getView().getMonthBox().getSelectedIndex()),
						new AsyncCallback<UserInformationResult>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(UserInformationResult result) {

								theWork.clear();

								if (getView().getClientBox().getSelectedValue().equals("All")) {
									initilizedaysUC(result.getResult().getCrams().iterator().next().getWorkList());
									initFerier();
									initConges();
								} else {
									for (WorkLoadWrapper w : result.getResult().getCrams().iterator().next()
											.getWorkList()) {
										if (w.getClientName().equals(getView().getClientBox().getSelectedValue())) {
											theWork.add(w);
										} else {
											theWork.add(new WorkLoadWrapper("", "", (float) 0));
										}
									}
									initilizedaysUC(theWork);
									initFerier();
									initConges();
								}
							}
						});

			}
		});

	}

	protected void modifierCram() {
		getView().getSaveCram().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// Convert days List to CramWrapper
				CramWrapper cram = new CramWrapper();
				cram.setDate(currentDate);

				for (WorkLoadWrapper w : daysUC) {
					cram.getWorkList().add(new WorkLoadWrapper(w.getClientName(), w.getDayLoad(), w.getLoad()));
				}
				dispatchAsync.execute(new SaveCramAction(currentUser.getColleageEmail(), cram, currentDate),
						new AsyncCallback<SaveCramResult>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(SaveCramResult result) {
								// PopupMessageEvent messageEvent;
								// messageEvent = new PopupMessageEvent("Great,
								// le cram est enregistré.");
								// eventBus.fireEvent(messageEvent);
								// addToPopupSlot(notifyPresenter);

							}
						});

				List<WorkLoadWrapper> work = new ArrayList<WorkLoadWrapper>();

				for (WorkLoad w : validationUC) {
					work.add(new WorkLoadWrapper(w.getClientName(), w.getDayLoad(), w.getLoad()));
				}
				dispatchAsync.execute(new NotifyCramAction(currentUser.getColleageEmail(), work, currentDate),
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
		});

	}

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

						// Logger logger = Logger.getLogger("MS");
						// logger.log(Level.SEVERE, "Month start " +
						// tempEvent.getStart().getMonth());
						// logger.log(Level.SEVERE, "Month New " +
						// currentDate.getMonth());
						if (addWorkDay(tokens[0], tokens[1], load, monthLength())
								&& tempEvent.getStart().getMonth() == currentDate.getMonth()) {
							addEvents(tempEvent.getStart(), tempEvent.getEnd(), event.getMessage(), 5);

						} else {
							messageEvent = new PopupMessageEvent("Erreur de saisie.");
							eventBus.fireEvent(messageEvent);
							addToPopupSlot(notifyPresenter);
						}
					} else {
						// Logger logger = Logger.getLogger("MS");
						// logger.log(Level.SEVERE, "Month start " +
						// tempEvent.getStart().getMonth());
						// logger.log(Level.SEVERE, "Month New " +
						// currentDate.getMonth());
						if (tempEvent.getStart().getMonth() == currentDate.getMonth()) {

							addWorkdaysUC(event.getMessage(), tempEvent.getStart().getDate(), monthLength());
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
						// Logger logger = Logger.getLogger("MS");
						// logger.log(Level.SEVERE,
						// "Hani Lenna " + tokens[0] + tokens[1] + load +
						// tempEvent.getStart().getDate());
						// logger.log(Level.SEVERE, "Month start " +
						// tempEvent.getStart().getMonth());
						// logger.log(Level.SEVERE, "Month New " +
						// currentDate.getMonth());
						if (addWorkDay(tokens[0], tokens[1], load, tempEvent.getStart().getDate())
								&& tempEvent.getStart().getMonth() == currentDate.getMonth())
							addEvents(tempEvent.getStart(), tempEvent.getEnd(), event.getMessage(), 5);
						else {
							messageEvent = new PopupMessageEvent("Erreur de saisie.");
							eventBus.fireEvent(messageEvent);
							addToPopupSlot(notifyPresenter);
						}

					} else {
						// Logger logger = Logger.getLogger("MS");
						// logger.log(Level.SEVERE, "Month start " +
						// tempEvent.getStart().getMonth());
						// logger.log(Level.SEVERE, "Month New " +
						// currentDate.getMonth());
						if (tempEvent.getStart().getMonth() == currentDate.getMonth()) {
							addWorkdaysUC(event.getMessage(), tempEvent.getStart().getDate(),
									tempEvent.getEnd().getDate() - 1);
							addEvents(tempEvent.getStart(), tempEvent.getEnd(), event.getMessage(), 1);
						} else {
							messageEvent = new PopupMessageEvent("Erreur de saisie.");
							eventBus.fireEvent(messageEvent);
							addToPopupSlot(notifyPresenter);
						}
					}
				}
				// Logger logger = Logger.getLogger("MS");
				// logger.log(Level.SEVERE, "The Work List " + daysUC);
				// logger.log(Level.SEVERE, "The validationUC List " +
				// validationUC);
			}
		}
	};

	private ConfirmHandler confirmHandler = new ConfirmHandler() {

		@Override
		public void onConfirm(ConfirmEvent event) {
			if (daysUC != null) {
				if (event.getMessage().equals("ok")) {
					if (event.getSender().equals("AlterCram")) {
						removeEvent(event.getDate());
						configuringCalendar.removeEvent(event.getId());
					}
				}
			}

		}
	};

	protected void initConges() {
		dispatchAsync.execute(new RetrieveCongesAction(currentUser.getColleageEmail()),
				new AsyncCallback<RetrieveCongesResult>() {

					@Override
					public void onFailure(Throwable arg0) {
						Window.alert("Error");

					}

					@Override
					public void onSuccess(RetrieveCongesResult result) {
						for (CongesWrapper c : result.getResult()) {
							if (c.getStatus().equals("Validé")) {
								if ((c.getStartDate().getYear() == currentDate.getYear())
										|| ((c.getStartDate().getYear() == currentDate.getYear() - 1)
												&& (c.getEndDate().getYear() == currentDate.getYear()))) {
									if ((c.getStartDate().getMonth() == currentDate.getMonth())
											&& (currentDate.getMonth() == c.getEndDate().getMonth())) {
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
													validationUC.get(c.getStartDate().getDate() - 1)
															.addClientName(c.getType());
													validationUC.get(c.getStartDate().getDate() - 1)
															.addDayLoad(c.getStartD());
													validationUC.get(c.getStartDate().getDate() - 1)
															.addLoad((float) 0.5);
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
													validationUC.get(c.getStartDate().getDate() - 1)
															.addClientName(c.getType());
													validationUC.get(c.getStartDate().getDate() - 1)
															.addDayLoad("Journée");
													validationUC.get(c.getStartDate().getDate() - 1).addLoad((float) 1);
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
													validationUC.get(c.getStartDate().getDate() - 1)
															.addClientName(c.getType());
													validationUC.get(c.getStartDate().getDate() - 1)
															.addDayLoad("Journée");
													validationUC.get(c.getStartDate().getDate() - 1).addLoad((float) 1);
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
													validationUC.get(c.getStartDate().getDate() - 1)
															.addClientName(c.getType());
													validationUC.get(c.getStartDate().getDate() - 1)
															.addDayLoad(c.getStartD());
													validationUC.get(c.getStartDate().getDate() - 1)
															.addLoad((float) 0.5);
												}
											}
											index++;
											while (index < c.getEndDate().getDate() - 1) {
												Event event = new Event("" + System.currentTimeMillis(), c.getType());
												Date date = new Date(currentDate.getTime());
												// date.setYear(currentDate.getYear());
												// date.setMonth(currentDate.getMonth());
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
													validationUC.get(date.getDate() - 1).addClientName(c.getType());
													validationUC.get(date.getDate() - 1).addDayLoad("Journée");
													validationUC.get(date.getDate() - 1).addLoad((float) 1);
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
													validationUC.get(c.getEndDate().getDate() - 1)
															.addClientName(c.getType());
													validationUC.get(c.getEndDate().getDate() - 1)
															.addDayLoad(c.getEndD());
													validationUC.get(c.getEndDate().getDate() - 1).addLoad((float) 0.5);
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
													validationUC.get(c.getEndDate().getDate() - 1)
															.addClientName(c.getType());
													validationUC.get(c.getEndDate().getDate() - 1)
															.addDayLoad("Journée");
													validationUC.get(c.getEndDate().getDate() - 1).addLoad((float) 1);
												}
											}

										}
									} else if (c.getStartDate().getMonth() == currentDate.getMonth()) {
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
												validationUC.get(c.getStartDate().getDate() - 1)
														.addClientName(c.getType());
												validationUC.get(c.getStartDate().getDate() - 1).addDayLoad("Journée");
												validationUC.get(c.getStartDate().getDate() - 1).addLoad((float) 1);
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
												validationUC.get(c.getStartDate().getDate() - 1)
														.addClientName(c.getType());
												validationUC.get(c.getStartDate().getDate() - 1)
														.addDayLoad(c.getStartD());
												validationUC.get(c.getStartDate().getDate() - 1).addLoad((float) 0.5);
											}
										}
										index++;
										while (index < monthLength()) {
											Event event = new Event("" + System.currentTimeMillis(), c.getType());
											Date date = new Date(currentDate.getTime());
											// date.setYear(currentDate.getYear());
											// date.setMonth(currentDate.getMonth());
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
												validationUC.get(date.getDate() - 1).addClientName(c.getType());
												validationUC.get(date.getDate() - 1).addDayLoad("Journée");
												validationUC.get(date.getDate() - 1).addLoad((float) 1);
											}
											index++;
										}

									} else if (c.getEndDate().getMonth() == currentDate.getMonth()) {
										int index = 0;
										while (index < c.getEndDate().getDate() - 1) {
											Event event = new Event("" + System.currentTimeMillis(), c.getType());
											Date date = new Date(currentDate.getTime());
											// date.setYear(currentDate.getYear());
											// date.setMonth(currentDate.getMonth());
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
												validationUC.get(date.getDate() - 1).addClientName(c.getType());
												validationUC.get(date.getDate() - 1).addDayLoad("Journée");
												validationUC.get(date.getDate() - 1).addLoad((float) 1);
											}
											index++;
										}
										if (c.getEndD().equals("Matin")) {
											Event event = new Event("" + System.currentTimeMillis(), c.getType());
											Date date = new Date(currentDate.getTime());
											// date.setYear(currentDate.getYear());
											// date.setMonth(currentDate.getMonth());
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
												validationUC.get(date.getDate() - 1).addClientName(c.getType());
												validationUC.get(date.getDate() - 1).addDayLoad(c.getEndD());
												validationUC.get(date.getDate() - 1).addLoad((float) 0.5);
											}
										} else if (c.getEndD().equals("Après-midi")) {
											Event event = new Event("" + System.currentTimeMillis(), c.getType());
											Date date = new Date(currentDate.getTime());
											// date.setYear(currentDate.getYear());
											// date.setMonth(currentDate.getMonth());
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
												validationUC.get(date.getDate() - 1).addClientName(c.getType());
												validationUC.get(date.getDate() - 1).addDayLoad("Journée");
												validationUC.get(date.getDate() - 1).addLoad((float) 1);
											}
										}

									}
								}
							}
						}
					}

				});
	}

	protected void updateDate() {
		currentDate.setYear(Integer.valueOf((getView().getYearBox().getSelectedValue())));
		if (getView().getMonthBox().getSelectedValue().equals("Janvier"))
			currentDate.setMonth(0);
		if (getView().getMonthBox().getSelectedValue().equals("Février"))
			currentDate.setMonth(1);
		if (getView().getMonthBox().getSelectedValue().equals("Mars"))
			currentDate.setMonth(2);
		if (getView().getMonthBox().getSelectedValue().equals("Avril"))
			currentDate.setMonth(3);
		if (getView().getMonthBox().getSelectedValue().equals("Mai"))
			currentDate.setMonth(4);
		if (getView().getMonthBox().getSelectedValue().equals("Juin"))
			currentDate.setMonth(5);
		if (getView().getMonthBox().getSelectedValue().equals("Juillet"))
			currentDate.setMonth(6);
		if (getView().getMonthBox().getSelectedValue().equals("Aout"))
			currentDate.setMonth(7);
		if (getView().getMonthBox().getSelectedValue().equals("Septembre"))
			currentDate.setMonth(8);
		if (getView().getMonthBox().getSelectedValue().equals("Octobre"))
			currentDate.setMonth(9);
		if (getView().getMonthBox().getSelectedValue().equals("Novembre"))
			currentDate.setMonth(10);
		if (getView().getMonthBox().getSelectedValue().equals("Décembre"))
			currentDate.setMonth(11);

	}

}
