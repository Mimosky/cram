package org.app.client.application.notify;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class NotifyModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
            bindPresenterWidget(NotifyPresenter.class, NotifyPresenter.MyView.class, NotifyView.class);
    }
}