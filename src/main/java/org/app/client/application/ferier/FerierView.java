package org.app.client.application.ferier;

import javax.inject.Inject;

import org.app.shared.wrapper.FerierWrapper;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.datepicker.client.ui.DateBox;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class FerierView extends ViewImpl implements FerierPresenter.MyView {
    interface Binder extends UiBinder<Widget, FerierView> {
    }

    @UiField
    HTMLPanel main;
    @UiField DateBox ferier;
    @UiField ListBox listBox;
    @UiField CellTable<FerierWrapper> ferierTable;
    @UiField Button addFerier;
    
    public Button getAddFerier() {
		return addFerier;
	}
    public DateBox getFerier() {
		return ferier;
	}
    public ListBox getListBox() {
		return listBox;
	}
    public CellTable<FerierWrapper> getFerierTable() {
		return ferierTable;
	}

    @Inject
    FerierView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}