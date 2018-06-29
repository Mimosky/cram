package org.app.client.application.admin;

import org.app.client.application.allconges.AllCongesPresenter;
import org.app.client.application.altercram.AlterCramPresenter;
import org.app.client.application.client.ClientPresenter;
import org.app.client.application.congesvalidator.CongesValidatorPresenter;
import org.app.client.application.ferier.FerierPresenter;
import org.app.client.application.groups.GroupsPresenter;
import org.app.client.application.header.HeaderPresenter;
import org.app.client.application.initconges.InitCongesPresenter;
import org.app.client.application.toolbox.CurrentUser;
import org.app.client.application.user.UserPresenter;
import org.app.client.application.usercram.UserCramPresenter;
import org.app.client.place.NameTokens;

import com.github.gwtbootstrap.client.ui.Tab;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class AdminPresenter extends Presenter<AdminPresenter.MyView, AdminPresenter.MyProxy> {

	public interface MyView extends View {
		public HTMLPanel getModalCollaborateurPanel();

		public HTMLPanel getModalClientPanel();
		public Tab getTabCalendar();
	}

	private CurrentUser currentUser;

	@NameToken(NameTokens.admin)
	@ProxyCodeSplit
	//@NoGatekeeper
	interface MyProxy extends ProxyPlace<AdminPresenter> {
	}

	@Inject
	AdminPresenter(EventBus eventBus, MyView view, MyProxy proxy, CurrentUser currentUser) {
		super(eventBus, view, proxy, HeaderPresenter.SLOT_Header);
		this.currentUser = currentUser;
	}

	public static final NestedSlot SLOT_Modal = new NestedSlot();
	public static final NestedSlot SLOT_ClientModal = new NestedSlot();
	public static final NestedSlot SLOT_CongesModal = new NestedSlot();
	public static final NestedSlot SLOT_Ferier = new NestedSlot();
	public static final NestedSlot SLOT_Group = new NestedSlot();
	public static final NestedSlot SLOT_ShowConges = new NestedSlot();
	public static final NestedSlot SLOT_ShowCram = new NestedSlot();
	public static final NestedSlot SLOT_AlterCram = new NestedSlot();
	public static final NestedSlot SLOT_InitConges = new NestedSlot();
	@Inject
	PlaceManager placeManager;

	
	@Inject
	UserPresenter userPresenter;
	@Inject
	ClientPresenter clientPresenter;
	@Inject 
	CongesValidatorPresenter congesValidatorPresenter;
	@Inject 
	GroupsPresenter groupPresenter;
	@Inject
	FerierPresenter ferierPresenter;
	@Inject
	AllCongesPresenter allCongesPresenter;
	@Inject
	UserCramPresenter userCramPresenter;
	@Inject
	AlterCramPresenter alterCramPresenter;
	@Inject 
	InitCongesPresenter initCongesPresenter;
	
	@Override
	protected void onBind() {
		super.onBind();
		
		if (!currentUser.isAdministrator()) {
			PlaceRequest request = new PlaceRequest.Builder().nameToken(NameTokens.home).build();
			placeManager.revealPlace(request);
		}
		
		getView().getTabCalendar().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				allCongesPresenter.forceReveal();
			}
		});
	}

	@Override
	protected void onReset() {
		super.onReset();
		if (!currentUser.isAdministrator()) {
			PlaceRequest request = new PlaceRequest.Builder().nameToken(NameTokens.home).build();
			placeManager.revealPlace(request);
		}

		setInSlot(SLOT_CongesModal,congesValidatorPresenter);
		setInSlot(SLOT_Ferier, ferierPresenter);
		setInSlot(SLOT_Group, groupPresenter);
		setInSlot(SLOT_ShowConges, allCongesPresenter);
		setInSlot(SLOT_ShowCram, userCramPresenter);
		setInSlot(SLOT_Modal, userPresenter);
		setInSlot(SLOT_ClientModal, clientPresenter);
		setInSlot(SLOT_AlterCram, alterCramPresenter);
		setInSlot(SLOT_InitConges, initCongesPresenter);

	}
}
