package org.app.client.application.validconge;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ValidCongeModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
            bindPresenterWidget(ValidCongePresenter.class, ValidCongePresenter.MyView.class, ValidCongeView.class);
    }
}