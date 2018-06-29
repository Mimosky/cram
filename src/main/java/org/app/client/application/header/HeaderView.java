package org.app.client.application.header;

import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class HeaderView extends ViewImpl implements HeaderPresenter.MyView {
    interface Binder extends UiBinder<Widget, HeaderView> {
    }

    @UiField
    HTMLPanel main;
    @UiField
    HTMLPanel contentPanel;
	@UiField
	NavLink home;
	@UiField
	NavLink admin;
	@UiField
	NavLink deconnexion;
	@UiField
	NavLink resetPassword;
	@UiField
	Modal modifyModal;
	@UiField
	PasswordTextBox passwordModify;
	@UiField
	Button modifyButton;
	@UiField
	ControlGroup passwordGroup;
	@UiField 
	HelpInline passwordHelpInline;
    @Inject
    HeaderView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == HeaderPresenter.SLOT_Header) {
        	contentPanel.clear();
            if(content != null){
            	contentPanel.add(content);
            }
        } else {
            super.setInSlot(slot, content);
        }
    }
    
	public HTMLPanel getMain() {
		return main;
	}
	public NavLink getHome() {
		return home;
	}

	public NavLink getAdmin() {
		return admin;
	}

	public NavLink getDeconnexion() {
		return deconnexion;

	}
	public NavLink getResetPassword() {
		return resetPassword;
	}
	public Modal getModifyModal() {
		return modifyModal;
	}
	public PasswordTextBox getPasswordModify() {
		return passwordModify;
	}
	public Button getModifyButton() {
		return modifyButton;
	}
	public ControlGroup getPasswordGroup() {
		return passwordGroup;
	}
	public HelpInline getPasswordHelpInline() {
		return passwordHelpInline;
	}
}