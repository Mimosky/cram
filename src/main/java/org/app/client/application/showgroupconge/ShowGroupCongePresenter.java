package org.app.client.application.showgroupconge;

import java.util.ArrayList;
import java.util.Date;

import org.app.client.application.toolbox.CurrentUser;
import org.app.shared.dispatch.RetrieveCongesAction;
import org.app.shared.dispatch.RetrieveCongesResult;
import org.app.shared.dispatch.RetrieveFerierAction;
import org.app.shared.dispatch.RetrieveFerierResult;
import org.app.shared.dispatch.RetrieveGroupAction;
import org.app.shared.dispatch.RetrieveGroupResult;
import org.app.shared.wrapper.CongesWrapper;
import org.app.shared.wrapper.FerierWrapper;
import org.app.shared.wrapper.GroupWrapper;
import org.app.shared.wrapper.UserWrapper;
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
import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.client.RpcDispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class ShowGroupCongePresenter
		extends Presenter<ShowGroupCongePresenter.MyView, ShowGroupCongePresenter.MyProxy> {
	interface MyView extends View {
		public Button getPreviousButton();

		public Button getNextButton();

		public HTMLPanel getCalendarPanel();

		public Label getMessage();

		public Button getDisplay();
	}

	@ProxyStandard
	interface MyProxy extends Proxy<ShowGroupCongePresenter> {
	}

	@Inject
	ShowGroupCongePresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, RevealType.Root);

	}

	private Date globalDate;
	private ArrayList<UserWrapper> users;
	private ArrayList<Integer> ferier;
	private FullCalendar configuringCalendar;
	private String myGroup;
	@Inject
	RpcDispatchAsync dispatchAsync;
	@Inject
	CurrentUser currentUser;

	@Override
	protected void onBind() {
		super.onBind();
		myGroup = "";
		globalDate = new Date();
		configureCalendar();
		configuringCalendar.today();
		// initListBox();
		myGroup();
		initButton();

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
			}

			@Override
			public void dayClick(JavaScriptObject moment, NativeEvent event, JavaScriptObject viewObject) {
			}
		});

		config.setClickHoverConfig(clickHover);

		// Configure when I click on an empty Cell
		SelectConfig selectConfig = new SelectConfig(new SelectEventCallback() {

			@Override
			public void select(JavaScriptObject start, JavaScriptObject end, NativeEvent event,
					JavaScriptObject viewObject) {
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



	protected void initCalendar() {
		users = new ArrayList<UserWrapper>();
		configuringCalendar.removeAllEvents();
		dispatchAsync.execute(new RetrieveGroupAction("All"), new AsyncCallback<RetrieveGroupResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(RetrieveGroupResult result) {
				for (GroupWrapper g : result.getGroup()) {
					if (g.getName().equals(myGroup)) {
						for (UserWrapper u : g.getCollaborateurs()) {
							users.add(u);
						}
						initConges();
					} 
				}
				
			}
		});
	}

	protected void initButton() {
		getView().getDisplay().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				configuringCalendar.today();
				globalDate = configuringCalendar.getDate();
				initFerier();
				initCalendar();

			}
		});
		getView().getNextButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				configuringCalendar.next();
				globalDate = configuringCalendar.getDate();
				initFerier();
				initCalendar();

			}
		});
		getView().getPreviousButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				configuringCalendar.previous();
				globalDate = configuringCalendar.getDate();
				initFerier();
				initCalendar();
			}
		});
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

					if (((globalDate.getYear() == Fw.getDate().getYear()))
							&& (globalDate.getMonth() == Fw.getDate().getMonth())) {
						Event blocked = new Event("some_event_id", "");
						blocked.setStart(Fw.getDate());
						blocked.setAllDay(true);
						blocked.setOverlap(false);
						blocked.setRendering("background");
						blocked.setBackgroundColor("#ff9f89");
						configuringCalendar.addEvent(blocked);
						ferier.add(Fw.getDate().getDate());
					}
				}
			}
		});
	}

	protected void initConges() {
		for (UserWrapper user : users) {
			dispatchAsync.execute(new RetrieveCongesAction(user.getUserEmail()),
					new AsyncCallback<RetrieveCongesResult>() {

						@Override
						public void onFailure(Throwable arg0) {
							Window.alert("Error");

						}

						@Override
						public void onSuccess(RetrieveCongesResult result) {
							for (CongesWrapper c : result.getResult()) {
								if ((c.getStatus().equals("Validé")) || (c.getStatus().equals("En Cours"))) {
									if ((c.getStartDate().getYear() == globalDate.getYear())
											|| ((c.getStartDate().getYear() == globalDate.getYear() - 1)
													&& (c.getEndDate().getYear() == globalDate.getYear()))) {
										if ((c.getStartDate().getMonth() == globalDate.getMonth())
												&& (globalDate.getMonth() == c.getEndDate().getMonth())) {
											if (c.getStartDate().compareTo(c.getEndDate()) == 0) {
												if (c.getEndD().equals(c.getStartD())) {
													if (c.getStartDate().getDay() == 0 || c.getStartDate().getDay() == 6
															|| ferier.contains(c.getStartDate().getDate())) {
														// Do Nothing
													} else {
														Event event = new Event("" + System.currentTimeMillis(),
																user.getUserName());
														event.setStart(c.getStartDate());
														event.setEnd(c.getEndDate());
														event.setAllDay(true);
														if (c.getStatus().equals("Validé"))
															event.setColor("#257e4a");
														else
															event.setColor("#ff7a33");
														event.setConstraint("availableForMeeting");
														configuringCalendar.addEvent(event);
													}
												} else {
													if (c.getStartDate().getDay() == 0 || c.getStartDate().getDay() == 6
															|| ferier.contains(c.getStartDate().getDate())) {
														// Do Nothing
													} else {
														Event event = new Event("" + System.currentTimeMillis(),
																user.getUserName());
														event.setStart(c.getStartDate());
														event.setEnd(c.getEndDate());
														event.setAllDay(true);
														if (c.getStatus().equals("Validé"))
															event.setColor("#257e4a");
														else
															event.setColor("#ff7a33");
														event.setConstraint("availableForMeeting");
														configuringCalendar.addEvent(event);
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
																user.getUserName());
														event.setStart(c.getStartDate());
														event.setEnd(c.getStartDate());
														event.setAllDay(true);
														if (c.getStatus().equals("Validé"))
															event.setColor("#257e4a");
														else
															event.setColor("#ff7a33");
														event.setConstraint("availableForMeeting");
														configuringCalendar.addEvent(event);
													}
												} else {
													if (c.getStartDate().getDay() == 0 || c.getStartDate().getDay() == 6
															|| ferier.contains(c.getStartDate().getDate())) {
														// Do Nothing
													} else {
														Event event = new Event("" + System.currentTimeMillis(),
																user.getUserName());
														event.setStart(c.getStartDate());
														event.setEnd(c.getStartDate());
														event.setAllDay(true);
														if (c.getStatus().equals("Validé"))
															event.setColor("#257e4a");
														else
															event.setColor("#ff7a33");
														event.setConstraint("availableForMeeting");
														configuringCalendar.addEvent(event);
													}
												}
												index++;
												while (index < c.getEndDate().getDate() - 1) {
													Event event = new Event("" + System.currentTimeMillis(),
															user.getUserName());
													Date date = new Date(globalDate.getTime());
													// date.set
													date.setDate(index + 1);
													if (date.getDay() == 0 || date.getDay() == 6
															|| ferier.contains(date.getDate())) {
														// Do Nothing
													} else {
														event.setStart(date);
														event.setEnd(date);
														event.setAllDay(true);
														if (c.getStatus().equals("Validé"))
															event.setColor("#257e4a");
														else
															event.setColor("#ff7a33");
														event.setConstraint("availableForMeeting");
														configuringCalendar.addEvent(event);
													}
													index++;
												}
												if (c.getEndD().equals("Matin")) {
													if (c.getEndDate().getDay() == 0 || c.getEndDate().getDay() == 6
															|| ferier.contains(c.getEndDate().getDate())) {
														// Do Nothing
													} else {
														Event event = new Event("" + System.currentTimeMillis(),
																user.getUserName());
														event.setStart(c.getEndDate());
														event.setEnd(c.getEndDate());
														event.setAllDay(true);
														if (c.getStatus().equals("Validé"))
															event.setColor("#257e4a");
														else
															event.setColor("#ff7a33");
														event.setConstraint("availableForMeeting");
														configuringCalendar.addEvent(event);
													}
												} else {
													if (c.getEndDate().getDay() == 0 || c.getEndDate().getDay() == 6
															|| ferier.contains(c.getEndDate().getDate())) {
														// Do Nothing
													} else {
														Event event = new Event("" + System.currentTimeMillis(),
																user.getUserName());
														event.setStart(c.getEndDate());
														event.setEnd(c.getEndDate());
														event.setAllDay(true);
														if (c.getStatus().equals("Validé"))
															event.setColor("#257e4a");
														else
															event.setColor("#ff7a33");
														event.setConstraint("availableForMeeting");
														configuringCalendar.addEvent(event);
													}
												}

											}
										} else if (c.getStartDate().getMonth() == globalDate.getMonth()) {
											int index = c.getStartDate().getDate() - 1;
											if (c.getStartD().equals("Matin")) {
												if (c.getStartDate().getDay() == 0 || c.getStartDate().getDay() == 6
														|| ferier.contains(c.getStartDate().getDate())) {
													// Do Nothing
												} else {
													Event event = new Event("" + System.currentTimeMillis(),
															user.getUserName());
													event.setStart(c.getStartDate());
													event.setEnd(c.getStartDate());
													event.setAllDay(true);
													if (c.getStatus().equals("Validé"))
														event.setColor("#257e4a");
													else
														event.setColor("#ff7a33");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
												}
											} else {
												if (c.getStartDate().getDay() == 0 || c.getStartDate().getDay() == 6
														|| ferier.contains(c.getStartDate().getDate())) {
													// Do Nothing
												} else {
													Event event = new Event("" + System.currentTimeMillis(),
															user.getUserName());
													event.setStart(c.getStartDate());
													event.setEnd(c.getStartDate());
													event.setAllDay(true);
													if (c.getStatus().equals("Validé"))
														event.setColor("#257e4a");
													else
														event.setColor("#ff7a33");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
												}
											}
											index++;
											while (index < monthLength()) {
												Event event = new Event("" + System.currentTimeMillis(),
														user.getUserName());
												Date date = new Date(globalDate.getTime());
												date.setDate(index + 1);
												if (date.getDay() == 0 || date.getDay() == 6
														|| ferier.contains(date.getDate())) {
													// Do Nothing
												} else {
													event.setStart(date);
													event.setEnd(date);
													event.setAllDay(true);
													if (c.getStatus().equals("Validé"))
														event.setColor("#257e4a");
													else
														event.setColor("#ff7a33");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
												}
												index++;
											}

										} else if (c.getEndDate().getMonth() == globalDate.getMonth()) {
											int index = 0;
											while (index < c.getEndDate().getDate() - 1) {
												Event event = new Event("" + System.currentTimeMillis(),
														user.getUserName());
												Date date = new Date(globalDate.getTime());
												date.setDate(index + 1);
												if (date.getDay() == 0 || date.getDay() == 6
														|| ferier.contains(date.getDate())) {
													// Do Nothing
												} else {
													event.setStart(date);
													event.setEnd(date);
													event.setAllDay(true);
													if (c.getStatus().equals("Validé"))
														event.setColor("#257e4a");
													else
														event.setColor("#ff7a33");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
												}
												index++;
											}
											if (c.getEndD().equals("Matin")) {
												Event event = new Event("" + System.currentTimeMillis(),
														user.getUserName());
												Date date = new Date(globalDate.getTime());
												date.setDate(index + 1);
												if (date.getDay() == 0 || date.getDay() == 6
														|| ferier.contains(date.getDate())) {
													// Do Nothing
												} else {
													event.setStart(date);
													event.setEnd(date);
													event.setAllDay(true);
													if (c.getStatus().equals("Validé"))
														event.setColor("#257e4a");
													else
														event.setColor("#ff7a33");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
												}
											} else if (c.getEndD().equals("Après-midi")) {
												Event event = new Event("" + System.currentTimeMillis(),
														user.getUserName());
												Date date = new Date(globalDate.getTime());
												date.setDate(index + 1);
												if (date.getDay() == 0 || date.getDay() == 6
														|| ferier.contains(date.getDate())) {
													// Do Nothing
												} else {
													event.setStart(date);
													event.setEnd(date);
													event.setAllDay(true);
													if (c.getStatus().equals("Validé"))
														event.setColor("#257e4a");
													else
														event.setColor("#ff7a33");
													event.setConstraint("availableForMeeting");
													configuringCalendar.addEvent(event);
												}
											}

										}
									}
								}
							}
						}

					});
		}
	}

	protected int monthLength() {
		// Date date = new Date();

		int listLength;
		// Année Bissextile
		if (globalDate.getMonth() == 1 && (globalDate.getYear() % 4) == 0) {
			listLength = 29;
		} else if (globalDate.getMonth() == 1 && (globalDate.getYear() % 4) != 0) {
			listLength = 28;
		} else if (globalDate.getMonth() == 0 || (globalDate.getMonth() == 2) || (globalDate.getMonth() == 4)
				|| (globalDate.getMonth() == 6) || (globalDate.getMonth() == 7) || (globalDate.getMonth() == 9)
				|| (globalDate.getMonth() == 11)) {
			listLength = 31;
		} else {
			listLength = 30;
		}
		return listLength;
	}

	protected void myGroup() {
		myGroup = "";
		dispatchAsync.execute(new RetrieveGroupAction("All"), new AsyncCallback<RetrieveGroupResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(RetrieveGroupResult result) {
				for (GroupWrapper g : result.getGroup()) {
					for (UserWrapper u : g.getCollaborateurs()) {
						if (u.getUserEmail().equals(currentUser.getLogin())) {
							myGroup = g.getName();
						}
					}
				}
				if (myGroup.equals("")) {
					getView().getDisplay().setEnabled(false);
					getView().getNextButton().setEnabled(false);
					getView().getPreviousButton().setEnabled(false);
					getView().getMessage().setText("Vous n'appartenez à aucun groupe !");
				} else {
					//initConges();
				}
			}
		});

	}

}