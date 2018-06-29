package org.app.client.application.conges;

import javax.inject.Inject;

import org.app.shared.Conges;

import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.RadioButton;
import com.github.gwtbootstrap.client.ui.SubmitButton;
import com.github.gwtbootstrap.datepicker.client.ui.DateBox;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class CongesView extends ViewImpl implements CongesPresenter.MyView {
    interface Binder extends UiBinder<Widget, CongesView> {
    }

    @UiField HTMLPanel main;
    @UiField CellTable<Conges> congesTable;
	@UiField RadioButton startMRadio;
	@UiField RadioButton startARadio;
	@UiField RadioButton endMRadio;
	@UiField RadioButton endARadio;
	@UiField ListBox listBox;
	@UiField ControlGroup congesStartControlGroup;
	@UiField DateBox congesStart;
	@UiField HelpInline congesStartHelpInline;
	@UiField ControlGroup congesEndControlGroup;
	@UiField DateBox congesEnd;
	@UiField HelpInline congesEndHelpInline;
	@UiField SubmitButton saveButton;
	@UiField ControlGroup radioEndControlGroup;
	@UiField HelpInline radioEndHelpInline;
	@UiField Label cpLabel;
	@UiField Label soldeLabel;
	@UiField Label pCpLabel;
	@UiField Label pSoldeLabel;
	@UiField Label rttPris;
	@UiField Label rttSolde;
	@UiField Label congesPris;
	@UiField Label conges;
	@UiField Label n_1Label;
	@UiField Label nLabel;
	
	public Label getN_1Label() {
		return n_1Label;
	}
	public Label getnLabel() {
		return nLabel;
	}

	public Label getRttPris() {
		return rttPris;
	}
	public Label getRttSolde() {
		return rttSolde;
	}
	public void setRttPris(Label rttPris) {
		this.rttPris = rttPris;
	}
	public void setRttSolde(Label rttSolde) {
		this.rttSolde = rttSolde;
	}
	public Label getConges() {
		return conges;
	}
	public void setConges(Label conges) {
		this.conges = conges;
	}
	public Label getCongesPris() {
		return congesPris;
	}
	public void setCongesPris(Label congesPris) {
		this.congesPris = congesPris;
	}
	
	public Label getpCpLabel() {
		return pCpLabel;
	}
	public Label getPSoldeLabel() {
		return pSoldeLabel;
	}
	public Label getCpLabel() {
		return cpLabel;
	}
	public Label getSoldeLabel() {
		return soldeLabel;
	}
	public ControlGroup getRadioEndControlGroup() {
		return radioEndControlGroup;
	}
	public HelpInline getRadioEndHelpInline() {
		return radioEndHelpInline;
	}
    public CellTable<Conges> getCongesTable() {
		return congesTable;
	}
	public RadioButton getStartMRadio() {
		return startMRadio;
	}
	public RadioButton getStartARadio() {
		return startARadio;
	}
	public RadioButton getEndMRadio() {
		return endMRadio;
	}
	public RadioButton getEndARadio() {
		return endARadio;
	}
	public ListBox getListBox() {
		return listBox;
	}
	public ControlGroup getCongesStartControlGroup() {
		return congesStartControlGroup;
	}
	public DateBox getCongesStart() {
		return congesStart;
	}
	public HelpInline getCongesStartHelpInline() {
		return congesStartHelpInline;
	}
	public ControlGroup getCongesEndControlGroup() {
		return congesEndControlGroup;
	}
	public DateBox getCongesEnd() {
		return congesEnd;
	}
	public HelpInline getCongesEndHelpInline() {
		return congesEndHelpInline;
	}
	public SubmitButton getSaveButton() {
		return saveButton;
	}

	@Inject
    CongesView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
    

}