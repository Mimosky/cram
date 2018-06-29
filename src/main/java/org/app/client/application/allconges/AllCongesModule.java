package org.app.client.application.allconges;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AllCongesModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(AllCongesPresenter.class, AllCongesPresenter.MyView.class, AllCongesView.class, AllCongesPresenter.MyProxy.class);
    }
}