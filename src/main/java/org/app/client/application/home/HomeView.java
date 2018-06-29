package org.app.client.application.home;

import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Tab;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class HomeView extends ViewImpl implements HomePresenter.MyView {

	interface Binder extends UiBinder<Widget, HomeView> {
	}

	@UiField Tab showCongesTab;
	@UiField HTMLPanel showCongesPanel;
	@UiField Tab cramTab;
	@UiField Tab congesTab;
	@UiField HTMLPanel calendarPanel;
	@UiField HTMLPanel congesPanel;
//	@UiField Heading userName;
	@UiField Button saveCram;
	@UiField Button notifyCram;
	@UiField ControlGroup notifyGroup;
	@UiField HelpInline notifyErrors;
	
	public HelpInline getNotifyErrors() {
		return notifyErrors;
	}
	public ControlGroup getNotifyGroup() {
		return notifyGroup;
	}
	
	public Tab getCongesTab() {
		return congesTab;
	}
	public Button getNotifyCram() {
		return notifyCram;
	}
	
	public Tab getCramTab() {
		return cramTab;
	}
	public Button getSaveCram() {
		return saveCram;
	}
	
//	public Heading getUserName() {
//		return userName;
//	}
//	
	public HTMLPanel getCalendarPanel() {
		return calendarPanel;
	}

	@Inject
	HomeView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
        if (slot == HomePresenter.SLOT_Conges) {
        	congesPanel.clear();
            if(content != null){
            	congesPanel.add(content);
            }
        }else if(slot == HomePresenter.Slot_ShowConge){
        	showCongesPanel.clear();
        	if(content !=null){
        		showCongesPanel.add(content);
        	}
        	
        }else{
            super.setInSlot(slot, content);
        }
	}


}