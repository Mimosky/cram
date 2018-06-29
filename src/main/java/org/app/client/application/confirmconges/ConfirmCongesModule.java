package org.app.client.application.confirmconges;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ConfirmCongesModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
            bindPresenterWidget(ConfirmCongesPresenter.class, ConfirmCongesPresenter.MyView.class, ConfirmCongesView.class);
    }
}