package org.app.client.application.initconges;

import javax.inject.Inject;

import org.app.shared.wrapper.UserWrapper;

import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.DataGrid;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class InitCongesView extends ViewImpl implements InitCongesPresenter.MyView {
    interface Binder extends UiBinder<Widget, InitCongesView> {
    }

    @UiField
    HTMLPanel main;
    @UiField CellTable<UserWrapper> congesTable;

    @Inject
    InitCongesView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    public CellTable<UserWrapper> getCongesTable() {
		return congesTable;
	}
}