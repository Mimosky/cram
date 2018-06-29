package org.app.client.application.congesvalidator;

import javax.inject.Inject;

import org.app.shared.wrapper.CongesWrapper;

import com.github.gwtbootstrap.client.ui.CellTable;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class CongesValidatorView extends ViewImpl implements CongesValidatorPresenter.MyView {
    interface Binder extends UiBinder<Widget, CongesValidatorView> {
    }

    @UiField HTMLPanel main;
    @UiField CellTable<CongesWrapper> congesTable;

    @Inject
    CongesValidatorView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
    public CellTable<CongesWrapper> getCongesTable() {
		return congesTable;
	}
}