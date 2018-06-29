package org.app.client.application.addwork;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AddWorkModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
            bindPresenterWidget(AddWorkPresenter.class, AddWorkPresenter.MyView.class, AddWorkView.class);
    }
}