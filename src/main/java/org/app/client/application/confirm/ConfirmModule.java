package org.app.client.application.confirm;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ConfirmModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
            bindPresenterWidget(ConfirmPresenter.class, ConfirmPresenter.MyView.class, ConfirmView.class);
    }
}