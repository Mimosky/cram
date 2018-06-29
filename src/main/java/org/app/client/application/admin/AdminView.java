package org.app.client.application.admin;

import com.github.gwtbootstrap.client.ui.Tab;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class AdminView extends ViewImpl implements AdminPresenter.MyView {
	public interface Binder extends UiBinder<Widget, AdminView> {
	}

	@Inject
	AdminView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	HTMLPanel collaborateurPanel;
	@UiField
	HTMLPanel clientPanel;
	@UiField
	HTMLPanel congesPanel;
	@UiField HTMLPanel ferierPanel;
	@UiField HTMLPanel groupPanel;
	@UiField HTMLPanel showCongesPanel;
	@UiField HTMLPanel showCramPanel;
	@UiField Tab tabCalendar;
	@UiField Tab tabCram;
	@UiField HTMLPanel alterCramPanel;
	@UiField HTMLPanel initCongesPanel;
	
	
	public Tab getTabCram() {
		return tabCram;
	}
	
	public Tab getTabCalendar() {
		return tabCalendar;
	}

	public HTMLPanel getCongesPanel() {
		return congesPanel;
	}

	public HTMLPanel getModalCollaborateurPanel() {
		return collaborateurPanel;
	}

	public HTMLPanel getModalClientPanel() {
		return clientPanel;
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == AdminPresenter.SLOT_Modal) {
			collaborateurPanel.clear();
			if (content != null) {
				collaborateurPanel.add(content);
			}
		} else if (slot == AdminPresenter.SLOT_ClientModal) {
			clientPanel.clear();
			if (content != null) {
				clientPanel.add(content);
			}
		}else if (slot == AdminPresenter.SLOT_Group) {
			groupPanel.clear();
			if (content != null) {
				groupPanel.add(content);
			}
		} else if (slot == AdminPresenter.SLOT_CongesModal) {
			congesPanel.clear();
			if (content != null) {
				congesPanel.add(content);
			}
		} else if (slot ==AdminPresenter.SLOT_Ferier){
        	ferierPanel.clear();
        	if(content != null){
        		ferierPanel.add(content);
        	}
        }else if (slot ==AdminPresenter.SLOT_ShowConges){
        	showCongesPanel.clear();
        	if(content != null){
        		showCongesPanel.add(content);
        	}
        }else if (slot ==AdminPresenter.SLOT_ShowCram){
        	showCramPanel.clear();
        	if(content != null){
        		showCramPanel.add(content);
        	}
        }else if (slot ==AdminPresenter.SLOT_AlterCram){
        	alterCramPanel.clear();
        	if(content != null){
        		alterCramPanel.add(content);
        	}
        }else if (slot ==AdminPresenter.SLOT_InitConges){
        	initCongesPanel.clear();
        	if(content != null){
        		initCongesPanel.add(content);
        	}
        }else {
			super.setInSlot(slot, content);
		}
	}

}
