package org.app.client.application.addonedaywork;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AddOneDayWorkModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenterWidget(AddOneDayWorkPresenter.class, AddOneDayWorkPresenter.MyView.class, AddOneDayWorkView.class);
    }
}